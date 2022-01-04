package io.cryptobrewmaster.ms.be.account.balance.configuration.kafka.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static io.cryptobrewmaster.ms.be.library.constants.ServiceName.BE_ACCOUNT_BALANCE;
import static io.cryptobrewmaster.ms.be.library.constants.kafka.KafkaCustomHeaders.SERVICE_PRODUCER;
import static io.cryptobrewmaster.ms.be.library.constants.kafka.KafkaCustomHeaders.TRACE_ID;
import static io.cryptobrewmaster.ms.be.library.util.MDCUtil.getTraceIdBytes;

public abstract class KafkaProducerInterceptor<T> implements ProducerInterceptor<String, T> {

    @Override
    public ProducerRecord<String, T> onSend(ProducerRecord<String, T> record) {
        var key = Optional.ofNullable(record.key()).orElse(UUID.randomUUID().toString());

        var headers = record.headers();
        headers.add(TRACE_ID.getHeaderName(), getTraceIdBytes());
        headers.add(SERVICE_PRODUCER.getHeaderName(), BE_ACCOUNT_BALANCE.name().getBytes(StandardCharsets.UTF_8));

        return new ProducerRecord<>(
                record.topic(), record.partition(), key,
                record.value(), headers
        );
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }

}
