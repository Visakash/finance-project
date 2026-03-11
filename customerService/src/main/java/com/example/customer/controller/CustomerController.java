package com.example.customer.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.example.customer.dto.*;
import com.example.customer.entity.User;
import com.example.customer.service.CustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

	@Autowired
	private CustomerService cs;

	@PostMapping("/register")
	public ResponseEntity<UserResponse> register(@Valid @RequestBody User user) {
		return ResponseEntity.status(HttpStatus.CREATED).body(cs.registerUserInfo(user));
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		return cs.getUserDataById(id);
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<?> getByEmail(@PathVariable String email) {
		return cs.getUserDataByEmail(email);
	}

	@GetMapping("/username/{username}")
	public ResponseEntity<?> getByUsername(@PathVariable String username) {
		return cs.getUserDataByUsername(username);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody User user) {
		return cs.updateUser(id, user);
	}

	@DeleteMapping("/soft-delete/{id}")
	public ResponseEntity<?> softDelete(@PathVariable Integer id) {
		return cs.softDeleteUser(id);
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest req) {
		return cs.forgotPassword(req);
	}

	@PostMapping("/verify-otp-reset")
	public ResponseEntity<?> verifyOtp(@Valid @RequestBody OtpResetRequest req) {
		return cs.verifyOtpAndResetPassword(req.getEmail(), req.getOtp(), req.getNewPassword());
	}

	@PostMapping("/change-password")
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest req) {
		return cs.changePassword(req);
	}

	@GetMapping("/getUsersByPage")
	public List<User> getByPage(@RequestParam int pageNumber, @RequestParam int recordNumber) {
		return cs.getUsersByPage(pageNumber, recordNumber);
	}
}