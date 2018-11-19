package com.zuehlke.cloudchallenge.messagegransformer.stats;

import com.zuehlke.cloudchallenge.messagegransformer.message.FlightMessageProcessEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MessageProcessEventListener
	implements ApplicationListener<FlightMessageProcessEvent>
{
	private final StatsDao statisticsStore;

	@Autowired
	public MessageProcessEventListener(StatsDao statisticsStore)
	{
		this.statisticsStore = statisticsStore;
	}

	@Override
	public void onApplicationEvent(FlightMessageProcessEvent event)
	{
		long airportQueries = statisticsStore.getMessageCountForAirport(event.getProcessedMessage().airport);

		airportQueries++;

		statisticsStore.storeMessageCountForAirport(event.getProcessedMessage().airport, airportQueries);
	}
}