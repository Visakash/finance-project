package com.example.admin.service;

import org.springframework.http.ResponseEntity;

import com.example.admin.dto.AssignRoleDTO;
import com.example.admin.entity.Role;

public interface RoleService {

	ResponseEntity<?> createRole(Role role);

	ResponseEntity<?> getAllRoles();

	ResponseEntity<?> updateRole(Integer id, Role role);

	ResponseEntity<?> softDeleteRole(Integer id);

	ResponseEntity<?> deleteRole(Integer id);

	String assignRole(AssignRoleDTO dto);

}
