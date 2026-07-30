package com.gym.management.repository;

import com.gym.management.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gym.management.entity.enums.SubscriptionStatus;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    List<UserSubscription> findByUserId(Long userId);

    //Finding an active subscription
    Optional<UserSubscription> findByUserIdAndStatus(Long userId, SubscriptionStatus status);
}
