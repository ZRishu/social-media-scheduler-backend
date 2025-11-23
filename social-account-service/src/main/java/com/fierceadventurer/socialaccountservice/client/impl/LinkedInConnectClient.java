package com.fierceadventurer.socialaccountservice.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fierceadventurer.socialaccountservice.dto.LinkedInErrorResponse;
import com.fierceadventurer.socialaccountservice.dto.LinkedInTokenResponse;
import com.fierceadventurer.socialaccountservice.dto.LinkedInUserInfo;
import com.fierceadventurer.socialaccountservice.dto.PublishRequestDto;
import com.fierceadventurer.socialaccountservice.exception.LinkedInServiceException;


import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.io.IOException;
import java.util.*;

@Slf4j
@Component

public class LinkedInConnectClient {


    private final String clientId;

    private final String clientSecret;

    private final RestClient apiRestClient;
    private final RestClient tokenRestClient;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public LinkedInConnectClient(
            @Value("${linkedin.client-id}") String clientId,
            @Value("${linkedin.client-secret}") String clientSecret
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.objectMapper = new ObjectMapper();
        HttpClient httpClient = HttpClients.createDefault();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectTimeout(15000);

        this.restTemplate = new RestTemplate(factory); // Initialize RestTemplate




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

    // Add this method to your existing client
    public String publishPost(String authorUrn, String accessToken, PublishRequestDto requestDto) {
        log.info("Publishing to LinkedIn for author: {}", authorUrn);

        String author = "urn:li:person:" + authorUrn;
        String url = "https://api.linkedin.com/v2/ugcPosts";

        // 1. Construct the Map Payload
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("author", author);
        bodyMap.put("lifecycleState", "PUBLISHED");

        Map<String, Object> specificContent = new HashMap<>();
        Map<String, Object> shareContent = new HashMap<>();

        Map<String, Object> shareCommentary = new HashMap<>();
        shareCommentary.put("text", requestDto.getContent());
        shareContent.put("shareCommentary", shareCommentary);

        if(requestDto.getMediaUrls() != null && !requestDto.getMediaUrls().isEmpty()){
            shareContent.put("shareMediaCategory", "ARTICLE");
            Map<String, Object> media = new HashMap<>();
            media.put("status","READY");
            media.put("originalUrl",requestDto.getMediaUrls().get(0));
            shareContent.put("media", Collections.singletonList(media));
        }
        else {
            shareContent.put("shareMediaCategory", "NONE");
        }

        specificContent.put("com.linkedin.ugc.ShareContent", shareContent);
        bodyMap.put("specificContent", specificContent);

        Map<String, Object> visibility = new HashMap<>();
        visibility.put("com.linkedin.ugc.MemberNetworkVisibility", "PUBLIC");
        bodyMap.put("visibility", visibility);

        try {
            // 2. Manually Serialize to JSON String
            // This gives us a simple String object. RestTemplate knows exactly how long a String is.
            // This prevents it from trying to "chunk" the data.
            String jsonBody = objectMapper.writeValueAsString(bodyMap);

            // 3. Prepare Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + accessToken);
            headers.set("X-Restli-Protocol-Version", "2.0.0");

            // 4. Send 'jsonBody' (String) instead of 'bodyMap' (Object)
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            log.info("LinkedIn API Response Code: {}", response.getStatusCode());
            return response.getBody();

        } catch (Exception e) {
            log.error("Failed to publish to LinkedIn: {}", e.getMessage());
            e.printStackTrace();
            throw new LinkedInServiceException("LinkedIn Publish Failed: " + e.getMessage());
        }
    }

}
