package com.bazinga.eg.orderservice.common.enums;

public enum OrderStatus {
    ACCEPTED,
    REJECTED,
    DISPATCHED,
    CANCELLED,
    COMPLETED;

    public String getValue() {
        return this.name().toLowerCase();
    }

    public static OrderStatus fromString(String value) {
        return valueOf(value.toUpperCase());
    }
}
