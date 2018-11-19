package com.zuehlke.cloudchallenge.messagegransformer.message;

import org.springframework.stereotype.Component;

@Component
public class MessageTransformer
{
	public ProcessedFlightMessage transformMessage(FlightMessage incomingMessage)
	{
		ProcessedFlightMessage processedFlightMessage = new ProcessedFlightMessage();

		processedFlightMessage.airport = incomingMessage.airport;
		processedFlightMessage.flightNumber = incomingMessage.flightNumber;
		processedFlightMessage.message = incomingMessage.message;
		processedFlightMessage.timestamp = incomingMessage.timestamp;

		return processedFlightMessage;
	}
}
