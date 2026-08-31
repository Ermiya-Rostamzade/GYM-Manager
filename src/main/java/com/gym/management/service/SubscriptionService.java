package com.gym.management.service;


import com.gym.management.dto.request.BuySubscriptionRequest;
import com.gym.management.dto.response.SubscriptionResponse;
import com.gym.management.entity.Payment;
import com.gym.management.entity.Plan;
import com.gym.management.entity.User;
import com.gym.management.entity.UserSubscription;
import com.gym.management.entity.enums.PaymentStatus;
import com.gym.management.entity.enums.SubscriptionStatus;
import com.gym.management.mapper.SubscriptionMapper;
import com.gym.management.repository.PaymentRepository;
import com.gym.management.repository.PlanRepository;
import com.gym.management.repository.UserRepository;
import com.gym.management.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class SubscriptionService {

    private final PaymentRepository paymentRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Transactional
    public SubscriptionResponse buyAndActivateDirectly(Long userId, BuySubscriptionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        Plan plan = planRepository.findById(request.planId())
                .orElseThrow(() -> new IllegalArgumentException("Plan not found with id: " + request.planId()));

        //We check the user has an expired active subscription or not ?
        userSubscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE)
                .ifPresent(sub -> {
                    boolean isDateValid = !sub.getEndDate().isBefore(LocalDate.now());
                    boolean hasSessions = sub.getRemainingSessions() == null || sub.getRemainingSessions() > 0;
                    if (isDateValid && hasSessions) {
                        throw new IllegalArgumentException("The user currently has an active subscription.");
                    }
                });
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(plan.getDurationDays());

        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUser(user);
        userSubscription.setPlan(plan);
        userSubscription.setStartDate(startDate);
        userSubscription.setEndDate(endDate);
        userSubscription.setStatus(SubscriptionStatus.ACTIVE);
        userSubscription.setRemainingSessions(plan.getTotalSessions());

        UserSubscription userSubscriptionSaved = userSubscriptionRepository.save(userSubscription);

        //save payment record.
        Payment payment = new Payment();
        payment.setUserSubscription(userSubscriptionSaved);
        payment.setAmount(plan.getPrice());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());
        payment.setRefCode("GYM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        paymentRepository.save(payment);

        return subscriptionMapper.toResponse(userSubscriptionSaved);
    }

    public List<SubscriptionResponse> getUserSubscriptions(Long userId) {
        return userSubscriptionRepository.findByUserId(userId)
                .stream()
                .map(sub -> subscriptionMapper.toResponse(sub))
                .toList();
    }

    //Checking the user's current active subscription with automatic expiration the end of the term or sessions.
    @Transactional
    public UserSubscription getActiveUserSubscription(Long userId) {

        UserSubscription activeSub = userSubscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE)
                .orElse(null);
        if (activeSub == null) {
            return null;
        }
        boolean isDateExpired = activeSub.getEndDate().isBefore(LocalDate.now());
        boolean isSessionsFinished = activeSub.getRemainingSessions() != null && activeSub.getRemainingSessions() <= 0;

        if (isDateExpired && isSessionsFinished) {
            activeSub.setStatus(SubscriptionStatus.EXPIRED);
            userSubscriptionRepository.save(activeSub);
            return null;
        }
        return activeSub;



    }
}
