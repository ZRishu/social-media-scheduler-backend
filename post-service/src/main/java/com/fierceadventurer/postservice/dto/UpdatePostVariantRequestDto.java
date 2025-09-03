package com.fierceadventurer.postservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class UpdatePostVariantRequestDto {
    private String platform;
    private String content;
    private List<String> hashtags;
    private List<UUID> mediaAssetIds;
    private LocalDateTime scheduledAt;
}
