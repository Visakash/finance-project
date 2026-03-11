package com.example.customer.service;

import java.util.List;
import org.springframework.http.ResponseEntity;
import com.example.customer.dto.*;
import com.example.customer.entity.User;

public interface CustomerService {
	UserResponse registerUserInfo(User user);

	ResponseEntity<?> getUserDataById(Integer id);

	ResponseEntity<?> getUserDataByEmail(String email);

	ResponseEntity<?> getUserDataByUsername(String username);

	ResponseEntity<?> updateUser(Integer id, User user);

	ResponseEntity<?> softDeleteUser(Integer id);

	ResponseEntity<?> forgotPassword(ForgotPasswordRequest request);

	ResponseEntity<?> verifyOtpAndResetPassword(String email, String otp, String newPassword);

	ResponseEntity<?> changePassword(ChangePasswordRequest request);

	List<User> getUsersByPage(int pageNumber, int numberOfRecords);
}