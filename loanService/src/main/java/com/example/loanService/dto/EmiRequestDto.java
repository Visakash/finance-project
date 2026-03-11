package com.example.loanService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmiRequestDto {

	private Double loanAmount;
	private Double interestRate;
	private Integer tenureMonths;
}