package com.example.loanService.feign;

import com.example.loanService.dto.CreditCheckResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@FeignClient(name = "credit-check-service", fallback = CreditCheckClientFallback.class)
public interface CreditCheckClient {

	@GetMapping("/credit/v1/check/{customerId}")
	CreditCheckResponse checkCredit(@PathVariable Long customerId, @RequestParam BigDecimal requestedAmount);
}

@Component
@Slf4j
class CreditCheckClientFallback implements CreditCheckClient {

	@Override
	public CreditCheckResponse checkCredit(Long customerId, BigDecimal requestedAmount) {
		log.warn("credit-check-service unavailable. Fallback: allowing loan for customerId={}", customerId);
		return null;
	}
}