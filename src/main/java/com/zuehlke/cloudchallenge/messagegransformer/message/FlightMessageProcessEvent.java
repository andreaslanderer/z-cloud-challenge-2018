package com.zuehlke.cloudchallenge.messagegransformer.message;

import org.springframework.context.ApplicationEvent;

public class FlightMessageProcessEvent
	extends ApplicationEvent
{
	private final FlightMessage processedMessage;

	public FlightMessageProcessEvent(Object source, FlightMessage processedMessage)
	{
		super(source);

		this.processedMessage = processedMessage;
	}

	public FlightMessage getProcessedMessage()
	{
		return processedMessage;
	}
}
