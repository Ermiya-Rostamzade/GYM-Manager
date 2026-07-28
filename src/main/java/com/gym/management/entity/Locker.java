package com.gym.management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "lockers")
public class Locker extends BaseEntity {

    private String lockerName;

    private String genderSection; // MEN, WOMEN

    private String status; // EMPTY, OCCUPIED, MAINTENANCE

    private String hardwareIp;

}
