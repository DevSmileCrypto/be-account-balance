package io.cryptobrewmaster.ms.be.account.balance.communication.config.uri;

import io.cryptobrewmaster.ms.be.account.balance.communication.config.properties.ConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@Service
public class ConfigUriServiceImpl implements ConfigUriService {

    private final ConfigProperties configProperties;

    @Override
    public URI getAllBalanceConfigUri() {
        return UriComponentsBuilder.fromUriString(configProperties.getUri())
                .path(configProperties.getPath().getBalanceConfigAll())
                .build()
                .encode()
                .toUri();
    }
}
