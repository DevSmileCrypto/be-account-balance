package io.cryptobrewmaster.ms.be.account.balance.communication.config.uri;


import java.net.URI;

/**
 * The interface Configuration data storage uri service.
 */
public interface ConfigUriService {

    /**
     * Gets all balance config uri.
     *
     * @return the all balance config uri
     */
    URI getAllBalanceConfigUri();

}
