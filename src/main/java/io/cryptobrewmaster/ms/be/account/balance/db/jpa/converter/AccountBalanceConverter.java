package io.cryptobrewmaster.ms.be.account.balance.db.jpa.converter;

import io.cryptobrewmaster.ms.be.account.balance.communication.config.dto.BalanceConfigDto;
import io.cryptobrewmaster.ms.be.account.balance.db.jpa.entity.balance.AccountBalance;
import io.cryptobrewmaster.ms.be.account.balance.web.model.AccountBalanceDto;
import io.cryptobrewmaster.ms.be.account.balance.web.model.AccountBalanceUiDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccountBalanceConverter {

    public static AccountBalance toEntity(String accountId, BalanceConfigDto balanceConfigDto) {
        return new AccountBalance(
                null, accountId, balanceConfigDto.getCurrency(), BigDecimal.valueOf(balanceConfigDto.getQuantity())
        );
    }

    public static AccountBalanceDto toDto(AccountBalance accountBalance) {
        return new AccountBalanceDto(
                accountBalance.getId(), accountBalance.getAccountId(), accountBalance.getCurrency(),
                accountBalance.getQuantity().doubleValue(), accountBalance.getCreatedDate(), accountBalance.getLastModifiedDate()
        );
    }

    public static AccountBalanceUiDto toUiDto(AccountBalance accountBalance) {
        return new AccountBalanceUiDto(
                accountBalance.getId(), accountBalance.getCurrency(), accountBalance.getQuantity().doubleValue(),
                accountBalance.getCreatedDate(), accountBalance.getLastModifiedDate()
        );
    }

}