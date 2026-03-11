package com.example.loanService.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.loanService.dto.*;
import com.example.loanService.entity.*;
import com.example.loanService.event.LoanEvent;
import com.example.loanService.exception.LoanNotFoundException;
import com.example.loanService.feign.*;
import com.example.loanService.kafka.LoanEventProducer;
import com.example.loanService.repository.*;
import com.example.loanService.service.LoanService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoanServiceImpl implements LoanService {

	@Autowired
	private LoanRepository loanRepository;
	
	@Autowired
	private RepaymentRepository repaymentRepository;

	@Autowired
	private CustomerClient customerClient;
	
	@Autowired
	private CreditCheckClient creditCheckClient; 
	
	@Autowired
	private LoanEventProducer loanEventProducer;

	@Override
	@Transactional
	public LoanResponseDTO applyLoan(LoanRequestDTO dto) {

		log.info("Loan application: customerId={}", dto.getCustomerId());

		CustomerDto customer = customerClient.getCustomerById(dto.getCustomerId().intValue());
		if (customer == null)
			throw new IllegalArgumentException("Customer not found: " + dto.getCustomerId());

		BigDecimal requestedAmount = BigDecimal.valueOf(dto.getLoanAmount());
		CreditCheckResponse creditResult = creditCheckClient.checkCredit(dto.getCustomerId(), requestedAmount);

		if (creditResult != null) {
			log.info("Credit check result: customerId={}, status={}, score={}", dto.getCustomerId(),
					creditResult.getEligibilityStatus(), creditResult.getCreditScore());

			if ("NOT_ELIGIBLE".equals(creditResult.getEligibilityStatus())) {
				throw new IllegalStateException("Loan application rejected due to credit check. Reason: "
						+ creditResult.getRemarks() + " | Credit Score: " + creditResult.getCreditScore());
			}
		} else {
			log.warn("Credit check unavailable for customerId={}. Proceeding without check.", dto.getCustomerId());
		}

		double emi = calcEmi(dto.getLoanAmount(), dto.getInterestRate(), dto.getTenureMonths());

		Loan loan = new Loan();
		loan.setCustomerId(dto.getCustomerId());
		loan.setLoanType(dto.getLoanType());
		loan.setLoanAmount(dto.getLoanAmount());
		loan.setInterestRate(dto.getInterestRate());
		loan.setTenureMonths(dto.getTenureMonths());
		loan.setEmiAmount(emi);
		loan.setOutstandingAmount(dto.getLoanAmount());
		loan.setAppliedDate(LocalDate.now());
		loan.setStatus(LoanStatus.PENDING);
		loan.setDeleted(false);

		if (creditResult != null) {
			loan.setCreditReportId(creditResult.getReportId());
			loan.setEligibilityStatus(creditResult.getEligibilityStatus());
		}

		Loan saved = loanRepository.save(loan);
		log.info("Loan created: loanId={}", saved.getLoanId());

		loanEventProducer.publishLoanApplied(LoanEvent.builder().eventType("LOAN_APPLIED").loanId(saved.getLoanId())
				.customerId(saved.getCustomerId()).customerEmail(customer.getEmail())
				.customerName(customer.getUsername()).loanAmount(saved.getLoanAmount()).emiAmount(saved.getEmiAmount())
				.loanType(saved.getLoanType()).build());

		return toResponse(saved, creditResult);
	}

	@Override
	public Loan getLoanById(Long loanId) {
		return loanRepository.findByLoanIdAndDeletedFalse(loanId)
				.orElseThrow(() -> new LoanNotFoundException("Loan not found: " + loanId));
	}

	@Override
	public List<Loan> getLoansByCustomer(Long customerId) {
		return loanRepository.findByCustomerIdAndDeletedFalse(customerId);
	}

	@Override
	public String getLoanStatus(Long loanId) {
		return getLoanById(loanId).getStatus().name();
	}

	@Override
	public Double calculateEmi(EmiRequestDto request) {
		return calcEmi(request.getLoanAmount(), request.getInterestRate(), request.getTenureMonths());
	}

	@Override
	@Transactional
	public Loan approveLoan(Long loanId) {

		log.info("Approving loanId={}", loanId);
		Loan loan = getLoanById(loanId);

		if (loan.getStatus() != LoanStatus.PENDING)
			throw new IllegalStateException("Only PENDING loans can be approved.");

		loan.setStatus(LoanStatus.APPROVED);
		loan.setApprovalDate(LocalDate.now());
		Loan saved = loanRepository.save(loan);

		CustomerDto customer = customerClient.getCustomerById(saved.getCustomerId().intValue());
		if (customer != null) {
			loanEventProducer.publishLoanApproved(LoanEvent.builder().eventType("LOAN_APPROVED").loanId(loanId)
					.customerId(saved.getCustomerId()).customerEmail(customer.getEmail())
					.customerName(customer.getUsername()).loanAmount(saved.getLoanAmount())
					.emiAmount(saved.getEmiAmount()).loanType(saved.getLoanType()).build());
		}
		return saved;
	}

	@Override
	@Transactional
	public Loan rejectLoan(Long loanId, String reason) {

		log.info("Rejecting loanId={}", loanId);
		Loan loan = getLoanById(loanId);

		if (loan.getStatus() != LoanStatus.PENDING)
			throw new IllegalStateException("Only PENDING loans can be rejected.");

		loan.setStatus(LoanStatus.REJECTED);
		loan.setRejectionReason(reason);
		Loan saved = loanRepository.save(loan);

		CustomerDto customer = customerClient.getCustomerById(saved.getCustomerId().intValue());
		if (customer != null) {
			loanEventProducer.publishLoanRejected(LoanEvent.builder().eventType("LOAN_REJECTED").loanId(loanId)
					.customerId(saved.getCustomerId()).customerEmail(customer.getEmail())
					.customerName(customer.getUsername()).loanAmount(saved.getLoanAmount())
					.loanType(saved.getLoanType()).rejectionReason(reason).build());
		}
		return saved;
	}

	@Override
	@Transactional
	public Repayment makeRepayment(Long loanId, Double amount) {

		Loan loan = getLoanById(loanId);
		if (loan.getStatus() != LoanStatus.APPROVED)
			throw new IllegalStateException("Repayments only on APPROVED loans.");
		if (amount <= 0)
			throw new IllegalArgumentException("Amount must be positive.");

		double outstanding = loan.getOutstandingAmount() != null ? loan.getOutstandingAmount() : loan.getLoanAmount();
		if (amount > outstanding)
			throw new IllegalArgumentException("Amount ₹" + amount + " exceeds outstanding ₹" + outstanding);

		double newBalance = outstanding - amount;
		loan.setOutstandingAmount(newBalance);
		if (newBalance == 0.0) {
			loan.setStatus(LoanStatus.CLOSED);
			loan.setClosureDate(LocalDate.now());
		}
		loanRepository.save(loan);

		Repayment r = new Repayment();
		r.setLoanId(loanId);
		r.setAmountPaid(amount);
		r.setPaymentDate(LocalDate.now());
		r.setRemainingBalance(newBalance);
		r.setStatus(RepaymentStatus.SUCCESS);
		r.setRemarks(newBalance == 0 ? "Loan fully repaid." : "EMI payment.");
		return repaymentRepository.save(r);
	}

	@Override
	public List<Repayment> getRepaymentHistory(Long loanId) {
		getLoanById(loanId);
		return repaymentRepository.findByLoanIdOrderByPaymentDateDesc(loanId);
	}

	@Override
	@Transactional
	public String softDeleteLoan(Long id) {
		Loan loan = getLoanById(id);
		if (loan.getStatus() == LoanStatus.APPROVED)
			throw new IllegalStateException("Approved loan cannot be deleted.");
		loan.setDeleted(true);
		loanRepository.save(loan);
		return "Loan deleted successfully.";
	}

	private double calcEmi(double P, double r, int N) {
		double R = r / (12 * 100);
		return (P * R * Math.pow(1 + R, N)) / (Math.pow(1 + R, N) - 1);
	}

	private LoanResponseDTO toResponse(Loan loan, CreditCheckResponse credit) {
		return LoanResponseDTO.builder().loanId(loan.getLoanId()).customerId(loan.getCustomerId())
				.loanAmount(loan.getLoanAmount()).emiAmount(loan.getEmiAmount())
				.outstandingAmount(loan.getOutstandingAmount()).status(loan.getStatus().name())
				.creditReportId(credit != null ? credit.getReportId() : null)
				.eligibilityStatus(credit != null ? credit.getEligibilityStatus() : "UNKNOWN")
				.creditRemarks(credit != null ? credit.getRemarks() : "Credit check unavailable").build();
	}
}