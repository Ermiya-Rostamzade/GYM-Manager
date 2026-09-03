package com.gym.management.dto.request;

import com.gym.management.entity.enums.GenderSection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LockerCreateRequest(
        @NotBlank(message = "Locker name or number is required")
        String lockerNumber,

        @NotNull(message = "Locker room section (Men/Women) must be specified")
        GenderSection genderSection,

        String hardwareIp
) {
}