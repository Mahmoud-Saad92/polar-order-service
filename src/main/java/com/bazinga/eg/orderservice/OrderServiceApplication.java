package com.bazinga.eg.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.bazinga.eg.orderservice"})
public class OrderServiceApplication {

    public static void main(String... args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
