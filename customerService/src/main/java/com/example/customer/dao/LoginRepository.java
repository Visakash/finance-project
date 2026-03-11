package com.example.customer.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.customer.entity.Login;

@Repository
public interface LoginRepository extends JpaRepository<Login, Integer> {
	
	Login findByUsername(String username);
}