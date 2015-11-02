package com.homefellas.rm.reminder;

import java.util.List;

import org.joda.time.DateTime;

import com.homefellas.rm.task.Task;
import com.homefellas.user.Member;

public interface IReminderDao
{
	
	//create
	public Alarm createAlarm(Alarm alarm);
	
	//read
	public List<Alarm> getBulkAlarms(List<String> ids, String loggedInEmail);
	public List<Alarm> getAlarmByTaskAndMember(Task task, Member member);
	public List<Alarm> getAlarmdsyTask(Task task);
	public Alarm getAlarmByTaskMemberAndTime(Task task, Member member, DateTime fireTime);
	public Alarm getAlarmById(String id);
	
	//update
	public Alarm updateAlarm(Alarm alarm);
	
}
