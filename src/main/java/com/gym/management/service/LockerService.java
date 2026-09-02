package com.gym.management.service;

import com.gym.management.entity.Locker;
import com.gym.management.entity.LockerReservation;
import com.gym.management.entity.enums.GenderSection;
import com.gym.management.repository.LockerRepository;
import com.gym.management.repository.LockerReservationRepository;
import com.gym.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LockerService {

    private final LockerRepository lockerRepository;
    private final LockerReservationRepository lockerReservationRepository;
    private final UserRepository userRepository;

    public List<Locker> getAllLockers(){
        return lockerRepository.findAll();
    }

    public List<Locker> getAllLockerBySection(GenderSection genderSection){
        return lockerRepository.findByGenderSection(genderSection);

    }
}
