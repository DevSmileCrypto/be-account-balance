package io.cryptobrewmaster.ms.be.account.balance.configuration.rest;

import io.cryptobrewmaster.ms.be.account.balance.communication.configuration.data.storage.properties.ConfigurationDataStorageProperties;
import io.cryptobrewmaster.ms.be.account.balance.configuration.rest.properties.RestTemplateProperties;
import io.cryptobrewmaster.ms.be.library.configuration.rest.interceptor.JsonContentTypeRestTemplateInterceptor;
import io.cryptobrewmaster.ms.be.library.exception.integration.CommunicationErrorHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import static io.cryptobrewmaster.ms.be.library.constants.MicroServiceName.BE_CONFIGURATION_DATA_STORAGE;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager(RestTemplateProperties restTemplateProperties) {
        var manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(restTemplateProperties.getMaxTotal());
        manager.setDefaultMaxPerRoute(restTemplateProperties.getDefaultMaxPerRoute());
        return manager;
    }

    @Bean
    public RequestConfig requestConfig(RestTemplateProperties restTemplateProperties) {
        return RequestConfig.custom()
                .setSocketTimeout(restTemplateProperties.getSocketTimeout())
                .build();
    }

    @Bean
    public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager, RequestConfig requestConfig) {
        return HttpClientBuilder.create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder(HttpClient httpClient) {
        return new RestTemplateBuilder()
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    @Bean(name = "configurationDataStorageRestTemplate")
    public RestTemplate configurationDataStorageRestTemplate(RestTemplateBuilder restTemplateBuilder,
                                                             ConfigurationDataStorageProperties configurationDataStorageProperties) {
        return restTemplateBuilder.errorHandler(new CommunicationErrorHandler(BE_CONFIGURATION_DATA_STORAGE))
                .rootUri(configurationDataStorageProperties.getUri())
                .interceptors(new JsonContentTypeRestTemplateInterceptor())
                .setConnectTimeout(Duration.ofMillis(configurationDataStorageProperties.getTimeout().getConnect()))
                .setReadTimeout(Duration.ofMillis(configurationDataStorageProperties.getTimeout().getRead()))
                .build();
    }

}
