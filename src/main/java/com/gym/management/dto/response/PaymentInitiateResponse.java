package com.gym.management.dto.response;

public record PaymentInitiateResponse(
        String paymentUrl, // لینک انتقال کاربر به درگاه بانک
        String trackId     // شماره پیگیری موقت جهت صحت‌سنجی
) {}