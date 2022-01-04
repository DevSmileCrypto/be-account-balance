package io.cryptobrewmaster.ms.be.account.balance.service.operation.strategy;

import io.cryptobrewmaster.ms.be.account.balance.constants.Operation;
import io.cryptobrewmaster.ms.be.account.balance.db.jpa.converter.blocked.AccountBlockedBalanceConverter;
import io.cryptobrewmaster.ms.be.account.balance.db.jpa.repository.AccountBalanceRepository;
import io.cryptobrewmaster.ms.be.account.balance.db.jpa.repository.blocked.AccountBlockedBalanceRepository;
import io.cryptobrewmaster.ms.be.account.balance.dto.AccountBalanceOperation;
import io.cryptobrewmaster.ms.be.account.balance.kafka.balance.AccountBalanceKafkaSender;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceOperationStatus;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceOperationType;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BlockedBalanceStatus;
import io.cryptobrewmaster.ms.be.library.exception.InnerServiceException;
import io.cryptobrewmaster.ms.be.library.exception.account.balance.NotEnoughBalanceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Component
public class AccountBalanceSubtractOperationStrategyImpl implements AccountBalanceOperationStrategy {

    private final AccountBalanceKafkaSender accountBalanceKafkaSender;

    private final AccountBalanceRepository accountBalanceRepository;
    private final AccountBlockedBalanceRepository accountBlockedBalanceRepository;

    @Transactional
    @Override
    public void handleOperation(AccountBalanceOperation accountBalanceOperation) {
        var accountBalance = accountBalanceRepository.findWithLockByAccountIdAndCurrency(
                accountBalanceOperation.getAccountId(), accountBalanceOperation.getCurrency()
        ).orElseThrow(() -> new InnerServiceException(
                String.format("Account balance with account id = %s and currency = %s not exists in system",
                        accountBalanceOperation.getAccountId(), accountBalanceOperation.getCurrency())
        ));

        var oldQuantity = accountBalance.getQuantity();
        var newQuantity = oldQuantity.subtract(BigDecimal.valueOf(accountBalanceOperation.getQuantity()));

        if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughBalanceException(
                    String.format("Account balance with id = %s not enough quantity = %s",
                            accountBalance.getId(), accountBalanceOperation.getQuantity())
            );
        }

        var accountBlockedBalance = AccountBlockedBalanceConverter.toEntity(
                oldQuantity, BlockedBalanceStatus.IN_PROCESS, Operation.SUBTRACT, accountBalance, accountBalanceOperation
        );
        accountBlockedBalance = accountBlockedBalanceRepository.save(accountBlockedBalance);

        accountBalance.setQuantity(newQuantity);
        accountBalanceRepository.save(accountBalance);

        accountBalanceKafkaSender.handleOperationCallback(
                accountBalanceOperation, BalanceOperationStatus.DONE, accountBlockedBalance.getId()
        );
    }

    @Override
    public BalanceOperationType getOperationType() {
        return BalanceOperationType.SUBTRACT;
    }
}
