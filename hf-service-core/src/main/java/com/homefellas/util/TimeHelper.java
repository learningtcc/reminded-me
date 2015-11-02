package com.homefellas.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class TimeHelper {

//	public static Map<String, Integer> monthMap = new HashMap<String, Integer>();
//	
//	static
//	{
//		monthMap.put(arg0, arg1)
//	};
	
	private static Calendar getCalendar()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		return calendar;
	}
	
	private static Calendar getCalendar(long timeInMilli)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeInMilli);
		return calendar;
	}
	
	public static int getPreviousMonth()
	{
		int prevMonth = getCurrentMonth()-1;
		if (prevMonth<1)
		{
			prevMonth = 12;
		}
		return prevMonth;
	}
	
	public static int getTruePreviousYear()
	{
		return getCurrentYear()-1;	
	}
	
	public static int getTrueNextYear()
	{
		return getCurrentYear()+1;	
	}
	
	public static int getCurrentMonth()
	{
		return getCalendar().get(Calendar.MONTH)+1;
	}
	
	public static int getCurrentYear()
	{
		return getCalendar().get(Calendar.YEAR);
	}
	
	public static int getNextMonth()
	{
		int nextMonth = getCurrentMonth()+1;
		if (nextMonth>12)
		{
			nextMonth = 1;
		}
		return nextMonth;
	}
	
	public static int getNextYear()
	{
		if (getNextMonth()==1)
		{
			return getTrueNextYear();
		}
		else
		{
			return getCurrentYear();
		}
	}
	
	public static int getPreviousYear()
	{
		if (getPreviousMonth()==12)
		{
			return getTruePreviousYear();
		}
		else
		{
			return getCurrentYear();
		}
	
	}
	
	
	public static String getRunOnceFromNowCronExpression(int numberOfMinDealyToStart)
	{
		long timeInMillis = System.currentTimeMillis();
		int offset = numberOfMinDealyToStart*1000*60;
		timeInMillis += offset;
		Calendar calendar = getCalendar(offset);
		
		String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
		String min = String.valueOf(calendar.get(Calendar.MINUTE));
		String second = String.valueOf(calendar.get(Calendar.SECOND));
		
		StringBuffer cronFormat = new StringBuffer(second);
		cronFormat.append(" ");
		cronFormat.append(min);
		cronFormat.append(" ");
		cronFormat.append(hour);
		cronFormat.append(" ");
		cronFormat.append(day);
		cronFormat.append(" ");
		cronFormat.append(month);
		cronFormat.append(" ");
		cronFormat.append(year);
		
		return cronFormat.toString();
	}
	
	public static String getMonthName(int month, Locale locale)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM", locale);
		Calendar calendar = getCalendar();
		calendar.set(Calendar.MONTH, month-1);
		
		return simpleDateFormat.format(calendar.getTime());
	}
	
	public static Date getBegin(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
		
		return calendar.getTime();
	}
	
	public static Date getEnd(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
		
		return calendar.getTime();
	}
	public static Date getBegin(int month, int year)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
		
		return calendar.getTime();
	}
	
	public static Date getEnd(int month, int year)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.YEAR, year);		
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
		
		return calendar.getTime();
	}
	
	public static List<Date> getMonths()
	{
		List<Date> months = new ArrayList<Date>();
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(Calendar.DAY_OF_MONTH, 15);
		for (int i=0;i<12;i++)
		{
			calendar.set(Calendar.MONTH, i);
			months.add(calendar.getTime());
		}				
		return months;
	}
	
	public static String getSimpleDateFormat(long milliseconds)
	{
		return getSimpleDateFormat(milliseconds, Locale.ENGLISH);
	}
	
	public static String getSimpleDateFormat(long milliseconds, Locale locale)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", locale);
		Calendar calendar = getCalendar();
		calendar.setTimeInMillis(milliseconds);
		
		return simpleDateFormat.format(calendar.getTime());
	}
