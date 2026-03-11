package com.example.admin.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.admin.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

	boolean existsByUserIdAndRoleId(Integer userId, Integer roleId);
}
