package com.fierceadventurer.userservice.mapper;

import com.fierceadventurer.userservice.dto.UserResponseDto;
import com.fierceadventurer.userservice.entity.User;
import com.fierceadventurer.userservice.events.KeyclockUserCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "userId")
    @Mapping(target = "displayName", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt" , ignore = true)
    @Mapping(target = "updatedAt",  ignore = true)
    User toEntity(KeyclockUserCreatedEvent event);

    UserResponseDto toDto(User user);
}
