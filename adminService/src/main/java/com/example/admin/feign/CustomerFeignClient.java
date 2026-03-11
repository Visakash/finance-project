package com.example.admin.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import com.example.admin.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;

@FeignClient(name = "customer-service", fallback = CustomerFeignClientFallback.class)
public interface CustomerFeignClient {

	@GetMapping("/api/customer/username/{username}")
	UserDTO getUserByUsername(@PathVariable String username);
}

@Component
@Slf4j
class CustomerFeignClientFallback implements CustomerFeignClient {

	@Override
	public UserDTO getUserByUsername(String username) {
		log.warn("customer-service down. Fallback username={}", username);
		return null;
	}
}