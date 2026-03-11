package com.example.loanService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.loanService.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {

	Optional<Loan> findByLoanIdAndDeletedFalse(Long loanId);

	List<Loan> findByCustomerIdAndDeletedFalse(Long customerId);
}