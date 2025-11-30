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
        if(provider == Provider.LINKEDIN){
            return context.getBean("LINKEDIN_REFRESH_CLIENT", TokenRefreshClient.class);
        }
        throw new UnsupportedOperationException("Refresh logic not implemented for: " + provider);
    }
}
