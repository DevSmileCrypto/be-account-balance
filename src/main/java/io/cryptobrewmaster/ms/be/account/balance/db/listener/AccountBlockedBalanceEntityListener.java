package io.cryptobrewmaster.ms.be.account.balance.db.listener;

import io.cryptobrewmaster.ms.be.account.balance.db.entity.blocked.AccountBlockedBalance;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Clock;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Component
public class AccountBlockedBalanceEntityListener {

    private Clock utcClock;

    @PrePersist
    public void handleBeforeSave(AccountBlockedBalance accountBlockedBalance) {
        long now = utcClock.millis();
        accountBlockedBalance.setCreatedDate(now);
        accountBlockedBalance.setLastModifiedDate(now);
    }

    @PreUpdate
    public void handleBeforeUpdate(AccountBlockedBalance accountBlockedBalance) {
        long now = utcClock.millis();
        accountBlockedBalance.setLastModifiedDate(now);
    }

}
