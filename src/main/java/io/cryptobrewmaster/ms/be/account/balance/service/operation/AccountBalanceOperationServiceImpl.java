package io.cryptobrewmaster.ms.be.account.balance.service.operation;

import io.cryptobrewmaster.ms.be.account.balance.db.mongodb.document.AccountBalanceOperationTransaction;
import io.cryptobrewmaster.ms.be.account.balance.db.mongodb.repository.AccountBalanceOperationTransactionHistoryRepository;
import io.cryptobrewmaster.ms.be.account.balance.db.mongodb.repository.AccountBalanceOperationTransactionRepository;
import io.cryptobrewmaster.ms.be.account.balance.dto.AccountBalanceOperation;
import io.cryptobrewmaster.ms.be.account.balance.kafka.balance.AccountBalanceKafkaSender;
import io.cryptobrewmaster.ms.be.account.balance.service.operation.strategy.AccountBalanceOperationStrategy;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceOperationStatus;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceOperationTransactionStatus;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceOperationType;
import io.cryptobrewmaster.ms.be.library.exception.InnerServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.List;

import static io.cryptobrewmaster.ms.be.account.balance.db.mongodb.converter.AccountBalanceOperationTransactionConverter.toEntity;
import static io.cryptobrewmaster.ms.be.account.balance.db.mongodb.converter.AccountBalanceOperationTransactionConverter.toHistoryEntity;
import static io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceOperationTransactionStatus.CREATED;
import static io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceOperationTransactionStatus.DONE;
import static io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceOperationTransactionStatus.FAILED;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountBalanceOperationServiceImpl implements AccountBalanceOperationService {

    private final List<AccountBalanceOperationStrategy> accountBalanceOperationStrategies;

    private final AccountBalanceOperationTransactionRepository accountBalanceOperationTransactionRepository;
    private final AccountBalanceOperationTransactionHistoryRepository accountBalanceOperationTransactionHistoryRepository;

    private final AccountBalanceKafkaSender accountBalanceKafkaSender;

    private final Clock utcClock;

    @Override
    public void handleOperation(AccountBalanceOperation accountBalanceOperation) {
        var exists = accountBalanceOperationTransactionHistoryRepository.existsByAccountIdAndRequestIdAndOperationIdAndOperationTransactionStatusIn(
                accountBalanceOperation.getAccountId(), accountBalanceOperation.getRequestId(),
                accountBalanceOperation.getOperationId(), List.of(DONE, FAILED)
        );
        if (exists) {
            log.warn("Account balance operation transaction with account id = {} and request id = {} and " +
                    "operation id = {} and status = {} or {} already handled", accountBalanceOperation.getAccountId(),
                    accountBalanceOperation.getRequestId(), accountBalanceOperation.getOperationId(), DONE, FAILED);
            return;
        }

        var accountBalanceOperationTransaction = accountBalanceOperationTransactionRepository.findByAccountIdAndRequestIdAndOperationIdAndOperationTransactionStatusIn(
                accountBalanceOperation.getAccountId(), accountBalanceOperation.getRequestId(), accountBalanceOperation.getOperationId(), List.of(CREATED)
        ).orElseGet(() -> accountBalanceOperationTransactionRepository.save(
                toEntity(accountBalanceOperation, CREATED, utcClock)
        ));

        try {
            getStrategy(accountBalanceOperation.getOperationType())
                    .handleOperation(accountBalanceOperation);

            saveOrUpdateTransactionToHistory(accountBalanceOperationTransaction, DONE);
        } catch (Exception e) {
            log.error("Error while on handle account balance operation. Account id = {}, {}. Error = {}",
                    accountBalanceOperation.getAccountId(), accountBalanceOperation, e.getMessage());

            saveOrUpdateTransactionToHistory(accountBalanceOperationTransaction, FAILED);

            accountBalanceKafkaSender.handleOperationCallback(
                    accountBalanceOperation, BalanceOperationStatus.FAILED
            );
        }
    }

    private AccountBalanceOperationStrategy getStrategy(BalanceOperationType operationType) {
        return accountBalanceOperationStrategies.stream()
                .filter(strategy -> strategy.getOperationType() == operationType)
                .findFirst()
                .orElseThrow(() -> new InnerServiceException(
                        String.format("Account balance operationType strategy with operationType = %s not exists in system",
                                operationType)
                ));
    }

    private void saveOrUpdateTransactionToHistory(AccountBalanceOperationTransaction accountBalanceOperationTransaction,
                                                  BalanceOperationTransactionStatus status) {
        accountBalanceOperationTransactionHistoryRepository.save(
                toHistoryEntity(accountBalanceOperationTransaction, status, utcClock)
        );

        accountBalanceOperationTransactionRepository.delete(accountBalanceOperationTransaction);
    }

}
