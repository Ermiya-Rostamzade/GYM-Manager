package com.gym.management.dto.response;

import com.gym.management.entity.enums.Role;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String mobileNumber,
        String fullName,
        Role role,
        Boolean isActive,
        LocalDateTime createdAt
) {
}
