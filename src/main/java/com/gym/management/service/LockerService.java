package com.gym.management.service;

import com.gym.management.dto.request.LockerCreateRequest;
import com.gym.management.entity.Locker;
import com.gym.management.entity.LockerReservation;
import com.gym.management.entity.User;
import com.gym.management.entity.enums.GenderSection;
import com.gym.management.entity.enums.LockerStatus;
import com.gym.management.repository.LockerRepository;
import com.gym.management.repository.LockerReservationRepository;
import com.gym.management.repository.TrafficLogRepository;
import com.gym.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LockerService {

    private final LockerRepository lockerRepository;
    private final LockerReservationRepository lockerReservationRepository;
    private final UserRepository userRepository;
    private final TrafficLogRepository trafficLogRepository;

    public List<Locker> getAllLockers() {
        return lockerRepository.findAll();
    }

    public List<Locker> getAllLockerBySection(GenderSection genderSection) {
        return lockerRepository.findByGenderSection(genderSection);
    }

    public LockerReservation getActiveReservationForUser(long userId) {
        return lockerReservationRepository.findFirstByUserIdAndReleasedAtIsNull(userId).orElse(null);
    }

    // Reserving a preferred locker by the user
    @Transactional
    public LockerReservation reserveLocker(long userId, long lockerId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean hasActiveCheckIn = trafficLogRepository.findFirstByUserIdAndCheckOutTimeIsNull(userId).isPresent();
        if(!hasActiveCheckIn){
            throw new IllegalStateException("You must check in to the gym before reserving a locker.");
        }

        if (lockerReservationRepository.findFirstByUserIdAndReleasedAtIsNull(userId).isPresent()) {
            throw new IllegalStateException("User is already reserved");
        }

        Locker locker = lockerRepository.findById(lockerId)
                .orElseThrow(() -> new IllegalArgumentException("Locker not found"));
        if (locker.getStatus() != LockerStatus.EMPTY) {
            throw new IllegalStateException("locker is not available");
        }

        locker.setStatus(LockerStatus.OCCUPIED);
        lockerRepository.save(locker);

        LockerReservation lockerReservation = new LockerReservation();
        lockerReservation.setUser(user);
        lockerReservation.setLocker(locker);
        lockerReservation.setAssignedAt(LocalDateTime.now());

        return lockerReservationRepository.save(lockerReservation);
    }

    // Release locker
    @Transactional
    public void releaseLocker(Long userId) {
        LockerReservation reservation = lockerReservationRepository.findFirstByUserIdAndReleasedAtIsNull(userId)
                .orElseThrow(() -> new IllegalStateException("هیچ کمد فعالی برای آزادسازی یافت نشد."));

        reservation.setReleasedAt(LocalDateTime.now());
        lockerReservationRepository.save(reservation);

        Locker locker = reservation.getLocker();
        locker.setStatus(LockerStatus.EMPTY);
        lockerRepository.save(locker);
    }

    @Transactional
    public Locker createLocker(LockerCreateRequest request) {
        if (lockerRepository.existsByLockerNumberAndGenderSection(request.lockerNumber(), request.genderSection())) {
            throw new IllegalArgumentException("A locker with this number already exists in the selected area.");
        }

        Locker locker = new Locker();
        locker.setLockerNumber(request.lockerNumber());
        locker.setGenderSection(request.genderSection());
        locker.setStatus(LockerStatus.EMPTY);
        locker.setHardwareIp(request.hardwareIp() != null && !request.hardwareIp().trim().isEmpty() ? request.hardwareIp().trim() : null);

        return lockerRepository.save(locker);
    }

    @Transactional
    public void updateLockerStatus(Long lockerId, LockerStatus newStatus) {
        Locker locker = lockerRepository.findById(lockerId)
                .orElseThrow(() -> new IllegalArgumentException("Locker not found"));

        locker.setStatus(newStatus);
        lockerRepository.save(locker);
    }


    @Transactional
    public void releaseLockerSafely(Long userId) {
        lockerReservationRepository.findFirstByUserIdAndReleasedAtIsNull(userId)
                .ifPresent(reservation -> {
                    reservation.setReleasedAt(LocalDateTime.now());
                    lockerReservationRepository.save(reservation);

                    Locker locker = reservation.getLocker();
                    locker.setStatus(LockerStatus.EMPTY);
                    lockerRepository.save(locker);
                });
    }

}
