package io.cryptobrewmaster.ms.be.account.balance.communication.configuration.data.storage.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "ms.configuration-data-storage")
public class ConfigurationDataStorageProperties {

    private String uri;
    private Path path;
    private Timeout timeout;

    @Getter
    @Setter
    public static class Path {
        private String balance;
    }

    @Getter
    @Setter
    public static class Timeout {
        private Integer connect;
        private Integer read;
    }

}
