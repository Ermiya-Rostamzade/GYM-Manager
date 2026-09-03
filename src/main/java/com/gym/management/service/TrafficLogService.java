package com.gym.management.service;

import com.gym.management.dto.request.TrafficLogUserRequest;
import com.gym.management.dto.response.TrafficLogResponse;
import com.gym.management.entity.TrafficLog;
import com.gym.management.entity.User;
import com.gym.management.entity.UserSubscription;
import com.gym.management.mapper.TrafficLogMapper;
import com.gym.management.repository.TrafficLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrafficLogService {

    private final UserService userService;
    private final TrafficLogRepository trafficLogRepository;
    private final TrafficLogMapper trafficLogMapper;
    private final SubscriptionService subscriptionService;

    @Transactional
    public TrafficLogResponse createTrafficLog(Long userId, TrafficLogUserRequest request) {
        User user = userService.getUserEntityById(userId);

        // Check for overlapping active gym sessions (check-in without check-out)
        trafficLogRepository.findFirstByUserIdAndCheckOutTimeIsNull(user.getId())
                .ifPresent(activeLog -> {
                    throw new IllegalStateException("User already has an open check-in.");
                });
        UserSubscription activeSubscription = subscriptionService.getActiveUserSubscription(user.getId());
        if (activeSubscription == null) {
            throw new IllegalStateException("User does not have an active subscription");
        }
        // کسر جلسه در صورت محدود بودن پلن
        if (activeSubscription.getRemainingSessions() != null) {
            if (activeSubscription.getRemainingSessions() <= 0) {
                throw new IllegalStateException("The allowed number of subscription sessions has been exhausted.");
            }
            activeSubscription.setRemainingSessions(activeSubscription.getRemainingSessions() - 1);
        }

        TrafficLog trafficLog = new TrafficLog();
        trafficLog.setUser(user);
        trafficLog.setCheckInTime(LocalDateTime.now());
        trafficLog.setMethod(request.method());

        return trafficLogMapper.toResponse(trafficLogRepository.save(trafficLog));
    }

    public TrafficLogResponse getTrafficLogById(Long id) {
        return trafficLogMapper.toResponse(
                trafficLogRepository.findById(id).orElse(null));
    }

    public List<TrafficLogResponse> getAllTrafficLogs() {
        return trafficLogRepository.findAllByOrderByCheckInTimeDesc()
                .stream()
                .map(trafficLogMapper::toResponse)
                .toList();
    }

    public List<TrafficLogResponse> getTrafficLogsForUser(Long userId) {
        return trafficLogRepository.findByUserIdOrderByCheckInTimeDesc(userId)
                .stream()
                .map(trafficLogMapper::toResponse)
                .toList();
    }

    public TrafficLog getTrafficLogEntityById(Long id) {
        return trafficLogRepository.findById(id).orElse(null);
    }

    @Transactional
    public TrafficLogResponse updateTrafficLog(Long id, TrafficLogUserRequest request) {
        TrafficLog trafficLog = trafficLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Traffic log not found with id: " + id));

        trafficLog.setMethod(request.method());

        return trafficLogMapper.toResponse(trafficLog);
    }

    @Transactional
    public void setTrafficLogCheckOutTime(Long id, Long userId) {
        TrafficLog trafficLog = trafficLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Traffic log not found with id: " + id));

        if (!trafficLog.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You cannot check out another user's traffic log.");
        }

        if (trafficLog.getCheckOutTime() != null) {
            throw new IllegalStateException(
                    "Traffic log has already been checked out: " + id);
        }

        trafficLog.setCheckOutTime(LocalDateTime.now());
    }

    @Transactional
    public void deleteTrafficLog(Long id) {
        TrafficLog trafficLog = trafficLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Traffic log not found with id: " + id));

        trafficLogRepository.delete(trafficLog);
    }

}
