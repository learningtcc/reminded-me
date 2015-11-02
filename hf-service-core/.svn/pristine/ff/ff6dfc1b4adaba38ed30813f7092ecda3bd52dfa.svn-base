package com.homefellas.ws.core;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;


public class SQLDateJsonDeSerializer extends JsonDeserializer<Date>
{

	public static final String dateFormat = ("yyyy-MM-dd");
	
	@Override
	public Date deserialize(JsonParser jsonparser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException
	{
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		Date date;
		try
		{
			date = new Date(format.parse(jsonparser.getText()).getTime());
		}
		catch (ParseException e)
		{
			throw new IOException(e.getMessage());
		}
		
		return date;
	}

}
