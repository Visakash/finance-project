package com.example.loanService.feign;

import org.springframework.stereotype.Component;
import com.example.loanService.dto.CustomerDto;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomerClientFallback implements CustomerClient {
	
	@Override
	public CustomerDto getCustomerById(Integer id) {
		log.warn("customer-service down. Fallback id={}", id);
		return null;
	}
}