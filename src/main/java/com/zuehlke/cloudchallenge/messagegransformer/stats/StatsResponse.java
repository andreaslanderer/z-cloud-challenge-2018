package com.zuehlke.cloudchallenge.messagegransformer.stats;

public class StatsResponse
{
	public final String airport;
	public final long messageCount;

	public StatsResponse(String airport, long messageCount)
	{
		this.airport = airport;
		this.messageCount = messageCount;
	}
}
