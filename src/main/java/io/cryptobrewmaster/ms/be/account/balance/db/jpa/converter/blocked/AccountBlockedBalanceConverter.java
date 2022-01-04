package io.cryptobrewmaster.ms.be.account.balance.db.jpa.converter.blocked;

import io.cryptobrewmaster.ms.be.account.balance.constants.Operation;
import io.cryptobrewmaster.ms.be.account.balance.db.jpa.entity.balance.AccountBalance;
import io.cryptobrewmaster.ms.be.account.balance.db.jpa.entity.balance.blocked.AccountBlockedBalance;
import io.cryptobrewmaster.ms.be.account.balance.db.jpa.entity.balance.blocked.AccountBlockedBalanceHistory;
import io.cryptobrewmaster.ms.be.account.balance.dto.AccountBalanceOperation;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BlockedBalanceStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccountBlockedBalanceConverter {

    public static AccountBlockedBalance toEntity(BigDecimal oldQuantity, BlockedBalanceStatus status, Operation operation,
                                                 AccountBalance accountBalance, AccountBalanceOperation accountBalanceOperation) {
        return new AccountBlockedBalance(
                null, accountBalance.getAccountId(), accountBalance.getCurrency(), oldQuantity,
                BigDecimal.valueOf(accountBalanceOperation.getQuantity()), operation, status, accountBalanceOperation.getAuditSource(),
                accountBalanceOperation.getAuditAction()
        );
    }

    public static AccountBlockedBalanceHistory toHistoryEntity(AccountBlockedBalance accountBlockedBalance) {
        return toHistoryEntity(accountBlockedBalance, accountBlockedBalance.getStatus());
    }

    public static AccountBlockedBalanceHistory toHistoryEntity(AccountBlockedBalance accountBlockedBalance, BlockedBalanceStatus status) {
        var accountBalanceBlockedHistory = new AccountBlockedBalanceHistory(
                accountBlockedBalance.getId(), accountBlockedBalance.getAccountId(), accountBlockedBalance.getCurrency(),
                accountBlockedBalance.getOldQuantity(), accountBlockedBalance.getBlockedQuantity(), status,
                accountBlockedBalance.getOperation(), accountBlockedBalance.getSource(), accountBlockedBalance.getAction()
        );
        accountBalanceBlockedHistory.setCreatedDate(accountBlockedBalance.getCreatedDate());
        return accountBalanceBlockedHistory;
    }

}