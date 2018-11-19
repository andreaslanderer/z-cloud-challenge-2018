package com.zuehlke.cloudchallenge.messagegransformer.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class FlightMessageTransformer
{
	private final JmsTemplate jmsTemplate;
	private final String outputQueueName;

	@Autowired
	public FlightMessageTransformer(JmsTemplate jmsTemplate,
		@Value("${queue.outgoingFlightMessages}") String outputQueueName)
	{
		this.jmsTemplate = jmsTemplate;
		this.outputQueueName = outputQueueName;
	}

	@JmsListener(destination = "${queue.incomingFlightMessages}", containerFactory = "jmsListenerContainerFactory")
	public void receiveMessage(@Payload Message<FlightMessage> message)
	{
		FlightMessage incomingMessage = message.getPayload();
		ProcessedFlightMessage processedFlightMessage = new ProcessedFlightMessage();

		processedFlightMessage.airport = incomingMessage.airport;
		processedFlightMessage.flightNumber = incomingMessage.flightNumber;
		processedFlightMessage.message = incomingMessage.message;
		processedFlightMessage.timestamp = incomingMessage.timestamp;

		jmsTemplate.convertAndSend(outputQueueName, processedFlightMessage);
	}
}
