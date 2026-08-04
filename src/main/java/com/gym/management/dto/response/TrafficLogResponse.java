package com.gym.management.dto.response;

import java.time.LocalDateTime;
import com.gym.management.entity.enums.TrafficLogMethod;

public record TrafficLogResponse (
        Long id,
        LocalDateTime checkInTime,
        LocalDateTime checkOutTime,
        String message, // پیام وضعیت
        TrafficLogMethod entryMethod
){}
