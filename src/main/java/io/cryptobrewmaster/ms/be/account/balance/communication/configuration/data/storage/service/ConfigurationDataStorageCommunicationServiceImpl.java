package io.cryptobrewmaster.ms.be.account.balance.communication.configuration.data.storage.service;

import io.cryptobrewmaster.ms.be.account.balance.communication.configuration.data.storage.dto.BalanceConfigDto;
import io.cryptobrewmaster.ms.be.account.balance.communication.configuration.data.storage.uri.ConfigurationDataStorageUriService;
import io.cryptobrewmaster.ms.be.library.communication.BaseCommunicationService;
import io.cryptobrewmaster.ms.be.library.communication.model.RequestLog;
import io.cryptobrewmaster.ms.be.library.constants.MicroServiceName;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
public class ConfigurationDataStorageCommunicationServiceImpl extends BaseCommunicationService implements ConfigurationDataStorageCommunicationService {

    private final ConfigurationDataStorageUriService configurationDataStorageUriService;

    public ConfigurationDataStorageCommunicationServiceImpl(RestTemplate configurationDataStorageRestTemplate,
                                                            ConfigurationDataStorageUriService configurationDataStorageUriService) {
        super(configurationDataStorageRestTemplate);
        this.configurationDataStorageUriService = configurationDataStorageUriService;
    }

    String getMicroServiceName() {
        return MicroServiceName.BE_CONFIGURATION_DATA_STORAGE.getProviderName();
    }

    @Override
    public List<BalanceConfigDto> getAllBalanceConfig() {
        List<Object> logArgs = List.of(getMicroServiceName());
        return performRequestWithResponse(
                configurationDataStorageUriService.getAllBalanceConfigUri(),
                HttpMethod.GET,
                new ParameterizedTypeReference<>() {
                },
                new RequestLog(
                        "Request to get configuration data for balances send to %s ms.", logArgs,
                        "Response on get configuration data for balances from %s ms.", logArgs,
                        "No response from %s ms on get configuration data for balances request.", logArgs
                )
        );
    }
}
