package com.example.loanService.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.loanService.entity.Repayment;

public interface RepaymentRepository extends JpaRepository<Repayment, Long> {

	List<Repayment> findByLoanIdOrderByPaymentDateDesc(Long loanId);
}