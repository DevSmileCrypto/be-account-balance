package io.cryptobrewmaster.ms.be.account.balance.web.model.criteria;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.cryptobrewmaster.ms.be.library.constants.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountBalanceUiFetchedCriteriaDto {
    private String accountId;
    private List<Currency> currencies;

    public boolean hasAccountId() {
        return StringUtils.isNotBlank(accountId);
    }

    public boolean hasCurrencies() {
        return Objects.nonNull(currencies) && !currencies.isEmpty();
    }
}
