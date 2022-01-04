package io.cryptobrewmaster.ms.be.account.balance.service.operation.strategy;

import io.cryptobrewmaster.ms.be.account.balance.dto.AccountBalanceOperation;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceOperationType;

/**
 * The interface Account balance operation strategy.
 */
public interface AccountBalanceOperationStrategy {

    /**
     * Handle operation.
     *
     * @param accountBalanceOperation the account balance operation
     */
    void handleOperation(AccountBalanceOperation accountBalanceOperation);

    /**
     * Gets operation type.
     *
     * @return the operation type
     */
    BalanceOperationType getOperationType();

}
