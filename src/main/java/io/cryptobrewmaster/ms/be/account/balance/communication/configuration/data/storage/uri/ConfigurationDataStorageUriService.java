package io.cryptobrewmaster.ms.be.account.balance.communication.configuration.data.storage.uri;


import java.net.URI;

/**
 * The interface Configuration data storage uri service.
 */
public interface ConfigurationDataStorageUriService {

    /**
     * Gets all balance config uri.
     *
     * @return the all balance config uri
     */
    URI getAllBalanceConfigUri();

}
