package com.gym.management.entity;

import com.gym.management.entity.enums.GenderSection;
import com.gym.management.entity.enums.LockerStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "lockers")
@NoArgsConstructor
public class Locker extends BaseEntity {

    @Column(name = "locker_number", nullable = false)
    private String lockerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_section", nullable = false)
    private GenderSection genderSection;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LockerStatus status;

    @Column(name = "hardware_ip", nullable = false)
    private String hardwareIp;

}
