package com.zuehlke.cloudchallenge.messagegransformer.jms;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.Session;

@Configuration
@EnableJms
public class JmsConfig
{
	private final SQSConnectionFactory connectionFactory;

	public JmsConfig(
		@Value("${cloud.aws.credentials.accessKey}") String awsAccessKey,
		@Value("${cloud.aws.credentials.secretKey}") String awsSecretKey,
		@Value("${cloud.aws.region}") String region)
	{
		connectionFactory = SQSConnectionFactory.builder()
			.withRegion(Region.getRegion(Regions.fromName(region)))
			.withAWSCredentialsProvider(new AWSStaticCredentialsProvider(
				new BasicAWSCredentials(awsAccessKey, awsSecretKey)
			))
			.build();
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory()
	{
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

		factory.setConnectionFactory(connectionFactory);
		factory.setDestinationResolver(new DynamicDestinationResolver());
		factory.setConcurrency("3-10");
		factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
		factory.setMessageConverter(messageConverter());

		return factory;
	}

	@Bean
	public JmsTemplate jmsTemplate()
	{
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);

		jmsTemplate.setMessageConverter(messageConverter());

		return jmsTemplate;
	}

	@Bean
	public MessageConverter messageConverter()
	{
		return new FlightMessageConverter();
	}
}
