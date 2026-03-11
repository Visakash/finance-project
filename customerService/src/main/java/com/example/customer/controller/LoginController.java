package com.example.customer.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.customer.dao.LoginRepository;
import com.example.customer.dto.LoginRequest;
import com.example.customer.entity.Login;
import com.example.customer.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/customer")
public class LoginController {

	@Autowired
	private LoginRepository loginRepository;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
		log.info("Login attempt: {}", request.getUsername());
		Login login = loginRepository.findByUsername(request.getUsername());
		if (login == null || !passwordEncoder.matches(request.getPassword(), login.getPassword()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
		return ResponseEntity.ok(jwtUtil.generateToken(request.getUsername()));
	}
}