package com.fierceadventurer.schedulerservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class PublishRequestDto {
    private String id;
    private String content;
    private List<String> mediaUrls;
}
