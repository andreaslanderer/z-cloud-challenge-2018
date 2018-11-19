package com.zuehlke.cloudchallenge.messagegransformer.sqs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.support.GenericMessage;

import java.io.IOException;
import java.io.UncheckedIOException;

public class FlightMessageConverter
	implements MessageConverter
{
	private static final ObjectMapper mapper = new ObjectMapper();

	@Override
	public Object fromMessage(Message<?> message, Class<?> targetClass)
	{
		try {
			return mapper.readValue(message.getPayload().toString(), targetClass);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public Message<?> toMessage(Object payload, MessageHeaders headers)
	{
		try {
			String json = mapper.writeValueAsString(payload);

			return new GenericMessage<>(json, headers);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
