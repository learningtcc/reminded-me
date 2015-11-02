package com.homefellas.rm.calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.annotations.Proxy;

import com.homefellas.model.core.AbstractGUIDModel;
import com.homefellas.model.core.AbstractSequenceModel;
import com.homefellas.rm.calendar.CalendarStoreCategory.DefaultCalendarStoreCategoryEnum;

@Entity
@Table(name="t_calendarstoresubcategory")
@Proxy(lazy=false)
@XmlRootElement
public class CalendarStoreSubCategory extends AbstractGUIDModel
{

	@Column(nullable=false)
	private String categoryName;
	
	private int priority=0;
	
	private String description;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name="calendarStoreCategoryId")
	@JsonBackReference("calendarStoreCategory")
	private CalendarStoreCategory calendarStoreCategory;

	public enum DefaultCalendarStoreSubCategoryEnum {
		
		yoga("0", "Yoga Class", DefaultCalendarStoreCategoryEnum.classes), 
		bjj("1", "Jui-Jitsu Class", DefaultCalendarStoreCategoryEnum.classes),
		nfl("2", "NFL Schedule", DefaultCalendarStoreCategoryEnum.proSports),
		mlb("3", "MLB Schedule", DefaultCalendarStoreCategoryEnum.proSports);
		
		private String categoryName;
		private DefaultCalendarStoreCategoryEnum categoryEnum;
		private String id;
		
		private DefaultCalendarStoreSubCategoryEnum(String id, String name, DefaultCalendarStoreCategoryEnum calendarStoreSubCategoryEnum)
		{
			this.id = id;
			this.categoryEnum = calendarStoreSubCategoryEnum;
			this.categoryName = name;
		}
		
		public String getId()
		{
			return this.id;
		}
		
		public DefaultCalendarStoreCategoryEnum getCalendarStoreCategory()
		{
			return categoryEnum;
		}
		
		public String getCategoryName()
		{
			return this.categoryName;
		}
	}
	
	public String getCategoryName()
	{
		return categoryName;
	}

	public void setCategoryName(String categoryName)
	{
		this.categoryName = categoryName;
	}

	public int getPriority()
	{
		return priority;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public CalendarStoreCategory getCalendarStoreCategory()
	{
		return calendarStoreCategory;
	}

	public void setCalendarStoreCategory(CalendarStoreCategory calendarStoreCategory)
	{
		this.calendarStoreCategory = calendarStoreCategory;
	}
	
	
}
