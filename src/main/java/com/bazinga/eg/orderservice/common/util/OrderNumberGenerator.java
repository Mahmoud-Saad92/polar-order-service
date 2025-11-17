package com.bazinga.eg.orderservice.common.util;

import com.github.f4b6a3.ulid.UlidCreator;

import static com.bazinga.eg.orderservice.common.util.Constant.ORDER_NUMBER_PREFIX;

public interface OrderNumberGenerator {

    static String generate() {
        return ORDER_NUMBER_PREFIX + UlidCreator.getMonotonicUlid().toString();
    }
}
