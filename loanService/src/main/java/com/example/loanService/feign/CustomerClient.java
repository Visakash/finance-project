package com.example.loanService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import com.example.loanService.dto.CustomerDto;

@FeignClient(name = "customer-service", fallback = CustomerClientFallback.class)
public interface CustomerClient {
	
	@GetMapping("/api/customer/id/{id}")
	CustomerDto getCustomerById(@PathVariable Integer id);
}