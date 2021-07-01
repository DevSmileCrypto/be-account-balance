package io.cryptobrewmaster.ms.be.account.balance.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.cryptobrewmaster.ms.be.account.balance.db.entity.AccountBalance;
import io.cryptobrewmaster.ms.be.library.constants.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountBalanceDto {
    @NotNull
    private Long id;
    @NotBlank
    private String accountId;
    @NotNull
    private Currency currency;
    @NotNull
    private Double quantity;
    @NotNull
    private Long createdDate;
    @NotNull
    private Long lastModifiedDate;

    public static AccountBalanceDto of(AccountBalance accountBalance) {
        return new AccountBalanceDto(
                accountBalance.getId(), accountBalance.getAccountId(), accountBalance.getCurrency(),
                accountBalance.getQuantity().doubleValue(), accountBalance.getCreatedDate(), accountBalance.getLastModifiedDate()
        );
    }
}
