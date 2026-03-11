package com.example.customer.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
	
	@NotBlank
	@Email
	private String email;
}