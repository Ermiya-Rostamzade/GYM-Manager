package com.gym.management.repository;

import com.gym.management.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolesPermissionsRepository extends JpaRepository<RolePermission, Long> {

    List<RolePermission> findByUserId(Long userId);
}
