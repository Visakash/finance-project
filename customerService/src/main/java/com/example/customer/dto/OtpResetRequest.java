package com.example.customer.dto;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class OtpResetRequest {

	@NotBlank
	@Email
	private String email;

	@NotBlank(message = "OTP is required")
	private String otp;

	@NotBlank
	@Size(min = 6, message = "New password must be at least 6 characters")
	private String newPassword;
}