package com.example.customer.dao;

import java.util.Optional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.customer.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

	Optional<User> findByEmailAndDeletedFalse(String email);

	Optional<User> findByUsernameAndDeletedFalse(String username);

	Page<User> findByDeletedFalse(Pageable pageable);
}