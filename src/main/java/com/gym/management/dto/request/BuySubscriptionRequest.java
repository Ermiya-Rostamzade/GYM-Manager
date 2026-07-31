package com.gym.management.dto.request;

import jakarta.validation.constraints.NotNull;


//This record is for when the user wants to purchase a subscription directly.
public record BuySubscriptionRequest(

        @NotNull(message = "plan_id is required")
        Long planId
) {}