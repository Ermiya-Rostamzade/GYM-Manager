package com.gym.management.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserProfileUpdateRequest(
        @NotBlank(message = "first and last name is required")
        String fullName
) {
}
