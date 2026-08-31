package com.gym.management.service;


import com.gym.management.dto.response.PaymentHistoryResponse;
import com.gym.management.mapper.PaymentMapper;
import com.gym.management.repository.PaymentRepository;
import com.gym.management.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public List<PaymentHistoryResponse> getPaymentHistoryBySubscriptionId(Long subscriptionId) {
        return  paymentRepository.findByUserSubscriptionId(subscriptionId)
                .stream()
                .map(payment -> paymentMapper.toHistoryResponse(payment))
                .toList();
    }


}
