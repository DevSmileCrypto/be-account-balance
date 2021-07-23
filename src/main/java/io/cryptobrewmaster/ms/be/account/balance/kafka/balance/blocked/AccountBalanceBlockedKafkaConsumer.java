package io.cryptobrewmaster.ms.be.account.balance.kafka.balance.blocked;

import io.cryptobrewmaster.ms.be.account.balance.service.blocked.AccountBalanceBlockedService;
import io.cryptobrewmaster.ms.be.library.kafka.dto.account.balance.KafkaAccountBalanceBlocked;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AccountBalanceBlockedKafkaConsumer {

    private final AccountBalanceBlockedService accountBalanceBlockedService;

    @KafkaListener(
            topics = "${kafka.topic.account-balance-blocked-complete}",
            groupId = "${kafka.config.group-id}",
            clientIdPrefix = "${kafka.config.client-id}-${kafka.topic.account-balance-blocked-complete}-${server.port}",
            containerFactory = "accountBalanceBlockedConcurrentKafkaListenerContainerFactory"
    )
    public void consumeAndComplete(ConsumerRecord<String, KafkaAccountBalanceBlocked> consumerRecord) {
        log.debug("Consumed message for complete account balance blocked: Consumer record = {}", consumerRecord);

        var kafkaAccountBalanceBlocked = consumerRecord.value();
        var accountBalanceBlockedLogInfo = kafkaAccountBalanceBlocked.toString();

        try {
            log.info("Consumed message for complete account balance blocked: {}", accountBalanceBlockedLogInfo);
            accountBalanceBlockedService.complete(kafkaAccountBalanceBlocked);
            log.info("Processed message for complete account balance blocked: {}", accountBalanceBlockedLogInfo);
        } catch (Exception e) {
            log.error("Error while on consumed for complete account balance blocked: {}. Error = {}",
                    accountBalanceBlockedLogInfo, e.getMessage());
        }
    }

    @KafkaListener(
            topics = "${kafka.topic.account-balance-blocked-rollback}",
            groupId = "${kafka.config.group-id}",
            clientIdPrefix = "${kafka.config.client-id}-${kafka.topic.account-balance-blocked-rollback}-${server.port}",
            containerFactory = "accountBalanceBlockedConcurrentKafkaListenerContainerFactory"
    )
    public void consumeAndRollback(ConsumerRecord<String, KafkaAccountBalanceBlocked> consumerRecord) {
        log.debug("Consumed message for rollback account balance blocked: Consumer record = {}", consumerRecord);

        var kafkaAccountBalanceBlocked = consumerRecord.value();
        var accountBalanceBlockedLogInfo = kafkaAccountBalanceBlocked.toString();

        try {
            log.info("Consumed message for rollback account balance blocked: {}", accountBalanceBlockedLogInfo);
            accountBalanceBlockedService.rollback(kafkaAccountBalanceBlocked);
            log.info("Processed message for rollback account balance blocked: {}", accountBalanceBlockedLogInfo);
        } catch (Exception e) {
            log.error("Error while on consumed for rollback account balance blocked: {}. Error = {}",
                    accountBalanceBlockedLogInfo, e.getMessage());
        }
    }

}
