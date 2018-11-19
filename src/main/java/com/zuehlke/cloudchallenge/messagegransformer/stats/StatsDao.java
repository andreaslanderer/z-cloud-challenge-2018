package com.zuehlke.cloudchallenge.messagegransformer.stats;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.internal.IteratorSupport;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class StatsDao
{
	private final Table statsTable;

	@Autowired
	public StatsDao(DynamoDB database)
	{
		statsTable = database.getTable("QueryStats");
	}

	public long getMessageCountForAirport(String airportName)
	{
		QuerySpec spec = new QuerySpec()
			.withKeyConditionExpression("airport = :v_id")
			.withValueMap(new ValueMap().withString(":v_id", airportName));
		ItemCollection<QueryOutcome> items = statsTable.query(spec);
		IteratorSupport<Item, QueryOutcome> itemIterator = items.iterator();
		long count = 0;

		if (itemIterator.hasNext()) {
			Item item = itemIterator.next();

			if (item.hasAttribute("count")) {
				count = item.getLong("count");
			}
		}

		return count;
	}

	public void storeMessageCountForAirport(String airport, long queries)
	{
		statsTable.putItem(new Item().withPrimaryKey("airport", airport).withNumber("count", queries));
	}
}
