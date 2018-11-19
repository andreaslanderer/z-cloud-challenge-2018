package com.zuehlke.cloudchallenge.messagegransformer.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FlightMessage
{
	public String flightNumber;
	public String airport;
	public String message;
	public String timestamp;

	@JsonCreator
	public FlightMessage(
		@JsonProperty("flight-number") String flightNumber,
		@JsonProperty("airport") String airport,
		@JsonProperty("message") String message,
		@JsonProperty("timestamp") String timestamp)
	{
		this.flightNumber = flightNumber;
		this.airport = airport;
		this.message = message;
		this.timestamp = timestamp;
	}
}
