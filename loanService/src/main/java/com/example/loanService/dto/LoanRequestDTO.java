package com.example.loanService.dto;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class LoanRequestDTO {

	@NotNull
	private Long customerId;

	@NotBlank
	private String loanType;

	@NotNull
	@Positive
	private Double loanAmount;

	@NotNull
	private Double interestRate;

	@NotNull
	private Integer tenureMonths;
}