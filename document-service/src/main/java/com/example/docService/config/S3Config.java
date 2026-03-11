//package com.example.docService.config;
//
//import com.amazonaws.auth.*;
//import com.amazonaws.services.s3.*;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.*;
//
//@Configuration
//public class S3Config {
//
//	@Value("${aws.access-key}")
//	private String accessKey;
//
//	@Value("${aws.secret-key}")
//	private String secretKey;
//
//	@Value("${aws.region}")
//	private String region;
//
//	@Bean
//	public AmazonS3 amazonS3() {
//
//		BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
//
//		return AmazonS3ClientBuilder.standard().withRegion(region)
//				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
//	}
//}