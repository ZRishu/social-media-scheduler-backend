package com.fierceadventurer.mediastorageservice.service;

import com.fierceadventurer.mediastorageservice.dto.MediaResourceDto;
import com.fierceadventurer.mediastorageservice.dto.MediaUploadResponseDto;
import com.fierceadventurer.mediastorageservice.entity.MediaFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface MediaStorageService {

    MediaUploadResponseDto uploadFile(MultipartFile file , UUID userId);

    MediaResourceDto getFile(UUID id);
}
