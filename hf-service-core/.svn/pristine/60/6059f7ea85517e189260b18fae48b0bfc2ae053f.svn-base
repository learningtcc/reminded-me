package com.homefellas.model.core;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.homefellas.dao.core.Persistable;


public abstract class AbstractPersistantObject implements Serializable, Persistable {

	protected long id;
	protected long timeCreated;
	protected long timeModified;
	
	private Locale locale;
	
	/**
	 * Return the object primary key
	 * 
	 * @return Serializable
	 */
	public Serializable getPrimaryKey() {
		return id;
	}	
	
	/**
	 * Returns a Calendar instance of the timeCreated
	 * 
	 * @return Calendar
	 */
	public Calendar getTimeCreatedCalendar() {
		if(timeCreated == 0)
			return null;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeCreated);
		return calendar;
	}
	
	/**
	 * Returns a Calendar instance of the timeModified
	 * 
	 * @return Calendar
	 */
	public Calendar getTimeModifiedCalendar() {
		if(timeModified == 0)
			return null;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeModified);
		return calendar;
	}	
	
	/**
	 * Returns a Date instance of the timeCreated
	 * 
	 * @return Date
	 */
	public Date getTimeCreatedDate() {
		if(timeCreated == 0)
			return null;
		
		return new Date(timeCreated);
	}
	
	/**
	 * Returns a Date instance of the timeModified
	 * 
	 * @return Date
	 */
	public Date getTimeModifiedDate() {
		if(timeModified == 0)
			return null;
		
		return new Date(timeModified);
	}
	
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + (int) (id ^ (id >>> 32));
//		result = prime * result + ((type == null) ? 0 : type.hashCode());
//		return result;
//	}

//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		final AbstractPersistantObject other = (AbstractPersistantObject) obj;
//		if (id != other.id)
//			return false;
//		if (type == null) {
//			if (other.type != null)
//				return false;
//		} else if (!type.equals(other.type))
//			return false;
//		return true;
//	}
	
		
	// getters and setters
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getTimeCreated() {
		return timeCreated;
	}
	public void setTimeCreated(long timeCreated) {
		this.timeCreated = timeCreated;
	}
	public long getTimeModified() {
		return timeModified;
	}
	public void setTimeModified(long timeModified) {
		this.timeModified = timeModified;
	}			
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (getId() ^ (getId()>>> 32));
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AbstractPersistantObject other = (AbstractPersistantObject) obj;
		if (getId() != other.getId())
			return false;
		return true;
	}


	public Locale getLocale()
	{
		if (locale==null)
		{
			locale = new Locale("en", "US");
		}
		return locale;
	}


	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}

	
}
