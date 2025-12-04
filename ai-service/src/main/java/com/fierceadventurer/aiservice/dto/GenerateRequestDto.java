package com.fierceadventurer.aiservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class GenerateRequestDto {
    @NotBlank(message = "prompt cannot be empty")
    private String prompt;

    private List<UUID> mediaIds;

    private String tone;

    private String platform;
}
