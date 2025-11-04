package com.fierceadventurer.userservice.service;

import com.fierceadventurer.userservice.dto.UpdateUserRequestDto;
import com.fierceadventurer.userservice.dto.UserResponseDto;
import com.fierceadventurer.userservice.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    UserResponseDto getUserById(UUID userId);

    UserResponseDto updateUser(UUID userId , UpdateUserRequestDto requestDto);

    void deleteUser(UUID userId);

    Page<UserResponseDto> getAllUsers(Pageable pageable, UserStatus status);

    void suspendUser(UUID userId);

    void activateUser(UUID userId);
}
