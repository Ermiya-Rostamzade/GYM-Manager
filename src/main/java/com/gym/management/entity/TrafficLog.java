package com.gym.management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "traffic_logs")
public class TrafficLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    private String method; // QR_CODE, RFID, FINGERPRINT

}
