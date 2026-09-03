package com.gym.management.repository;

import com.gym.management.entity.Locker;
import com.gym.management.entity.enums.GenderSection;
import com.gym.management.entity.enums.LockerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LockerRepository extends JpaRepository<Locker, Long> {

    Optional<Locker> findFirstByGenderSectionAndStatus(GenderSection genderSection, LockerStatus lockerStatus);

    List<Locker> findByGenderSection(GenderSection genderSection);

}
