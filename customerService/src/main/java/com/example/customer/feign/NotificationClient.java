package com.example.customer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "notification-service", fallback = NotificationClientFallback.class)
public interface NotificationClient {

	@PostMapping("/notify/email")
	String sendEmail(@RequestParam String to, @RequestParam String msg);
}