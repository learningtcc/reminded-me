package com.homefellas.rm.calendar;

import java.util.List;

import com.homefellas.exception.ValidationException;
import com.homefellas.user.Member;

public interface ICalendarDao {

	//create 
	public Calendar createCalendar(Calendar calendar) throws ValidationException;
	public CalendarStoreUserDefinedCategory createCalendarStoreUserDefinedCategory(CalendarStoreUserDefinedCategory calendarStoreUserDefinedCategory);
	public CalendarStoreCategory createCalendarStoreCategory(CalendarStoreCategory calendarStoreCategory);
	public CalendarStoreSubCategory createCalendarStoreSubCategory(CalendarStoreSubCategory calendarStoreSubCategory);
		
	//get
	public Calendar getCalendarById(String calendarId);
	public List<Calendar> getUserDefinedCalendars(Member member);
	public List<Calendar> getGenericCalendars();
	public List<CalendarStoreCategory> getCalendarStoreCategories();
	public List<CalendarStoreUserDefinedCategory> getCalendarStoreUserDefinedCategory(String calendarStoreSubCategoryId);
	public List<CalendarStoreUserDefinedCategory> getCalendarStoreUserDefinedCategory(String calendarStoreSubCategoryId, String email);
	public CalendarStoreSubCategory getCalendarStoreSubCategoryById(String id);
	public CalendarStoreUserDefinedCategory getCalendarStoreUserDefinedCategoryById(String id);
	
	//update
	public Calendar updateCalendar(Calendar calendar);
}
