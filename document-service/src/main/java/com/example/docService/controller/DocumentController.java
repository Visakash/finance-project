//package com.example.docService.controller;
//
//import java.util.List;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.example.docService.entity.Document;
//import com.example.docService.service.DocumentService;
//
//@RestController
//@RequestMapping("/app/document")
//public class DocumentController {
//
//    private final DocumentService service;
//
//    public DocumentController(DocumentService service) {
//        this.service = service;
//    }
//
//    @PostMapping("/upload")
//    public Document upload(
//            @RequestParam MultipartFile file,
//            @RequestParam Long loanId,
//            @RequestParam Long customerId,
//            @RequestParam String documentType
//    ) throws Exception {
//
//        return service.upload(
//                file, loanId, customerId, documentType);
//    }
//    
//    @GetMapping("/loan/{loanId}")
//    public List<Document> getDocumentsByLoan(@PathVariable Long loanId) {
//        return service.getDocumentsByLoanId(loanId);
//    }
//}
