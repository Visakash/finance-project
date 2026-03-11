//package com.example.docService.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.example.docService.entity.Document;
//
//public interface DocumentRepository extends JpaRepository<Document, Long> {
//
//	List<Document> findByLoanIdAndDeletedFalse(Long loanId);
//
//}
