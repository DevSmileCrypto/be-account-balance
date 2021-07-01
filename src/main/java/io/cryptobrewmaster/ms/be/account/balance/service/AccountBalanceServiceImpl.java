package io.cryptobrewmaster.ms.be.account.balance.service;

import io.cryptobrewmaster.ms.be.account.balance.db.entity.blocked.AccountBlockedBalance;
import io.cryptobrewmaster.ms.be.account.balance.db.repository.AccountBalanceRepository;
import io.cryptobrewmaster.ms.be.account.balance.db.repository.blocked.AccountBlockedBalanceRepository;
import io.cryptobrewmaster.ms.be.account.balance.web.model.AccountBalanceDto;
import io.cryptobrewmaster.ms.be.account.balance.web.model.param.AccountBalanceRequestParam;
import io.cryptobrewmaster.ms.be.account.balance.web.model.request.AccountBalanceChangedRequestDto;
import io.cryptobrewmaster.ms.be.account.balance.web.model.response.AccountBalanceChangedResponseDto;
import io.cryptobrewmaster.ms.be.library.constants.Currency;
import io.cryptobrewmaster.ms.be.library.dto.PageDto;
import io.cryptobrewmaster.ms.be.library.exception.balance.NotEnoughBalanceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static io.cryptobrewmaster.ms.be.account.balance.constants.BalanceOperation.ADD;
import static io.cryptobrewmaster.ms.be.account.balance.constants.BalanceOperation.SUBTRACT;
import static io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceChangeStatus.IN_PROCESS;

@RequiredArgsConstructor
@Service
public class AccountBalanceServiceImpl implements AccountBalanceService {

    private final AccountBalanceRepository accountBalanceRepository;
    private final AccountBlockedBalanceRepository accountBlockedBalanceRepository;

    @Override
    public AccountBalanceDto getByAccountIdAndCurrency(String accountId, Currency currency) {
        return accountBalanceRepository.getByAccountIdAndCurrency(accountId, currency, AccountBalanceDto::of);
    }

    @Override
    public PageDto<AccountBalanceDto> getAll(AccountBalanceRequestParam param) {
        if (param.hasAccountId() && param.hasCurrencies()) {
            return accountBalanceRepository.getAllByAccountIdAndCurrencyIn(param.getAccountId(), param.getCurrencies(), AccountBalanceDto::of);
        }
        if (param.hasAccountId()) {
            return accountBalanceRepository.getAllByAccountId(param.getAccountId(), AccountBalanceDto::of);
        }
        return accountBalanceRepository.getAll(AccountBalanceDto::of);
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

        accountBalance.setQuantity(newQuantity);
        accountBalance = accountBalanceRepository.save(accountBalance);

        var accountBlockedBalanceHistory = AccountBlockedBalance.of(
                oldQuantity, IN_PROCESS, ADD, accountBalance, accountBalanceChangedRequestDto
        );
        accountBlockedBalanceRepository.save(accountBlockedBalanceHistory);

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

        accountBalance.setQuantity(newQuantity);
        accountBalance = accountBalanceRepository.save(accountBalance);

        var accountBlockedBalance = AccountBlockedBalance.of(
                oldQuantity, IN_PROCESS, SUBTRACT, accountBalance, accountBalanceChangedRequestDto
        );
        accountBlockedBalanceRepository.save(accountBlockedBalance);

        return AccountBalanceChangedResponseDto.of(accountBlockedBalance.getId(), accountBalance);
    }
}
