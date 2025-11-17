package com.bazinga.eg.orderservice;

import com.bazinga.eg.orderservice.application.domain.model.Order;
import com.bazinga.eg.orderservice.application.service.OrderService;
import com.bazinga.eg.orderservice.common.enums.OrderStatus;
import com.bazinga.eg.orderservice.common.util.OrderNumberGenerator;
import com.bazinga.eg.orderservice.resource.controller.OrderController;
import com.bazinga.eg.orderservice.resource.payload.NewOrder;
import com.bazinga.eg.orderservice.resource.payload.OrderDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@WebFluxTest(OrderController.class)
@TestPropertySource(properties = {
        "ACTIVE_PROFILE=it",
        "SERVER_PORT=8080"
})
public class OrderControllerWebFluxTests {

    @Autowired
    private WebTestClient webClient;

    @MockitoBean
    private OrderService orderService;

    @Test
    void whenBookNotAvailableThenRejectOrder() {
        var newOrder = new NewOrder("1234567890", 3);

        var expectedOrder = new Order(newOrder, OrderNumberGenerator.generate(), OrderStatus.REJECTED);

        given(orderService.submitOrder(newOrder))
                .willReturn(Mono.just(new OrderDTO(expectedOrder)));

        webClient
                .post()
                .uri("/api/v1/orders")
                .bodyValue(newOrder)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(OrderDTO.class).value(actualOrder -> {
                    assertThat(actualOrder).isNotNull();
                    assertThat(actualOrder.status()).isEqualTo(OrderStatus.REJECTED);
                });

    }
}
