package com.fierceadventurer.mediaservice.controller;

import com.fierceadventurer.mediaservice.dto.MediaUploadResponseDto;
import com.fierceadventurer.mediaservice.service.MediaStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaStorageService mediaStorageService;

    @PostMapping("/upload")
    public ResponseEntity<MediaUploadResponseDto> uploadFile(
            @RequestParam("file") MultipartFile file){

        MediaUploadResponseDto response = mediaStorageService.uploadFile(file);
        return ResponseEntity.ok(response);
    }

}
