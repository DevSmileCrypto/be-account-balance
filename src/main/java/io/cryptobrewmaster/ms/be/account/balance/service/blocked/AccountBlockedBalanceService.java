package io.cryptobrewmaster.ms.be.account.balance.service.blocked;

import io.cryptobrewmaster.ms.be.library.kafka.dto.account.balance.KafkaAccountBlockedBalance;

/**
 * The interface Account blocked balance service.
 */
public interface AccountBlockedBalanceService {

    /**
     * Complete.
     *
     * @param kafkaAccountBlockedBalance the kafka account blocked balance
     */
    void complete(KafkaAccountBlockedBalance kafkaAccountBlockedBalance);

    /**
     * Rollback.
     *
     * @param kafkaAccountBlockedBalance the kafka account blocked balance
     */
    void rollback(KafkaAccountBlockedBalance kafkaAccountBlockedBalance);

}
