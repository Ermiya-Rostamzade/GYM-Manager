package com.gym.management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "locker_reservations")
@NoArgsConstructor
public class LockerReservation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locker_id", nullable = false)
    private Locker locker;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "traffic_log_id")
//    private TrafficLog trafficLog; واقعا چرا باید ترافیک رو ربط بدیم؟

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    @Column(name = "released_at")
    private LocalDateTime releasedAt; // اگر خالی باشد یعنی بسته، اگر زمان داشته باشد یعنی اکتیو. اینطوری مدیریتش ساده تره

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private LockerReservationStatus status; نیازی نیست

}
