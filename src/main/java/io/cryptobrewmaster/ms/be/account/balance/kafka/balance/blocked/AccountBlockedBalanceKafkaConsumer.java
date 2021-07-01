package io.cryptobrewmaster.ms.be.account.balance.kafka.balance.blocked;

import io.cryptobrewmaster.ms.be.library.kafka.dto.account.balance.KafkaAccountBlockedBalance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AccountBlockedBalanceKafkaConsumer {

    private final AccountBlockedBalanceKafkaReceiver accountBlockedBalanceKafkaReceiver;

    @KafkaListener(
            topics = "${kafka.topic.account-balance-block-complete}",
            groupId = "${kafka.config.group-id}",
            clientIdPrefix = "${kafka.config.client-id}-${kafka.topic.account-balance-block-complete}-${server.port}",
            containerFactory = "accountBlockedBalanceConcurrentKafkaListenerContainerFactory"
    )
    public void consumeAndComplete(ConsumerRecord<String, KafkaAccountBlockedBalance> consumerRecord) {
        log.debug("Consumed message for complete account balance: Consumer record = {}", consumerRecord);

        var kafkaAccountBlockedBalance = consumerRecord.value();
        var accountBlockedBalanceLogInfo = kafkaAccountBlockedBalance.toString();

        try {
            log.info("Consumed message for complete account balance: {}", accountBlockedBalanceLogInfo);
            accountBlockedBalanceKafkaReceiver.complete(kafkaAccountBlockedBalance);
            log.info("Processed message for complete account balance: {}", accountBlockedBalanceLogInfo);
        } catch (Exception e) {
            log.error("Error while on consumed for complete account balance: {}. Error = {}",
                    accountBlockedBalanceLogInfo, e.getMessage());
        }
    }

    @KafkaListener(
            topics = "${kafka.topic.account-balance-block-rollback}",
            groupId = "${kafka.config.group-id}",
            clientIdPrefix = "${kafka.config.client-id}-${kafka.topic.account-balance-block-rollback}-${server.port}",
            containerFactory = "accountBlockedBalanceConcurrentKafkaListenerContainerFactory"
    )
    public void consumeAndRollback(ConsumerRecord<String, KafkaAccountBlockedBalance> consumerRecord) {
        log.debug("Consumed message for rollback account balance kafka: Consumer record = {}", consumerRecord);

        var kafkaAccountBlockedBalance = consumerRecord.value();
        var accountBlockedBalanceLogInfo = kafkaAccountBlockedBalance.toString();

        try {
            log.info("Consumed message for rollback account balance: {}", accountBlockedBalanceLogInfo);
            accountBlockedBalanceKafkaReceiver.rollback(kafkaAccountBlockedBalance);
            log.info("Processed message for rollback account balance: {}", accountBlockedBalanceLogInfo);
        } catch (Exception e) {
            log.error("Error while on consumed for rollback account balance: {}. Error = {}",
                    accountBlockedBalanceLogInfo, e.getMessage());
        }
    }

}
