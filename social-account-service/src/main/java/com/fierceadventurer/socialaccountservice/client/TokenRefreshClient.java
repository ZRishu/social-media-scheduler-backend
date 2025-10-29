package com.fierceadventurer.socialaccountservice.client;

import com.fierceadventurer.socialaccountservice.dto.OAuthRefreshResponse;

public interface TokenRefreshClient {
    OAuthRefreshResponse refreshAccessToken(String refreshToken);
}
