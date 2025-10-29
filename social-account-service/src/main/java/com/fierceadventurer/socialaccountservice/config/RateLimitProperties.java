package com.fierceadventurer.socialaccountservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@ConfigurationProperties(prefix = "rate-limits")
@Data
public class RateLimitProperties {

    private Map<String , ProviderConfig> providers;

    @Data
    public static class ProviderConfig {
        private int limit;
        private int windowMinutes;
    }
}
