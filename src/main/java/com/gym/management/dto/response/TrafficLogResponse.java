package com.gym.management.dto.response;

import com.gym.management.entity.enums.TrafficLogMethod;

import java.time.LocalDateTime;

public record TrafficLogResponse(
        Long id,
        LocalDateTime checkInTime,
        LocalDateTime checkOutTime,
        String message, // پیام وضعیت
        TrafficLogMethod entryMethod
) {
}
