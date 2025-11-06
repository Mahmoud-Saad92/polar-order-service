package com.bazinga.eg.orderservice.common.audit;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.ReactiveAuditorAware;
import reactor.core.publisher.Mono;

public class AuditorAwareImpl implements ReactiveAuditorAware<String> {

    @Override
    public @NotNull Mono<String> getCurrentAuditor() {
        return Mono.just("SYS_ADMIN");
    }
}
