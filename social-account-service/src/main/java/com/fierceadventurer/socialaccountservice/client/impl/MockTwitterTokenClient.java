package com.fierceadventurer.socialaccountservice.client.impl;

import com.fierceadventurer.socialaccountservice.client.TokenRefreshClient;
import com.fierceadventurer.socialaccountservice.dto.OAuthRefreshResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MockTwitterTokenClient implements TokenRefreshClient {

    @Override
    public OAuthRefreshResponse refreshAccessToken(String refreshToken) {
        OAuthRefreshResponse response = new OAuthRefreshResponse();
        response.setAccessToken("mock-refresh-access-token-" + UUID.randomUUID());
        response.setRefreshToken("mock-new-refresh-token-" + UUID.randomUUID());
        response.setExpiresIn(3600L);
        return response;

    }
}
