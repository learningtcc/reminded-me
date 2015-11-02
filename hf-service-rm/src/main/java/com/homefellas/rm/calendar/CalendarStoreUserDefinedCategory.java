package com.homefellas.rm.calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Proxy;

import com.homefellas.model.core.AbstractGUIDModel;
import com.homefellas.rm.calendar.CalendarStoreSubCategory.DefaultCalendarStoreSubCategoryEnum;
import com.homefellas.user.Member;

@Entity
@Table(name="t_calendarstoreuserdefinedcategory")
@Proxy(lazy=false)
@XmlRootElement
public class CalendarStoreUserDefinedCategory extends AbstractGUIDModel
{

	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name="calendarStoreSubCategoryId")
	private CalendarStoreSubCategory calendarStoreSubCategory;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=true)
	@JoinColumn(name="memberId")
	private Member member;
	
	@Column(nullable=false)
	private String categoryName;

//	public enum DefaultCalendarStoreUserDefinedCategoryEnum 
//	{
//		delawareBJJ("0", "Delaware JuiJitsu", DefaultCalendarStoreSubCategoryEnum.bjj);
//		
//		private DefaultCalendarStoreSubCategoryEnum calendarStoreSubCategoryEnum;
//		private String name;
//		private String id;
//		
//		private DefaultCalendarStoreUserDefinedCategoryEnum(String id, String categoryName, DefaultCalendarStoreSubCategoryEnum calendarStoreSubCategoryEnum)
//		{
//			this.id = id;
//			this.name = categoryName;
//			this.calendarStoreSubCategoryEnum = calendarStoreSubCategoryEnum;
//		}
//
//		public DefaultCalendarStoreSubCategoryEnum getCalendarStoreSubCategoryEnum()
//		{
//			return calendarStoreSubCategoryEnum;
//		}
//
//		public String getName()
//		{
//			return name;
//		}
//		
//		public String getId()
//		{
//			return this.id;
//		}
//		
//	}
	
	public CalendarStoreSubCategory getCalendarStoreSubCategory()
	{
		return calendarStoreSubCategory;
	}

	public void setCalendarStoreSubCategory(
			CalendarStoreSubCategory calendarStoreSubCategory)
	{
		this.calendarStoreSubCategory = calendarStoreSubCategory;
	}

	public Member getMember()
	{
		return member;
	}

	public void setMember(Member member)
	{
		this.member = member;
	}

	public String getCategoryName()
	{
		return categoryName;
	}

	public void setCategoryName(String categoryName)
	{
		this.categoryName = categoryName;
	}
	
	
}
