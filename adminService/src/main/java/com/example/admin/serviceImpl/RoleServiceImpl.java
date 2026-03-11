package com.example.admin.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.admin.dao.*;
import com.example.admin.dto.*;
import com.example.admin.entity.*;
import com.example.admin.exception.*;
import com.example.admin.feign.CustomerFeignClient;
import com.example.admin.service.RoleService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRoleRepository userRoleRepo;
	@Autowired
	private CustomerFeignClient customerFeignClient;

	@Override
	@Transactional
	@CacheEvict(value = "roles", allEntries = true)
	public ResponseEntity<?> createRole(Role role) {
		String name = role.getRoleName().toUpperCase().trim();
		if (roleRepository.existsByRoleName(name))
			throw new ResourceAlreadyExistsException("Role exists: " + name);
		role.setRoleName(name);
		role.setStatus(true);
		role.setDeleted(false);
		return ResponseEntity.ok(roleRepository.save(role));
	}

	@Override
	@Cacheable("roles")
	public ResponseEntity<?> getAllRoles() {
		return ResponseEntity.ok(roleRepository.findAllByDeletedFalse());
	}

	@Override
	@Transactional
	@CacheEvict(value = "roles", allEntries = true)
	public ResponseEntity<?> updateRole(Integer id, Role updatedRole) {
		Role role = getActiveRole(id);
		String name = updatedRole.getRoleName().toUpperCase().trim();
		roleRepository.findByRoleName(name).ifPresent(r -> {
			if (!r.getId().equals(id))
				throw new ResourceAlreadyExistsException("Role name in use: " + name);
		});
		role.setRoleName(name);
		role.setStatus(updatedRole.getStatus());
		roleRepository.save(role);
		return ResponseEntity.ok("Role updated successfully.");
	}

	@Override
	@Transactional
	@CacheEvict(value = "roles", allEntries = true)
	public ResponseEntity<?> softDeleteRole(Integer id) {
		Role role = getActiveRole(id);
		role.setDeleted(true);
		role.setStatus(false);
		roleRepository.save(role);
		return ResponseEntity.ok("Role soft-deleted.");
	}

	@Override
	@Transactional
	@CacheEvict(value = "roles", allEntries = true)
	public ResponseEntity<?> deleteRole(Integer id) {
		Role role = roleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Role not found: " + id));
		roleRepository.delete(role);
		return ResponseEntity.ok("Role permanently deleted.");
	}

	@Override
	@Transactional
	public String assignRole(AssignRoleDTO dto) {
		UserDTO user = customerFeignClient.getUserByUsername(dto.getUsername());
		if (user == null)
			throw new ResourceNotFoundException("User not found: " + dto.getUsername());

		String name = dto.getRoleName().toUpperCase().trim();
		Role role = roleRepository.findByRoleName(name)
				.orElseThrow(() -> new ResourceNotFoundException("Role not found: " + name));

		if (Boolean.TRUE.equals(role.getDeleted()) || Boolean.FALSE.equals(role.getStatus()))
			throw new IllegalStateException("Role '" + name + "' is inactive.");

		if (userRoleRepo.existsByUserIdAndRoleId(user.getId(), role.getId()))
			throw new ResourceAlreadyExistsException("Role already assigned to: " + dto.getUsername());

		userRoleRepo.save(UserRole.builder().userId(user.getId()).roleId(role.getId()).build());
		return "Role assigned successfully.";
	}

	private Role getActiveRole(Integer id) {
		Role role = roleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Role not found: " + id));
		if (Boolean.TRUE.equals(role.getDeleted()))
			throw new ResourceNotFoundException("Role already deleted.");
		return role;
	}
}