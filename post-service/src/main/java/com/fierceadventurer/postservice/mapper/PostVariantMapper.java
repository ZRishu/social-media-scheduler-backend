package com.fierceadventurer.postservice.mapper;

import com.fierceadventurer.postservice.dto.CreatePostVariantRequestDto;
import com.fierceadventurer.postservice.dto.PostVariantResponseDto;
import com.fierceadventurer.postservice.dto.UpdatePostVariantRequestDto;
import com.fierceadventurer.postservice.entity.PostVariant;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PostVariantMapper {

    PostVariantResponseDto toDto(PostVariant postVariant);

    @Mapping(target = "variantId" , ignore = true)
    @Mapping(target = "post" , ignore = true)
    @Mapping(target = "mediaAssets" , ignore = true)
    PostVariant toEntity(CreatePostVariantRequestDto requestDtoDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "variantId" , ignore = true)
    @Mapping(target = "post" , ignore = true)
    @Mapping(target = "mediaAssets" , ignore = true)
    void updateFromDto(UpdatePostVariantRequestDto requestDtoDto, @MappingTarget PostVariant postVariant);
}
