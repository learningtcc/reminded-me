package com.homefellas.rm.share;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.homefellas.rm.ISynchronizeableFilteredDateRange;

public class SynchronizeShare extends Share implements ISynchronizeableFilteredDateRange
{

//	@Override
//	@JsonIgnore
//	public boolean isStatusExclusive()
//	{
//		return false;
//	}

	@JsonIgnore
	public String getSyncId()
	{
		return "task.id";
	}
	
	@Override
	@JsonIgnore
	public String getModelName()
	{
		return Share.class.getSimpleName();
	}
	
	@Override
	@JsonIgnore
	public String getMemberAttributeName()
	{
		return "user";
	}

	@Override
	@JsonIgnore
	public String getEndTimeAttributeName()
	{
		return "task.endTimeMilli";
	}


	@Override
	@JsonIgnore
	public String getStartTimeAttributeName()
	{
		return "task.startTimeMilli";
	}

	@Override
	@JsonIgnore
	public String getTimeLessTaskAttributeName()
	{
		return "task.timeLessTask";
	}
	
	
}
