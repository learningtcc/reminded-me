package com.homefellas.rm.calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.routines.CalendarValidator;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.exception.ValidationException;
import com.homefellas.rm.RMValidator;
import com.homefellas.rm.ValidationCodeEnum;
import com.homefellas.service.core.AbstractService;
import com.homefellas.user.Member;

public class CalendarService extends AbstractService {

	@Autowired
	private ICalendarDao calendarDao;
	
	@Autowired
	private RMValidator validator;
	
	/**
	 * This will create or update a user defined Calendar.  It will validate the calendar.
	 * @param calendar
	 * @see Calendar
	 * @see CalendarValidator
	 */
	@Transactional
	public Calendar createCalendar(Calendar calendar) throws ValidationException
	{
		validator.validateCalendar(calendar);
		
		calendarDao.createCalendar(calendar);
		
		return calendar;

	}
	
	/**
	 * This will return the calendar by id
	 */
	@Transactional
	public Calendar getCalendarById(String calendarId)
	{
		return calendarDao.getCalendarById(calendarId);
	}
	
	@Transactional
	public Calendar updateCalendar(Calendar calendar, String email) throws ValidationException
	{
		final Calendar calendarFromDB = getCalendarById(calendar.getId());
		
		if (calendarFromDB == null)
			throw new ValidationException(ValidationCodeEnum.CALENDAR_NOT_FOUND);

		if (email==null||calendarFromDB.getMember()==null||!calendarFromDB.getMember().getEmail().equals(email))
			throw new ValidationException(ValidationCodeEnum.MEMBER_ID_IS_NULL);
		
		//check to see if the modified date from the db is before the one being passed
		validator.validateSynchronizationUpdate(calendar, calendarFromDB);
		calendar.setModifiedDateZone(new DateTime());
		
		calendarDao.updateCalendar(calendar);
		
		return calendar;
	}
	
	@Transactional
	public List<CalendarStoreCategory> getCalendarStoreCategories()
	{
		return calendarDao.getCalendarStoreCategories();
	}
	
	@Transactional
	public List<CalendarStoreUserDefinedCategory> getCalendarStoreUserDefiedCategories(String calendarStoreSubCategoryId, String email)
	{
		List<CalendarStoreUserDefinedCategory> categories = calendarDao.getCalendarStoreUserDefinedCategory(calendarStoreSubCategoryId);
		categories.addAll(calendarDao.getCalendarStoreUserDefinedCategory(calendarStoreSubCategoryId, email)); 
		
		return categories;
	}
	
	@Transactional
	public CalendarStoreUserDefinedCategory createCalendarStoreUserDefinedCategory(CalendarStoreUserDefinedCategory calendarStoreUserDefinedCategory) throws ValidationException
	{
		if (calendarStoreUserDefinedCategory.getCalendarStoreSubCategory() == null)
			throw new ValidationException(ValidationCodeEnum.CALENDAR_STORE_USER_DEFINED_CATEGORY_IS_NULL);
		
		if (calendarStoreUserDefinedCategory.getCategoryName() == null || "".equals(calendarStoreUserDefinedCategory.getCategoryName()))
			throw new ValidationException(ValidationCodeEnum.CALENDAR_STORE_CATEGORY_NAME_IS_NULL);
		
		if (calendarStoreUserDefinedCategory.getMember()==null||calendarStoreUserDefinedCategory.getMember().getId()==null)
			throw new ValidationException(ValidationCodeEnum.MEMBER_ID_IS_NULL);
		
		validator.validatePrimaryKeyIsSet(calendarStoreUserDefinedCategory);
		
		calendarDao.createCalendarStoreUserDefinedCategory(calendarStoreUserDefinedCategory);
		
		return calendarStoreUserDefinedCategory;
	}
	
	/**
	 * This will return all the generic Calendars.
	 * @return public List<Calendar>
	 */
	@Transactional
	public List<Calendar> getGenericCalendar()
	{
		return calendarDao.getGenericCalendars();
	}

	/**
	 * This will return all the calendars that were created by the passed in member.
	 * @param member 
	 * @return List<Calendar>
	 */
	@Transactional
	public List<Calendar> getUserDefinedCalendars(Member member)
	{
		return calendarDao.getUserDefinedCalendars(member);
	}
	
	/**
	 * This will create a list of all the Calendars for a member.  This includes the generic plus the user defined ones.
	 * @param member
	 * @return Collection<Calendar>
	 */ 
	@Transactional(readOnly=true)
	public List<Calendar> getAllCalendarsForMember(Member member)
	{
		List<Calendar> genericCalendars = getGenericCalendar();
		List<Calendar> userDefinedCalendars = getUserDefinedCalendars(member);
//		Map<String, Calendar> map = new HashMap<String, Calendar>(genericCalendars.size()+userDefinedCalendars.size());
//		for (Calendar calendar:userDefinedCalendars)
//		{
//			map.put(calendar.getCalendarName(), calendar);
//		}
//		
//		for (Calendar calendar:genericCalendars)
//		{
//			if (map.get(calendar.getCalendarName())==null)
//				map.put(calendar.getCalendarName(), calendar);
//		}
//		return map.values();
		
		List<Calendar> calendars = new ArrayList<Calendar>(genericCalendars.size()+userDefinedCalendars.size());
		calendars.addAll(genericCalendars);
		calendars.addAll(userDefinedCalendars);
		
		return calendars;
	}
	
	@Transactional
	public CalendarStoreSubCategory getCalendarStoreSubCategoryById(String id)
	{
		return calendarDao.getCalendarStoreSubCategoryById(id);
	}
	
	@Transactional
	public CalendarStoreUserDefinedCategory getCalendarStoreUserDefinedCategoryById(String id)
	{
		return calendarDao.getCalendarStoreUserDefinedCategoryById(id);
	}
}
