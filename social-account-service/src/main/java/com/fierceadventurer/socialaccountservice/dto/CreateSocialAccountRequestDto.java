package com.fierceadventurer.socialaccountservice.dto;

import com.fierceadventurer.socialaccountservice.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateSocialAccountRequestDto {

    @NotNull(message = "provider cannot be null")
    private String provider;

    @NotNull(message = "Account Type must be specified")
    private AccountType accountType;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    private String displayName;

    private String profileImageUrl;

    @NotBlank(message = "External ID cannot be blank")
    private String externalId;

    @NotNull
    private String authCode;

    private String redirectUri;





}
