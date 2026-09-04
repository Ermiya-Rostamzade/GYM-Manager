package com.gym.management.service;

import com.gym.management.dto.request.UserLoginRequest;
import com.gym.management.dto.request.UserProfileUpdateRequest;
import com.gym.management.dto.request.UserRegisterRequest;
import com.gym.management.dto.response.UserResponse;
import com.gym.management.entity.*;
import com.gym.management.entity.enums.LockerStatus;
import com.gym.management.entity.enums.Role;
import com.gym.management.mapper.UserMapper;
import com.gym.management.repository.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // Inject required repositories to handle cascading deletes
    private final LockerReservationRepository lockerReservationRepository;
    private final LockerRepository lockerRepository;
    private final TrafficLogRepository trafficLogRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final PaymentRepository paymentRepository;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            LockerReservationRepository lockerReservationRepository,
            LockerRepository lockerRepository,
            TrafficLogRepository trafficLogRepository,
            UserSubscriptionRepository userSubscriptionRepository,
            PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.lockerReservationRepository = lockerReservationRepository;
        this.lockerRepository = lockerRepository;
        this.trafficLogRepository = trafficLogRepository;
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.paymentRepository = paymentRepository;
    }

    public UserResponse getUserById(Long id) {
        return userMapper.toResponse(userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id)));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponse getUserByMobileNumber(String mobileNumber) {
        return userMapper.toResponse(userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new IllegalArgumentException("User not found with mobile number: " + mobileNumber)));
    }

    public User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    @Transactional
    public UserResponse updateUserProfile(Long id, UserProfileUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        user.setFullName(request.fullName());
        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        // 1. Release active locker reservations
        List<LockerReservation> reservations = lockerReservationRepository.findByUserId(id);
        for (LockerReservation res : reservations) {
            if (res.getReleasedAt() == null) {
                Locker locker = res.getLocker();
                locker.setStatus(LockerStatus.EMPTY);
                lockerRepository.save(locker);
            }
        }
        lockerReservationRepository.deleteAll(reservations);

        // 2. Delete traffic logs
        List<TrafficLog> logs = trafficLogRepository.findByUserIdOrderByCheckInTimeDesc(id);
        trafficLogRepository.deleteAll(logs);

        // 3. Delete subscriptions and associated payments
        List<UserSubscription> subs = userSubscriptionRepository.findByUserId(id);
        for (UserSubscription sub : subs) {
            List<Payment> payments = paymentRepository.findByUserSubscriptionId(sub.getId());
            paymentRepository.deleteAll(payments);
        }
        userSubscriptionRepository.deleteAll(subs);

        // 4. Safely delete the user
        userRepository.delete(user);
    }

    @Transactional
    public UserResponse registerUser(UserRegisterRequest request) {
        if (userRepository.existsByMobileNumber(request.mobileNumber())) {
            throw new IllegalArgumentException("Mobile number is already registered");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    public UserResponse loginUser(UserLoginRequest request) {
        User user = userRepository.findByMobileNumber(request.mobileNumber())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!user.getIsActive()) {
            throw new IllegalStateException("User is inactive");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse registerSelf(UserRegisterRequest request) {
        if (userRepository.existsByMobileNumber(request.mobileNumber())) {
            throw new IllegalArgumentException("Mobile number is already registered.");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.ATHLETE);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

}
