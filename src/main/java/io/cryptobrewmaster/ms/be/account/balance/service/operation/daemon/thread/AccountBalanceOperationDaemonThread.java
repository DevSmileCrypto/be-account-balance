package io.cryptobrewmaster.ms.be.account.balance.service.operation.daemon.thread;

import io.cryptobrewmaster.ms.be.account.balance.dto.AccountBalanceOperation;
import io.cryptobrewmaster.ms.be.account.balance.service.operation.AccountBalanceOperationService;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class AccountBalanceOperationDaemonThread {

    private final Queue<AccountBalanceOperation> accountBalanceOperationQueue;

    private final AccountBalanceOperationService accountBalanceOperationService;

    private final Long sleepTimeout;

    public void start() {
        var thread = new Thread(run());
        thread.setDaemon(true);
        thread.start();
    }

    private Runnable run() {
        return () -> {
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(sleepTimeout);

                    var accountBalanceOperation = accountBalanceOperationQueue.poll();
                    if (Objects.nonNull(accountBalanceOperation)) {
                        accountBalanceOperationService.handleOperation(accountBalanceOperation);
                    }

                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
        };
    }

}
