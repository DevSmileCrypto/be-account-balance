package io.cryptobrewmaster.ms.be.account.balance.service;

import io.cryptobrewmaster.ms.be.account.balance.communication.config.dto.BalanceConfigDto;
import io.cryptobrewmaster.ms.be.account.balance.communication.config.service.ConfigCommunicationService;
import io.cryptobrewmaster.ms.be.account.balance.db.jpa.converter.AccountBalanceConverter;
import io.cryptobrewmaster.ms.be.account.balance.db.jpa.entity.balance.AccountBalance;
import io.cryptobrewmaster.ms.be.account.balance.db.jpa.repository.AccountBalanceRepository;
import io.cryptobrewmaster.ms.be.account.balance.web.model.AccountBalanceDto;
import io.cryptobrewmaster.ms.be.account.balance.web.model.AccountBalanceUiDto;
import io.cryptobrewmaster.ms.be.library.constants.Currency;
import io.cryptobrewmaster.ms.be.library.kafka.dto.account.KafkaAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AccountBalanceServiceImpl implements AccountBalanceService {

    private final ConfigCommunicationService configCommunicationService;

    private final AccountBalanceRepository accountBalanceRepository;

    @Transactional
    public void init(KafkaAccount kafkaAccount) {
        List<BalanceConfigDto> balanceConfigDtos = configCommunicationService.getAllBalanceConfig();

        List<AccountBalance> accountBalances = balanceConfigDtos.stream()
                .map(balanceConfigDto -> AccountBalanceConverter.toEntity(kafkaAccount.getId(), balanceConfigDto))
                .collect(Collectors.toList());

        accountBalanceRepository.saveAll(accountBalances);
    }

    @Override
    public AccountBalanceDto getByAccountIdAndCurrency(String accountId, Currency currency) {
        return accountBalanceRepository.getByAccountIdAndCurrency(accountId, currency, AccountBalanceConverter::toDto);
    }

    @Override
    public AccountBalanceUiDto getByAccountIdAndCurrencyForUi(String accountId, Currency currency) {
        return accountBalanceRepository.getByAccountIdAndCurrency(accountId, currency, AccountBalanceConverter::toUiDto);
    }

}
