package com.example.customer.feign;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NotificationClientFallback implements NotificationClient {

	@Override
	public String sendEmail(String to, String msg) {
		log.warn("notification-service unavailable. Email NOT sent to {}", to);
		return "NOTIFICATION_SERVICE_UNAVAILABLE";
	}
}