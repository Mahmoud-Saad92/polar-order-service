package com.bazinga.eg.orderservice.resource.payload;

import com.bazinga.eg.orderservice.application.domain.model.Order;
import com.bazinga.eg.orderservice.common.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record OrderDTO(@JsonProperty("order_number") String orderNumber,
                       @JsonProperty("book_isbn") String bookIsbn,
                       @JsonProperty("book_name") String bookName,
                       @JsonProperty("book_price") Double bookPrice,
                       Integer quantity,
                       OrderStatus status,
                       @JsonProperty("created_date") Instant createdDate) {
    public OrderDTO(Order order) {
        this(order.orderNumber(), order.bookIsbn(), order.bookName(), order.bookPrice(), order.quantity(), order.status(), order.createdDate());
    }
}
