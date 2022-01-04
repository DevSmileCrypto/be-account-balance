package io.cryptobrewmaster.ms.be.account.balance.kafka.balance;

import io.cryptobrewmaster.ms.be.account.balance.dto.AccountBalanceOperation;
import io.cryptobrewmaster.ms.be.account.balance.dto.converter.AccountBalanceOperationConverter;
import io.cryptobrewmaster.ms.be.library.constants.ServiceName;
import io.cryptobrewmaster.ms.be.library.constants.kafka.KafkaCustomHeaders;
import io.cryptobrewmaster.ms.be.library.kafka.dto.account.balance.KafkaAccountBalanceOperation;
import io.cryptobrewmaster.ms.be.library.util.KafkaConsumerMDCUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Queue;

@Slf4j
@RequiredArgsConstructor
@Component
public class AccountBalanceKafkaConsumer {

    private final Queue<AccountBalanceOperation> accountBalanceOperationQueue;

    @KafkaListener(
            topics = "${kafka.topic.account-balance-operation-handle}",
            groupId = "${kafka.config.group-id}",
            clientIdPrefix = "${kafka.config.client-id}-${kafka.topic.account-balance-operation-handle}-${server.port}",
            containerFactory = "accountBalanceOperationConcurrentKafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, KafkaAccountBalanceOperation> consumerRecord) {
        var kafkaAccountBalanceOperation = consumerRecord.value();
        var kafkaAccountBalanceOperationLogInfo = kafkaAccountBalanceOperation.toString();

        var producerHeader = consumerRecord.headers().lastHeader(KafkaCustomHeaders.SERVICE_PRODUCER.getHeaderName());
        var producerServiceName = ServiceName.valueOf(new String(producerHeader.value(), StandardCharsets.UTF_8));

        KafkaConsumerMDCUtil.submit(
                consumerRecord,
                () -> {
                    log.info("Consumed message for handle account balance operation: {}", kafkaAccountBalanceOperationLogInfo);
                    accountBalanceOperationQueue.add(
                            AccountBalanceOperationConverter.toModel(consumerRecord.key(), producerServiceName, kafkaAccountBalanceOperation)
                    );
                    log.info("Processed message for handle account balance operation: {}", kafkaAccountBalanceOperationLogInfo);
                },
                e -> log.error("Error while on consumed for handle account balance operation: {}. Error = {}",
                        kafkaAccountBalanceOperationLogInfo, e.getMessage())
        );
    }

}
