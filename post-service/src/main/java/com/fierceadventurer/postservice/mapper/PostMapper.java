package com.fierceadventurer.postservice.mapper;

import com.fierceadventurer.postservice.dto.PostDto;
import com.fierceadventurer.postservice.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper(uses = {PostMapper.class , MediaAssetMapper.class})
public interface PostMapper {
    PostMapper Instance = Mappers.getMapper(PostMapper.class);
    PostDto toDto(Post post);
    Post toEntity(PostDto postDto);
}
