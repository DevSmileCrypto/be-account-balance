package io.cryptobrewmaster.ms.be.account.balance.service.operation.strategy;

import io.cryptobrewmaster.ms.be.account.balance.db.jpa.converter.blocked.AccountBlockedBalanceConverter;
import io.cryptobrewmaster.ms.be.account.balance.db.jpa.repository.AccountBalanceRepository;
import io.cryptobrewmaster.ms.be.account.balance.db.jpa.repository.blocked.AccountBlockedBalanceHistoryRepository;
import io.cryptobrewmaster.ms.be.account.balance.db.jpa.repository.blocked.AccountBlockedBalanceRepository;
import io.cryptobrewmaster.ms.be.account.balance.dto.AccountBalanceOperation;
import io.cryptobrewmaster.ms.be.account.balance.kafka.balance.AccountBalanceKafkaSender;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceOperationType;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BlockedBalanceStatus;
import io.cryptobrewmaster.ms.be.library.exception.InnerServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class AccountBalanceCompleteOperationStrategyImpl implements AccountBalanceOperationStrategy {

    private final AccountBalanceKafkaSender accountBalanceKafkaSender;

    private final AccountBalanceRepository accountBalanceRepository;
    private final AccountBlockedBalanceRepository accountBlockedBalanceRepository;
    private final AccountBlockedBalanceHistoryRepository accountBlockedBalanceHistoryRepository;

    @Transactional
    @Override
    public void handleOperation(AccountBalanceOperation accountBalanceOperation) {
        var accountBlockedBalance = accountBlockedBalanceRepository.findWithLockByIdAndAccountId(
                accountBalanceOperation.getAccountBlockedBalanceId(), accountBalanceOperation.getAccountId()
        ).orElseThrow(() -> new InnerServiceException(
                String.format("Account blocked balance with id = %s and account id = %s not exists",
                        accountBalanceOperation.getAccountBlockedBalanceId(), accountBalanceOperation.getAccountId())
        ));

        var accountBlockedBalanceHistory = AccountBlockedBalanceConverter.toHistoryEntity(
                accountBlockedBalance, BlockedBalanceStatus.DONE
        );
        accountBlockedBalanceHistoryRepository.save(accountBlockedBalanceHistory);

        accountBlockedBalanceRepository.delete(accountBlockedBalance);

        accountBalanceRepository.findByAccountIdAndCurrency(accountBalanceOperation.getAccountId(), accountBlockedBalance.getCurrency())
                .ifPresent(accountBalanceKafkaSender::outcome);
    }

    @Override
    public BalanceOperationType getOperationType() {
        return BalanceOperationType.COMPLETE;
    }
}
