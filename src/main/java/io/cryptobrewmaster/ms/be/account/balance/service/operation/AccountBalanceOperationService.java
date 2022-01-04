package io.cryptobrewmaster.ms.be.account.balance.service.operation;

import io.cryptobrewmaster.ms.be.account.balance.dto.AccountBalanceOperation;

/**
 * The interface Account balance operation service.
 */
public interface AccountBalanceOperationService {

    /**
     * Handle operation.
     *
     * @param accountBalanceOperation the account balance operation
     */
    void handleOperation(AccountBalanceOperation accountBalanceOperation);

}
