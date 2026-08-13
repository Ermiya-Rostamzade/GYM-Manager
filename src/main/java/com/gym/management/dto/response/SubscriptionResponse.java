package com.gym.management.dto.response;

import java.time.LocalDate;

public record SubscriptionResponse(
        Long id,
        PlanResponse plan,
        LocalDate startDate,
        LocalDate endDate,
        Integer remainingSessions,
        String status
) {}