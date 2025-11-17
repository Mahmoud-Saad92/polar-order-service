package com.bazinga.eg.orderservice.infrastructure.proxy.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import java.time.Duration;

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "polar")
public class ClientProperties {

    private URI catalogServiceUri;
    private Integer timeoutDurationSeconds;
    private Integer retryAttempts;
    private Integer retryInitialBackoffMs;

    public Duration getTimeoutDuration() {
        return Duration.ofSeconds(this.timeoutDurationSeconds);
    }

    public Duration getRetryInitialBackoff() {
        return Duration.ofMillis(this.retryInitialBackoffMs);
    }
}
