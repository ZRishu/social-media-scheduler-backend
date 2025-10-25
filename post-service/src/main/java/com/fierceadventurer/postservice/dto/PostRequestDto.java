package com.fierceadventurer.postservice.dto;

import com.fierceadventurer.postservice.enums.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PostRequestDto {
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title cannot be longer than 255 characters")
    private String title;
    @NotBlank(message = "Content cannot be blank")
    private String content;
    private List<MediaAssetDto> mediaAssets;

}
