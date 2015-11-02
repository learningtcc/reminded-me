package com.homefellas.ws.model;

import org.joda.time.DateTime;

import com.homefellas.rm.reminder.Alarm;

public class AlarmUI extends AbstractUI
{

	private String lastModifiedDeviceId;
	private String message;
	
	private boolean activeReminder;
	private boolean emailReminder;
	
	private int notificationTyp;
	private int pushType;
	private int snoozeTimeInMin;
	private int reminderIntervaleType;
	private int reminderIntervale;
	private int alarmStatus;
	
	private DateTime alarmTime;
	private String alarmTimeZone;
	
	AlarmUI()
	{
		
	}
	
	public AlarmUI(Alarm alarm)
	{
		super(alarm.getId(), alarm.getLastModifiedDeviceId(), alarm.getCreatedDate(), alarm.getModifiedDate(), alarm.getCreatedDateZone(), alarm.getModifiedDateZone(), alarm.getClientUpdateTimeStamp());
		
		this.message=alarm.getMessage();
		
		this.activeReminder=alarm.isActiveReminder();
		this.emailReminder=alarm.isEmailReminder();
		
		this.notificationTyp=alarm.getNotificationType();
		this.pushType=alarm.getPushType();
		this.snoozeTimeInMin=alarm.getSnoozeTimeInMin();
		this.reminderIntervaleType=alarm.getReminderIntervaleType();
		this.reminderIntervale=alarm.getReminderIntervale();
		this.alarmStatus=alarm.getAlarmStatus();
		
		this.alarmTime=new DateTime(alarm.getAlarmTime().getMillis());
		this.alarmTimeZone=alarm.getAlarmTimeZone();
		
	}

	public String getLastModifiedDeviceId()
	{
		return lastModifiedDeviceId;
	}

	public String getMessage()
	{
		return message;
	}

	public boolean isActiveReminder()
	{
		return activeReminder;
	}

	public boolean isEmailReminder()
	{
		return emailReminder;
	}

	public int getNotificationTyp()
	{
		return notificationTyp;
	}

	public int getPushType()
	{
		return pushType;
	}

	public int getSnoozeTimeInMin()
	{
		return snoozeTimeInMin;
	}

	public int getReminderIntervaleType()
	{
		return reminderIntervaleType;
	}

	public int getReminderIntervale()
	{
		return reminderIntervale;
	}

	public int getAlarmStatus()
	{
		return alarmStatus;
	}

	public DateTime getAlarmTime()
	{
		return alarmTime;
	}

	public String getAlarmTimeZone()
	{
		return alarmTimeZone;
	}

	void setLastModifiedDeviceId(String lastModifiedDeviceId)
	{
		this.lastModifiedDeviceId = lastModifiedDeviceId;
	}

	void setMessage(String message)
	{
		this.message = message;
	}

	void setActiveReminder(boolean activeReminder)
	{
		this.activeReminder = activeReminder;
	}

	void setEmailReminder(boolean emailReminder)
	{
		this.emailReminder = emailReminder;
	}

	void setNotificationTyp(int notificationTyp)
	{
		this.notificationTyp = notificationTyp;
	}

	void setPushType(int pushType)
	{
		this.pushType = pushType;
	}

	void setSnoozeTimeInMin(int snoozeTimeInMin)
	{
		this.snoozeTimeInMin = snoozeTimeInMin;
	}

	void setReminderIntervaleType(int reminderIntervaleType)
	{
		this.reminderIntervaleType = reminderIntervaleType;
	}

	void setReminderIntervale(int reminderIntervale)
	{
		this.reminderIntervale = reminderIntervale;
	}

	void setAlarmStatus(int alarmStatus)
	{
		this.alarmStatus = alarmStatus;
	}

	void setAlarmTime(DateTime alarmTime)
	{
		this.alarmTime = alarmTime;
	}

	void setAlarmTimeZone(String alarmTimeZone)
	{
		this.alarmTimeZone = alarmTimeZone;
	}

	
	
	
	
	
}
