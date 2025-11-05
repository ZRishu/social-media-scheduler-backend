package com.fierceadventurer.postservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CreatePostVariantRequestDto {

    @NotNull(message = "Social Account ID must be provided")
    private UUID socialAccountId;

    @NotBlank(message = "Platform cannot be blank")
    private String platform;

    private String content;
    private List<String> hashtags;
    private List<UUID> mediaAssetIds;
    private LocalDateTime scheduledAt;



}
