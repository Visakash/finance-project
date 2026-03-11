package com.example.creditCheck.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(CreditCheckException.class)
	public ResponseEntity<String> handleCreditCheck(CreditCheckException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGeneral(Exception ex) {
		log.error("Credit check service error", ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Credit check error: " + ex.getMessage());
	}
}