package com.bazinga.eg.orderservice.application.domain.repository;

import com.bazinga.eg.orderservice.application.domain.model.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderRepository {
    Flux<Order> findAll();
    Mono<Order> save(Order order);
}
