package com.zuehlke.cloudchallenge.messagegransformer.sqs;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.QueueMessageHandler;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Collections;

@Configuration
public class SqsConfig
{
	private final String awsAccessKey;
	private final String awsSecretKey;
	private final String awsRegion;

	public SqsConfig(
		@Value("${cloud.aws.credentials.accessKey}") String awsAccessKey,
		@Value("${cloud.aws.credentials.secretKey}") String awsSecretKey,
		@Value("${cloud.aws.region}") String awsRegion)
	{
		this.awsAccessKey = awsAccessKey;
		this.awsSecretKey = awsSecretKey;
		this.awsRegion = awsRegion;
	}

	@Bean
	public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSqsAsync)
	{
		return new QueueMessagingTemplate(amazonSqsAsync);
	}

	@Bean(name = "amazonSqsAsync", destroyMethod = "shutdown")
	public AmazonSQSAsync amazonSqsAsync()
	{
		return AmazonSQSAsyncClientBuilder.standard()
			.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
			.withRegion(Regions.fromName(awsRegion))
			.build();
	}

	@Bean
	public SimpleMessageListenerContainer simpleMessageListenerContainer(QueueMessageHandler queueMessageHandler,
		SimpleMessageListenerContainerFactory listenerContainerFactory)
	{
		SimpleMessageListenerContainer container = listenerContainerFactory.createSimpleMessageListenerContainer();

		container.setMessageHandler(queueMessageHandler);

		return container;
	}

	@Bean
	public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync sqsAsync)
	{
		SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(16);
		executor.setQueueCapacity(1000);
		executor.afterPropertiesSet();

		factory.setAmazonSqs(sqsAsync);
		factory.setTaskExecutor(executor);

		return factory;
	}

	@Bean
	public QueueMessageHandler queueMessageHandler(AmazonSQSAsync sqsAsync)
	{
		QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();

		factory.setAmazonSqs(sqsAsync);
		factory.setMessageConverters(Collections.singletonList(new FlightMessageConverter()));

		return factory.createQueueMessageHandler();
	}
}
