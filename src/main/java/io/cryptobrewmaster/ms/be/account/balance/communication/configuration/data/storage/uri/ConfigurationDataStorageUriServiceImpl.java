package io.cryptobrewmaster.ms.be.account.balance.communication.configuration.data.storage.uri;

import io.cryptobrewmaster.ms.be.account.balance.communication.configuration.data.storage.properties.ConfigurationDataStorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@Service
public class ConfigurationDataStorageUriServiceImpl implements ConfigurationDataStorageUriService {

    private final ConfigurationDataStorageProperties configurationDataStorageProperties;

    @Override
    public URI getAllBalanceConfigUri() {
        return UriComponentsBuilder.fromUriString(configurationDataStorageProperties.getUri())
                .path(configurationDataStorageProperties.getPath().getBalance())
                .build()
                .encode()
                .toUri();
    }
}
