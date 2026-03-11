package com.example.notification.kafka;

import com.example.notification.event.LoanEvent;
import com.example.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoanEventConsumer {

    private final EmailService emailService;

    @KafkaListener(
            topics = "loan.applied",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onLoanApplied(
            @Payload LoanEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("Consumed [LOAN_APPLIED] loanId={} partition={} offset={}",
                event.getLoanId(), partition, offset);

        String subject = "LoanCore — Application Received";
        String body = "Dear " + event.getCustomerName() + ",\n\n"
                + "Your loan application has been received.\n\n"
                + "Loan ID     : #" + event.getLoanId() + "\n"
                + "Loan Type   : " + event.getLoanType() + "\n"
                + "Amount      : ₹" + event.getLoanAmount() + "\n"
                + "Monthly EMI : ₹" + String.format("%.2f", event.getEmiAmount()) + "\n"
                + "Status      : PENDING REVIEW\n\n"
                + "We will notify you once a decision is made.\n\n"
                + "Regards,\nLoanCore Team";

        emailService.sendEmail(event.getCustomerEmail(), subject, body);
    }

  

    @KafkaListener(
            topics = "loan.approved",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onLoanApproved(
            @Payload LoanEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("Consumed [LOAN_APPROVED] loanId={} partition={} offset={}",
                event.getLoanId(), partition, offset);

        String subject = "LoanCore — Loan Approved! 🎉";
        String body = "Dear " + event.getCustomerName() + ",\n\n"
                + "Congratulations! Your loan has been APPROVED.\n\n"
                + "Loan ID     : #" + event.getLoanId() + "\n"
                + "Loan Type   : " + event.getLoanType() + "\n"
                + "Amount      : ₹" + event.getLoanAmount() + "\n"
                + "Monthly EMI : ₹" + String.format("%.2f", event.getEmiAmount()) + "\n"
                + "Status      : APPROVED\n\n"
                + "The amount will be disbursed to your account shortly.\n\n"
                + "Regards,\nLoanCore Team";

        emailService.sendEmail(event.getCustomerEmail(), subject, body);
    }

    @KafkaListener(
            topics = "loan.rejected",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onLoanRejected(
            @Payload LoanEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("Consumed [LOAN_REJECTED] loanId={} partition={} offset={}",
                event.getLoanId(), partition, offset);

        String subject = "LoanCore — Loan Application Update";
        String body = "Dear " + event.getCustomerName() + ",\n\n"
                + "We regret to inform you that your loan application has been rejected.\n\n"
                + "Loan ID   : #" + event.getLoanId() + "\n"
                + "Loan Type : " + event.getLoanType() + "\n"
                + "Amount    : ₹" + event.getLoanAmount() + "\n"
                + "Reason    : " + event.getRejectionReason() + "\n\n"
                + "You may reapply after addressing the above reason.\n\n"
                + "Regards,\nLoanCore Team";

        emailService.sendEmail(event.getCustomerEmail(), subject, body);
    }
}