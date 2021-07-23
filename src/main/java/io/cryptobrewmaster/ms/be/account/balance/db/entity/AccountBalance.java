package io.cryptobrewmaster.ms.be.account.balance.db.entity;

import io.cryptobrewmaster.ms.be.account.balance.communication.config.dto.BalanceConfigDto;
import io.cryptobrewmaster.ms.be.account.balance.db.listener.AccountBalanceEntityListener;
import io.cryptobrewmaster.ms.be.library.constants.Currency;
import io.cryptobrewmaster.ms.be.library.kafka.dto.account.balance.KafkaAccountBalance;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AccountBalanceEntityListener.class)
@Entity
@SequenceGenerator(
        name = "account_balance_sequence_generator",
        sequenceName = "account_balance_sequence",
        allocationSize = 1
)
@Table(name = "account_balance")
public class AccountBalance {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_balance_sequence_generator"
    )
    @Column(name = "id")
    private Long id;
    @Column(name = "account_id")
    private String accountId;
    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "created_date")
    private Long createdDate;
    @Column(name = "last_modified_date")
    private Long lastModifiedDate;

    public static AccountBalance of(String accountId, BalanceConfigDto balanceConfigDto) {
        return new AccountBalance(
                null, accountId, balanceConfigDto.getCurrency(),
                BigDecimal.valueOf(balanceConfigDto.getQuantity()),
                null, null
        );
    }

    public KafkaAccountBalance generateKafkaDto() {
        return new KafkaAccountBalance(
                getId(), getAccountId(), getCurrency(), getQuantity(), getCreatedDate(), getLastModifiedDate()
        );
    }
}