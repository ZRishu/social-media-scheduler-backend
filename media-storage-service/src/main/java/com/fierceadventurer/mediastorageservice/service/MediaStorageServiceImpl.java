package com.fierceadventurer.mediastorageservice.service;

import com.fierceadventurer.mediastorageservice.dto.MediaUploadResponseDto;
import com.fierceadventurer.mediastorageservice.entity.MediaFile;
import com.fierceadventurer.mediastorageservice.repository.MediaFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaStorageServiceImpl implements MediaStorageService {

    private final MediaFileRepository mediaFileRepository;

    @Override
    @Transactional
    public MediaUploadResponseDto uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload empty file");
        }
        try {
            MediaFile mediaFile = new MediaFile();
            mediaFile.setFileName(file.getOriginalFilename());
            mediaFile.setContentType(file.getContentType());
            mediaFile.setSize(file.getSize());
            mediaFile.setData(file.getBytes());

            MediaFile savedFile = mediaFileRepository.save(mediaFile);

            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/media/")
                    .path(savedFile.getId().toString())
                    .toUriString();

            return new MediaUploadResponseDto(
                    fileUrl,
                    savedFile.getFileName(),
                    savedFile.getContentType(),
                    savedFile.getSize()
            );
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to store file", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MediaFile getFile(UUID id) {
        return mediaFileRepository.findById(id).orElseThrow(()-> new RuntimeException("File not found with id " + id));
    }
}
