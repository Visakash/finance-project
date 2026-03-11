package com.example.creditCheck.controller;

import com.example.creditCheck.dto.CreditCheckDto;
import com.example.creditCheck.model.CreditReport;
import com.example.creditCheck.service.CreditCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/credit/v1")
@RequiredArgsConstructor
public class CreditCheckController {

	private final CreditCheckService creditCheckService;

	@GetMapping("/check/{customerId}")
	public ResponseEntity<CreditCheckDto.Response> check(@PathVariable Long customerId,
			@RequestParam(defaultValue = "0") BigDecimal requestedAmount) {

		return ResponseEntity.ok(creditCheckService.checkEligibility(customerId, requestedAmount));
	}

	@GetMapping("/latest/{customerId}")
	public ResponseEntity<CreditCheckDto.Response> latest(@PathVariable Long customerId) {
		return ResponseEntity.ok(creditCheckService.getLatestReport(customerId));
	}

	@GetMapping("/history/{customerId}")
	public ResponseEntity<List<CreditReport>> history(@PathVariable Long customerId) {
		return ResponseEntity.ok(creditCheckService.getHistoryByCustomer(customerId));
	}

}