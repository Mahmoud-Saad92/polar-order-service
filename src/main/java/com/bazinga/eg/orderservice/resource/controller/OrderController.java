package com.bazinga.eg.orderservice.resource.controller;

import com.bazinga.eg.orderservice.application.service.OrderService;
import com.bazinga.eg.orderservice.resource.payload.NewOrder;
import com.bazinga.eg.orderservice.resource.payload.OrderDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
public record OrderController(OrderService orderService) {

    @GetMapping
    public Flux<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping
    public Mono<OrderDTO> submitOrder(@RequestBody @Valid NewOrder newOrder) {
        return orderService.submitOrder(newOrder);
    }
}
