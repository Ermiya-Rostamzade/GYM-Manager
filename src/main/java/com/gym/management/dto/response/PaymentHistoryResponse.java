package com.gym.management.dto.response;

import com.gym.management.entity.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentHistoryResponse(
        Long id,
        BigDecimal amount,
        String refCode,
        PaymentStatus status,
        LocalDateTime paidAt,
        Long userSubscriptionId //شناسه اشتراکی که برای آن پرداخت انجام شده
) {
}