//	public static LoadableDetachableModel getDetachableMonths()
//	{
//		return new LoadableDetachableModel()
//		{
//			@Override
//			public Object load()
//			{
//				return getMonths();
//			}
//		};
//	}
	
	/**
	    * Elapsed days based on current time
	    *
	    * @param date Date
	    *
	    * @return int number of days
	    */
	    public static int getElapsedDays(Date date) {
	        return elapsed(date, Calendar.DATE);
	    }
	   /**
	    * Elapsed days based on two Date objects
	    *
	    * @param d1 Date
	    * @param d2 Date
	    *
	    * @return int number of days
	    */
	    public static int getElapsedDays(Date d1, Date d2) {
	        return elapsed(d1, d2, Calendar.DATE);
	    }
	    /**
	    * Elapsed months based on current time
	    *
	    * @param date Date
	    *
	    * @return int number of months
	    */
	    public static int getElapsedMonths(Date date) {
	        return elapsed(date, Calendar.MONTH);
	    }
	   /**
	    * Elapsed months based on two Date objects
	    *
	    * @param d1 Date
	    * @param d2 Date
	    *
	    * @return int number of months
	    */
	    public static int getElapsedMonths(Date d1, Date d2) {
	        return elapsed(d1, d2, Calendar.MONTH);
	    }
	     /**
	    * Elapsed years based on current time
	    *
	    * @param date Date
	    *
	    * @return int number of years
	    */
	    public static int getElapsedYears(Date date) {
	        return elapsed(date, Calendar.YEAR);
	    }
	   /**
	    * Elapsed years based on two Date objects
	    *
	    * @param d1 Date
	    * @param d2 Date
	    *
	    * @return int number of years
	    */
	    public static int getElapsedYears(Date d1, Date d2) {
	        return elapsed(d1, d2, Calendar.YEAR);
	    }
	     /**
	     * All elaspsed types
	     *
	     * @param g1 GregorianCalendar
	     * @param g2 GregorianCalendar
	     * @param type int (Calendar.FIELD_NAME)
	     *
	     * @return int number of elapsed "type"
	     */
	    private static int elapsed(GregorianCalendar g1, GregorianCalendar g2, int type) {
	        GregorianCalendar gc1, gc2;
	        int elapsed = 0;
	        // Create copies since we will be clearing/adding
	        if (g2.after(g1)) {
	            gc2 = (GregorianCalendar) g2.clone();
	            gc1 = (GregorianCalendar) g1.clone();
	        } else  {
	            gc2 = (GregorianCalendar) g1.clone();
	            gc1 = (GregorianCalendar) g2.clone();
	        }
	        if (type == Calendar.MONTH || type == Calendar.YEAR) {
	            gc1.clear(Calendar.DATE);
	            gc2.clear(Calendar.DATE);
	        }
	        if (type == Calendar.YEAR) {
	            gc1.clear(Calendar.MONTH);
	            gc2.clear(Calendar.MONTH);
	        }
	        while (gc1.before(gc2)) {
	            gc1.add(type, 1);
	            elapsed++;
	        }
	        return elapsed;
	    }
	     /**
	     * All elaspsed types based on date and current Date
	     *
	     * @param date Date
	     * @param type int (Calendar.FIELD_NAME)
	     *
	     * @return int number of elapsed "type"
	     */
	    private static int elapsed(Date date, int type) {
	        return elapsed(date, new Date(), type);
	    }
	     /**
	     * All elaspsed types
	     *
	     * @param d1 Date
	     * @param d2 Date
	     * @param type int (Calendar.FIELD_NAME)
	     *
	     * @return int number of elapsed "type"
	     */
	    private static int elapsed(Date d1, Date d2, int type) {
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(d1);
	        GregorianCalendar g1 = new GregorianCalendar(
	            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
	        cal.setTime(d2);
	        GregorianCalendar g2 = new GregorianCalendar(
	            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
	        return elapsed(g1, g2, type);
	    }
	    
	    public static DateTime convertLongUTCToDateTimeByZone(long utcTimeInMilliseconds, DateTimeZone zone)
	    {
	    	DateTime utc = new DateTime(utcTimeInMilliseconds);
			utc = utc.withZone(DateTimeZone.UTC);
			return utc.withZone(zone);
	    }
	    
	    public static long convertDateTimeToLongUTC(DateTime dateTime)
	    {
	    	DateTime utc = dateTime.withZone(DateTimeZone.UTC);
	    	return utc.getMillis();
	    }
}
