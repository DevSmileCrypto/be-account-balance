package io.cryptobrewmaster.ms.be.account.balance.db.entity.blocked;

import io.cryptobrewmaster.ms.be.account.balance.constants.BalanceOperation;
import io.cryptobrewmaster.ms.be.account.balance.db.entity.AccountBalance;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceChangeAction;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceChangeSource;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceChangeStatus;
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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account_blocked_balance_history")
public class AccountBlockedBalanceHistory {
    @Id
    @Column(name = "id")
    private Long id;
    @OneToOne(targetEntity = AccountBalance.class)
    @JoinColumn(name = "account_balance_id")
    private AccountBalance accountBalance;
    @Column(name = "old_quantity")
    private BigDecimal oldQuantity;
    @Column(name = "blocked_quantity")
    private BigDecimal blockedQuantity;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BalanceChangeStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "operation")
    private BalanceOperation operation;
    @Enumerated(EnumType.STRING)
    @Column(name = "source")
    private BalanceChangeSource source;
    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private BalanceChangeAction action;
    @Column(name = "created_date")
    private Long createdDate;
    @Column(name = "last_modified_date")
    private Long lastModifiedDate;

    public static AccountBlockedBalanceHistory of(AccountBlockedBalance accountBlockedBalance) {
        return new AccountBlockedBalanceHistory(
                accountBlockedBalance.getId(), accountBlockedBalance.getAccountBalance(),
                accountBlockedBalance.getOldQuantity(), accountBlockedBalance.getBlockedQuantity(),
                accountBlockedBalance.getStatus(), accountBlockedBalance.getOperation(),
                accountBlockedBalance.getSource(), accountBlockedBalance.getAction(),
                accountBlockedBalance.getCreatedDate(), accountBlockedBalance.getLastModifiedDate()
        );
    }
}