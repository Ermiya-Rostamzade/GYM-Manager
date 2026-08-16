package com.gym.management.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SelfCheckInRequest(
        @NotBlank(message = "scan QR code")
        String qrCode
) {
}
