package com.bazinga.eg.orderservice.infrastructure.proxy.model;

public record Book(String isbn,
                   String title,
                   String author,
                   Double price) {
}
