package io.cryptobrewmaster.ms.be.account.balance.db.mongodb.converter;

import io.cryptobrewmaster.ms.be.account.balance.db.mongodb.document.AccountBalanceOperationTransaction;
import io.cryptobrewmaster.ms.be.account.balance.db.mongodb.document.AccountBalanceOperationTransactionHistory;
import io.cryptobrewmaster.ms.be.account.balance.dto.AccountBalanceOperation;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceOperationTransactionStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Clock;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccountBalanceOperationTransactionConverter {

    public static AccountBalanceOperationTransaction toEntity(AccountBalanceOperation accountBalanceOperation,
                                                              BalanceOperationTransactionStatus transactionStatus, Clock utcClock) {
        var now = utcClock.millis();
        return new AccountBalanceOperationTransaction(
                null, accountBalanceOperation.getAccountId(), accountBalanceOperation.getRequestId(),
                accountBalanceOperation.getOperationId(), accountBalanceOperation.getOperationType(),
                transactionStatus, accountBalanceOperation.getCurrency(), accountBalanceOperation.getQuantity(),
                accountBalanceOperation.getAuditSource(), accountBalanceOperation.getAuditAction(),
                accountBalanceOperation.getAccountBlockedBalanceId(), accountBalanceOperation.getProducerServiceName(),
                now, now
        );
    }

    public static AccountBalanceOperationTransactionHistory toHistoryEntity(AccountBalanceOperationTransaction transaction,
                                                                            BalanceOperationTransactionStatus transactionStatus,
                                                                            Clock utcClock) {
        return new AccountBalanceOperationTransactionHistory();
    }

}
