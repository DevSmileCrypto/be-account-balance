package io.cryptobrewmaster.ms.be.account.balance.service.blocked;

import io.cryptobrewmaster.ms.be.account.balance.constants.BalanceOperation;
import io.cryptobrewmaster.ms.be.account.balance.db.entity.AccountBalance;
import io.cryptobrewmaster.ms.be.account.balance.db.entity.blocked.AccountBalanceBlocked;
import io.cryptobrewmaster.ms.be.account.balance.db.repository.AccountBalanceRepository;
import io.cryptobrewmaster.ms.be.account.balance.db.repository.blocked.AccountBalanceBlockedRepository;
import io.cryptobrewmaster.ms.be.account.balance.kafka.balance.AccountBalanceKafkaSender;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceChangeStatus;
import io.cryptobrewmaster.ms.be.library.kafka.dto.account.balance.KafkaAccountBalanceBlocked;
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
    public void complete(KafkaAccountBalanceBlocked kafkaAccountBalanceBlocked) {
        var accountBalanceBlocked = accountBalanceBlockedRepository.getWithLockByIdAndAccountId(
                kafkaAccountBalanceBlocked.getAccountBalanceBlockedId(),
                kafkaAccountBalanceBlocked.getAccountId()
        );
        accountBalanceBlocked.setStatus(BalanceChangeStatus.DONE);

        accountBalanceBlockedRepository.delete(accountBalanceBlocked);

        accountBalanceKafkaSender.outcome(accountBalanceBlocked.getAccountBalance());
    }

    @Transactional
    public void rollback(KafkaAccountBalanceBlocked kafkaAccountBalanceBlocked) {
        var accountBalanceBlocked = accountBalanceBlockedRepository.getWithLockByIdAndAccountId(
                kafkaAccountBalanceBlocked.getAccountBalanceBlockedId(),
                kafkaAccountBalanceBlocked.getAccountId()
        );
        accountBalanceBlocked.setStatus(BalanceChangeStatus.FAILED);

        var accountBalance = accountBalanceBlocked.getAccountBalance();

        operationRollbackMap.get(accountBalanceBlocked.getOperation())
                .accept(accountBalance, accountBalanceBlocked);

        accountBalanceRepository.save(accountBalance);

        accountBalanceBlockedRepository.delete(accountBalanceBlocked);
    }

}
