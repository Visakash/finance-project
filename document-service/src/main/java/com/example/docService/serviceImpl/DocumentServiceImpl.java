//package com.example.docService.serviceImpl;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.UUID;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.example.docService.entity.Document;
//import com.example.docService.repository.DocumentRepository;
//import com.example.docService.service.DocumentService;
//
//@Service
//public class DocumentServiceImpl implements DocumentService {
//
//	@Autowired
//	private DocumentRepository repository;
//	
//	@Autowired
//	private AmazonS3 amazonS3;
//
//	@Value("${aws.bucket-name}")
//	private String bucketName;
//
//	public DocumentServiceImpl(DocumentRepository repository, AmazonS3 amazonS3) {
//		this.repository = repository;
//		this.amazonS3 = amazonS3;
//	}
//
//	@Override
//	public Document upload(MultipartFile file, Long loanId, Long customerId, String documentType) throws Exception {
//
//		String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
//
//		ObjectMetadata metadata = new ObjectMetadata();
//		metadata.setContentLength(file.getSize());
//
//		amazonS3.putObject(bucketName, key, file.getInputStream(), metadata);
//
//		String fileUrl = amazonS3.getUrl(bucketName, key).toString();
//
//		Document document = new Document();
//		document.setLoanId(loanId);
//		document.setCustomerId(customerId);
//		document.setDocumentType(documentType);
//		document.setDocumentName(file.getOriginalFilename());
//		document.setS3Key(key);
//		document.setS3Url(fileUrl);
//		document.setStatus("UPLOADED");
//		document.setUploadDate(LocalDate.now());
//
//		return repository.save(document);
//	}
//	
//	@Override
//	public List<Document> getDocumentsByLoanId(Long loanId) {
//	    return repository.findByLoanIdAndDeletedFalse(loanId);
//	}
//}