package com.bazinga.eg.orderservice.common.configuration;

import com.bazinga.eg.orderservice.application.domain.repository.OrderRepository;
import com.bazinga.eg.orderservice.common.audit.DataConfiguration;
import com.bazinga.eg.orderservice.persistence.mapper.OrderPersistableMapper;
import com.bazinga.eg.orderservice.persistence.repository.OrderRepositoryImpl;
import com.bazinga.eg.orderservice.persistence.repository.r2dbc.OrderR2dbcRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({DataConfiguration.class})
public class DomainConfiguration {

    @Bean
    public OrderRepository orderRepository(OrderR2dbcRepository orderR2dbcRepository,
                                           OrderPersistableMapper orderPersistableMapper) {
        return new OrderRepositoryImpl(orderR2dbcRepository, orderPersistableMapper);
    }
}
