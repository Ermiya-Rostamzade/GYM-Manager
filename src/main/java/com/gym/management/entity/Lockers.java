package com.gym.management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "lockers")
public class Lockers extends BaseEntity {

    private String lockerName;

    private String genderSection;

    private String status;

    private String hardwareIp;

}
