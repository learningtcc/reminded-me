package com.homefellas.rm;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.homefellas.test.core.AbstractTest;


public class TimeTester extends AbstractTest
{
	
	@Test
	public void testTime()
	{
//		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//		StatusPrinter.print(lc);
		
		DateTime dateTime = new DateTime(DateTimeZone.forOffsetHours(2));
		
		String testString = "2013-01-19T17:54:56-0200";
		
		
		
		String dateFormat = ("yyyy-MM-dd'T'HH:mm:ssZ");
		
//		DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
		DateTimeFormatter fmt = DateTimeFormat.forPattern(dateFormat).withOffsetParsed();
		logger.info(fmt.parseDateTime(testString).toString());
		
		
//		DateTimeParser[] parsers = { DateTimeFormat.forPattern(dateFormat).getParser()};
//		DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(null, parsers).toFormatter();
//		DateTime formaterDateTime = formatter.parseDateTime(testString);
//		System.out.println(formaterDateTime.toString());
//		
//		String formattedDate = DateTimeFormat.forPattern(dateFormat).print(dateTime);
//		System.out.println(formattedDate);
//		
//		DateTimeFormatter fmt = DateTimeFormat.forPattern(dateFormat);
//		DateTime dt = fmt.parseDateTime(testString);
//		System.out.println(dt.getZone().toString());
//		System.out.println(dt.toString());
	}
}
