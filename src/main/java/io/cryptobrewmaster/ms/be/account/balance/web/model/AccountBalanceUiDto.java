package io.cryptobrewmaster.ms.be.account.balance.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.cryptobrewmaster.ms.be.library.constants.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountBalanceUiDto {
    @NotNull
    private Long id;
    @NotNull
    private Currency currency;
    @NotNull
    private Double quantity;
    @NotNull
    private Long createdDate;
    @NotNull
    private Long lastModifiedDate;
}
