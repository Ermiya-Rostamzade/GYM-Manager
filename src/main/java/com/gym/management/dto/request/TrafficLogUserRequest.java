package com.gym.management.dto.request;

import com.gym.management.entity.enums.TrafficLogMethod;
import jakarta.validation.constraints.NotNull;

public record TrafficLogUserRequest(

        @NotNull(message = "the type of method should be defined")
        TrafficLogMethod method
) {
}
