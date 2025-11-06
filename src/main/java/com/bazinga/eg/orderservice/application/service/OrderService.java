package com.bazinga.eg.orderservice.application.service;

import com.bazinga.eg.orderservice.resource.payload.NewOrder;
import com.bazinga.eg.orderservice.resource.payload.OrderDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {
    Flux<OrderDTO> getAllOrders();
    Mono<OrderDTO> submitOrder(NewOrder newOrder);
}
