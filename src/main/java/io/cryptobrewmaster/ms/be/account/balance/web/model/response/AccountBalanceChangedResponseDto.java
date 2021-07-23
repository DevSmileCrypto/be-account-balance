package io.cryptobrewmaster.ms.be.account.balance.web.model.response;

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
public class AccountBalanceChangedResponseDto {
    @NotBlank
    private String accountId;
    @NotNull
    private Currency currency;
    @NotNull
    private Double quantity;
    @NotNull
    private Long accountBalanceBlockedId;

    public static AccountBalanceChangedResponseDto of(Long accountBalanceBlockedId, AccountBalance accountBalance) {
        return new AccountBalanceChangedResponseDto(
                accountBalance.getAccountId(), accountBalance.getCurrency(), accountBalance.getQuantity().doubleValue(),
                accountBalanceBlockedId
        );
    }
}
