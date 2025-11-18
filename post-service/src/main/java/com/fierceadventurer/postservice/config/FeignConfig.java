package com.fierceadventurer.postservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
@Slf4j
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if(authentication != null && authentication.getCredentials() instanceof Jwt){
                    Jwt jwt = (Jwt) authentication.getCredentials();
                    String tokenValue = jwt.getTokenValue();
                    template.header("Authorization" , "Bearer" + tokenValue);
                    log.debug("Feign Interceptor: Added Authorization header to outgoing request.");
                }
                else {
                    log.warn("Feign Interceptor: No JWT found in SecurityContext. Outgoing request will be unauthenticated.");
                }
            }
        };
    }
}
