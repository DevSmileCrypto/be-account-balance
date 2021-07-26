package io.cryptobrewmaster.ms.be.account.balance.kafka.balance;

import io.cryptobrewmaster.ms.be.account.balance.configuration.kafka.properties.KafkaProperties;
import io.cryptobrewmaster.ms.be.account.balance.db.entity.balance.AccountBalance;
import io.cryptobrewmaster.ms.be.library.kafka.dto.account.balance.KafkaAccountBalance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountBalanceKafkaSender {

    private final KafkaTemplate<String, KafkaAccountBalance> accountBalanceKafkaTemplate;

    private final KafkaProperties kafkaProperties;

    public void outcome(AccountBalance accountBalance) {
        var kafkaAccountBalance = accountBalance.generateKafkaDto();

        log.info("Send message to gateway ms for account balance outcome: {}", kafkaAccountBalance);

        accountBalanceKafkaTemplate.send(
                kafkaProperties.getTopic().getAccountBalanceOutcome(),
                kafkaAccountBalance.getAccountId(),
                kafkaAccountBalance
        );
    }

}
