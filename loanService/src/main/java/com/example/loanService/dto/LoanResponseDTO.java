package com.example.loanService.dto;

import lombok.*;

@Data
@Builder
public class LoanResponseDTO {

	private Long loanId;
	private Long customerId;
	private Double loanAmount;
	private Double emiAmount;
	private Double outstandingAmount;
	private String status;

	private Long creditReportId;
	private String eligibilityStatus;
	private String creditRemarks;
}