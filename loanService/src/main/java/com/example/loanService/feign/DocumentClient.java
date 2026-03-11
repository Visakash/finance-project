package com.example.loanService.feign;

import java.util.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import com.example.loanService.dto.DocumentDto;
import lombok.extern.slf4j.Slf4j;

@FeignClient(name = "document-service", fallback = DocumentClientFallback.class)
public interface DocumentClient {
	@GetMapping("/app/document/loan/{loanId}")
	List<DocumentDto> getDocumentsByLoan(@PathVariable Long loanId);
}

@Component
@Slf4j
class DocumentClientFallback implements DocumentClient {
	@Override
	public List<DocumentDto> getDocumentsByLoan(Long loanId) {
		log.warn("document-service down. Empty docs for loanId={}", loanId);
		return Collections.emptyList();
	}
}