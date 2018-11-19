package com.zuehlke.cloudchallenge.messagegransformer.stats;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDbConfig
{
	private final String awsAccessKey;
	private final String awsSecretKey;
	private final String awsRegion;

	public DynamoDbConfig(
		@Value("${cloud.aws.credentials.accessKey}") String awsAccessKey,
		@Value("${cloud.aws.credentials.secretKey}") String awsSecretKey,
		@Value("${cloud.aws.region}") String awsRegion)
	{
		this.awsAccessKey = awsAccessKey;
		this.awsSecretKey = awsSecretKey;
		this.awsRegion = awsRegion;
	}

	@Bean
	public DynamoDB dynamoDB()
	{
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
			.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
			.withRegion(Regions.fromName(awsRegion))
			.build();

		return new DynamoDB(client);
	}
}
