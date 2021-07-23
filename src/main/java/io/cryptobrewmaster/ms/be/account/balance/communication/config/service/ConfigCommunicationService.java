package io.cryptobrewmaster.ms.be.account.balance.communication.config.service;

import io.cryptobrewmaster.ms.be.account.balance.communication.config.dto.BalanceConfigDto;

import java.util.List;

/**
 * The interface Configuration data storage communication service.
 */
public interface ConfigCommunicationService {

    /**
     * Gets all balance config.
     *
     * @return the all balance config
     */
    List<BalanceConfigDto> getAllBalanceConfig();

}
