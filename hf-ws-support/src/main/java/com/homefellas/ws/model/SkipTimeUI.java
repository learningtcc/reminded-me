package com.homefellas.ws.model;

import org.joda.time.DateTime;

import com.homefellas.rm.task.SkipTime;

public class SkipTimeUI extends AbstractUI
{

	private DateTime skipStart;
	private DateTime skipEnd;
	
	SkipTimeUI(){}
	
	public SkipTimeUI(SkipTime skipTime)
	{
		super(skipTime.getId(), null, skipTime.getCreatedDate(), skipTime.getModifiedDate(), skipTime.getCreatedDateZone(), skipTime.getModifiedDateZone(), skipTime.getClientUpdateTimeStamp());
		
		skipStart = new DateTime(skipTime.getSkipStart().getMillis());
		skipEnd = new DateTime(skipTime.getSkipEnd().getMillis());
	}

	public DateTime getSkipStart()
	{
		return skipStart;
	}

	public DateTime getSkipEnd()
	{
		return skipEnd;
	}

	void setSkipStart(DateTime skipStart)
	{
		this.skipStart = skipStart;
	}

	void setSkipEnd(DateTime skipEnd)
	{
		this.skipEnd = skipEnd;
	}
	
	
}
