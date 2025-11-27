package com.fierceadventurer.mediastorageservice.service;

import com.fierceadventurer.mediastorageservice.dto.MediaResourceDto;
import com.fierceadventurer.mediastorageservice.dto.MediaUploadResponseDto;
import com.fierceadventurer.mediastorageservice.entity.MediaFile;
import com.fierceadventurer.mediastorageservice.repository.MediaFileRepository;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaStorageServiceImpl implements MediaStorageService {

    private final MediaFileRepository mediaFileRepository;
    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;


    @Override
    @Transactional
    public MediaUploadResponseDto uploadFile(MultipartFile file , UUID userId) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload empty file");
        }
        try {
            String originalFilename = file.getOriginalFilename();
            String storagePath = userId.toString() + "/" + UUID.randomUUID() + "-" + originalFilename;

            log.info("Uploading to remote MinIO path: {}", storagePath);

            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName)
                            .object(storagePath)
                            .stream(file.getInputStream() , file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            MediaFile mediaFile = new MediaFile();
            mediaFile.setFileName(originalFilename);
            mediaFile.setContentType(file.getContentType());
            mediaFile.setSize(file.getSize());
            mediaFile.setStoragePath(storagePath);
            mediaFile.setUserId(userId);

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
        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("Upload failed", e);
            throw new IllegalArgumentException("Failed to store file", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MediaResourceDto getFile(UUID id) {
        MediaFile mediaFile = mediaFileRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("File not found with id " + id));

        try{
            log.info("Streaming file from Remote MinIO: {}" , mediaFile.getStoragePath());

            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(mediaFile.getStoragePath())
                            .build()
            );

            return new MediaResourceDto(
                    mediaFile.getFileName(),
                    mediaFile.getContentType(),
                    stream.readAllBytes()
            );
        } catch (Exception e) {
            log.error("Download failed",e);
            throw new RuntimeException("Failed to fetch file content: " + e.getMessage());
        }
    }
}
