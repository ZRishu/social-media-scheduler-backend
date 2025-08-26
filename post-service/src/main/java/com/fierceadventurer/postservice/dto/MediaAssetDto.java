package com.fierceadventurer.postservice.dto;

import com.fierceadventurer.postservice.enums.PostType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MediaAssetDto {
    private UUID assetId;
    private String storageUrl;
    private Long size;
    private LocalDateTime uploadDate;
    private PostType mediaType;
}
