package com.example.customer.serviceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.customer.dao.*;
import com.example.customer.dto.*;
import com.example.customer.entity.*;
import com.example.customer.exception.*;
import com.example.customer.feign.NotificationClient;
import com.example.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LoginRepository loginRepository;

	@Autowired
	private NotificationClient notificationClient;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public UserResponse registerUserInfo(User user) {
		if (userRepository.existsByEmail(user.getEmail()))
			throw new ResourceAlreadyExistsException("Email already registered: " + user.getEmail());
		if (userRepository.existsByUsername(user.getUsername()))
			throw new ResourceAlreadyExistsException("Username already taken: " + user.getUsername());

		String encoded = passwordEncoder.encode(user.getPassword());
		user.setPassword(encoded);
		user.setStatus(true);
		user.setDeleted(false);
		user.setCreatedBy(user.getUsername());
		user.setUpdatedBy(user.getUsername());

		Login login = new Login();
		login.setUsername(user.getUsername());
		login.setEmail(user.getEmail());
		login.setPassword(encoded);
		login.setUser(user);
		user.setLogin(login);

		User saved = userRepository.save(user);
		log.info("User registered: id={}", saved.getId());
		sendEmailAsync(saved.getEmail(),
				"Welcome to LoanApp, " + saved.getUsername() + "! Your account has been created.");
		return UserResponse.builder().username(saved.getUsername()).message("User registered successfully.").build();
	}

	@Async
	public void sendEmailAsync(String email, String msg) {
		try {
			notificationClient.sendEmail(email, msg);
		} catch (Exception e) {
			log.warn("Email failed for {}: {}", email, e.getMessage());
		}
	}

	@Override
	public ResponseEntity<?> getUserDataById(Integer id) {
		return ResponseEntity.ok(getActiveUser(id));
	}

	@Override
	public ResponseEntity<?> getUserDataByEmail(String email) {
		User user = userRepository.findByEmailAndDeletedFalse(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
		return ResponseEntity.ok(user);
	}

	@Override
	public ResponseEntity<?> getUserDataByUsername(String username) {
		User user = userRepository.findByUsernameAndDeletedFalse(username)
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
		return ResponseEntity.ok(user);
	}

	@Override
	@Transactional
	public ResponseEntity<?> updateUser(Integer id, User u) {
		User existing = getActiveUser(id);
		existing.setFname(u.getFname());
		existing.setLname(u.getLname());
		existing.setMobileNo(u.getMobileNo());
		existing.setAddress(u.getAddress());
		existing.setCountry(u.getCountry());
		existing.setState(u.getState());
		existing.setCity(u.getCity());
		existing.setZipcode(u.getZipcode());
		existing.setDob(u.getDob());
		existing.setUpdatedBy(u.getUsername());
		userRepository.save(existing);
		return ResponseEntity.ok("User updated successfully.");
	}

	@Override
	@Transactional
	public ResponseEntity<?> softDeleteUser(Integer id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
		if (Boolean.TRUE.equals(user.getDeleted()))
			return ResponseEntity.ok("User is already deleted.");
		user.setDeleted(true);
		user.setStatus(false);
		userRepository.save(user);
		return ResponseEntity.ok("User deleted successfully.");
	}

	@Override
	@Transactional
	public ResponseEntity<?> forgotPassword(ForgotPasswordRequest request) {
		User user = userRepository.findByEmailAndDeletedFalse(request.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("No account: " + request.getEmail()));
		String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
		Login login = user.getLogin();
		login.setOtp(otp);
		loginRepository.save(login);
		try {
			notificationClient.sendEmail(user.getEmail(), "Your LoanCore OTP: " + otp + ". Valid 10 min.");
		} catch (Exception e) {
			log.warn("OTP email failed: {}", e.getMessage());
		}
		return ResponseEntity.ok("OTP sent to registered email.");
	}

	@Override
	@Transactional
	public ResponseEntity<?> verifyOtpAndResetPassword(String email, String otp, String newPassword) {
		User user = userRepository.findByEmailAndDeletedFalse(email)
				.orElseThrow(() -> new ResourceNotFoundException("No account: " + email));
		Login login = user.getLogin();
		if (login.getOtp() == null || !login.getOtp().equals(otp))
			throw new IllegalArgumentException("Invalid or expired OTP.");
		String encoded = passwordEncoder.encode(newPassword);
		login.setPassword(encoded);
		login.setOtp(null);
		user.setPassword(encoded);
		userRepository.save(user);
		return ResponseEntity.ok("Password reset successfully.");
	}

	@Override
	@Transactional
	public ResponseEntity<?> changePassword(ChangePasswordRequest request) {
		User user = userRepository.findByUsernameAndDeletedFalse(request.getUsername())
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUsername()));
		Login login = user.getLogin();
		if (!passwordEncoder.matches(request.getOldPassword(), login.getPassword()))
			throw new IllegalArgumentException("Old password is incorrect.");
		String encoded = passwordEncoder.encode(request.getNewPassword());
		login.setPassword(encoded);
		user.setPassword(encoded);
		userRepository.save(user);
		return ResponseEntity.ok("Password changed successfully.");
	}

	@Override
	public List<User> getUsersByPage(int pageNumber, int numberOfRecords) {
		return userRepository.findByDeletedFalse(PageRequest.of(pageNumber, numberOfRecords)).getContent();
	}

	private User getActiveUser(Integer id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
		if (Boolean.TRUE.equals(user.getDeleted()))
			throw new ResourceNotFoundException("User account is deactivated.");
		return user;
	}
}