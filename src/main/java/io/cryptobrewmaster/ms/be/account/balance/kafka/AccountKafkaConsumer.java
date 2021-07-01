package io.cryptobrewmaster.ms.be.account.balance.kafka;

import io.cryptobrewmaster.ms.be.library.kafka.dto.account.KafkaAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AccountKafkaConsumer {

    private final AccountKafkaReceiver accountKafkaReceiver;

    @KafkaListener(
            topics = "${kafka.topic.account-init}",
            groupId = "${kafka.config.group-id}",
            clientIdPrefix = "${kafka.config.client-id}-${kafka.topic.account-init}-${server.port}",
            containerFactory = "accountConcurrentKafkaListenerContainerFactory"
    )
    public void consumeAndInit(ConsumerRecord<String, KafkaAccount> consumerRecord) {
        log.debug("Consumed message for init account: Consumer record = {}", consumerRecord);

        var kafkaAccount = consumerRecord.value();
        var accountLogInfo = kafkaAccount.toString();

        try {
            log.info("Consumed message for init account: {}", accountLogInfo);
            accountKafkaReceiver.init(kafkaAccount);
            log.info("Processed message for init account: {}", accountLogInfo);
        } catch (Exception e) {
            log.error("Error while on consumed for init account: {}. Error = {}",
                    accountLogInfo, e.getMessage());
        }
    }

}
