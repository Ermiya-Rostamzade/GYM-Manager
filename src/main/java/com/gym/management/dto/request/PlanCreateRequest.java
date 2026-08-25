package com.gym.management.dto.request;

import com.gym.management.entity.enums.PlanType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

//Defining a new exercise plan
public record PlanCreateRequest(

        @NotBlank(message = "plan title is required")
        String title,

        @NotNull(message = "price title is required")
        @Min(value = 0, message = "amount cannot be negative")
        BigDecimal price,

        @NotNull(message = "The number of validity days is mandatory")
        @Min(value = 1, message = "The validity should be at least 30 days")
        Integer durationDays,

        @Min(value = 1, message = "Sessions count must be at least 1")
        Integer totalSessions,//Optional for unlimited plans

        @NotNull(message = "the type of plan should be defined")
        PlanType planType
) {
}
