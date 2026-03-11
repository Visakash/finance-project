package com.example.creditCheck.repository;

import com.example.creditCheck.model.CreditReport;
import com.example.creditCheck.model.EligibilityStatus;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditReportRepository extends JpaRepository<CreditReport, Long> {

	Optional<CreditReport> findTopByCustomerIdOrderByCheckedAtDesc(Long customerId);

	List<CreditReport> findByCustomerIdOrderByCheckedAtDesc(Long customerId);

	long countByCustomerId(Long customerId);

	List<CreditReport> findByEligibilityStatus(EligibilityStatus status);
}