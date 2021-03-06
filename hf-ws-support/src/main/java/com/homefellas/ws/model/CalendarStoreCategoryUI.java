package com.homefellas.ws.model;

import com.homefellas.rm.calendar.CalendarStoreCategory;

public class CalendarStoreCategoryUI extends AbstractUI
{

	private String categoryName;
	private int priority;
	
	private String description;
	
	CalendarStoreCategoryUI() 
	{
		
	}
	
	public CalendarStoreCategoryUI(CalendarStoreCategory calendarStoreCategory)
	{
		
		super(calendarStoreCategory.getId(), null, calendarStoreCategory.getCreatedDate(), calendarStoreCategory.getModifiedDate(), calendarStoreCategory.getCreatedDateZone(), calendarStoreCategory.getModifiedDateZone(), calendarStoreCategory.getClientUpdateTimeStamp());
		
		this.categoryName = calendarStoreCategory.getCategoryName();
		this.priority = calendarStoreCategory.getPriority();
		this.description = calendarStoreCategory.getDescription();
		
	}

	public String getCategoryName()
	{
		return categoryName;
	}

	public int getPriority()
	{
		return priority;
	}

	public String getDescription()
	{
		return description;
	}

	void setCategoryName(String categoryName)
	{
		this.categoryName = categoryName;
	}

	void setPriority(int priority)
	{
		this.priority = priority;
	}

	void setDescription(String description)
	{
		this.description = description;
	}
	
	
}
