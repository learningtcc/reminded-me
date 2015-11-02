package com.homefellas.ws.model;

import com.homefellas.rm.calendar.CalendarStoreSubCategory;

public class CalendarStoreSubCategoryUI extends AbstractUI
{

private String categoryName;
	
	private String description;

	private int priority;
	
	private CalendarStoreCategoryUI calendarStoreCategory;
	
	CalendarStoreSubCategoryUI()
	{
		
	}
	
	public CalendarStoreSubCategoryUI(CalendarStoreSubCategory calendarStoreSubCategory)
	{
		super(calendarStoreSubCategory.getId(), null, calendarStoreSubCategory.getCreatedDate(), calendarStoreSubCategory.getModifiedDate(), calendarStoreSubCategory.getCreatedDateZone(), calendarStoreSubCategory.getModifiedDateZone(), calendarStoreSubCategory.getClientUpdateTimeStamp());
		
		this.description = calendarStoreSubCategory.getDescription();
		this.priority = calendarStoreSubCategory.getPriority();
		this.calendarStoreCategory = new CalendarStoreCategoryUI(calendarStoreSubCategory.getCalendarStoreCategory());
	}

	public String getCategoryName()
	{
		return categoryName;
	}

	public String getDescription()
	{
		return description;
	}

	public int getPriority()
	{
		return priority;
	}

	public CalendarStoreCategoryUI getCalendarStoreCategory()
	{
		return calendarStoreCategory;
	}

	void setCategoryName(String categoryName)
	{
		this.categoryName = categoryName;
	}

	void setDescription(String description)
	{
		this.description = description;
	}

	void setPriority(int priority)
	{
		this.priority = priority;
	}

	void setCalendarStoreCategory(CalendarStoreCategoryUI calendarStoreCategory)
	{
		this.calendarStoreCategory = calendarStoreCategory;
	}
	
	

}
