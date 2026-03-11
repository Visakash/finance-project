package com.example.customer.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

	@NotBlank
	private String username;

	@NotBlank
	private String oldPassword;

	@NotBlank
	@Size(min = 6)
	private String newPassword;
}