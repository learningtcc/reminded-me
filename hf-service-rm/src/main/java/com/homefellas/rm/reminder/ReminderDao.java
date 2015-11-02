package com.homefellas.rm.reminder;

import java.util.List;

import org.hibernate.Query;
import org.joda.time.DateTime;

import com.homefellas.dao.hibernate.core.HibernateCRUDDao;
import com.homefellas.rm.task.Task;
import com.homefellas.user.Member;

public class ReminderDao extends HibernateCRUDDao implements IReminderDao
{

	
	@Override
	public List<Alarm> getAlarmdsyTask(Task task)
	{
		Query query = getQuery("from Alarm a where a.task.id = ?");
		
		query.setParameter(0, task.getId());
		
		return query.list();
	}
	
	@Override
	public List<Alarm> getAlarmByTaskAndMember(Task task, Member member)
	{
		Query query = getQuery("from Alarm a where a.task.id = ? and a.member.id=?");
		
		query.setParameter(0, task.getId());
		query.setParameter(1, member.getId());
		
		return query.list();
	}
	
	@Override
	public List<Alarm> getBulkAlarms(List<String> ids, String loggedInEmail)
	{
		Query query = getQuery("from Alarm a " +
			"where a.id in (:ids)");
		query.setParameterList("ids", ids);
		
		return query.list();	
	}

	@Override
	public Alarm getAlarmByTaskMemberAndTime(Task task, Member member, DateTime fireTime)
	{
		Query query = getQuery("from Alarm a where a.task.id = ? and a.member.id = ? and a.alarmTime = ?");
		
		query.setParameter(0, task.getId());
		query.setParameter(1, member.getId()); 
		query.setParameter(2, fireTime.getMillis());
		
		return (Alarm)query.uniqueResult();
	}
	
	@Override
	public Alarm createAlarm(Alarm alarm)
	{
		save(alarm);
		return alarm;
	}

	@Override
	public Alarm getAlarmById(String id) {
		return loadByPrimaryKey(Alarm.class, id);
	}

	@Override
	public Alarm updateAlarm(Alarm alarm) {
		updateObject(alarm);
		
		return alarm;
	}

	

}
