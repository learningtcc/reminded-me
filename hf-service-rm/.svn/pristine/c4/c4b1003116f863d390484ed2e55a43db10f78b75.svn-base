package com.homefellas.rm.task;

import java.io.Serializable;

import javax.persistence.Column;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;

import com.homefellas.rm.ISynchronizeable;

public class ConcreteSynchronizeable implements ISynchronizeable
{

	private String primaryKey;
	private long modifiedDate;
	private String modelName;
	private String lastModifiedDeviceId;
	
	@Override
	public String getPrimaryKey()
	{
		return primaryKey;
	}

	@Override
	public long getModifiedDate()
	{
		return modifiedDate;
	}

	@Override
	public String getModelName()
	{
		return modelName;
	}

	@Override
	public void setModelName(String modelName)
	{
		
	}

	@Override
	@JsonIgnore
	public String getMemberAttributeName()
	{
		return null;
	}

	public String getLastModifiedDeviceId()
	{
		return lastModifiedDeviceId;
	}

	public void setLastModifiedDeviceId(String lastModifiedDeviceId)
	{
		this.lastModifiedDeviceId = lastModifiedDeviceId;
	}

	public void setModifiedDate(long modifiedDate)
	{
		this.modifiedDate = modifiedDate;
	}

	@Override
	public void setModifiedDateZone(DateTime modifiedDateZone)
	{
	
	}
	
	@JsonIgnore
	public String getSyncId()
	{
		return "id";
	}

	@Override
	public DateTime getClientUpdateTimeStamp()
	{
		return null;
	}
	

}
