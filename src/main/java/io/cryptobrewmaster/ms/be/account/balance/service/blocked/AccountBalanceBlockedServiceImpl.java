package io.cryptobrewmaster.ms.be.account.balance.service.blocked;

import io.cryptobrewmaster.ms.be.account.balance.db.repository.AccountBalanceRepository;
import io.cryptobrewmaster.ms.be.account.balance.db.repository.blocked.AccountBalanceBlockedRepository;
import io.cryptobrewmaster.ms.be.account.balance.kafka.balance.AccountBalanceKafkaSender;
import io.cryptobrewmaster.ms.be.account.balance.service.blocked.history.AccountBalanceBlockedHistoryService;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceChangeStatus;
import io.cryptobrewmaster.ms.be.library.kafka.dto.account.balance.KafkaAccountBalanceBlocked;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AccountBalanceBlockedServiceImpl implements AccountBalanceBlockedService {

    private final AccountBalanceKafkaSender accountBalanceKafkaSender;

    private final AccountBalanceRepository accountBalanceRepository;
    private final AccountBalanceBlockedRepository accountBalanceBlockedRepository;
    private final AccountBalanceBlockedHistoryService accountBalanceBlockedHistoryService;

    @Transactional
    public void complete(KafkaAccountBalanceBlocked kafkaAccountBalanceBlocked) {
        var accountBalanceBlocked = accountBalanceBlockedRepository.getWithLockByIdAndAccountId(
                kafkaAccountBalanceBlocked.getAccountBalanceBlockedId(),
                kafkaAccountBalanceBlocked.getAccountId()
        );
        accountBalanceBlocked.setStatus(BalanceChangeStatus.DONE);

        accountBalanceBlockedHistoryService.saveHistory(accountBalanceBlocked);

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

        accountBalanceBlocked.getOperation()
                .getRollbackOperationBiConsumer()
                .accept(accountBalance, accountBalanceBlocked);

        accountBalanceRepository.save(accountBalance);

        accountBalanceBlockedHistoryService.saveHistory(accountBalanceBlocked);

        accountBalanceBlockedRepository.delete(accountBalanceBlocked);
    }

}
