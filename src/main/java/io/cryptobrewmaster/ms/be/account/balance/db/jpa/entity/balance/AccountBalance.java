package io.cryptobrewmaster.ms.be.account.balance.db.jpa.entity.balance;

import io.cryptobrewmaster.ms.be.account.balance.db.jpa.entity.AbstractEntity;
import io.cryptobrewmaster.ms.be.library.constants.Currency;
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
        name = "account_balance_sequence_generator",
        sequenceName = "account_balance_sequence",
        allocationSize = 1
)
@Table(name = "account_balance")
public class AccountBalance extends AbstractEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_balance_sequence_generator"
    )
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "account_id", nullable = false)
    private String accountId;
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;
    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity;
}