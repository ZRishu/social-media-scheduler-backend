package com.fierceadventurer.socialaccountservice.client.impl;

import com.fierceadventurer.socialaccountservice.dto.LinkedInTokenResponse;
import com.fierceadventurer.socialaccountservice.dto.LinkedInUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class LinkedInConnectClient {

    @Value("${linkedin.client-id}")
    private String clientId;
    @Value("${linkedin.client-secret}")
    private String clientSecret;

    private final RestClient restClient = RestClient.create();

    public LinkedInTokenResponse exchangeAuthCode(String authCode , String redirectUri) {
        log.info("Exchanging LinkedIn auth code for tokens");
        MultiValueMap<String, String> fromData = new LinkedMultiValueMap<>();
        fromData.add("grant_type", "authorization_code");
        fromData.add("code", authCode);
        fromData.add("redirect_uri", redirectUri);
        fromData.add("client_id", clientId);
        fromData.add("client_secret", clientSecret);

        try {
            LinkedInTokenResponse response = restClient.post()
                    .uri("https://www.linkedin.com/oauth/v2/accessToken")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(fromData)
                    .retrieve()
                    .body(LinkedInTokenResponse.class);

            if ((response == null) || (response.getAccessToken() == null)) {
                throw new RuntimeException("Received empty or invalid token response from LinkedIn");
            }

            log.info("Successfully exchanged auth code for LinkedIn access token");
            return response;
        }

        catch (Exception e) {
            log.error("Failed to connect LinkedIn account: {}", e.getMessage());
            throw new RuntimeException("Failed to connect LinkedIn account. Please try again.");
        }
    }

    public LinkedInUserInfo fetchUserProfile(String accessToken) {
        log.info("Fetching LinkedIn user profile");
        try {
            return restClient.get()
                    .uri("https://api.linkedin.com/v2/userinfo")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .body(LinkedInUserInfo.class);
        } catch (Exception e) {
            log.error("Failed to fetch LinkedIn profile: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch LinkedIn profile data.");
        }
    }

}
