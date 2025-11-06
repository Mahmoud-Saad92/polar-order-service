package com.bazinga.eg.orderservice.common.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

@EnableR2dbcAuditing(auditorAwareRef = "auditorAware")
public class DataConfiguration {

    @Bean
    public ReactiveAuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }
}
