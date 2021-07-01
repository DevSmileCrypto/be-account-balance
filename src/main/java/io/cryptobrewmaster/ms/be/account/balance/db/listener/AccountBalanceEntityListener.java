package io.cryptobrewmaster.ms.be.account.balance.db.listener;

import io.cryptobrewmaster.ms.be.account.balance.db.entity.AccountBalance;
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
public class AccountBalanceEntityListener {

    private Clock utcClock;

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
