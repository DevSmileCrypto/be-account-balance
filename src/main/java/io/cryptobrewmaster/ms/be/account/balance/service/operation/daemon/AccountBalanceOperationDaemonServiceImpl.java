package io.cryptobrewmaster.ms.be.account.balance.service.operation.daemon;

import io.cryptobrewmaster.ms.be.account.balance.db.mongodb.repository.AccountBalanceOperationTransactionRepository;
import io.cryptobrewmaster.ms.be.account.balance.dto.AccountBalanceOperation;
import io.cryptobrewmaster.ms.be.account.balance.dto.converter.AccountBalanceOperationConverter;
import io.cryptobrewmaster.ms.be.account.balance.service.operation.AccountBalanceOperationService;
import io.cryptobrewmaster.ms.be.account.balance.service.operation.daemon.thread.AccountBalanceOperationDaemonThread;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceOperationTransactionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Service
public class AccountBalanceOperationDaemonServiceImpl implements AccountBalanceOperationDaemonService {

    private final Queue<AccountBalanceOperation> accountBalanceOperationQueue;

    private final AccountBalanceOperationService accountBalanceOperationService;

    private final AccountBalanceOperationTransactionRepository accountBalanceOperationTransactionRepository;

    @Value("${account-balance.operation.pagination.size}")
    private Integer size;
    @Value("${account-balance.operation.daemon.sleep-timeout}")
    private Long sleepTimeout;

    @Override
    public void startDaemon() {
        var page = new AtomicInteger(0);

        var transactions = accountBalanceOperationTransactionRepository.findAllByOperationTransactionStatus(
                BalanceOperationTransactionStatus.CREATED, page.getAndIncrement(), size
        );
        while (!transactions.isEmpty()) {
            transactions.stream()
                    .map(AccountBalanceOperationConverter::toModel)
                    .forEach(accountBalanceOperationQueue::add);

            transactions = accountBalanceOperationTransactionRepository.findAllByOperationTransactionStatus(
                    BalanceOperationTransactionStatus.CREATED, page.getAndIncrement(), size
            );
        }

        var thread = new AccountBalanceOperationDaemonThread(accountBalanceOperationQueue, accountBalanceOperationService, sleepTimeout);
        thread.start();
    }
}
