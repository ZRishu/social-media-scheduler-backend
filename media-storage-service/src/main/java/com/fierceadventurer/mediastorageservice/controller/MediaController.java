package com.fierceadventurer.mediastorageservice.controller;

import com.fierceadventurer.mediastorageservice.dto.MediaResourceDto;
import com.fierceadventurer.mediastorageservice.dto.MediaUploadResponseDto;
import com.fierceadventurer.mediastorageservice.entity.MediaFile;
import com.fierceadventurer.mediastorageservice.service.MediaStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaStorageService mediaStorageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MediaUploadResponseDto> uploadMedia(@RequestParam("file") MultipartFile file,
                                                              @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        MediaUploadResponseDto response = mediaStorageService.uploadFile(file, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getMediaFile(@PathVariable UUID id) {
        MediaResourceDto resource = mediaStorageService.getFile(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, resource.getContentType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFileName() +"\"")
                .body(resource.getData());

    }
}
