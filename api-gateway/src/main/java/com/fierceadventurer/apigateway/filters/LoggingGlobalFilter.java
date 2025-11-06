package com.fierceadventurer.apigateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class LoggingGlobalFilter {

    @Bean
    @Order(-1)
    public GlobalFilter logRequest() {
        return (exchange, chain) -> {
            log.info("Request received: {} {}",
                    exchange.getRequest().getMethod(),
                    exchange.getRequest().getURI());

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("Response sent: {} {}",
                        exchange.getRequest().getURI(),
                        exchange.getResponse().getStatusCode());
            }));
        };
    }
}
