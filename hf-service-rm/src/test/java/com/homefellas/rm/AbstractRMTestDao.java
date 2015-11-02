package com.homefellas.rm;

import java.util.Map;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.batch.PushTypeEnum;
import com.homefellas.exception.ValidationException;
import com.homefellas.rm.calendar.Calendar;
import com.homefellas.rm.calendar.CalendarService;
import com.homefellas.rm.calendar.CalendarStoreCategory;
import com.homefellas.rm.calendar.CalendarStoreCategory.DefaultCalendarStoreCategoryEnum;
import com.homefellas.rm.calendar.CalendarStoreSubCategory;
import com.homefellas.rm.calendar.CalendarStoreSubCategory.DefaultCalendarStoreSubCategoryEnum;
import com.homefellas.rm.calendar.GenericCalendarEnum;
import com.homefellas.rm.notification.ClientNotificationService;
import com.homefellas.rm.notification.Device;
import com.homefellas.rm.reminder.Alarm;
import com.homefellas.rm.reminder.ReminderService;
import com.homefellas.rm.share.Share;
import com.homefellas.rm.share.ShareCalendar;
import com.homefellas.rm.share.ShareService;
import com.homefellas.rm.task.Task;
import com.homefellas.rm.task.TaskService;
import com.homefellas.user.AbstractUserTestDao;
import com.homefellas.user.Member;
import com.homefellas.user.Profile;

public abstract class AbstractRMTestDao extends AbstractUserTestDao {
	

	@Resource(name="taskService")
	protected TaskService taskService;
	
	@Resource(name="shareService")
	protected ShareService shareService;
	
	@Resource(name="calendarService")
	protected CalendarService calendarService;
	
	@Resource(name="remindedMeService")
	protected RemindedMeService remindedMeService;
	
	@Resource(name="reminderService")
	protected ReminderService reminderService;
	
	@Resource(name="clientNotificationService")
	protected ClientNotificationService clientNotificationService;
	
	protected Calendar calendarDefaultPublic;

	/* (non-Javadoc)
	 * @see com.homefellas.dao.core.AbstractTestDao#createDatabaseDefaults()
	 */
	@Override
	@Transactional
	protected void createDatabaseDefaults()
	{
		super.createDatabaseDefaults();
		
		
		//create defaults
		remindedMeService.createDefaultDatabaseEntries();
		
		assertRowCount(DefaultCalendarStoreSubCategoryEnum.values().length, new CalendarStoreSubCategory());
		assertRowCount(DefaultCalendarStoreCategoryEnum.values().length, new CalendarStoreCategory());
//		assertRowCount(DefaultCalendarStoreUserDefinedCategoryEnum.values().length, new CalendarStoreUserDefinedCategory());
		
		//load the defaults
//		categoryDefaultWork = dao.loadObject(Category.class, GenericCategoryEnum.Homework.getCategory().getId());
//		categoryDefaultPersonal = dao.loadObject(Category.class, GenericCategoryEnum.Personal.getCategory().getId());
		calendarDefaultPublic = calendarService.getCalendarById(GenericCalendarEnum.School.getCalendar().getId());

	}
	
	protected void assertTableRowChecks()
	{
		super.assertTableRowChecks();

	}
	
	protected void assertValidation(ValidationException exception, ValidationCodeEnum validationCodeEnum)
	{
		Assert.assertTrue(exception.getValidationErrors().contains(validationCodeEnum));
	}
	
//	protected SubTask createSubTask(Task parent, Task child)
//	{
//		SubTask subTask = new SubTask();
//		subTask.setChildTask(child);
//		subTask.setParentTask(parent);
//		taskService.createSubTaskTX(subTask);
//		
//		return subTask;
//	}
	
	protected Task createTask(Profile profile)
	{
		return createTask(profile, null);
	}
	
	protected Task createTask(Profile profile, String taskTitle)
	{
		Task task = RMTestModelBuilder.task(profile);
		if (taskTitle!=null)
			task.setTitle(taskTitle);
		try
		{
			taskService.createTask(task);
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
		}
		 
		 return task;
	}
	
	protected Task createTask(Task task)
	{
		try
		{
			taskService.createTask(task);
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
		}
		 
		 return task;
	}
	
	protected Task createTaskAndProfile()
	{
		Profile profile = createProfile();
		return createTask(profile);
	}
	
	protected Share createShare(Task task, Profile profile)
	{
		try
		{
			shareService.shareTask(RMTestModelBuilder.invite(task, profile));
			return shareService.getShareForUserAndTask(task, profile.getMember());
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
			return null;
		}
	} 
	
//	return shareService.getShareForUserAndTaskTX(task, profile.getMember());
	
	protected void createShare(Task task, Map<String, String>emailAddresses)
	{
		try
		{
			shareService.shareTask(RMTestModelBuilder.invite(task, emailAddresses));
			
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
			return;
		}
	} 
	
	protected void createCalendarShare(Calendar calendar, Map<String,String>emailAddresses)
	{
		try
		{
			shareService.shareCalender(RMTestModelBuilder.invite(calendar, emailAddresses));
			
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
			return;
		}
	}
	
	
	protected ShareCalendar createCalendarShare(Calendar calendar, Profile profile)
	{
		try
		{
			shareService.shareCalender(RMTestModelBuilder.invite(calendar, profile));
			return shareService.getShareCalendarByCalendarAndMember(calendar, profile.getMember());
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
			return null;
		}
	}
	
	protected Calendar createCalendar(Profile profile)
	{
		try
		{
			Calendar calendar = RMTestModelBuilder.calendar(profile.getMember());
			calendarService.createCalendar(calendar);
			return calendar;
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
			return null;
		}
	}
	
	
	protected Alarm createAlarm(Task task, DateTime alarmTime)
	{
		try
		{
			Alarm alarm = RMTestModelBuilder.alarm(task, alarmTime);
			alarm = reminderService.createAlarmTX(alarm);
			return alarm;
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
			return null;
		}
	}
	
	protected Alarm createAlarm(Task task, Member member, DateTime alarmTime)
	{
		try
		{
			Alarm alarm = RMTestModelBuilder.alarm(task, member, alarmTime);
			alarm = reminderService.createAlarmTX(alarm);
			return alarm;
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
			return null;
		}
	}
	
	protected Device createDevice(Profile profile, PushTypeEnum pushTypeEnum)
	{
		Device device = new Device();
		device.generateGUIDKey();
		device.setProfile(profile);
		device.setPushTypeOrdinal(pushTypeEnum.ordinal());
		try
		{
			return clientNotificationService.createDevice(device);
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
			return null;
		}
	}
}
