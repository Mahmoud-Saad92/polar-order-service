package com.bazinga.eg.orderservice.persistence.repository;

import com.bazinga.eg.orderservice.application.domain.model.Order;
import com.bazinga.eg.orderservice.application.domain.repository.OrderRepository;
import com.bazinga.eg.orderservice.persistence.mapper.OrderPersistableMapper;
import com.bazinga.eg.orderservice.persistence.repository.r2dbc.OrderR2dbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderR2dbcRepository orderR2dbcRepository;
    private final OrderPersistableMapper orderPersistableMapper;

    @Override
    public Flux<Order> findAll() {
        return orderR2dbcRepository.findAll().map(orderPersistableMapper::toOrder);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Mono<Order> save(final Order order) {
        return Mono.justOrEmpty(order)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Order cannot be null")))
                .doOnNext(o -> log.debug("Saving order with number: {}", o.orderNumber()))
                .map(orderPersistableMapper::toOrderPersistable)
                .flatMap(orderR2dbcRepository::save)
                .map(orderPersistableMapper::toOrder)
                .doOnSuccess(savedOrder -> log.info("Order saved successfully: {}", savedOrder.orderNumber()))
                .doOnError(error -> log.error("Failed to save order", error));
    }
}
