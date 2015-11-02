package com.homefellas.ws.core;

import java.io.IOException;
import java.text.ParseException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class JodaDateTimeJsonDeSerializer extends JsonDeserializer<DateTime>
{

	public static final String dateFormat = ("yyyy-MM-dd'T'HH:mm:ssZZ");
	
	@Override
	public DateTime deserialize(JsonParser jsonparser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException
	{
//		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		DateTimeFormatter fmt = DateTimeFormat.forPattern(dateFormat).withOffsetParsed();
		DateTime dt = fmt.parseDateTime(jsonparser.getText());
//        String date = jsonparser.getText();
		return dt;
	}

}
