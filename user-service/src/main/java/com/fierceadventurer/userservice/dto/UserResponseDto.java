package com.fierceadventurer.userservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDto {
    private String email;
    private String displayName;
    private String status;
    private LocalDateTime createAt;
}
