package com.zuehlke.cloudchallenge.messagegransformer.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FlightMessage
{
	@JsonProperty("flight-number")
	public String flightNumber;
	public String airport;
	public String message;
	public String timestamp;
}
