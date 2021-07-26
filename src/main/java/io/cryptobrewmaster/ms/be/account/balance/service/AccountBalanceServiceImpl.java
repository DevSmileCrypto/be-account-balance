package io.cryptobrewmaster.ms.be.account.balance.service;

import io.cryptobrewmaster.ms.be.account.balance.communication.config.dto.BalanceConfigDto;
import io.cryptobrewmaster.ms.be.account.balance.communication.config.service.ConfigCommunicationService;
import io.cryptobrewmaster.ms.be.account.balance.db.entity.balance.AccountBalance;
import io.cryptobrewmaster.ms.be.account.balance.db.entity.balance.blocked.AccountBalanceBlocked;
import io.cryptobrewmaster.ms.be.account.balance.db.repository.AccountBalanceRepository;
import io.cryptobrewmaster.ms.be.account.balance.db.repository.blocked.AccountBalanceBlockedRepository;
import io.cryptobrewmaster.ms.be.account.balance.web.model.AccountBalanceDto;
import io.cryptobrewmaster.ms.be.account.balance.web.model.AccountBalanceUiDto;
import io.cryptobrewmaster.ms.be.account.balance.web.model.criteria.AccountBalanceFetchedCriteriaDto;
import io.cryptobrewmaster.ms.be.account.balance.web.model.request.AccountBalanceChangedRequestDto;
import io.cryptobrewmaster.ms.be.account.balance.web.model.response.AccountBalanceChangedResponseDto;
import io.cryptobrewmaster.ms.be.library.constants.Currency;
import io.cryptobrewmaster.ms.be.library.dto.PageDto;
import io.cryptobrewmaster.ms.be.library.exception.account.balance.NotEnoughBalanceException;
import io.cryptobrewmaster.ms.be.library.kafka.dto.account.KafkaAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static io.cryptobrewmaster.ms.be.account.balance.constants.BalanceOperation.ADD;
import static io.cryptobrewmaster.ms.be.account.balance.constants.BalanceOperation.SUBTRACT;
import static io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceChangeStatus.IN_PROCESS;

@RequiredArgsConstructor
@Service
public class AccountBalanceServiceImpl implements AccountBalanceService {

    private final ConfigCommunicationService configCommunicationService;

    private final AccountBalanceRepository accountBalanceRepository;
    private final AccountBalanceBlockedRepository accountBalanceBlockedRepository;

    @Transactional
    public void init(KafkaAccount kafkaAccount) {
        List<BalanceConfigDto> balanceConfigDtos = configCommunicationService.getAllBalanceConfig();

        List<AccountBalance> accountBalances = balanceConfigDtos.stream()
                .map(balanceConfigDto -> AccountBalance.of(kafkaAccount.getId(), balanceConfigDto))
                .collect(Collectors.toList());

        accountBalanceRepository.saveAll(accountBalances);
    }

    @Override
    public AccountBalanceDto getByAccountIdAndCurrency(String accountId, Currency currency) {
        return accountBalanceRepository.getByAccountIdAndCurrency(accountId, currency, AccountBalanceDto::of);
    }

    @Override
    public PageDto<AccountBalanceDto> fetchByCriteria(AccountBalanceFetchedCriteriaDto criteria) {
        var page = accountBalanceRepository.fetchByCriteria(criteria, AccountBalanceDto::of);
        return PageDto.of(page.getContent(), page.getTotalPages(), page.getTotalElements());
    }

    @Override
    public PageDto<AccountBalanceUiDto> fetchByCriteriaForUi(AccountBalanceFetchedCriteriaDto criteria) {
        var page = accountBalanceRepository.fetchByCriteria(criteria, AccountBalanceUiDto::of);
        return PageDto.of(page.getContent(), page.getTotalPages(), page.getTotalElements());
    }

    @Transactional
    @Override
    public AccountBalanceChangedResponseDto add(AccountBalanceChangedRequestDto accountBalanceChangedRequestDto) {
        var accountBalance = accountBalanceRepository.getWithLockByAccountIdAndCurrency(
                accountBalanceChangedRequestDto.getAccountId(),
                accountBalanceChangedRequestDto.getCurrency()
        );
        var oldQuantity = accountBalance.getQuantity();
        var newQuantity = oldQuantity.add(BigDecimal.valueOf(accountBalanceChangedRequestDto.getQuantity()));

        var accountBlockedBalanceHistory = AccountBalanceBlocked.of(
                oldQuantity, IN_PROCESS, ADD, accountBalance, accountBalanceChangedRequestDto
        );
        accountBlockedBalanceHistory = accountBalanceBlockedRepository.save(accountBlockedBalanceHistory);

        accountBalance.setQuantity(newQuantity);
        accountBalance = accountBalanceRepository.save(accountBalance);

        return AccountBalanceChangedResponseDto.of(accountBlockedBalanceHistory.getId(), accountBalance);
    }

    @Transactional
    @Override
    public AccountBalanceChangedResponseDto subtract(AccountBalanceChangedRequestDto accountBalanceChangedRequestDto) {
        var accountBalance = accountBalanceRepository.getWithLockByAccountIdAndCurrency(
                accountBalanceChangedRequestDto.getAccountId(),
                accountBalanceChangedRequestDto.getCurrency()
        );
        var oldQuantity = accountBalance.getQuantity();
        var newQuantity = oldQuantity.subtract(BigDecimal.valueOf(accountBalanceChangedRequestDto.getQuantity()));

        if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughBalanceException(
                    String.format("Account balance = %s not enough quantity = %s",
                            accountBalance, accountBalanceChangedRequestDto.getQuantity())
            );
        }

        var accountBlockedBalance = AccountBalanceBlocked.of(
                oldQuantity, IN_PROCESS, SUBTRACT, accountBalance, accountBalanceChangedRequestDto
        );
        accountBlockedBalance = accountBalanceBlockedRepository.save(accountBlockedBalance);

        accountBalance.setQuantity(newQuantity);
        accountBalance = accountBalanceRepository.save(accountBalance);

        return AccountBalanceChangedResponseDto.of(accountBlockedBalance.getId(), accountBalance);
    }
}
