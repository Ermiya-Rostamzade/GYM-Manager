package com.gym.management.repository;

import com.gym.management.entity.TrafficLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrafficLogRepository extends JpaRepository<TrafficLog, Long> {

    List<TrafficLog> findByUserId(Long userId);

    List<TrafficLog> findAllByOrderByCheckInTimeDesc();

    Optional<TrafficLog> findFirstByUserIdAndCheckOutTimeIsNull(Long userId);

}
