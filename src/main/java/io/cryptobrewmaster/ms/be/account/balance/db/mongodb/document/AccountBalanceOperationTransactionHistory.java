package io.cryptobrewmaster.ms.be.account.balance.db.mongodb.document;

import io.cryptobrewmaster.ms.be.library.constants.Currency;
import io.cryptobrewmaster.ms.be.library.constants.ServiceName;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceOperationTransactionStatus;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceOperationType;
import io.cryptobrewmaster.ms.be.library.constants.audit.AuditAction;
import io.cryptobrewmaster.ms.be.library.constants.audit.AuditSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "accountBalanceOperationTransactionHistory")
public class AccountBalanceOperationTransactionHistory {
    @Id
    private ObjectId id;
    private String accountId;
    private String requestId;
    private String operationId;
    private BalanceOperationType operationType;
    private BalanceOperationTransactionStatus operationTransactionStatus;

    private Currency currency;
    private Double quantity;
    private AuditSource auditSource;
    private AuditAction auditAction;
    private Long accountBlockedBalanceId;

    private ServiceName producerServiceName;

    private Long createdDate;
    private Long lastModifiedDate;
}
