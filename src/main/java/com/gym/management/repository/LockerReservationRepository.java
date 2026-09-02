package com.gym.management.repository;

import com.gym.management.entity.LockerReservation;
import com.gym.management.entity.enums.LockerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LockerReservationRepository extends JpaRepository<LockerReservation, Long> {

    List<LockerReservation> findByUserId(Long userId);

    List<LockerReservation> findByLockerId(Long lockerId);

    Optional<LockerReservation> findFirstByUserIdAndReleasedAtIsNull(Long userId);

    Optional<LockerReservation> findFirstByLockerIdAndReleasedAtIsNull(Long lockerId);

}
