package io.cryptobrewmaster.ms.be.account.balance.db.jpa.entity.balance.blocked;

import io.cryptobrewmaster.ms.be.account.balance.constants.Operation;
import io.cryptobrewmaster.ms.be.account.balance.db.jpa.entity.AbstractEntity;
import io.cryptobrewmaster.ms.be.library.constants.Currency;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BlockedBalanceStatus;
import io.cryptobrewmaster.ms.be.library.constants.audit.AuditAction;
import io.cryptobrewmaster.ms.be.library.constants.audit.AuditSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(
        name = "account_blocked_balance_sequence_generator",
        sequenceName = "account_blocked_balance_sequence",
        allocationSize = 1
)
@Table(name = "account_blocked_balance")
public class AccountBlockedBalance extends AbstractEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_blocked_balance_sequence_generator"
    )
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "account_id", nullable = false)
    private String accountId;
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;
    @Column(name = "old_quantity", nullable = false)
    private BigDecimal oldQuantity;
    @Column(name = "blocked_quantity", nullable = false)
    private BigDecimal blockedQuantity;
    @Enumerated(EnumType.STRING)
    @Column(name = "operation", nullable = false)
    private Operation operation;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BlockedBalanceStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private AuditSource source;
    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private AuditAction action;
}