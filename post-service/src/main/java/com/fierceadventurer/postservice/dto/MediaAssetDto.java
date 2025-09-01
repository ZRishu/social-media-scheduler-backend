package com.fierceadventurer.postservice.dto;

import com.fierceadventurer.postservice.enums.PostType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MediaAssetDto {

    private String storageUrl;
    private Long size;
    private PostType mediaType;
}
