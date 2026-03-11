package com.example.notification.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.notification.service.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class NotificationController {

	private final EmailService emailService;

	@PostMapping("/email")
	public String sendEmail(
			@RequestParam String to, 
			@RequestParam String msg) {

		return emailService.sendEmail(to, msg);
	}
}
