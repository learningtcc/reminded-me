package com.homefellas.model.core;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.JoinTable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.homefellas.dao.core.Persistable;
import com.homefellas.ws.core.JodaDateTimeJsonDeSerializer;
import com.homefellas.ws.core.JodaDateTimeJsonSerializer;

@MappedSuperclass
public abstract class AbstractModel implements Persistable, Serializable {

	/**
	 * This is a calculated value of the createdDateZone.  It should not be set by itself.
	 */
	@Column
	protected long createdDate;
	
	@Column
//	@Index(name="modifiedDateIndex")
	protected long modifiedDate;
	
	@Columns(columns={@Column(name="createdDateDT",insertable=true,updatable=false),@Column(name="createdDateZone",insertable=true,updatable=false)})
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTimeTZ")
//	@JsonSerialize(using=JodaDateTimeJsonSerializer.class)
	protected DateTime createdDateZone;
	
	@Columns(columns={@Column(name="modifiedDateDT",insertable=true,updatable=true),@Column(name="modifiedDateZone",insertable=true,updatable=true)})
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTimeTZ")
	protected DateTime modifiedDateZone;
	
	@Transient
	@JsonSerialize(using=JodaDateTimeJsonSerializer.class)
	@JsonDeserialize(using=JodaDateTimeJsonDeSerializer.class)
	protected DateTime clientUpdateTimeStamp;
	
	public AbstractModel()
	{
		createdDateZone = new DateTime();
		modifiedDateZone = new DateTime();
		createdDate = createdDateZone.getMillis();
		modifiedDate = modifiedDateZone.getMillis();
	}
	
	public abstract boolean isPrimaryKeySet();

	/**
	 * @return the createdDate
	 */
	public long getCreatedDate()
	{		
		return createdDate; 
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(long createdDate)
	{
		this.createdDate = createdDate;
//		if (createdDateZone!=null) 
//			this.createdDateZone = TimeHelper.convertLongUTCToDateTimeByZone(createdDate, createdDateZone.getZone());
	}

	/**
	 * @return the modifiedDate
	 */
	public long getModifiedDate()
	{
		return modifiedDate;
	}

	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(long modifiedDate)
	{
		if (modifiedDate > System.currentTimeMillis())
			modifiedDate = System.currentTimeMillis();
		
		this.modifiedDate = modifiedDate;
//		if (modifiedDateZone!=null)
//			this.modifiedDateZone = TimeHelper.convertLongUTCToDateTimeByZone(modifiedDate, modifiedDateZone.getZone());
	}

	/**
	 * @return the createdDateZone
	 */
	public DateTime getCreatedDateZone()
	{
		return createdDateZone;
	}

	/**
	 * @param createdDateZone the createdDateZone to set
	 */
	public void setCreatedDateZone(DateTime createdDateZone)
	{
		if (createdDateZone.isAfterNow())
			createdDateZone = new DateTime();
		
		this.createdDateZone = createdDateZone;
		if (createdDateZone!=null)
			this.createdDate = createdDateZone.getMillis();
	}

	/**
	 * @return the modifiedDateZone
	 */
	public DateTime getModifiedDateZone()
	{
		return modifiedDateZone;
	}

	/**
	 * @param modifiedDateZone the modifiedDateZone to set
	 */
	public void setModifiedDateZone(DateTime modifiedDateZone)
	{
		if (modifiedDateZone.isAfterNow())
		{
			modifiedDateZone = new DateTime();
		}
		this.modifiedDateZone = modifiedDateZone;
		if (modifiedDateZone!=null)
			this.modifiedDate = modifiedDateZone.getMillis();
		//		this.modifiedDate = TimeHelper.convertDateTimeToLongUTC(modifiedDateZone);
	}

	
//	/**
//	 * @return the createdDateZone
//	 */
//	public DateTime getCreatedDateZone()
//	{
//		return convertMillsToDateTime(createdDate);
//	}
//
//	/**
//	 * @param createdDateZone the createdDateZone to set
//	 */
//	public void setCreatedDateZone(DateTime createdDateZone)
//	{
//		this.createdDate = convertDateTimeToMills(createdDateZone);
//	}
//
//	/**
//	 * @return the modifiedDateZone
//	 */
//	public DateTime getModifiedDateZone()
//	{
//		return convertMillsToDateTime(modifiedDate);
//	}
//
//	/**
//	 * @param modifiedDateZone the modifiedDateZone to set
//	 */
//	public void setModifiedDateZone(DateTime modifiedDateZone)
//	{
//		this.modifiedDate = convertDateTimeToMills(modifiedDateZone);
//	}


	@JsonIgnore
	public String getTableName()
	{
		return getClass().getAnnotation(Table.class).name();
	}
	
	@JsonIgnore
	public String getJoinTableName(String fieldName)
	{
		try
		{
			Field privateStringField = null;
			try
			{
				privateStringField = getClass().getDeclaredField(fieldName);
			}
			catch (NoSuchFieldException suchFieldException)
			{
				privateStringField = getClass().getSuperclass().getDeclaredField(fieldName);
			}
			privateStringField.setAccessible(true);
			
			String joinTable = privateStringField.getAnnotation(JoinTable.class).name();

			return joinTable;

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	
	public String generateUnquieId()
	{
		UUID uuid = UUID.randomUUID();
		return String.valueOf(uuid.getMostSignificantBits())+String.valueOf(uuid.getLeastSignificantBits());
	}

	public DateTime getClientUpdateTimeStamp()
	{
		return clientUpdateTimeStamp;
	}

	public void setClientUpdateTimeStamp(DateTime clientUpdateTimeStamp)
	{
		this.clientUpdateTimeStamp = clientUpdateTimeStamp;
	}
	
	

	
}
