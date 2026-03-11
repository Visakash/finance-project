package com.example.loanService.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.loanService.dto.*;
import com.example.loanService.entity.*;
import com.example.loanService.service.LoanService;

@RestController
@RequestMapping("/api/loan")
public class LoanController {

	@Autowired
	private LoanService loanService;

	@PostMapping("/apply")
	public ResponseEntity<?> apply(@Valid @RequestBody LoanRequestDTO dto) {
		return ResponseEntity.ok(loanService.applyLoan(dto));
	}

	@GetMapping("/{loanId}")
	public ResponseEntity<Loan> get(@PathVariable Long loanId) {
		return ResponseEntity.ok(loanService.getLoanById(loanId));
	}

	@GetMapping("/customer/{customerId}")
	public ResponseEntity<List<Loan>> getByCustomer(@PathVariable Long customerId) {
		return ResponseEntity.ok(loanService.getLoansByCustomer(customerId));
	}

	@PostMapping("/calculate-emi")
	public ResponseEntity<Double> emi(@RequestBody EmiRequestDto dto) {
		return ResponseEntity.ok(loanService.calculateEmi(dto));
	}

	@PutMapping("/approve/{loanId}")
	public ResponseEntity<Loan> approve(@PathVariable Long loanId) {
		return ResponseEntity.ok(loanService.approveLoan(loanId));
	}

	@PutMapping("/reject/{loanId}")
	public ResponseEntity<Loan> reject(@PathVariable Long loanId, @RequestParam String reason) {
		return ResponseEntity.ok(loanService.rejectLoan(loanId, reason));
	}

	@PostMapping("/{loanId}/repay")
	public ResponseEntity<Repayment> repay(@PathVariable Long loanId, @RequestParam Double amount) {
		return ResponseEntity.ok(loanService.makeRepayment(loanId, amount));
	}

	@GetMapping("/{loanId}/repayments")
	public ResponseEntity<List<Repayment>> repayments(@PathVariable Long loanId) {
		return ResponseEntity.ok(loanService.getRepaymentHistory(loanId));
	}

	@GetMapping("/{loanId}/status")
	public ResponseEntity<String> status(@PathVariable Long loanId) {
		return ResponseEntity.ok(loanService.getLoanStatus(loanId));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		return ResponseEntity.ok(loanService.softDeleteLoan(id));
	}
}