package com.fierceadventurer.socialaccountservice.client.impl;

import com.fierceadventurer.socialaccountservice.client.TokenRefreshClient;
import com.fierceadventurer.socialaccountservice.dto.OAuthRefreshResponse;
import com.fierceadventurer.socialaccountservice.exception.LinkedInServiceException;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Slf4j
@Component("LINKEDIN_REFRESH_CLIENT")
public class LinkedInTokenRefreshClient implements TokenRefreshClient {
    private final String clientId;
    private final String clientSecret;
    private final RestClient restClient;

    public LinkedInTokenRefreshClient(
            @Value("${linkedin.client-id}") String clientId,
            @Value("${linkedin.client-secret}") String clientSecret
    ){
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.restClient = RestClient.builder()
                .baseUrl("https://www.linkedin.com")
                .build();
    }

    @Override
    public OAuthRefreshResponse refreshAccessToken(String refreshToken) {
        log.info("Attempting to refresh LinkedIn Access Token...");

        MultiValueMap<String , String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);
        body.add("client_id", clientId);
        body.add("client_secret",clientSecret);

        try {
            OAuthRefreshResponse response = restClient.post()
                    .uri("/oauth/v2/accessToken")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .body(OAuthRefreshResponse.class);

            if(response == null || response.getAccessToken() == null){
                throw  new LinkedInServiceException("LinkedIn returned empty tokens during refresh");
            }

            log.info("Successfully refreshed LinkedIn token.");
            return response;
        }
        catch (Exception e){
            log.error("Failed to refresh LinkedIn token " , e);
            throw new LinkedInServiceException("Token Refresh Failed: " + e.getMessage());
        }
    }

}
