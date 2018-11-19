package com.zuehlke.cloudchallenge.messagegransformer.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class FlightMessageTransformer
{
	private final QueueMessagingTemplate messagingTemplate;
	private final String outputQueueName;

	@Autowired
	public FlightMessageTransformer(QueueMessagingTemplate messagingTemplate,
		@Value("${queue.outgoingFlightMessages}") String outputQueueName)
	{
		this.messagingTemplate = messagingTemplate;
		this.outputQueueName = outputQueueName;
	}

	@SqsListener("${queue.incomingFlightMessages}")
	public void receiveMessage(@Payload FlightMessage incomingMessage)
	{
		ProcessedFlightMessage processedFlightMessage = new ProcessedFlightMessage();

		processedFlightMessage.airport = incomingMessage.airport;
		processedFlightMessage.flightNumber = incomingMessage.flightNumber;
		processedFlightMessage.message = incomingMessage.message;
		processedFlightMessage.timestamp = incomingMessage.timestamp;

		messagingTemplate.convertAndSend(outputQueueName, processedFlightMessage);
	}
}
