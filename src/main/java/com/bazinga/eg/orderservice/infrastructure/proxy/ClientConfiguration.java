package com.bazinga.eg.orderservice.infrastructure.proxy;

import com.bazinga.eg.orderservice.infrastructure.proxy.property.ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class ClientConfiguration {

    @Bean
    public WebClient webClient(ClientProperties clientProperties, WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl(clientProperties.getCatalogServiceUri().toString()).build();
    }

    @Bean
    public BookClient bookClient(WebClient webClient, ClientProperties clientProperties) {
        return new BookClient(webClient, clientProperties);
    }
}
