package com.fierceadventurer.userservice.service.impl;

import com.fierceadventurer.userservice.dto.UpdateUserRequestDto;
import com.fierceadventurer.userservice.dto.UserResponseDto;
import com.fierceadventurer.userservice.entity.User;
import com.fierceadventurer.userservice.enums.UserStatus;
import com.fierceadventurer.userservice.mapper.UserMapper;
import com.fierceadventurer.userservice.repository.UserRepository;
import com.fierceadventurer.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(UUID userId) {
        log.info("Fetching user Profile for id: {}", userId);
        return userRepository.findById(userId)
                .map(userMapper::toDto)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id: " + userId));
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(UUID userId, UpdateUserRequestDto requestDto) {
        log.info("Updating user Profile for id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id: " + userId));

        user.setDisplayName(requestDto.getDisplayName());
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        log.warn("soft-deleting user with id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id: " + userId));

        user.setStatus(UserStatus.DELETED);
        user.setEmail(user.getId() + "@deleted.user");
        userRepository.save(user);
    }

    @Override
    @Transactional
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        log.info("Fetching all users for Pageable: {}", pageable.getPageNumber());
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(userMapper::toDto);
    }

    @Override
    @Transactional
    public void suspendUser(UUID userId) {
        log.warn("Suspending user with id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id: " + userId));
        user.setStatus(UserStatus.SUSPENDED);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void activateUser(UUID userId) {
        log.info("Activating user with id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id: " + userId));

        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }
}
