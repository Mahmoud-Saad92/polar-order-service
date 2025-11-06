package com.bazinga.eg.orderservice.resource.payload;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewOrder(@NotBlank(message = "The book ISBN must be defined.") String bookIsbn,
                       @NotNull(message = "The book quantity must be defined.")
                       @Min(value = 1, message = "The book quantity must be at least 1 item.")
                       @Max(value = 5, message = "The book quantity cannot be more than 5 items.")
                       Integer quantity) {
}
