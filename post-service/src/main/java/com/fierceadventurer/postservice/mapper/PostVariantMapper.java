package com.fierceadventurer.postservice.mapper;

import com.fierceadventurer.postservice.dto.PostVariantDto;
import com.fierceadventurer.postservice.entity.PostVariant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {MediaAssetMapper.class})
public interface PostVariantMapper {
    PostVariantMapper Instance = Mappers.getMapper(PostVariantMapper.class);
    PostVariantDto toDto(PostVariant postVariant);
    PostVariant toEntity(PostVariantDto postVariantDto);
}
