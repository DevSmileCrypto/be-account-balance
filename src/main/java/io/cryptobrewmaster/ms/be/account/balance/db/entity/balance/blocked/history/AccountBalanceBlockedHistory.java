package io.cryptobrewmaster.ms.be.account.balance.db.entity.balance.blocked.history;

import io.cryptobrewmaster.ms.be.account.balance.constants.BalanceOperation;
import io.cryptobrewmaster.ms.be.account.balance.db.entity.AbstractEntity;
import io.cryptobrewmaster.ms.be.account.balance.db.entity.balance.AccountBalance;
import io.cryptobrewmaster.ms.be.account.balance.db.entity.balance.blocked.AccountBalanceBlocked;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceChangeStatus;
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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account_balance_blocked_history")
public class AccountBalanceBlockedHistory extends AbstractEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @OneToOne(targetEntity = AccountBalance.class)
    @JoinColumn(name = "account_balance_id", nullable = false)
    private AccountBalance accountBalance;
    @Column(name = "old_quantity", nullable = false)
    private BigDecimal oldQuantity;
    @Column(name = "blocked_quantity", nullable = false)
    private BigDecimal blockedQuantity;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BalanceChangeStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "operation", nullable = false)
    private BalanceOperation operation;
    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private AuditSource source;
    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private AuditAction action;

    public static AccountBalanceBlockedHistory of(AccountBalanceBlocked accountBalanceBlocked) {
        var accountBalanceBlockedHistory = new AccountBalanceBlockedHistory(
                accountBalanceBlocked.getId(), accountBalanceBlocked.getAccountBalance(),
                accountBalanceBlocked.getOldQuantity(), accountBalanceBlocked.getBlockedQuantity(),
                accountBalanceBlocked.getStatus(), accountBalanceBlocked.getOperation(),
                accountBalanceBlocked.getSource(), accountBalanceBlocked.getAction()
        );
        accountBalanceBlockedHistory.setCreatedDate(accountBalanceBlocked.getCreatedDate());
        return accountBalanceBlockedHistory;
    }
}