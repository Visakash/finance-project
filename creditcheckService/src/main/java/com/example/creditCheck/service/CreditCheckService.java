package com.example.creditCheck.service;

import com.example.creditCheck.dto.CreditCheckDto;
import com.example.creditCheck.model.*;
import com.example.creditCheck.repository.CreditReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.*;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditCheckService {

    private final CreditReportRepository creditReportRepository;


    @Transactional
    public CreditCheckDto.Response checkEligibility(Long customerId, BigDecimal requestedAmount) {

        log.info("Running credit check: customerId={}, requestedAmount={}", customerId, requestedAmount);

        int creditScore         = 600 + new Random(customerId).nextInt(250);
        BigDecimal annualIncome = BigDecimal.valueOf(400000 + new Random(customerId + 1).nextInt(1600000));
        int existingLoans       = new Random(customerId + 2).nextInt(4);
        BigDecimal existingEmi  = BigDecimal.valueOf(new Random(customerId + 3).nextInt(20000));

        
        BigDecimal monthlyIncome = annualIncome.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
        BigDecimal dtiRatio = monthlyIncome.compareTo(BigDecimal.ZERO) > 0
                ? existingEmi.divide(monthlyIncome, 4, RoundingMode.HALF_UP)
                             .multiply(BigDecimal.valueOf(100))
                : BigDecimal.valueOf(100);

        BigDecimal availableForEmi = monthlyIncome
                .multiply(BigDecimal.valueOf(0.40))
                .subtract(existingEmi);
        BigDecimal maxEligible = availableForEmi
                .multiply(BigDecimal.valueOf(60))
                .max(BigDecimal.ZERO);

     
        EligibilityStatus status;
        String remarks;

        if (creditScore >= 750 && dtiRatio.compareTo(BigDecimal.valueOf(40)) <= 0) {
            status  = EligibilityStatus.ELIGIBLE;
            remarks = "Good credit profile. Eligible for full loan amount.";
        } else if (creditScore >= 650 && dtiRatio.compareTo(BigDecimal.valueOf(50)) <= 0) {
            status  = EligibilityStatus.CONDITIONALLY_ELIGIBLE;
            remarks = "Moderate credit profile. Eligible with conditions.";
        } else {
            status  = EligibilityStatus.NOT_ELIGIBLE;
            remarks = "Poor credit profile or high debt-to-income ratio.";
        }

   
        if (status != EligibilityStatus.NOT_ELIGIBLE
                && requestedAmount.compareTo(BigDecimal.ZERO) > 0
                && requestedAmount.compareTo(maxEligible) > 0) {
            status  = EligibilityStatus.CONDITIONALLY_ELIGIBLE;
            remarks = "Requested amount ₹" + requestedAmount
                    + " exceeds max eligible ₹" + maxEligible.setScale(2, RoundingMode.HALF_UP)
                    + ". Loan may be partially approved.";
        }

       
        CreditReport report = CreditReport.builder()
                .customerId(customerId)
                .creditScore(creditScore)
                .annualIncome(annualIncome)
                .existingLoanCount(existingLoans)
                .existingEmiAmount(existingEmi)
                .debtToIncomeRatio(dtiRatio)
                .maxEligibleAmount(maxEligible)
                .eligibilityStatus(status)
                .remarks(remarks)
                .build();

        report = creditReportRepository.save(report);
        log.info("Credit check done: customerId={}, score={}, status={}", customerId, creditScore, status);

        return CreditCheckDto.Response.builder()
                .reportId(report.getId())
                .customerId(customerId)
                .creditScore(creditScore)
                .annualIncome(annualIncome)
                .existingLoanCount(existingLoans)
                .existingEmiAmount(existingEmi)
                .debtToIncomeRatio(dtiRatio)
                .maxEligibleAmount(maxEligible)
                .requestedAmount(requestedAmount)
                .eligibilityStatus(status)
                .eligible(status != EligibilityStatus.NOT_ELIGIBLE)
                .remarks(remarks)
                .build();
    }

    
    @Transactional(readOnly = true)
    public List<CreditReport> getHistoryByCustomer(Long customerId) {
        log.info("Fetching credit history: customerId={}", customerId);
        return creditReportRepository.findByCustomerIdOrderByCheckedAtDesc(customerId);
    }

    @Transactional(readOnly = true)
    public CreditCheckDto.Response getLatestReport(Long customerId) {
        CreditReport report = creditReportRepository
                .findTopByCustomerIdOrderByCheckedAtDesc(customerId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No credit report found for customerId: " + customerId));

        return CreditCheckDto.Response.builder()
                .reportId(report.getId())
                .customerId(report.getCustomerId())
                .creditScore(report.getCreditScore())
                .annualIncome(report.getAnnualIncome())
                .existingLoanCount(report.getExistingLoanCount())
                .existingEmiAmount(report.getExistingEmiAmount())
                .debtToIncomeRatio(report.getDebtToIncomeRatio())
                .maxEligibleAmount(report.getMaxEligibleAmount())
                .eligibilityStatus(report.getEligibilityStatus())
                .eligible(report.getEligibilityStatus() != EligibilityStatus.NOT_ELIGIBLE)
                .remarks(report.getRemarks())
                .build();
    }
}