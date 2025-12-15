package com.fierceadventurer.analyticsservice.client;

import com.fierceadventurer.analyticsservice.enums.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialMediaClientFactory {


    private final ApplicationContext context;

    public ExternalPlatformClient getClient(Provider provider){
        String beanName = provider.name().toLowerCase() + "ApiClient";

        return context.getBean(beanName, ExternalPlatformClient.class);
    }
}
