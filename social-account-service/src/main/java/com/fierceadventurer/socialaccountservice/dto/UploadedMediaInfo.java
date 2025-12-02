package com.fierceadventurer.socialaccountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadedMediaInfo {
    private String assetUrn;
    private String category;
}
