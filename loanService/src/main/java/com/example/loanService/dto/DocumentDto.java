package com.example.loanService.dto;

import lombok.Data;

@Data
public class DocumentDto {

    private Long documentId;
    private String documentType;
    private String status;
    private String s3Url;
}