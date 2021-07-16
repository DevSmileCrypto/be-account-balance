package io.cryptobrewmaster.ms.be.account.balance.db.entity.blocked;

import io.cryptobrewmaster.ms.be.account.balance.constants.BalanceOperation;
import io.cryptobrewmaster.ms.be.account.balance.db.entity.AccountBalance;
import io.cryptobrewmaster.ms.be.account.balance.db.listener.blocked.AccountBalanceBlockedEntityListener;
import io.cryptobrewmaster.ms.be.account.balance.web.model.request.AccountBalanceChangedRequestDto;
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
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AccountBalanceBlockedEntityListener.class)
@Entity
@SequenceGenerator(
        name = "account_blocked_balance_sequence_generator",
        sequenceName = "account_blocked_balance_sequence",
        allocationSize = 1
)
@Table(name = "account_blocked_balance")
public class AccountBalanceBlocked {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_blocked_balance_sequence_generator"
    )
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
    @Column(name = "operation")
    private BalanceOperation operation;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BalanceChangeStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "source")
    private AuditSource source;
    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private AuditAction action;
    @Column(name = "created_date")
    private Long createdDate;
    @Column(name = "last_modified_date")
    private Long lastModifiedDate;

    public static AccountBalanceBlocked of(BigDecimal oldQuantity, BalanceChangeStatus status, BalanceOperation operation,
                                           AccountBalance accountBalance, AccountBalanceChangedRequestDto accountBalanceChangedRequestDto) {
        return new AccountBalanceBlocked(
                null, accountBalance, oldQuantity, BigDecimal.valueOf(accountBalanceChangedRequestDto.getQuantity()),
                operation, status, accountBalanceChangedRequestDto.getSource(),
                accountBalanceChangedRequestDto.getAction(), null, null
        );
    }
}