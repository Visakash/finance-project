package com.example.creditCheck.dto;

import lombok.*;
import java.math.BigDecimal;

import com.example.creditCheck.model.EligibilityStatus;

public class CreditCheckDto {

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response {

		private Long reportId;
		private Long customerId;
		private Integer creditScore;
		private BigDecimal annualIncome;
		private Integer existingLoanCount;
		private BigDecimal existingEmiAmount;
		private BigDecimal debtToIncomeRatio;
		private BigDecimal maxEligibleAmount;
		private BigDecimal requestedAmount;
		private EligibilityStatus eligibilityStatus;
		private boolean eligible;
		private String remarks;
	}
}