package com.example.loanService.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Data
public class Loan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long loanId;

	@NotBlank(message = "Loan type is required")
	private String loanType;

	@NotNull
	private Long customerId;

	@NotNull
	private Double loanAmount;

	@NotNull
	private Double interestRate;

	@NotNull
	private Integer tenureMonths;

	private Double emiAmount;
	private Double outstandingAmount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private LoanStatus status = LoanStatus.PENDING;

	private LocalDate appliedDate;
	private LocalDate approvalDate;
	private LocalDate closureDate;
	private String rejectionReason;

	private Long creditReportId;
	private String eligibilityStatus;

	@Column(nullable = false)
	private Boolean deleted = false;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;
}
