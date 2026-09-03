package com.gym.management.repository;

import com.gym.management.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    Optional<Plan> findByTitle(String title);

    boolean existsByTitle(String title);

}
