package com.example.loanService.event;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanEvent {

    
    private String eventType;

    private Long   loanId;
    private Long   customerId;
    private String customerEmail;
    private String customerName;
    private Double loanAmount;
    private Double emiAmount;
    private String loanType;
    private String rejectionReason;
}