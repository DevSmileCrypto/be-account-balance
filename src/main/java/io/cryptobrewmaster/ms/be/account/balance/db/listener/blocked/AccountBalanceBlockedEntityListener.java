package io.cryptobrewmaster.ms.be.account.balance.db.listener.blocked;

import io.cryptobrewmaster.ms.be.account.balance.db.entity.blocked.AccountBalanceBlocked;
import io.cryptobrewmaster.ms.be.account.balance.service.blocked.history.AccountBalanceBlockedHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostRemove;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Clock;

@Component
public class AccountBalanceBlockedEntityListener {

    private static AccountBalanceBlockedHistoryService accountBalanceBlockedHistoryService;

    private static Clock utcClock;

    @Autowired
    public void injectDependencies(AccountBalanceBlockedHistoryService accountBalanceBlockedHistoryService,
                                   Clock utcClock) {
        AccountBalanceBlockedEntityListener.accountBalanceBlockedHistoryService = accountBalanceBlockedHistoryService;
        AccountBalanceBlockedEntityListener.utcClock = utcClock;
    }

    @PrePersist
    public void handleBeforeSave(AccountBalanceBlocked accountBalanceBlocked) {
        long now = utcClock.millis();
        accountBalanceBlocked.setCreatedDate(now);
        accountBalanceBlocked.setLastModifiedDate(now);
    }

    @PreUpdate
    public void handleBeforeUpdate(AccountBalanceBlocked accountBalanceBlocked) {
        long now = utcClock.millis();
        accountBalanceBlocked.setLastModifiedDate(now);
    }

    @PostRemove
    public void handleAfterDelete(AccountBalanceBlocked accountBalanceBlocked) {
        accountBalanceBlockedHistoryService.saveHistory(accountBalanceBlocked);
    }

}
