package com.fierceadventurer.postservice.dto;

import com.fierceadventurer.postservice.enums.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class PostDto {
    private UUID id;
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title cannot be longer than 255 characters")
    private String title;
    @NotBlank(message = "Content cannot be blank")
    private String content;
    private PostStatus status;
    private LocalDateTime datePosted;
    private LocalDateTime lastEdited;
    private List<MediaAssetDto> mediaAssets;
    private List<PostVariantDto> postVariants;
}
