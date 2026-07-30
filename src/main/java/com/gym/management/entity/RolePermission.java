package com.gym.management.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles_permissions")
@Getter
@Setter
@NoArgsConstructor
public class RolePermission extends BaseEntity {

    @Column(name = "permission_name", nullable = false)
    private String permissionName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
