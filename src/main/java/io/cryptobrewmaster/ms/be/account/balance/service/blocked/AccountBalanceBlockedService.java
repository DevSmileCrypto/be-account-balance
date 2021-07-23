package io.cryptobrewmaster.ms.be.account.balance.service.blocked;

import io.cryptobrewmaster.ms.be.library.kafka.dto.account.balance.KafkaAccountBalanceBlocked;

/**
 * The interface Account balance blocked service.
 */
public interface AccountBalanceBlockedService {

    /**
     * Complete.
     *
     * @param kafkaAccountBalanceBlocked the kafka account balance blocked
     */
    void complete(KafkaAccountBalanceBlocked kafkaAccountBalanceBlocked);

    /**
     * Rollback.
     *
     * @param kafkaAccountBalanceBlocked the kafka account balance blocked
     */
    void rollback(KafkaAccountBalanceBlocked kafkaAccountBalanceBlocked);

}
