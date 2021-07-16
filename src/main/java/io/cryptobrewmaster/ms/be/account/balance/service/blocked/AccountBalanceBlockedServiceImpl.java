package io.cryptobrewmaster.ms.be.account.balance.service.blocked;

import io.cryptobrewmaster.ms.be.account.balance.constants.BalanceOperation;
import io.cryptobrewmaster.ms.be.account.balance.db.entity.AccountBalance;
import io.cryptobrewmaster.ms.be.account.balance.db.entity.blocked.AccountBalanceBlocked;
import io.cryptobrewmaster.ms.be.account.balance.db.entity.blocked.AccountBalanceBlockedHistory;
import io.cryptobrewmaster.ms.be.account.balance.db.repository.AccountBalanceRepository;
import io.cryptobrewmaster.ms.be.account.balance.db.repository.blocked.AccountBalanceBlockedHistoryRepository;
import io.cryptobrewmaster.ms.be.account.balance.db.repository.blocked.AccountBalanceBlockedRepository;
import io.cryptobrewmaster.ms.be.account.balance.kafka.balance.AccountBalanceKafkaSender;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceChangeStatus;
import io.cryptobrewmaster.ms.be.library.kafka.dto.account.balance.KafkaAccountBlockedBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.BiConsumer;

@RequiredArgsConstructor
@Service
public class AccountBalanceBlockedServiceImpl implements AccountBalanceBlockedService {

    private final AccountBalanceKafkaSender accountBalanceKafkaSender;

    private final AccountBalanceRepository accountBalanceRepository;
    private final AccountBalanceBlockedRepository accountBalanceBlockedRepository;
    private final AccountBalanceBlockedHistoryRepository accountBalanceBlockedHistoryRepository;

    private final Map<BalanceOperation, BiConsumer<AccountBalance, AccountBalanceBlocked>> operationRollbackMap = Map.of(
            BalanceOperation.ADD, (accountBalance, accountBlockedBalance) -> {
                var newQuantity = accountBalance.getQuantity().subtract(accountBlockedBalance.getBlockedQuantity());
                newQuantity = newQuantity.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : newQuantity;
                accountBalance.setQuantity(newQuantity);
            },
            BalanceOperation.SUBTRACT, (accountBalance, accountBlockedBalance) -> {
                var newQuantity = accountBalance.getQuantity().add(accountBlockedBalance.getBlockedQuantity());
                accountBalance.setQuantity(newQuantity);
            }
    );

    @Transactional
    public void complete(KafkaAccountBlockedBalance kafkaAccountBlockedBalance) {
        var accountBlockedBalance = accountBalanceBlockedRepository.getWithLockByIdAndAccountId(
                kafkaAccountBlockedBalance.getAccountBlockedBalanceId(),
                kafkaAccountBlockedBalance.getAccountId()
        );
        accountBlockedBalance.setStatus(BalanceChangeStatus.DONE);

        var accountBlockedBalanceHistory = AccountBalanceBlockedHistory.of(accountBlockedBalance);
        accountBalanceBlockedHistoryRepository.save(accountBlockedBalanceHistory);

        accountBalanceBlockedRepository.delete(accountBlockedBalance);

        accountBalanceKafkaSender.outcome(accountBlockedBalance.getAccountBalance());
    }

    @Transactional
    public void rollback(KafkaAccountBlockedBalance kafkaAccountBlockedBalance) {
        var accountBlockedBalance = accountBalanceBlockedRepository.getWithLockByIdAndAccountId(
                kafkaAccountBlockedBalance.getAccountBlockedBalanceId(),
                kafkaAccountBlockedBalance.getAccountId()
        );
        accountBlockedBalance.setStatus(BalanceChangeStatus.FAILED);

        var accountBalance = accountBlockedBalance.getAccountBalance();

        operationRollbackMap.get(accountBlockedBalance.getOperation())
                .accept(accountBalance, accountBlockedBalance);

        accountBalanceRepository.save(accountBalance);

        var accountBlockedBalanceHistory = AccountBalanceBlockedHistory.of(accountBlockedBalance);
        accountBalanceBlockedHistoryRepository.save(accountBlockedBalanceHistory);

        accountBalanceBlockedRepository.delete(accountBlockedBalance);
    }

}
