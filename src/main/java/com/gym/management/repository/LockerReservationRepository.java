package com.gym.management.repository;

import com.gym.management.entity.LockerReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LockerReservationRepository extends JpaRepository<LockerReservation, Long> {

    List<LockerReservation> findByUserId(Long userId);

    List<LockerReservation> findByLockerId(Long lockerId);

}
