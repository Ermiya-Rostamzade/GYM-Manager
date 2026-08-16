package com.gym.management.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
        @NotBlank(message = "your phone number is required")
        String mobileNumber,

        @NotBlank(message = "password is required")
        String password
) {
}
