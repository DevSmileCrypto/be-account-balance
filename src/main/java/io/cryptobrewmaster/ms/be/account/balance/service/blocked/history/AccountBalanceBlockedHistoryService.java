package io.cryptobrewmaster.ms.be.account.balance.service.blocked.history;

import io.cryptobrewmaster.ms.be.account.balance.db.entity.blocked.AccountBalanceBlocked;

/**
 * The interface Account balance blocked history service.
 */
public interface AccountBalanceBlockedHistoryService {

    /**
     * Save history.
     *
     * @param accountBalanceBlocked the account balance blocked
     */
    void saveHistory(AccountBalanceBlocked accountBalanceBlocked);

}
