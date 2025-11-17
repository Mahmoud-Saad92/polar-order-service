package com.bazinga.eg.orderservice.common.configuration;

import com.bazinga.eg.orderservice.application.domain.repository.OrderRepository;
import com.bazinga.eg.orderservice.application.service.OrderService;
import com.bazinga.eg.orderservice.application.service.impl.OrderServiceImpl;
import com.bazinga.eg.orderservice.infrastructure.InfrastructureConfiguration;
import com.bazinga.eg.orderservice.infrastructure.proxy.BookClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DomainConfiguration.class, InfrastructureConfiguration.class})
public class ApplicationConfiguration {

    @Bean
    public OrderService orderService(OrderRepository orderRepository,
                                     BookClient bookClient) {
        return new OrderServiceImpl(orderRepository, bookClient);
    }
}
