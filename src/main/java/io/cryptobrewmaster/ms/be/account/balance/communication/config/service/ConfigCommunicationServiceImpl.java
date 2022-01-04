package io.cryptobrewmaster.ms.be.account.balance.communication.config.service;

import io.cryptobrewmaster.ms.be.account.balance.communication.config.dto.BalanceConfigDto;
import io.cryptobrewmaster.ms.be.account.balance.communication.config.uri.ConfigUriService;
import io.cryptobrewmaster.ms.be.library.communication.BaseCommunicationService;
import io.cryptobrewmaster.ms.be.library.communication.model.RequestLog;
import io.cryptobrewmaster.ms.be.library.constants.ServiceName;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
public class ConfigCommunicationServiceImpl extends BaseCommunicationService implements ConfigCommunicationService {

    private final ConfigUriService configUriService;

    public ConfigCommunicationServiceImpl(RestTemplate configRestTemplate, ConfigUriService configUriService) {
        super(configRestTemplate);
        this.configUriService = configUriService;
    }

    String getServiceName() {
        return ServiceName.BE_CONFIG.getProviderName();
    }

    @Override
    public List<BalanceConfigDto> getAllBalanceConfig() {
        List<Object> logArgs = List.of(getServiceName());
        return performRequestWithResponse(
                configUriService.getAllBalanceConfigUri(),
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
