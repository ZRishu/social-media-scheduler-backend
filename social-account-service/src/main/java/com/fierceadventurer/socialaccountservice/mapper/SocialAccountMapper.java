package com.fierceadventurer.socialaccountservice.mapper;

import com.fierceadventurer.socialaccountservice.dto.AuthTokenDto;
import com.fierceadventurer.socialaccountservice.dto.CreateSocialAccountRequestDto;
import com.fierceadventurer.socialaccountservice.dto.RateLimitQuotaDto;
import com.fierceadventurer.socialaccountservice.dto.SocialAccountResponseDto;
import com.fierceadventurer.socialaccountservice.entities.AuthToken;
import com.fierceadventurer.socialaccountservice.entities.RateLimitQuota;
import com.fierceadventurer.socialaccountservice.entities.SocialAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SocialAccountMapper {

    @Mapping(target = "accountId" , ignore = true)
    @Mapping(target = "status" , ignore = true)
    @Mapping(target = "createdAt" , ignore = true)
    @Mapping(target = "updatedAt" , ignore = true)
    @Mapping(target = "rateLimitQuota" , ignore = true)
    @Mapping(source = "authToken" , target = "authTokens")
    SocialAccount toEntity(CreateSocialAccountRequestDto requestDto);

    @Mapping(target = "tokenId" , ignore = true)
    @Mapping(target = "socialAccount" , ignore = true)
    AuthToken toEntity(AuthTokenDto authTokenDto) ;

    SocialAccountResponseDto toDto(SocialAccount socialAccount) ;

    @Mapping(target = "accessToken", ignore = true )
    @Mapping(target = "refreshToken" , ignore = true)
    AuthTokenDto toDto(AuthToken authToken);

    RateLimitQuotaDto toDto(RateLimitQuota rateLimitQuota);
}
