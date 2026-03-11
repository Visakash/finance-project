package com.example.loanService.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

	public static final String TOPIC_LOAN_APPLIED = "loan.applied";
	public static final String TOPIC_LOAN_APPROVED = "loan.approved";
	public static final String TOPIC_LOAN_REJECTED = "loan.rejected";

	@Bean
	public NewTopic loanApplied() {
		return TopicBuilder.name(TOPIC_LOAN_APPLIED).partitions(3).replicas(1).build();
	}

	@Bean
	public NewTopic loanApproved() {
		return TopicBuilder.name(TOPIC_LOAN_APPROVED).partitions(3).replicas(1).build();
	}

	@Bean
	public NewTopic loanRejected() {
		return TopicBuilder.name(TOPIC_LOAN_REJECTED).partitions(3).replicas(1).build();
	}
}