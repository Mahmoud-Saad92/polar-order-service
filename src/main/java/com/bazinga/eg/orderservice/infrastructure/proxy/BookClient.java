package com.bazinga.eg.orderservice.infrastructure.proxy;

import com.bazinga.eg.orderservice.infrastructure.proxy.model.Book;
import com.bazinga.eg.orderservice.infrastructure.proxy.property.ClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.util.Objects;

import static com.bazinga.eg.orderservice.common.util.Constant.BOOKS_ROOT_API;

@Slf4j
public record BookClient(WebClient webClient,
                         ClientProperties clientProperties) {

//    public Mono<Book> getBookByIsbn(String isbn) {
//        return webClient
//                .get()
//                .uri(BOOKS_ROOT_API + "/{isbn}", isbn)
//                .retrieve()
//                .bodyToMono(Book.class)
//                .timeout(clientProperties.getTimeoutDuration(), Mono.empty())
//                .onErrorResume(WebClientResponseException.NotFound.class, exception -> {
//                    log.error("Failed to retrieve book details, not found exception occur.", exception);
//                    return Mono.empty();
//                })
//                .retryWhen(Retry.backoff(clientProperties.getRetryAttempts(), clientProperties.getRetryInitialBackoff()));
//    }

    public Mono<Book> getBookByIsbn(String isbn) {
        return webClient
                .get()
                .uri(BOOKS_ROOT_API + "/{isbn}", isbn)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() && !status.equals(HttpStatus.NOT_FOUND),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("Client error retrieving book with ISBN: {}. Status: {}, Body: {}",
                                            isbn, response.statusCode(), errorBody);
                                    return Mono.error(new WebClientResponseException(
                                            response.statusCode().value(),
                                            "Client Error",
                                            response.headers().asHttpHeaders(),
                                            errorBody.getBytes(),
                                            null
                                    ));
                                })
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("Server error retrieving book with ISBN: {}. Status: {}, Body: {}",
                                            isbn, response.statusCode(), errorBody);
                                    return Mono.error(new WebClientResponseException(
                                            response.statusCode().value(),
                                            "Server Error",
                                            response.headers().asHttpHeaders(),
                                            errorBody.getBytes(),
                                            null
                                    ));
                                })
                )
                .bodyToMono(Book.class)
                .timeout(clientProperties.getTimeoutDuration(), Mono.empty())
                .onErrorResume(WebClientResponseException.NotFound.class, exception -> {
                    log.warn("Book not found for ISBN: {}", isbn);
                    return Mono.empty();
                })
                .retryWhen(Retry
                        .backoff(clientProperties.getRetryAttempts(), clientProperties.getRetryInitialBackoff())
                        .filter(throwable ->
                                !(throwable instanceof WebClientResponseException wex
                                        && wex.getStatusCode().is4xxClientError()))
                        .doBeforeRetry(retrySignal ->
                                log.warn("Retrying request for book with ISBN: {}. Attempt: {}. Error: {}",
                                        isbn, retrySignal.totalRetries() + 1, retrySignal.failure().getMessage()))
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            log.error("Retry exhausted for book with ISBN: {} after {} attempts",
                                    isbn, retrySignal.totalRetries());
                            return retrySignal.failure();
                        })
                )
                .doOnSuccess(book -> {
                    if (Objects.nonNull(book)) {
                        log.debug("Successfully retrieved book with ISBN: {}", isbn);
                    }
                })
                .doOnError(throwable -> log.error("Failed to retrieve book with ISBN: {} after all retry attempts", isbn, throwable));
    }
}
