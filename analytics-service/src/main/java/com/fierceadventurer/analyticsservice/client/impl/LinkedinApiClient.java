package com.fierceadventurer.analyticsservice.client.impl;

import com.fierceadventurer.analyticsservice.client.ExternalPlatformClient;
import com.fierceadventurer.analyticsservice.dto.HistoricalPost;
import com.fierceadventurer.analyticsservice.dto.linkedin.LinkedInCounts;
import com.fierceadventurer.analyticsservice.dto.linkedin.LinkedInElement;
import com.fierceadventurer.analyticsservice.dto.linkedin.LinkedInPostResponse;
import com.fierceadventurer.analyticsservice.dto.linkedin.LinkedInProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component("linkedinApiClient")
@RequiredArgsConstructor
public class LinkedinApiClient implements ExternalPlatformClient {

    private final RestTemplate restTemplate;
    private static final double SHARE_WEIGHT = 3.0;
    private static final double COMMENT_WEIGHT = 2.0;
    private static final double LIKE_WEIGHT = 1.0;


    @Value("${clients.linkedin.api-url:https://api.linkedin.com/v2}")
    private String linkedinApiUrl;

    @Override
    public List<HistoricalPost> getHistoricalData(String accessToken) throws Exception {
        log.info("Fetching historical data from LinkedIn...");

        try {
            String userUrn = fetchUserUrn(accessToken);
            log.debug("Resolved Linkedin User URN: {}", userUrn);

            return fetchUserPosts(accessToken , userUrn);
        }
        catch (HttpClientErrorException e){
            log.error("Linkedin API Error: Status {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new Exception("Linkedin API Error: " +  e.getStatusCode() + " - " + e.getStatusText());
        }
        catch (Exception e){
            log.error("Unexpected error fetching Linkedin data", e);
            throw  e;
        }
    }
    private String fetchUserUrn(String accessToken) {
        String url = linkedinApiUrl + "/userinfo";
        HttpHeaders headers = createHeaders(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<LinkedInProfileResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    LinkedInProfileResponse.class
            );

            if(response.getBody() == null || response.getBody().getId() == null){
                throw new RuntimeException("Failed to retrieve LinkedIn Profile: Empty Response");
            }

            String id = response.getBody().getSub();

            if(id == null){
                id = response.getBody().getId();
            }

            if(id == null){
                log.error("Linkedin response missing 'sub. Body: {}", response.getBody());
                throw new RuntimeException("Failed to retrieve LinkedIn Profile ID ('sub' field missing)");
            }

            return "urn:li:person:" + response.getBody().getId();
        } catch (RuntimeException e) {
            log.error("/userinfo endpoint failed");
            throw new RuntimeException(e);
        }
    }


    private List<HistoricalPost> fetchUserPosts(String accessToken, String userUrn) {
        String authorsParamValue = "List(" + userUrn + ")";

        String encodedAuthors = URLEncoder.encode(authorsParamValue , StandardCharsets.UTF_8);

        String projection = "(elements*(created(time),socialDetail(totalSocialActivityCounts)))";

        String url = String.format(
                "%s/ugcPosts?q=authors&authors=%s&count=50&projection=%s",
                linkedinApiUrl
                ,encodedAuthors
                ,projection
        );

        log.debug("Fetching LinkedIn posts from URL: {}", url);

        HttpHeaders headers = createHeaders(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try{
            ResponseEntity<LinkedInPostResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    LinkedInPostResponse.class
            );

            if(response.getBody() == null || response.getBody().getElements() == null){
                log.info("LinkedIn returned no posts for user: {}", userUrn);
                return Collections.emptyList();
            }

            return response.getBody().getElements().stream()
                    .map(this::mapToHistoricalPost)
                    .collect(Collectors.toList());
        }
        catch (HttpClientErrorException e){
            log.error("LinkedIn API Error [{}]: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch LinkedIn posts: " + e.getStatusText());
        }


    }

    private HttpHeaders createHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.set("X-Restli-Protocol-Version", "2.0.0");
        return headers;
    }

    private HistoricalPost mapToHistoricalPost(LinkedInElement element) {
        LocalDateTime createdAt;

        if(element.getCreated() != null){
            createdAt = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(element.getCreated().getTime()),
                    ZoneId.of("UTC")
            );
        }
        else {
            log.warn("Found LinkedIn post with missing creation time. Defaulting to NOW.");
            createdAt = LocalDateTime.now(ZoneId.of("UTC"));
        }

        int weightedScore = 0;

        if(element.getSocialDetail() != null &&
                element.getSocialDetail().getTotalSocialActivityCounts() != null){
            LinkedInCounts counts = element.getSocialDetail().getTotalSocialActivityCounts();

            weightedScore = calculate(
                    counts.getNumLikes(),
                    counts.getNumComments(),
                    counts.getNumShares(),
                    0
            );
        }
        return new HistoricalPost(createdAt, weightedScore);
    }

    private static int calculate(int likes , int comments , int shares , int impressions){
        double weightedInteractions = (likes* LIKE_WEIGHT) +
                (comments * COMMENT_WEIGHT) +
                (shares * SHARE_WEIGHT);

        if (impressions > 0){
            return (int) ((weightedInteractions / impressions) * 1000);
        }

        return (int) weightedInteractions;
    }




}
