package io.cryptobrewmaster.ms.be.account.balance.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cryptobrewmaster.ms.be.account.balance.dto.AccountBalanceOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.PriorityQueue;
import java.util.Queue;

@Configuration
public class MicroServiceConfiguration {

    @Bean
    public Clock utcClock() {
        return Clock.systemUTC();
    }

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }

    @Bean
    public Queue<AccountBalanceOperation> accountBalanceOperationQueue() {
        return new PriorityQueue<>(AccountBalanceOperation.COMPARATOR);
    }

}
