package com.gym.management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "plans")
public class Plan extends BaseEntity {

    private String title;

    private double price;

    // Duration Days = Sessions.
    // Sessions remaining = duration days - traffic logs
    private int durationDays;

    private String planType; // MONTHLY, SESSIONAL, VIP

}
