package com.fierceadventurer.mediastorageservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaResourceDto {
    private String fileName;
    private String contentType;
    private byte[] data;
}
