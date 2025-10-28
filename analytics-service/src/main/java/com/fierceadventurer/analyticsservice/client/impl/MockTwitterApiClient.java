package com.fierceadventurer.analyticsservice.client.impl;

import com.fierceadventurer.analyticsservice.client.ExternalPlatformClient;
import com.fierceadventurer.analyticsservice.dto.HistoricalPost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component("twitterApiClient")
@Slf4j
public class MockTwitterApiClient implements ExternalPlatformClient {

    @Override
    public List<HistoricalPost> getHistoricalData(String accessToken) throws Exception {
        log.info("Mock CLIENT: Simulating fetch from Twitter API");
        List<HistoricalPost> posts = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 200; i++) {
            LocalDateTime postTime;
            int engagement;

            if(random.nextDouble() < 0.4){
                postTime = LocalDateTime.now().minusWeeks(i % 10)
                        .with(DayOfWeek.TUESDAY)
                        .withHour(9 + random.nextInt(3));
                engagement = 80 + random.nextInt(40);
            }
            else if(random.nextDouble() < 0.3){
                postTime = LocalDateTime.now().minusWeeks(i % 10)
                        .with(DayOfWeek.FRIDAY)
                        .withHour(15 + random.nextInt(3));
                engagement = 60 + random.nextInt(30);
            }
            else{
                postTime = LocalDateTime.now().minusDays(i)
                        .withHour(random.nextInt(24));
                engagement = 5 + random.nextInt(20);

            }
            posts.add(new HistoricalPost(postTime, engagement));
        }
        Thread.sleep(1500);
        return posts;
    }
}
