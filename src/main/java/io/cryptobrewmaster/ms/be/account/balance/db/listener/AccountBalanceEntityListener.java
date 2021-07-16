package io.cryptobrewmaster.ms.be.account.balance.db.listener;

import io.cryptobrewmaster.ms.be.account.balance.db.entity.AccountBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Clock;

@Component
public class AccountBalanceEntityListener {

    private Clock utcClock;

    @Autowired
    public void setUtcClock(Clock utcClock) {
        this.utcClock = utcClock;
    }

    @PrePersist
    public void handleBeforeSave(AccountBalance accountBalance) {
        long now = utcClock.millis();
        accountBalance.setCreatedDate(now);
        accountBalance.setLastModifiedDate(now);
    }

    @PreUpdate
    public void handleBeforeUpdate(AccountBalance accountBalance) {
        long now = utcClock.millis();
        accountBalance.setLastModifiedDate(now);
    }

}
