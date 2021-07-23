package io.cryptobrewmaster.ms.be.account.balance.service;

import io.cryptobrewmaster.ms.be.account.balance.web.model.AccountBalanceDto;
import io.cryptobrewmaster.ms.be.account.balance.web.model.AccountBalanceUiDto;
import io.cryptobrewmaster.ms.be.account.balance.web.model.criteria.AccountBalanceFetchedCriteriaDto;
import io.cryptobrewmaster.ms.be.account.balance.web.model.request.AccountBalanceChangedRequestDto;
import io.cryptobrewmaster.ms.be.account.balance.web.model.response.AccountBalanceChangedResponseDto;
import io.cryptobrewmaster.ms.be.library.constants.Currency;
import io.cryptobrewmaster.ms.be.library.dto.PageDto;
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
     * Fetch by criteria page dto.
     *
     * @param criteria the criteria
     * @return the page dto
     */
    PageDto<AccountBalanceDto> fetchByCriteria(AccountBalanceFetchedCriteriaDto criteria);

    /**
     * Fetch by criteria for ui page dto.
     *
     * @param criteria the criteria
     * @return the page dto
     */
    PageDto<AccountBalanceUiDto> fetchByCriteriaForUi(AccountBalanceFetchedCriteriaDto criteria);

    /**
     * Add account balance changed response dto.
     *
     * @param accountBalanceChangedRequestDto the account balance changed request dto
     * @return the account balance changed response dto
     */
    AccountBalanceChangedResponseDto add(AccountBalanceChangedRequestDto accountBalanceChangedRequestDto);

    /**
     * Subtract account balance changed response dto.
     *
     * @param accountBalanceChangedRequestDto the account balance changed request dto
     * @return the account balance changed response dto
     */
    AccountBalanceChangedResponseDto subtract(AccountBalanceChangedRequestDto accountBalanceChangedRequestDto);

}
