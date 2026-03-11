package com.example.admin.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.admin.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	Optional<Role> findByRoleName(String roleName);

	boolean existsByRoleName(String roleName);

	List<Role> findAllByDeletedFalse();
}