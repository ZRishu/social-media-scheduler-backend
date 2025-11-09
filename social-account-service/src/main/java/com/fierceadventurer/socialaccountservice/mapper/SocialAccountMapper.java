package com.fierceadventurer.socialaccountservice.mapper;

import com.fierceadventurer.socialaccountservice.dto.*;
import com.fierceadventurer.socialaccountservice.entities.AuthToken;
import com.fierceadventurer.socialaccountservice.entities.RateLimitQuota;
import com.fierceadventurer.socialaccountservice.entities.SocialAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SocialAccountMapper {

    @Mapping(target = "accountId" , ignore = true)
    @Mapping(target = "status" , ignore = true)
    @Mapping(target = "createdAt" , ignore = true)
    @Mapping(target = "updatedAt" , ignore = true)
    @Mapping(target = "rateLimitQuota" , ignore = true)
    SocialAccount toEntity(CreateSocialAccountRequestDto requestDto);


    @Mapping(target = "tokenId" , ignore = true)
    @Mapping(target = "socialAccount" , ignore = true)
    AuthToken toEntity(CreateAuthTokenRequestDto createDto) ;

    default List<AuthToken> mapAuthTokenDtoToList(CreateAuthTokenRequestDto createDto){
        if(createDto == null){
            return new ArrayList<>();
        }
        List<AuthToken> list = new ArrayList<>();
        list.add(toEntity(createDto));
        return list;
    }

    SocialAccountResponseDto toDto(SocialAccount socialAccount) ;

    AuthTokenDto toDto(AuthToken authToken);

    RateLimitQuotaDto toDto(RateLimitQuota rateLimitQuota);
}
