package com.bazinga.eg.orderservice.application.domain.model;

import com.bazinga.eg.orderservice.common.enums.OrderStatus;
import com.bazinga.eg.orderservice.resource.payload.NewOrder;
import jakarta.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public record Order(Long orderId,
                    @NotBlank String orderNumber,
                    String bookIsbn,
                    String bookName,
                    Double bookPrice,
                    Integer quantity,
                    OrderStatus status,
                    Long version,
                    String createdBy,
                    String lastModifiedBy,
                    Instant createdDate,
                    Instant lastModifiedDate) {

    public Order(NewOrder newOrder, @NotNull String orderNumber, OrderStatus status) {
        this(null, orderNumber, newOrder.bookIsbn(), null, null, newOrder.quantity(), status, null, null, null, null, null);
    }
}
