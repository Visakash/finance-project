package com.example.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.admin.dto.AssignRoleDTO;
import com.example.admin.entity.Role;
import com.example.admin.service.RoleService;

@RestController
@RequestMapping("/api/admin/roles")
public class RoleController {

	@Autowired
	private RoleService roleService;

	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody Role role) {
		return roleService.createRole(role);
	}

	@GetMapping("/all")
	public ResponseEntity<?> all() {
		return roleService.getAllRoles();
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Role role) {
		return roleService.updateRole(id, role);
	}

	@DeleteMapping("/softdelete/{id}")
	public ResponseEntity<?> softDelete(@PathVariable Integer id) {
		return roleService.softDeleteRole(id);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		return roleService.deleteRole(id);
	}

	@PostMapping("/assign-role")
	public ResponseEntity<?> assign(@RequestBody AssignRoleDTO dto) {
		return ResponseEntity.ok(roleService.assignRole(dto));
	}
}