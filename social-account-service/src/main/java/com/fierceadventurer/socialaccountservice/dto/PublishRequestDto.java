package com.fierceadventurer.socialaccountservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class PublishRequestDto {
    private String content;
    private List<String> mediaUrls;
}
