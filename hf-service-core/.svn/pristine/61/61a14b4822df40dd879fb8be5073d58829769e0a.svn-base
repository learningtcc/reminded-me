package com.homefellas.ws.core;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * @JsonSerialize(using=JodaDateTimeJsonSerializer.class)
 * @author prc9041
 *
 */
public class SQLDateJsonSerializer extends JsonSerializer<Date>
{

	private static final String dateFormat = ("yyyy-MM-dd");

	@Override
	public void serialize(Date date, JsonGenerator gen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException
	{
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		String formattedDate = format.format(date);
		
		
//		String formattedDate = DateTimeFormat.forPattern(dateFormat)
//				.print(date);

		gen.writeString(formattedDate);
	}

}
