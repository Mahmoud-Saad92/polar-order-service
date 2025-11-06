package com.bazinga.eg.orderservice.common.util;

import com.github.f4b6a3.ulid.UlidCreator;

public interface OrderNumberGenerator {

    String ORDER_NUMBER_PREFIX = "ORD-";

    static String generate() {
        return ORDER_NUMBER_PREFIX + UlidCreator.getMonotonicUlid().toString();
    }
}
