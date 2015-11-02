package com.homefellas.rm.share;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.homefellas.rm.ISynchronizeable;
import com.homefellas.rm.ISynchronizeableFilteredDateRange;

public class SynchronizeShareCalendar extends ShareCalendar implements ISynchronizeable {

	@JsonIgnore
	public String getSyncId()
	{
		return "calendar.id";
	}
	
	@Override
	@JsonIgnore
	public String getModelName()
	{
		return ShareCalendar.class.getSimpleName();
	}
	
	@Override
	@JsonIgnore
	public String getMemberAttributeName()
	{
		return "user";
	}

	
}
