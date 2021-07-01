package io.cryptobrewmaster.ms.be.account.balance.communication.configuration.data.storage.service;

import io.cryptobrewmaster.ms.be.account.balance.communication.configuration.data.storage.dto.BalanceConfigDto;

import java.util.List;

/**
 * The interface Configuration data storage communication service.
 */
public interface ConfigurationDataStorageCommunicationService {

    /**
     * Gets all balance config.
     *
     * @return the all balance config
     */
    List<BalanceConfigDto> getAllBalanceConfig();

}
