package com.fierceadventurer.postservice.mapper;

import com.fierceadventurer.postservice.dto.PostResponseDto;
import com.fierceadventurer.postservice.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring" , uses = {MediaAssetMapper.class , PostVariantMapper.class})
public interface PostResponseMapper {
    PostResponseDto toDto(Post post);
}
