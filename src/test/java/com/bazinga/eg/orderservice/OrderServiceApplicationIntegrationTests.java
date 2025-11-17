package com.bazinga.eg.orderservice;

import com.bazinga.eg.orderservice.common.enums.OrderStatus;
import com.bazinga.eg.orderservice.infrastructure.proxy.BookClient;
import com.bazinga.eg.orderservice.infrastructure.proxy.model.Book;
import com.bazinga.eg.orderservice.resource.payload.NewOrder;
import com.bazinga.eg.orderservice.resource.payload.OrderDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@Testcontainers
public class OrderServiceApplicationIntegrationTests {

    @Container
    static PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"));

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private BookClient bookClient;

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", OrderServiceApplicationIntegrationTests::r2dbcUrl);
        registry.add("spring.r2dbc.username", postgresql::getUsername);
        registry.add("spring.r2dbc.password", postgresql::getPassword);
        registry.add("spring.flyway.url", postgresql::getJdbcUrl);
        registry.add("spring.flyway.user", postgresql::getUsername);
        registry.add("spring.flyway.password", postgresql::getPassword);
    }

    private static String r2dbcUrl() {
        return String.format("r2dbc:postgresql://%s:%s/%s", postgresql.getHost(),
                postgresql.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT), postgresql.getDatabaseName());
    }

    @Test
    void whenGetOrdersThenReturn() {
        String bookIsbn = "1234567893";
        Book book = new Book(bookIsbn, "Title", "Author", 9.90);

        given(bookClient.getBookByIsbn(bookIsbn)).willReturn(Mono.just(book));

        NewOrder orderRequest = new NewOrder(bookIsbn, 1);

        OrderDTO expectedOrder =
                webTestClient.post()
                        .uri("/api/v1/orders")
                        .bodyValue(orderRequest)
                        .exchange()
                        .expectStatus().is2xxSuccessful()
                        .expectBody(OrderDTO.class).returnResult().getResponseBody();

        assertThat(expectedOrder).isNotNull();

        webTestClient.get()
                .uri("/api/v1/orders")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(OrderDTO.class).value(orders -> assertThat(orders.stream().filter(order -> order.bookIsbn().equals(bookIsbn)).findAny()).isNotEmpty());
    }

    @Test
    void whenPostRequestAndBookExistsThenOrderAccepted() {
        String bookIsbn = "1234567899";
        Book book = new Book(bookIsbn, "Title", "Author", 9.90);

        given(bookClient.getBookByIsbn(bookIsbn)).willReturn(Mono.just(book));

        NewOrder newOrder = new NewOrder(bookIsbn, 3);

        OrderDTO createdOrder =
                webTestClient.post()
                        .uri("/api/v1/orders")
                        .bodyValue(newOrder)
                        .exchange()
                        .expectStatus().is2xxSuccessful()
                        .expectBody(OrderDTO.class).returnResult().getResponseBody();

        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.bookIsbn()).isEqualTo(newOrder.bookIsbn());
        assertThat(createdOrder.quantity()).isEqualTo(newOrder.quantity());
        assertThat(createdOrder.bookName()).isEqualTo(book.title() + " - " + book.author());
        assertThat(createdOrder.bookPrice()).isEqualTo(book.price());
        assertThat(createdOrder.status()).isEqualTo(OrderStatus.ACCEPTED);
    }
}
