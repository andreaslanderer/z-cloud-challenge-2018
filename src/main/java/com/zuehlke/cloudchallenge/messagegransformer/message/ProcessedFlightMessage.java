package com.zuehlke.cloudchallenge.messagegransformer.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProcessedFlightMessage
{
	@JsonProperty("flight-number")
	public String flightNumber;
	public String airport;
	public String message;
	public String timestamp;

	@JsonProperty("message-word-count")
	public String getWordCount()
	{
		if (message != null) {
			return Integer.toString(message.split(" ").length);
		} else {
			return "0";
		}
	}
}
