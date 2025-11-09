package com.fierceadventurer.apigateway.filters;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.util.UUID;

@Configuration
public class CorrelationIdFilter {
    private static final String X_CORRELATION_ID = "X-Correlation-Id";

    @Bean
    @Order(0)
    public GlobalFilter correlationIdInjector() {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String correlationId = request.getHeaders().getFirst(X_CORRELATION_ID);

            if (correlationId == null) {
                correlationId = UUID.randomUUID().toString();
            }

            String finalCorrelationId = correlationId;

            ServerHttpRequest newRequest = request.mutate().header(X_CORRELATION_ID, finalCorrelationId).build();

            ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();

            newExchange.getResponse().getHeaders().add(X_CORRELATION_ID, finalCorrelationId);

            return chain.filter(newExchange);
        });

    }
}
