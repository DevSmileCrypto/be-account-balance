package io.cryptobrewmaster.ms.be.account.balance.db.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Clock;

@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class AbstractEntity {
    @Column(name = "created_date", nullable = false)
    protected Long createdDate;
    @Column(name = "last_modified_date", nullable = false)
    protected Long lastModifiedDate;

    @PrePersist
    protected void handleBeforeSave() {
        var now = generateUtcClock().millis();
        setCreatedDate(now);
        setLastModifiedDate(now);
    }

    @PreUpdate
    protected void handleBeforeUpdate() {
        var now = generateUtcClock().millis();
        setLastModifiedDate(now);
    }

    private Clock generateUtcClock() {
        return Clock.systemUTC();
    }

}
