package com.zuehlke.cloudchallenge.messagegransformer.sqs;

import com.zuehlke.cloudchallenge.messagegransformer.message.FlightMessage;
import com.zuehlke.cloudchallenge.messagegransformer.message.MessageTransformer;
import com.zuehlke.cloudchallenge.messagegransformer.message.ProcessedFlightMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.swing.text.DateFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class FlightMessageProcessor
{
	private final QueueMessagingTemplate messagingTemplate;
	private final MessageTransformer messageTransformer;
	private final ApplicationEventPublisher eventPublisher;
	private final String outputQueueName;

	@Autowired
	public FlightMessageProcessor(QueueMessagingTemplate messagingTemplate,
		MessageTransformer messageTransformer,
		ApplicationEventPublisher eventPublisher,
		@Value("${queue.outgoingFlightMessages}") String outputQueueName)
	{
		this.messagingTemplate = messagingTemplate;
		this.messageTransformer = messageTransformer;
		this.eventPublisher = eventPublisher;
		this.outputQueueName = outputQueueName;
	}

	@SqsListener("${queue.incomingFlightMessages}")
	public void receiveMessage(@Payload FlightMessage incomingMessage)
	{
		System.out.println(new SimpleDateFormat().format(new Date()) + ": Receive incoming message.");
		ProcessedFlightMessage processedFlightMessage = messageTransformer.transformMessage(incomingMessage);
		FlightMessageProcessEvent event = new FlightMessageProcessEvent(this, incomingMessage);

		messagingTemplate.convertAndSend(outputQueueName, processedFlightMessage);
		eventPublisher.publishEvent(event);
		System.out.println(new SimpleDateFormat().format(new Date()) + ": Sent outgoing message.");
	}
}
