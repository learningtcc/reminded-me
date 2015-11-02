package com.homefellas.ws.core;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * @JsonSerialize(using=JodaDateTimeJsonSerializer.class)
 * @author prc9041
 *
 */
public class JodaDateTimeJsonSerializer extends JsonSerializer<DateTime>
{

	private static final String dateFormat = ("yyyy-MM-dd'T'HH:mm:ssZZ");

	@Override
	public void serialize(DateTime date, JsonGenerator gen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException
	{
		String formattedDate = DateTimeFormat.forPattern(dateFormat)
				.print(date);

		gen.writeString(formattedDate);
	}

}
