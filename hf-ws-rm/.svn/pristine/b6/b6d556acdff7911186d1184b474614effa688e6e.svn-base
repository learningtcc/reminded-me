package com.homefellas.rm.reminder;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Test;

import com.homefellas.rm.reminder.Alarm.AlarmStatusEnum;
import com.homefellas.rm.task.Task;
import com.homefellas.user.Profile;
import com.homefellas.ws.core.AbstractTestRMWebService;
import com.sun.jersey.api.client.GenericType;


public class ReminderWebServiceTest extends AbstractTestRMWebService
{
	@Test
	public void syncOnAlarmDelete()
	{
		Profile profile = createAndRetrieveProfile();
		Task task1 = createAndRetrieveTask(profile);
		
		Alarm alarm1 = createAndRetrieveAlarm(task1);
		Alarm alarm2 = createAndRetrieveAlarm(task1);
		
		Map<String, List<Object>> syns = sync(profile.getId(), 0);
		List alarms = syns.get(Alarm.class.getName());
		Assert.assertTrue(alarms.contains(alarm1.getId()));
		Assert.assertTrue(alarms.contains(alarm2.getId()));
		
		callWebService(ReminderWebService.class, "deleteAlarm", Boolean.class, buildPathParms("{id}", alarm2.getId()));
		
		syns = sync(profile.getId(), 0);
		alarms = syns.get(Alarm.class.getName());
		Assert.assertTrue(alarms.contains(alarm1.getId()));
		Assert.assertFalse(alarms.contains(alarm2.getId()));
	}

	@Test
	public void bulkAlarm()
	{
		Profile profile = createAndRetrieveProfile();
		Task task1 = createAndRetrieveTask(profile);
		
		Alarm alarm1 = createAndRetrieveAlarm(task1);
		Alarm alarm2 = createAndRetrieveAlarm(task1);
		
		List<Alarm> alarms = callSecuredWebService(ReminderWebService.class, "getBulkAlarms", new GenericType<List<Alarm>>(){}, buildPathParms("{ids}", alarm1.getId()+","+alarm2.getId()), profile.getEmail());
		Assert.assertTrue(alarms.contains(alarm1));
		Assert.assertTrue(alarms.contains(alarm2));
	}
	
	@Test
	public void updateAlarm()
	{
		Profile profile = createAndRetrieveProfile();
		Task task1 = createAndRetrieveTask(profile);
		
		Alarm alarm1 = createAndRetrieveAlarm(task1);
		
		DateTime newDateTime = new DateTime().plusMonths(1);
		alarm1.setAlarmTime(newDateTime);
		callWebService(ReminderWebService.class, "updateAlarm", Alarm.class, alarm1);
		
		Alarm alarmUnderTest = callSecuredWebService(ReminderWebService.class, "getBulkAlarms", new GenericType<List<Alarm>>(){}, buildPathParms("{ids}", alarm1.getId()), profile.getEmail()).get(0);
		Assert.assertEquals(newDateTime.getMonthOfYear(), alarmUnderTest.getAlarmTime().getMonthOfYear());
	}
	
	@Test
	public void deleteAlarm()
	{
		Profile profile = createAndRetrieveProfile();
		Task task1 = createAndRetrieveTask(profile);
		
		Alarm alarm1 = createAndRetrieveAlarm(task1);
		
		callWebService(ReminderWebService.class, "deleteAlarm", Boolean.class, buildPathParms("{id}", alarm1.getId()));
		
		Alarm alarmUnderTest = callSecuredWebService(ReminderWebService.class, "getBulkAlarms", new GenericType<List<Alarm>>(){}, buildPathParms("{ids}", alarm1.getId()), profile.getEmail()).get(0);
		Assert.assertEquals(AlarmStatusEnum.DELETED.ordinal(), alarmUnderTest.getAlarmStatus());
	}
	

	
}
