package io.cryptobrewmaster.ms.be.account.balance.service;

import io.cryptobrewmaster.ms.be.account.balance.web.model.AccountBalanceDto;
import io.cryptobrewmaster.ms.be.account.balance.web.model.AccountBalanceUiDto;
import io.cryptobrewmaster.ms.be.library.constants.Currency;
import io.cryptobrewmaster.ms.be.library.kafka.dto.account.KafkaAccount;

/**
 * The interface Account balance service.
 */
public interface AccountBalanceService {

    /**
     * Init.
     *
     * @param kafkaAccount the kafka account
     */
    void init(KafkaAccount kafkaAccount);

    /**
     * Gets by account id and currency.
     *
     * @param accountId the account id
     * @param currency  the currency
     * @return the by account id and currency
     */
    AccountBalanceDto getByAccountIdAndCurrency(String accountId, Currency currency);

    /**
     * Gets by account id and currency for ui.
     *
     * @param accountId the account id
     * @param currency  the currency
     * @return the by account id and currency for ui
     */
    AccountBalanceUiDto getByAccountIdAndCurrencyForUi(String accountId, Currency currency);

}
