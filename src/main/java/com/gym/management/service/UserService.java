package com.gym.management.service;

import com.gym.management.dto.request.UserLoginRequest;
import com.gym.management.dto.request.UserRegisterRequest;
import com.gym.management.dto.response.UserResponse;
import com.gym.management.entity.User;
import com.gym.management.mapper.UserMapper;
import com.gym.management.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponse registerUser(UserRegisterRequest request) {
        if (userRepository.existsByMobileNumber(request.mobileNumber())) {
            throw new IllegalArgumentException("Mobile number is already registered");
        }

        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    public UserResponse loginUser(UserLoginRequest request) {
        User user = userRepository.findByMobileNumber(request.mobileNumber())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!user.getIsActive()) {
            throw new IllegalStateException("User is inactive");
        }

        if (!user.getPassword().equals(request.password())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return userMapper.toResponse(user);
    }
}