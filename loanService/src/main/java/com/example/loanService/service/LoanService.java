package com.example.loanService.service;

import java.util.List;
import com.example.loanService.dto.*;
import com.example.loanService.entity.*;

public interface LoanService {

	LoanResponseDTO applyLoan(LoanRequestDTO dto);

	Loan getLoanById(Long loanId);

	List<Loan> getLoansByCustomer(Long customerId);

	Double calculateEmi(EmiRequestDto request);

	Loan approveLoan(Long loanId);

	Loan rejectLoan(Long loanId, String reason);

	Repayment makeRepayment(Long loanId, Double amount);

	List<Repayment> getRepaymentHistory(Long loanId);

	String getLoanStatus(Long loanId);

	String softDeleteLoan(Long id);
}