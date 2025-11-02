package com.fierceadventurer.mediaservice.service.impl;

import com.fierceadventurer.mediaservice.dto.MediaUploadResponseDto;
import com.fierceadventurer.mediaservice.service.MediaStorageService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3StorageServiceImpl implements MediaStorageService {

    private  final S3Client s3Client;
    private final String bucketName;
    private final String region;

    public S3StorageServiceImpl(S3Client s3Client,
                                @Value("${aws.S3.bucket-name}") String bucketName,
                                @Value("${aws.S3.region}")String region) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.region = region;
    }

    @Override
    public MediaUploadResponseDto uploadFile(MultipartFile file) {
        if(file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload empty file");
        }

        try{
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if(originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String newFileName = UUID.randomUUID().toString() + fileExtension;
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(newFileName)
                    .contentType(newFileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putRequest , RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String fileUrl = String.format("http://%s.s3.%s.amazonaws.com/%s",
                    bucketName,
                    region,
                    newFileName);

            return new MediaUploadResponseDto(fileUrl , newFileName , file.getContentType(), file.getSize());

        } catch (S3Exception | IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }
}
