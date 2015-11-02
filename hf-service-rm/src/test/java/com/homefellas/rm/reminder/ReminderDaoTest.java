package com.homefellas.rm.reminder;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.rm.AbstractRMTestDao;

public class ReminderDaoTest extends AbstractRMTestDao
{

	@Resource(name="reminderDao")
	private IReminderDao reminderDao;
	
	@Test
	@Transactional
	public void test()
	{
		Assert.assertTrue(true);
	}
//	@Test
//	@Transactional
//	public void testDeleteReminderNotificationsByReminder()
//	{
//		Reminder reminder = task1.getReminders().iterator().next();
//		
//		ReminderNotification reminderNotification1 = new ReminderNotification(new DateTime(), reminder);
//		dao.save(reminderNotification1);
//		reminderNotificationCounter++;
//		
//		ReminderNotification reminderNotification2 = new ReminderNotification((new DateTime()).plusDays(4), reminder);
//		dao.save(reminderNotification2);
//		reminderNotificationCounter++;
//		
//		ReminderNotification reminderNotification3 = new ReminderNotification((new DateTime()).plusMonths(1), reminder);
//		dao.save(reminderNotification3);
//		reminderNotificationCounter++;
//		
//		Assert.assertEquals(reminderNotificationCounter, countRowsInTable("r_reminder_notifications"));
//		dao.flush();
//		reminderDao.deleteReminderNotificationsByReminder(reminder);
//		dao.flush();
//		Assert.assertEquals(0, countRowsInTable("r_reminder_notifications"));
//		
//	}
//	
//	@Test
//	@Transactional
//	public void testGetReminderNotifications()
//	{
//	
//		Reminder reminder = task1.getReminders().iterator().next();
//		
//		ReminderNotification reminderNotification1 = new ReminderNotification(new DateTime(), reminder);
//		dao.save(reminderNotification1);
//		reminderNotificationCounter++;
//		
//		ReminderNotification reminderNotification2 = new ReminderNotification((new DateTime()).plusDays(4), reminder);
//		dao.save(reminderNotification2);
//		reminderNotificationCounter++;
//		
//		ReminderNotification reminderNotification3 = new ReminderNotification((new DateTime()).plusMonths(1), reminder);
//		dao.save(reminderNotification3);
//		reminderNotificationCounter++;
//		
//		Assert.assertEquals(reminderNotificationCounter, countRowsInTable("r_reminder_notifications"));
//		dao.flush();
//		
//		List<ReminderNotification> notifications = reminderDao.getReminderNotifications(reminder);
//		Assert.assertTrue(notifications.contains(reminderNotification1));
//		Assert.assertTrue(notifications.contains(reminderNotification2));
//		Assert.assertTrue(notifications.contains(reminderNotification3));
//	}

}
