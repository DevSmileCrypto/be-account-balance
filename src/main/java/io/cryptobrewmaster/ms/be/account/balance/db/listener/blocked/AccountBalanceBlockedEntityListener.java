package io.cryptobrewmaster.ms.be.account.balance.db.listener.blocked;

import io.cryptobrewmaster.ms.be.account.balance.db.entity.blocked.AccountBalanceBlocked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Clock;

@Component
public class AccountBalanceBlockedEntityListener {

    private static Clock utcClock;

    @Autowired
    public void injectDependencies(Clock utcClock) {
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

}
