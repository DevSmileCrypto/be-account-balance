package io.cryptobrewmaster.ms.be.account.balance.kafka.balance.converter;

import io.cryptobrewmaster.ms.be.account.balance.db.jpa.entity.balance.AccountBalance;
import io.cryptobrewmaster.ms.be.account.balance.dto.AccountBalanceOperation;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceOperationStatus;
import io.cryptobrewmaster.ms.be.library.kafka.dto.account.balance.KafkaAccountBalance;
import io.cryptobrewmaster.ms.be.library.kafka.dto.account.balance.KafkaAccountBalanceOperationCallback;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccountBalanceKafkaConverter {

    public static KafkaAccountBalance toBalanceKafkaDto(AccountBalance accountBalance) {
        return new KafkaAccountBalance(
                accountBalance.getId(), accountBalance.getAccountId(), accountBalance.getCurrency(),
                accountBalance.getQuantity(), accountBalance.getCreatedDate(), accountBalance.getLastModifiedDate()
        );
    }

    public static KafkaAccountBalanceOperationCallback toBalanceOperationCallbackKafkaDto(AccountBalanceOperation accountBalanceOperation,
                                                                                          BalanceOperationStatus status,
                                                                                          Long accountBlockedBalanceId) {
        return new KafkaAccountBalanceOperationCallback(
                accountBalanceOperation.getRequestId(), accountBalanceOperation.getOperationId(), status, accountBlockedBalanceId
        );
    }

}
