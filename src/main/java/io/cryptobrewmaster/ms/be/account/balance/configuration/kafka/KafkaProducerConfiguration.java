package io.cryptobrewmaster.ms.be.account.balance.configuration.kafka;

import io.cryptobrewmaster.ms.be.account.balance.configuration.kafka.interceptor.KafkaAccountBalanceOperationCallbackProducerInterceptor;
import io.cryptobrewmaster.ms.be.account.balance.configuration.kafka.interceptor.KafkaAccountBalanceProducerInterceptor;
import io.cryptobrewmaster.ms.be.account.balance.configuration.kafka.interceptor.KafkaProducerInterceptor;
import io.cryptobrewmaster.ms.be.account.balance.configuration.kafka.properties.KafkaProperties;
import io.cryptobrewmaster.ms.be.library.kafka.dto.account.balance.KafkaAccountBalance;
import io.cryptobrewmaster.ms.be.library.kafka.dto.account.balance.KafkaAccountBalanceOperationCallback;
import io.cryptobrewmaster.ms.be.library.kafka.serde.account.balance.KafkaAccountBalanceOperationCallbackSerde;
import io.cryptobrewmaster.ms.be.library.kafka.serde.account.balance.KafkaAccountBalanceSerde;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@EnableKafka
@Configuration
public class KafkaProducerConfiguration {

    private final KafkaProperties kafkaProperties;

    private <V> Map<String, Object> producerConfigs(Class<? extends Serializer<V>> valueSerializer,
                                                    Class<? extends KafkaProducerInterceptor<V>> interceptorClass) {

        var config = new HashMap<String, Object>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        config.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, interceptorClass.getName());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        return config;
    }

    private <V> KafkaTemplate<String, V> getKafkaTemplate(Class<? extends Serializer<V>> valueSerializer,
                                                          Class<? extends KafkaProducerInterceptor<V>> interceptorClass) {

        var producerConfigs = producerConfigs(valueSerializer, interceptorClass);
        var kafkaProducerFactory = new DefaultKafkaProducerFactory<String, V>(producerConfigs);
        return new KafkaTemplate<>(kafkaProducerFactory);
    }

    @Bean
    public KafkaTemplate<String, KafkaAccountBalance> accountBalanceKafkaTemplate() {
        return getKafkaTemplate(KafkaAccountBalanceSerde.class, KafkaAccountBalanceProducerInterceptor.class);
    }

    @Bean
    public KafkaTemplate<String, KafkaAccountBalanceOperationCallback> accountBalanceOperationCallbackKafkaTemplate() {
        return getKafkaTemplate(KafkaAccountBalanceOperationCallbackSerde.class, KafkaAccountBalanceOperationCallbackProducerInterceptor.class);
    }

}
