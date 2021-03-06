package com.homefellas.ws.model;

import org.joda.time.DateTime;

import com.homefellas.rm.repeatsetup.TaskRepeat;

public class TaskRepeatUI extends AbstractUI
{
	private DateTime datetime;
	
	private boolean enabled;
	
	private int status;
	
	TaskRepeatUI(){}
	
	public TaskRepeatUI(TaskRepeat taskRepeat)
	{
		super(taskRepeat.getId(), null, taskRepeat.getCreatedDate(), taskRepeat.getModifiedDate(), taskRepeat.getCreatedDateZone(), taskRepeat.getModifiedDateZone(), taskRepeat.getClientUpdateTimeStamp());
	
		this.datetime = new DateTime(taskRepeat.getDatetime().getMillis());
		this.enabled = taskRepeat.isEnabled();
		this.status = taskRepeat.getStatus();
		
	}

	public DateTime getDatetime()
	{
		return datetime;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public int getStatus()
	{
		return status;
	}

	void setDatetime(DateTime datetime)
	{
		this.datetime = datetime;
	}

	void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	void setStatus(int status)
	{
		this.status = status;
	}
	
	
}
