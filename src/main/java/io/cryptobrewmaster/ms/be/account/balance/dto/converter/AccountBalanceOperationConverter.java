package io.cryptobrewmaster.ms.be.account.balance.dto.converter;

import io.cryptobrewmaster.ms.be.account.balance.db.mongodb.document.AccountBalanceOperationTransaction;
import io.cryptobrewmaster.ms.be.account.balance.dto.AccountBalanceOperation;
import io.cryptobrewmaster.ms.be.library.constants.ServiceName;
import io.cryptobrewmaster.ms.be.library.kafka.dto.account.balance.KafkaAccountBalanceOperation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccountBalanceOperationConverter {

    public static AccountBalanceOperation toModel(AccountBalanceOperationTransaction transaction) {
        return AccountBalanceOperation.builder()
                .requestId(transaction.getRequestId())
                .operationId(transaction.getOperationId())
                .operationType(transaction.getOperationType())
                .currency(transaction.getCurrency())
                .quantity(transaction.getQuantity())
                .auditSource(transaction.getAuditSource())
                .auditAction(transaction.getAuditAction())
                .accountBlockedBalanceId(transaction.getAccountBlockedBalanceId())
                .accountId(transaction.getAccountId())
                .producerServiceName(transaction.getProducerServiceName())
                .build();
    }

    public static AccountBalanceOperation toModel(String accountId, ServiceName producerServiceName,
                                                  KafkaAccountBalanceOperation kafkaAccountBalanceOperation) {
        return AccountBalanceOperation.builder()
                .requestId(kafkaAccountBalanceOperation.getRequestId())
                .operationId(kafkaAccountBalanceOperation.getOperationId())
                .operationType(kafkaAccountBalanceOperation.getOperationType())
                .currency(kafkaAccountBalanceOperation.getCurrency())
                .quantity(kafkaAccountBalanceOperation.getQuantity())
                .auditSource(kafkaAccountBalanceOperation.getAuditSource())
                .auditAction(kafkaAccountBalanceOperation.getAuditAction())
                .accountBlockedBalanceId(kafkaAccountBalanceOperation.getAccountBlockedBalanceId())
                .accountId(accountId)
                .producerServiceName(producerServiceName)
                .build();
    }
}
