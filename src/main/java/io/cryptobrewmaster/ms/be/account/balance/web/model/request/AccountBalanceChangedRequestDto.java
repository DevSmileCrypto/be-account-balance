package io.cryptobrewmaster.ms.be.account.balance.web.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.cryptobrewmaster.ms.be.library.constants.Currency;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceChangeAction;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceChangeSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountBalanceChangedRequestDto {
    @NotBlank
    private String accountId;
    @NotNull
    private Currency currency;
    @NotNull
    private Double quantity;
    @NotNull
    private BalanceChangeSource source;
    @NotNull
    private BalanceChangeAction action;
}
