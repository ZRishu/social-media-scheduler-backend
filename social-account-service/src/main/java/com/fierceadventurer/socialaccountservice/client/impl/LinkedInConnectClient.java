package com.fierceadventurer.socialaccountservice.client.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fierceadventurer.socialaccountservice.dto.*;
import com.fierceadventurer.socialaccountservice.exception.LinkedInServiceException;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
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

        this.restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
            if (request.getHeaders().getContentLength() > 0) {
                request.getHeaders().remove("Transfer-Encoding");
            }
            return execution.execute(request, body);
        }));

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

    public String publishPost(String authorUrn, String accessToken, PublishRequestDto requestDto) {
        log.info("Publishing to LinkedIn for author: {}", authorUrn);

        String author = "urn:li:person:" + authorUrn;
        String url = "https://api.linkedin.com/v2/ugcPosts";

        UploadedMediaInfo uploadInfo = null;
        if(requestDto.getMediaUrls() != null && !requestDto.getMediaUrls().isEmpty()){
            String internalUrl = requestDto.getMediaUrls().get(0);
            uploadInfo = uploadImageToLinkedIn(author , accessToken , internalUrl);
        }

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("author", author);
        bodyMap.put("lifecycleState", "PUBLISHED");

        Map<String, Object> specificContent = new HashMap<>();
        Map<String, Object> shareContent = new HashMap<>();

        Map<String, Object> shareCommentary = new HashMap<>();
        shareCommentary.put("text", requestDto.getContent());
        shareContent.put("shareCommentary", shareCommentary);

        if(uploadInfo != null){
            shareContent.put("shareMediaCategory", uploadInfo.getCategory());
            Map<String, Object> media = new HashMap<>();
            media.put("status","READY");
            media.put("media" , uploadInfo.getAssetUrn());
            shareContent.put("media", Collections.singletonList(media));
        }
        else if(requestDto.getMediaUrls() != null && !requestDto.getMediaUrls().isEmpty()){
            log.info("Native upload unavailable. Falling back to Article/Link share.");
            shareContent.put("shareMediaCategory" , "ARTICLE");
            Map<String , Object> media = new HashMap<>();
            media.put("status", "READY");
            media.put("originalUrl", requestDto.getMediaUrls().get(0));
            shareContent.put("media", Collections.singletonList(media));
        }
        else {
            shareContent.put("shareMediaCategory","NONE");
        }

        specificContent.put("com.linkedin.ugc.ShareContent", shareContent);
        bodyMap.put("specificContent", specificContent);
        bodyMap.put("visibility", Map.of("com.linkedin.ugc.MemberNetworkVisibility", "PUBLIC"));

        try {

            String jsonBody = objectMapper.writeValueAsString(bodyMap);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + accessToken);
            headers.set("X-Restli-Protocol-Version", "2.0.0");

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
            throw new LinkedInServiceException("LinkedIn Publish Failed: " + e.getMessage());
        }
    }

    private UploadedMediaInfo uploadImageToLinkedIn(String author, String accessToken, String mediaServiceUrl) {
        log.info("starting Native Image Upload for Internal URL: {}" , mediaServiceUrl);

        try{
            String keycloakToken = "";
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication != null && authentication.getPrincipal() instanceof Jwt jwt){
                keycloakToken = jwt.getTokenValue();
            }

            HttpHeaders internalHeaders = new HttpHeaders();
            if(!keycloakToken.isEmpty()){
                internalHeaders.set("Authorization","Bearer " + keycloakToken);
            }
            HttpEntity<Void> responseEntity = new HttpEntity<>(internalHeaders);

            ResponseEntity<byte[]> mediaResponse = restTemplate.exchange(
                    mediaServiceUrl,
                    HttpMethod.GET,
                    responseEntity,
                    byte[].class
            );

            byte[] mediaBytes = mediaResponse.getBody();
            if(mediaBytes == null || mediaBytes.length == 0) {
                throw new RuntimeException("Empty image response from Media Service");
            }

            MediaType contentType  = mediaResponse.getHeaders().getContentType();
            log.info("Detected Content-Type: {}", contentType);

            String recipe;
            String category;

            if(contentType.includes(MediaType.IMAGE_JPEG) || contentType.includes(MediaType.IMAGE_PNG) || contentType.includes(MediaType.IMAGE_GIF)){
                recipe = "urn:li:digitalmediaRecipe:feedshare-image";
                category = "IMAGE";
            }
            else if(contentType.toString().startsWith("video")){
                recipe = "urn:li:digitalmediaRecipe:feedshare-video";
                category = "VIDEO";
            }
            else if(contentType.equals(MediaType.APPLICATION_PDF)){
                recipe = "urn:li:digitalmediaRecipe:feedshare-document";
                category = "DOCUMENT";
            }
            else {
                log.warn("Unsupported media type for native upload: {}",contentType);
                return null;
            }

            String registerUrl = "https://api.linkedin.com/v2/assets?action=registerUpload";

            Map<String, Object> regBody = new HashMap<>();
            Map<String , Object> registerUploadRequest = new HashMap<>();
            registerUploadRequest.put("recipes", Collections.singletonList(recipe));
            registerUploadRequest.put("owner" , author);
            registerUploadRequest.put("serviceRelationships", Collections.singletonList(Map.of(
                    "relationshipType", "OWNER",
                    "identifier", "urn:li:userGeneratedContent")));
            regBody.put("registerUploadRequest", registerUploadRequest);

            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.set("Authorization", "Bearer " + accessToken);

            String regJson = objectMapper.writeValueAsString(regBody);
            HttpEntity<String> regEntity = new HttpEntity<>(regJson , authHeader);

            ResponseEntity<String> regResponse = restTemplate.exchange(
                    registerUrl , HttpMethod.POST , regEntity , String.class);
            JsonNode regNode = objectMapper.readTree(regResponse.getBody());

            String uploadUrl = regNode.path("value")
                    .path("uploadMechanism")
                    .path("com.linkedin.digitalmedia.uploading.MediaUploadHttpRequest")
                    .path("uploadUrl").asText();
            String assetUrn = regNode.path("value").path("asset").asText();

            log.info("Image Registered. Asset URN: {}", assetUrn);

            HttpHeaders uploadHeaders = new HttpHeaders();
            uploadHeaders.set("Authorization","Bearer " + accessToken);
            uploadHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            HttpEntity<byte[]> uploadEntity = new HttpEntity<>(mediaBytes , uploadHeaders);
            restTemplate.put(uploadUrl , uploadEntity);

            log.info("Binary Upload Completed Successfully");
            return new UploadedMediaInfo(assetUrn , category);

        }
        catch (Exception e) {
            log.error("Failed to upload media to Linkedin. Continuing as Text-Only. Reason: {}" , e.getMessage());
            return null;
        }

    }


}
