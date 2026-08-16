package com.gym.management.dto.response;

import com.gym.management.entity.enums.PlanType;

import java.math.BigDecimal;

public record PlanResponse(
        Long id,
        String title,
        BigDecimal price,
        Integer sessions,
        Integer durationDays,
        PlanType planType
) {
}
