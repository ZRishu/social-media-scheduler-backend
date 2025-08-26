package com.fierceadventurer.postservice.mapper;

import com.fierceadventurer.postservice.dto.MediaAssetDto;
import com.fierceadventurer.postservice.entity.MediaAsset;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MediaAssetMapper {
    MediaAssetMapper Instance = Mappers.getMapper(MediaAssetMapper.class);
    MediaAssetDto toDto(MediaAsset mediaAsset);
    MediaAsset toEntity(MediaAssetDto mediaAssetDto);
}
