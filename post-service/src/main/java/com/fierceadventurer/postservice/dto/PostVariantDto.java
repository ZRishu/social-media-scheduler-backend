package com.fierceadventurer.postservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class PostVariantDto {
    private UUID variantId;
    private String platform;
    private String content;
    private List<String> HashTags;
    private LocalDateTime scheduledAt;
    private List<MediaAssetDto> mediaAssets;
}
