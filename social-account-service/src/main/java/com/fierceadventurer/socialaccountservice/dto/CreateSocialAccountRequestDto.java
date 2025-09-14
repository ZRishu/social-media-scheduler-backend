package com.fierceadventurer.socialaccountservice.dto;

import com.fierceadventurer.socialaccountservice.enums.Provider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateSocialAccountRequestDto {

    @NotNull(message = "User ID must be provided")
    private UUID userId;

    @NotNull(message = "provider cannot be null")
    private Provider provider;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    private String displayName;

    private String profileImageUrl;

    @NotNull
    private CreateAuthTokenRequestDto authToken;
}
