package com.zuehlke.cloudchallenge.messagegransformer.stats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController
{
	private final StatsDao statisticsStore;

	@Autowired
	public StatsController(StatsDao statisticsStore)
	{
		this.statisticsStore = statisticsStore;
	}

	@GetMapping(path = "/counters/{airport}", produces = "application/json")
	public StatsResponse counts(@PathVariable("airport") String airport)
	{
		long messageCount = statisticsStore.getMessageCountForAirport(airport);

		return new StatsResponse(airport, messageCount);
	}
}
