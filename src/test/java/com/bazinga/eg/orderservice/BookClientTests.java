package com.bazinga.eg.orderservice;

import com.bazinga.eg.orderservice.infrastructure.proxy.BookClient;
import com.bazinga.eg.orderservice.infrastructure.proxy.model.Book;
import com.bazinga.eg.orderservice.infrastructure.proxy.property.ClientProperties;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class BookClientTests {

    private MockWebServer mockWebServer;
    private BookClient bookClient;

    @BeforeEach
    void setup() throws Exception {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(this.mockWebServer.url("/").toString())
                .build();

        ClientProperties clientProperties = new ClientProperties();
        clientProperties.setCatalogServiceUri(this.mockWebServer.url("/").uri());
        clientProperties.setTimeoutDurationSeconds(3);
        clientProperties.setRetryAttempts(1);
        clientProperties.setRetryInitialBackoffMs(100);

        this.bookClient = new BookClient(webClient, clientProperties);
    }

    @AfterEach
    void cleanup() throws Exception {
        this.mockWebServer.shutdown();
    }

    @Test
    void whenBookNotExistsThenReturnEmpty() {
        var bookIsbn = "1234567891";

        var mockResponse = new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404);

        mockWebServer.enqueue(mockResponse);

        StepVerifier.create(bookClient.getBookByIsbn(bookIsbn))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void whenBookExistsThenReturnBook() {
        var bookIsbn = "1234567893";

        MockResponse mockResponse = new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""
                        {
                            "isbn": %s,
                            "author": "mahmoud saad mohamed",
                            "title": "spring cloud native in action 4",
                            "price": 1400.99
                        }
                        """.formatted(bookIsbn))
                .setResponseCode(200);

        mockWebServer.enqueue(mockResponse);

        Mono<Book> bookByIsbn = bookClient.getBookByIsbn(bookIsbn);

        StepVerifier.create(bookByIsbn)
                .expectNextMatches(book -> book.isbn().equals(bookIsbn))
                .verifyComplete();
    }
}
