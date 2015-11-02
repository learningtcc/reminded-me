package com.homefellas.rm.calendar;

import java.util.List;

import org.hibernate.Query;

import com.homefellas.dao.hibernate.core.HibernateCRUDDao;
import com.homefellas.exception.ValidationException;
import com.homefellas.user.Member;

public class CalendarHibernateDao extends HibernateCRUDDao implements ICalendarDao {

	@Override
	public Calendar getCalendarById(String calendarId) {
		return loadByPrimaryKey(Calendar.class, calendarId);
	}

	
	@Override
	public List<Calendar> getUserDefinedCalendars(Member member)
	{
		Query query = getQuery("from Calendar c where c.member.id=?");
		query.setString(0, member.getId());
		
		return query.list();
	}
	
	@Override
	public List<Calendar> getGenericCalendars()
	{
		Query query = getQuery("from Calendar c where c.generic=true");
		return query.list();
	}


	@Override
	public Calendar createCalendar(Calendar calendar) throws ValidationException {
		save(calendar);
		
		return calendar;
	}


	@Override
	public Calendar updateCalendar(Calendar calendar) {
		merge(calendar);
		
		return calendar;
	}


	@Override
	public List<CalendarStoreCategory> getCalendarStoreCategories() {
		return loadAllObjects(CalendarStoreCategory.class);
	}
	
	@Override
	public List<CalendarStoreUserDefinedCategory> getCalendarStoreUserDefinedCategory(String calendarStoreSubCategoryId)
	{
		Query query = getQuery("from CalendarStoreUserDefinedCategory usercat where usercat.member is null and usercat.calendarStoreSubCategory.id=?");
		query.setString(0, calendarStoreSubCategoryId);
		
		return query.list();
	}
	
	@Override
	public List<CalendarStoreUserDefinedCategory> getCalendarStoreUserDefinedCategory(String calendarStoreSubCategoryId, String email)
	{
		Query query = getQuery("from CalendarStoreUserDefinedCategory usercat where usercat.member.email = ? and usercat.calendarStoreSubCategory.id=?");
		query.setString(0, email);
		query.setString(1, calendarStoreSubCategoryId);
		
		return query.list();
	}


	@Override
	public CalendarStoreUserDefinedCategory createCalendarStoreUserDefinedCategory(
			CalendarStoreUserDefinedCategory calendarStoreUserDefinedCategory) {

		save(calendarStoreUserDefinedCategory);
		return calendarStoreUserDefinedCategory;
	}


	@Override
	public CalendarStoreCategory createCalendarStoreCategory(
			CalendarStoreCategory calendarStoreCategory) {
		save(calendarStoreCategory);
		return calendarStoreCategory;
	}
	
	@Override
	public CalendarStoreSubCategory createCalendarStoreSubCategory(
			CalendarStoreSubCategory calendarStoreSubCategory) {
		save(calendarStoreSubCategory);
		return calendarStoreSubCategory;
	}


	@Override
	public CalendarStoreSubCategory getCalendarStoreSubCategoryById(String id) {
		return loadByPrimaryKey(CalendarStoreSubCategory.class, id);
	}


	@Override
	public CalendarStoreUserDefinedCategory getCalendarStoreUserDefinedCategoryById(
			String id) {
		return loadByPrimaryKey(CalendarStoreUserDefinedCategory.class, id);
	}
	
	
	
	
}
