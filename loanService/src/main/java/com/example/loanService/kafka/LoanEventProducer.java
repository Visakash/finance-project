package com.example.loanService.kafka;

import com.example.loanService.config.KafkaTopicConfig;
import com.example.loanService.event.LoanEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
public class LoanEventProducer {

    @Autowired
    private KafkaTemplate<String, LoanEvent> kafkaTemplate;

    public void publishLoanApplied(LoanEvent event) {
        publish(KafkaTopicConfig.TOPIC_LOAN_APPLIED, event);
    }

    public void publishLoanApproved(LoanEvent event) {
        publish(KafkaTopicConfig.TOPIC_LOAN_APPROVED, event);
    }

    public void publishLoanRejected(LoanEvent event) {
        publish(KafkaTopicConfig.TOPIC_LOAN_REJECTED, event);
    }

    private void publish(String topic, LoanEvent event) {

        kafkaTemplate.send(topic, String.valueOf(event.getLoanId()), event)
                .addCallback(new ListenableFutureCallback<SendResult<String, LoanEvent>>() {

                    @Override
                    public void onSuccess(SendResult<String, LoanEvent> result) {

                        log.info("Published [{}] loanId={} → topic={} partition={} offset={}",
                                event.getEventType(),
                                event.getLoanId(),
                                topic,
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }

                    @Override
                    public void onFailure(Throwable ex) {

                        log.error("Failed to publish [{}] loanId={} → {} : {}",
                                event.getEventType(),
                                event.getLoanId(),
                                topic,
                                ex.getMessage());
                    }
                });
    }
}