package com.example.loanService.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "repayments")
@Data
public class Repayment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long repaymentId;

	private Long loanId;
	private Double amountPaid;
	private LocalDate paymentDate;
	private Double remainingBalance;

	@Enumerated(EnumType.STRING)
	private RepaymentStatus status;

	private String remarks;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;
}