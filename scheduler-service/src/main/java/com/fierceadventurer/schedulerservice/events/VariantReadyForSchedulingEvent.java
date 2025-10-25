package com.fierceadventurer.schedulerservice.events;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class VariantReadyForSchedulingEvent {
    private UUID postId;
    private UUID variantId;
    private UUID socialAccountId;
    private String platform;
    private String title;
    private String content;
    private List<String> hashtags;
    private List<String> mediaUrls;
    private LocalDateTime scheduledAt;

}
