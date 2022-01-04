package io.cryptobrewmaster.ms.be.account.balance.kafka.balance;

import io.cryptobrewmaster.ms.be.account.balance.configuration.kafka.properties.KafkaProperties;
import io.cryptobrewmaster.ms.be.account.balance.db.jpa.entity.balance.AccountBalance;
import io.cryptobrewmaster.ms.be.account.balance.dto.AccountBalanceOperation;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceOperationStatus;
import io.cryptobrewmaster.ms.be.library.constants.kafka.KafkaCustomHeaders;
import io.cryptobrewmaster.ms.be.library.kafka.dto.account.balance.KafkaAccountBalance;
import io.cryptobrewmaster.ms.be.library.kafka.dto.account.balance.KafkaAccountBalanceOperationCallback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

import static io.cryptobrewmaster.ms.be.account.balance.kafka.balance.converter.AccountBalanceKafkaConverter.toBalanceKafkaDto;
import static io.cryptobrewmaster.ms.be.account.balance.kafka.balance.converter.AccountBalanceKafkaConverter.toBalanceOperationCallbackKafkaDto;
import static io.cryptobrewmaster.ms.be.account.balance.utility.KafkaUtility.toRecord;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountBalanceKafkaSender {

    private final KafkaTemplate<String, KafkaAccountBalance> accountBalanceKafkaTemplate;
    private final KafkaTemplate<String, KafkaAccountBalanceOperationCallback> accountBalanceOperationCallbackKafkaTemplate;

    private final KafkaProperties kafkaProperties;

    public void outcome(AccountBalance accountBalance) {
        var kafkaAccountBalance = toBalanceKafkaDto(accountBalance);

        log.info("Send message for account balance outcome: {}", kafkaAccountBalance);

        accountBalanceKafkaTemplate.send(
                toRecord(
                        kafkaProperties.getTopic().getAccountBalanceOutcome(),
                        kafkaAccountBalance.getAccountId(),
                        kafkaAccountBalance
                )
        );
    }

    public void handleOperationCallback(AccountBalanceOperation accountBalanceOperation, BalanceOperationStatus status) {
        handleOperationCallback(accountBalanceOperation, status, null);
    }

    public void handleOperationCallback(AccountBalanceOperation accountBalanceOperation,
                                        BalanceOperationStatus status, Long accountBlockedBalanceId) {
        var kafkaAccountBalanceOperationCallback = toBalanceOperationCallbackKafkaDto(
                accountBalanceOperation, status, accountBlockedBalanceId
        );

        log.info("Send message for account balance operation handle callback: {}", kafkaAccountBalanceOperationCallback);

        accountBalanceOperationCallbackKafkaTemplate.send(
                toRecord(
                        kafkaProperties.getTopic().getAccountBalanceOperationHandleCallback(),
                        accountBalanceOperation.getAccountId(),
                        kafkaAccountBalanceOperationCallback,
                        Map.of(KafkaCustomHeaders.SERVICE_TARGET.getHeaderName(), accountBalanceOperation.getProducerServiceName().name())
                )
        );
    }
}
