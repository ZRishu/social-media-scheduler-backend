package com.fierceadventurer.postservice.dto;

import com.fierceadventurer.postservice.enums.PostStatus;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class PostResponseDto {

    private String title;
    private String content;
    private PostStatus status;
    private LocalDateTime datePosted;
    private LocalDateTime lastEdited;
    private List<MediaAssetDto> mediaAssets;

}
