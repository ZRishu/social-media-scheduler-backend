package com.fierceadventurer.analyticsservice.client;

import com.fierceadventurer.analyticsservice.dto.HistoricalPost;

import java.util.List;

public interface ExternalPlatformClient {
    List<HistoricalPost> getHistoricalData(String accessToken) throws Exception;
}
