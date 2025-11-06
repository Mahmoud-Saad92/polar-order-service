package com.bazinga.eg.orderservice.application.service.impl;

import com.bazinga.eg.orderservice.application.domain.model.Order;
import com.bazinga.eg.orderservice.application.domain.repository.OrderRepository;
import com.bazinga.eg.orderservice.application.service.OrderService;
import com.bazinga.eg.orderservice.common.enums.OrderStatus;
import com.bazinga.eg.orderservice.common.util.OrderNumberGenerator;
import com.bazinga.eg.orderservice.resource.payload.NewOrder;
import com.bazinga.eg.orderservice.resource.payload.OrderDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public record OrderServiceImpl(OrderRepository orderRepository) implements OrderService {

    @Override
    public Flux<OrderDTO> getAllOrders() {
        return orderRepository.findAll().map(OrderDTO::new);
    }

    @Override
    public Mono<OrderDTO> submitOrder(NewOrder newOrder) {
        return orderRepository.save(new Order(newOrder, OrderNumberGenerator.generate(), OrderStatus.REJECTED)).map(OrderDTO::new);
    }
}
