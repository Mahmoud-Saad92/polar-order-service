package com.bazinga.eg.orderservice.application.service.impl;

import com.bazinga.eg.orderservice.application.domain.model.Order;
import com.bazinga.eg.orderservice.application.domain.repository.OrderRepository;
import com.bazinga.eg.orderservice.application.service.OrderService;
import com.bazinga.eg.orderservice.common.enums.OrderStatus;
import com.bazinga.eg.orderservice.common.util.OrderNumberGenerator;
import com.bazinga.eg.orderservice.infrastructure.proxy.BookClient;
import com.bazinga.eg.orderservice.infrastructure.proxy.model.Book;
import com.bazinga.eg.orderservice.resource.payload.NewOrder;
import com.bazinga.eg.orderservice.resource.payload.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public record OrderServiceImpl(OrderRepository orderRepository,
                               BookClient bookClient) implements OrderService {

    @Override
    public Flux<OrderDTO> getAllOrders() {
        return orderRepository.findAll().map(OrderDTO::new);
    }

    @Override
    public Mono<OrderDTO> submitOrder(NewOrder newOrder) {
        return bookClient.getBookByIsbn(newOrder.bookIsbn())
                .onErrorResume(error -> {
                    log.error("Failed to retrieve book details", error);
                    return Mono.empty();
                })
                .map(book -> buildAcceptedOrder(book, newOrder.quantity()))
                .defaultIfEmpty(buildRejectedOrder(newOrder))
                .flatMap(orderRepository::save)
                .map(OrderDTO::new);
    }

    private Order buildRejectedOrder(NewOrder newOrder) {
        return new Order(newOrder, OrderNumberGenerator.generate(), OrderStatus.REJECTED);
    }

    private Order buildAcceptedOrder(Book book, Integer quantity) {
        return new Order(book, OrderNumberGenerator.generate(), quantity, OrderStatus.ACCEPTED);
    }
}
