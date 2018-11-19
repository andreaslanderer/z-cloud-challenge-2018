package com.zuehlke.cloudchallenge.messagegransformer.sqs;

import com.zuehlke.cloudchallenge.messagegransformer.message.FlightMessage;
import org.springframework.context.ApplicationEvent;

public class FlightMessageProcessEvent
	extends ApplicationEvent
{
	private final FlightMessage processedMessage;

	FlightMessageProcessEvent(Object source, FlightMessage processedMessage)
	{
		super(source);

		this.processedMessage = processedMessage;
	}

	public FlightMessage getProcessedMessage()
	{
		return processedMessage;
	}
}
