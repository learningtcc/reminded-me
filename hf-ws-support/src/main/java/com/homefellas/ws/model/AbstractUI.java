package com.homefellas.ws.model;

import org.joda.time.DateTime;

public abstract class AbstractUI
{

	protected String lastModifiedDeviceId;
	protected String id;
	
	protected long createdDate;
	protected long modifiedDate;
	
	protected DateTime createdDateZone;
	protected DateTime modifiedDateZone;
	protected DateTime clientUpdateTimeStamp;
	
	AbstractUI()
	{
		
	}
	
	public AbstractUI(String id, String lastModifiedDeviceId, long cDate, long mDate, DateTime cDateZone, DateTime mDateZone, DateTime cuTS)
	{
		this.lastModifiedDeviceId = lastModifiedDeviceId;
		this.id = id;
		this.createdDate = cDate;
		this.modifiedDate = mDate;
		
		this.createdDateZone = new DateTime(cDateZone.getMillis());
		this.modifiedDateZone = new DateTime(mDateZone.getMillis());
		this.clientUpdateTimeStamp = this.clientUpdateTimeStamp != null ? new DateTime(cuTS.getMillis()) : null;
	}
}
