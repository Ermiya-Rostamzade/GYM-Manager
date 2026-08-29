package com.gym.management.repository;

import com.gym.management.entity.TrafficLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrafficLogRepository extends JpaRepository<TrafficLog, Long> {

    List<TrafficLog> findByUserId(Long userId);

    List<TrafficLog> findAllByOrderByCheckInTimeDesc();

}
