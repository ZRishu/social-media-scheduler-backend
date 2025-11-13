package com.fierceadventurer.socialaccountservice.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fierceadventurer.socialaccountservice.dto.LinkedInErrorResponse;
import com.fierceadventurer.socialaccountservice.dto.LinkedInTokenResponse;
import com.fierceadventurer.socialaccountservice.dto.LinkedInUserInfo;
import com.fierceadventurer.socialaccountservice.exception.LinkedInServiceException;

import com.fierceadventurer.socialaccountservice.exception.LinkedInAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component

public class LinkedInConnectClient {

    @Value("${linkedin.client-id}")
    private String clientId;
    @Value("${linkedin.client-secret}")
    private String clientSecret;

    private final RestClient apiRestClient;
    private final RestClient tokenRestClient;
    private final ObjectMapper objectMapper;

    public LinkedInConnectClient(
            @Value("${linkedin.client-id}") String clientId,
            @Value("${linkedin.client-secret}") String clientSecret
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.objectMapper = new ObjectMapper();

        this.tokenRestClient = RestClient.builder()
                .baseUrl("https://www.linkedin.com")
                .defaultStatusHandler(
                        status -> status.is4xxClientError(),
                        (request, response) -> {
                            String errorMsg = getErrorMessage(response);
                            log.error("LinkedIn Client Error (4xx): {}", errorMsg);
                            throw new LinkedInServiceException(errorMsg);
                        }
                )
                .defaultStatusHandler(status -> status.is5xxServerError(),
                        (request, response) -> {
                    String errorMsg = getErrorMessage(response);
                    log.error("LinkedIn Client Error (5xx): {}", errorMsg);
                    throw new LinkedInServiceException(errorMsg);
                        })
                .build();
        this.apiRestClient = RestClient.create("https://api.linkedin.com");
    }

    private String getErrorMessage(ClientHttpResponse response) {
        try {
            LinkedInErrorResponse error = objectMapper.readValue(response.getBody(), LinkedInErrorResponse.class);
            return Optional.ofNullable(error)
                    .map(LinkedInErrorResponse::getErrorDescription)
                    .orElse(response.getStatusText());
        } catch (Exception e) {
            log.warn("Could not parse LinkedIn error response body: {}", e.getMessage());

            try{
                return response.getStatusText();
            }
            catch (IOException ex){
                return "Unknown HTTP Error";
            }

        }
    }

    public LinkedInTokenResponse exchangeAuthCode(String authCode , String redirectUri) {
        log.info("Exchanging LinkedIn auth code for tokens");
        MultiValueMap<String, String> fromData = new LinkedMultiValueMap<>();
        fromData.add("grant_type", "authorization_code");
        fromData.add("code", authCode);
        fromData.add("redirect_uri", redirectUri);
        fromData.add("client_id", clientId);
        fromData.add("client_secret", clientSecret);


        LinkedInTokenResponse response = tokenRestClient.post()
                .uri("/oauth/v2/accessToken")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromData)
                .retrieve()
                .body(LinkedInTokenResponse.class);

        if ((response == null) || (response.getAccessToken() == null)) {
            log.error("Received empty or invalid token response from LinkedIn despite 200 OK");
            throw new RuntimeException("Received empty or invalid token response from LinkedIn");
        }

        log.info("Successfully exchanged auth code for LinkedIn access token");
        return response;

    }

    public LinkedInUserInfo fetchUserProfile(String accessToken) {
        log.info("Fetching LinkedIn user profile");
        try {
            return apiRestClient.get()
                    .uri("/v2/userinfo")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .body(LinkedInUserInfo.class);
        } catch (Exception e) {
            log.error("Failed to fetch LinkedIn profile: {}", e.getMessage());
            throw new LinkedInServiceException("Failed to fetch LinkedIn profile data: " + e.getMessage());
        }
    }

}
