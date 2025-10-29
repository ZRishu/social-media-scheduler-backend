package com.fierceadventurer.socialaccountservice.client;

import com.fierceadventurer.socialaccountservice.enums.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenRefreshClientFactory {
    private final ApplicationContext context;

    public TokenRefreshClient getClient(Provider provider) {
        String beanName = provider.name().toLowerCase() + "TokenClient";
        return context.getBean(beanName, TokenRefreshClient.class);
    }
}
