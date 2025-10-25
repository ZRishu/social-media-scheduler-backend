package com.fierceadventurer.postservice.mapper;

import com.fierceadventurer.postservice.dto.PostRequestDto;
import com.fierceadventurer.postservice.entity.Post;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PostRequestMapper {
    PostRequestDto toDto(Post post);

    @Mapping(target = "id" , ignore = true)
    Post toEntity(PostRequestDto postRequestDto);

    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "datePosted" , ignore = true)
    void updatePost(PostRequestDto postRequestDto, @MappingTarget Post post);
}
