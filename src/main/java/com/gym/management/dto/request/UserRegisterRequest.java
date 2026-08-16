package com.gym.management.dto.request;

import com.gym.management.entity.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRegisterRequest(


        @NotBlank(message = "mobile number is required.")
        @Pattern(regexp = "^09\\d{9}$", message = "The mobile number format is invalid.")
        String mobileNumber,

        @NotBlank(message = "your full name is required")
        String fullName,

        @NotBlank(message = "password is required")
        String password,

        @NotBlank(message = "The User's role must be defind.")
        Role role
) {
}
