package com.fierceadventurer.mediaservice.service;

import com.fierceadventurer.mediaservice.dto.MediaUploadResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface MediaStorageService {
    MediaUploadResponseDto uploadFile(MultipartFile file);
}
