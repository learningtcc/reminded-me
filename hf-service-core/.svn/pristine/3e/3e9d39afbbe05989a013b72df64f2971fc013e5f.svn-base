package com.homefellas.util;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

public class TimeHelperTest
{

	private final long nonUtcNowLong = System.currentTimeMillis();
	private final DateTime nonUtcNow = new DateTime(nonUtcNowLong);
	private final DateTime utcNow = nonUtcNow.withZone(DateTimeZone.UTC);
	private final long utcNowLong = utcNow.getMillis();
	private final DateTime eastern = utcNow.withZone(DateTimeZone.forID("US/Eastern"));
	private final DateTime pacific = utcNow.withZone(DateTimeZone.forID("US/Pacific"));
	private final DateTime easternUTC = eastern.withZone(DateTimeZone.UTC);
	private final DateTime pacificUTC = pacific.withZone(DateTimeZone.UTC);


	@Test
	public void testConvertLongUTCToDateTimeByZone()
	{		
		DateTime dateTimeUnderTest = TimeHelper.convertLongUTCToDateTimeByZone(easternUTC.getMillis(), eastern.getZone());
		Assert.assertEquals(eastern, dateTimeUnderTest);
		
		dateTimeUnderTest = TimeHelper.convertLongUTCToDateTimeByZone(pacificUTC.getMillis(), pacific.getZone());
		Assert.assertEquals(pacific, dateTimeUnderTest);
		
		Assert.assertEquals(nonUtcNow, TimeHelper.convertLongUTCToDateTimeByZone(utcNowLong, nonUtcNow.getZone()));
	}
	
	@Test
	public void testConvertDateTimeToLongUTC()
	{
		long longUnderTest = TimeHelper.convertDateTimeToLongUTC(eastern);
		Assert.assertEquals(easternUTC.getMillis(), longUnderTest);
		
		longUnderTest = TimeHelper.convertDateTimeToLongUTC(pacific);
		Assert.assertEquals(pacificUTC.getMillis(), longUnderTest);
	}

}
