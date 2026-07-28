package com.gym.management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "locker_reservations")
public class LockerReservation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locker_id", nullable = false)
    private Locker locker;

    // FIXME: I doubt it.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "traffic_log_id")
    private TrafficLog trafficLog;

    private LocalDateTime assignedAt;

    private LocalDateTime releasedAt;

    private String status; // ACTIVE, CLOSED

}
