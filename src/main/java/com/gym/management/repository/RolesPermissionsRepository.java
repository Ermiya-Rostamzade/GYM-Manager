package com.gym.management.repository;

import com.gym.management.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolesPermissionsRepository extends JpaRepository<RolePermission, Long> {

    List<RolePermission> findByUserId(Long userId);

}
