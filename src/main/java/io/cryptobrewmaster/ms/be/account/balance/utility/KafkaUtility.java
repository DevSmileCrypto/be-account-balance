package io.cryptobrewmaster.ms.be.account.balance.utility;

import io.cryptobrewmaster.ms.be.library.constants.ServiceName;
import io.cryptobrewmaster.ms.be.library.constants.kafka.KafkaCustomHeaders;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KafkaUtility {

    public static final String SERVICE_TARGET_DELIMITER = ";";

    public static  <T> ProducerRecord<String, T> toRecord(String topic, String key, T value, Map<String, String> headers) {
        var producerRecord = new ProducerRecord<>(topic, null, key, value);

        headers.forEach((headerKey, headerValue) -> producerRecord.headers().add(headerKey, headerValue.getBytes(StandardCharsets.UTF_8)));

        return producerRecord;
    }

    public static <T> ProducerRecord<String, T> toRecord(String topic, String key, T value) {
        return toRecord(topic, key, value, Map.of());
    }

    public static <T> RecordFilterStrategy<String, T> getTargetServiceRecordFilterStrategy() {
        return consumerRecord -> {
            var targetHeader = consumerRecord.headers().lastHeader(KafkaCustomHeaders.SERVICE_TARGET.getHeaderName());
            var targets = new String(targetHeader.value(), StandardCharsets.UTF_8).split(SERVICE_TARGET_DELIMITER);
            var targetServiceNames = Stream.of(targets)
                    .map(ServiceName::valueOf)
                    .collect(Collectors.toList());

            return targetServiceNames.contains(ServiceName.BE_ACCOUNT_BALANCE);
        };
    }

}
