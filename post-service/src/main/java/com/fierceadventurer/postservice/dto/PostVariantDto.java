package com.fierceadventurer.postservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class PostVariantDto {
    private String platform;
    private String content;
    private List<String> hashtags;
    private LocalDateTime scheduledAt;
    private List<UUID> mediaAssetIds;
}
