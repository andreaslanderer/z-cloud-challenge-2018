package com.zuehlke.cloudchallenge.messagegransformer.jms;

import com.amazon.sqs.javamessaging.SQSMessagingClientConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuehlke.cloudchallenge.messagegransformer.message.FlightMessage;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.IOException;

public class FlightMessageConverter
	implements MessageConverter
{
	private static final ObjectMapper mapper = new ObjectMapper();

	@Override
	public Message toMessage(Object object, Session session)
		throws MessageConversionException
	{
		try {
			Message message = session.createTextMessage(mapper.writeValueAsString(object));
			String dedupId = Long.toString(System.currentTimeMillis());

			message.setStringProperty(SQSMessagingClientConstants.JMSX_GROUP_ID, "processedFlightMessages");
			message.setStringProperty(SQSMessagingClientConstants.JMS_SQS_DEDUPLICATION_ID, dedupId);

			return message;
		} catch (IOException | JMSException e) {
			throw new MessageConversionException(e.getMessage());
		}
	}

	@Override
	public Object fromMessage(Message message)
		throws JMSException, MessageConversionException
	{
		try {
			TextMessage textMessage = (TextMessage) message;

			return mapper.readValue(textMessage.getText(), FlightMessage.class);
		} catch (IOException e) {
			throw new MessageConversionException(e.getMessage(), e);
		}
	}
}
