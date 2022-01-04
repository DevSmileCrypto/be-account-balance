package io.cryptobrewmaster.ms.be.account.balance.dto;

import io.cryptobrewmaster.ms.be.library.constants.Currency;
import io.cryptobrewmaster.ms.be.library.constants.ServiceName;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceOperationType;
import io.cryptobrewmaster.ms.be.library.constants.audit.AuditAction;
import io.cryptobrewmaster.ms.be.library.constants.audit.AuditSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalanceOperation {

    public static final Comparator<AccountBalanceOperation> COMPARATOR = Comparator.comparing(
            operation -> operation.getOperationType().getPriority()
    );

    private String requestId;
    private String operationId;
    private BalanceOperationType operationType;
    private Currency currency;
    private Double quantity;
    private AuditSource auditSource;
    private AuditAction auditAction;
    private Long accountBlockedBalanceId;

    private ServiceName producerServiceName;
    private String accountId;
}
