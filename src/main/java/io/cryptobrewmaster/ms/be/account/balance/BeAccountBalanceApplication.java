package io.cryptobrewmaster.ms.be.account.balance;

import io.cryptobrewmaster.ms.be.account.balance.service.operation.daemon.AccountBalanceOperationDaemonService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableJpaRepositories(basePackages = "io.cryptobrewmaster.ms.be.account.balance.db.jpa.repository")
@EnableMongoRepositories(basePackages = "io.cryptobrewmaster.ms.be.account.balance.db.mongodb.repository")
@SpringBootApplication
public class BeAccountBalanceApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(BeAccountBalanceApplication.class, args);
        var accountBalanceOperationDaemonService = context.getBean(AccountBalanceOperationDaemonService.class);
        accountBalanceOperationDaemonService.startDaemon();
    }

}
