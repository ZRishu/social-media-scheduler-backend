package com.fierceadventurer.socialaccountservice.dto;

import lombok.Data;

@Data
public class OAuthRefreshResponse {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
}
