package com.fierceadventurer.mediaservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaUploadResponseDto {
    private String storageUrl;
    private String fileName;
    private String contentType;
    private Long size;
}
