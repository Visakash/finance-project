package com.example.creditCheck.model;

import javax.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "credit_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "customer_id", nullable = false)
	private Long customerId;

	@Column(name = "credit_score")
	private Integer creditScore;

	@Column(name = "annual_income")
	private BigDecimal annualIncome;

	@Column(name = "existing_loan_count")
	private Integer existingLoanCount;

	@Column(name = "existing_emi_amount")
	private BigDecimal existingEmiAmount;

	@Column(name = "debt_to_income_ratio")
	private BigDecimal debtToIncomeRatio;

	@Column(name = "max_eligible_amount")
	private BigDecimal maxEligibleAmount;

	@Enumerated(EnumType.STRING)
	@Column(name = "eligibility_status")
	private EligibilityStatus eligibilityStatus;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "checked_at", updatable = false)
	private LocalDateTime checkedAt;

	@PrePersist
	protected void onCreate() {
		checkedAt = LocalDateTime.now();
	}
}