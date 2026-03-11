package com.example.loanService.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreditCheckResponse {

	private Long reportId;
	private Long customerId;
	private Integer creditScore;
	private BigDecimal annualIncome;
	private Integer existingLoanCount;
	private BigDecimal existingEmiAmount;
	private BigDecimal debtToIncomeRatio;
	private BigDecimal maxEligibleAmount;
	private BigDecimal requestedAmount;
	private String eligibilityStatus; 
	private boolean eligible;
	private String remarks;
}