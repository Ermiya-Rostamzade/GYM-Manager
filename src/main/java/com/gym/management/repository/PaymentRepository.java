package com.gym.management.repository;

import com.gym.management.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByRefCode(String refCode);

    List<Payment> findByUserSubscriptionId(Long subscriptionid);

}
