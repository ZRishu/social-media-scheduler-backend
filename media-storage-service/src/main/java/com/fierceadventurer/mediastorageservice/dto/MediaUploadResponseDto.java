package com.fierceadventurer.mediastorageservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaUploadResponseDto {

    private String downloadUrl;

    private String fileName;

    private String contentType;

    private Long size;
}
