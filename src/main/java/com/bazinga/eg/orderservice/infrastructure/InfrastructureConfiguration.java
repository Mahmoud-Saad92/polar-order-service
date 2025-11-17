package com.bazinga.eg.orderservice.infrastructure;

import com.bazinga.eg.orderservice.infrastructure.proxy.ClientConfiguration;
import com.bazinga.eg.orderservice.infrastructure.proxy.property.ClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@Import({ClientConfiguration.class})
@EnableConfigurationProperties({ClientProperties.class})
public class InfrastructureConfiguration {
}
