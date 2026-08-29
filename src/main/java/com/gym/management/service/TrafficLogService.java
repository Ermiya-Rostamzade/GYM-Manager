package com.gym.management.service;

import com.gym.management.dto.request.TrafficLogUserRequest;
import com.gym.management.dto.response.TrafficLogResponse;
import com.gym.management.entity.TrafficLog;
import com.gym.management.entity.User;
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

    public TrafficLogResponse createTrafficLog(TrafficLogUserRequest request) {
        User user = userService.getUserEntityById(request.userId());

        TrafficLog trafficLog = new TrafficLog();
        trafficLog.setUser(user);
        trafficLog.setCheckInTime(LocalDateTime.now());
        trafficLog.setMethod(request.method());

        return trafficLogMapper.toResponse(trafficLogRepository.save(trafficLog));
    }

    public TrafficLogResponse getTrafficLogById(Long id) {
        return trafficLogMapper.toResponse(
                trafficLogRepository.findById(id).orElse(null)
        );
    }

    public List<TrafficLogResponse> getAllTrafficLogs() {
        return trafficLogRepository.findAllByOrderByCheckInTimeDesc()
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
    public void setTrafficLogCheckOutTime(Long id) {
        TrafficLog trafficLog = trafficLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Traffic log not found with id: " + id));

        if (trafficLog.getCheckOutTime() != null) {
            throw new IllegalStateException(
                    "Traffic log has already been checked out: " + id
            );
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
