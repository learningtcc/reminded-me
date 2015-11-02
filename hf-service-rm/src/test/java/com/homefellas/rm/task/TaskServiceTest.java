package com.homefellas.rm.task;

import static com.homefellas.rm.RMTestModelBuilder.buildCalendar;
import static com.homefellas.rm.RMTestModelBuilder.buildInvite;
import static com.homefellas.rm.RMTestModelBuilder.task;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.junit.Test;

import com.homefellas.batch.Notification;
import com.homefellas.batch.NotificationService;
import com.homefellas.exception.ValidationException;
import com.homefellas.rm.AbstractRMTestDao;
import com.homefellas.rm.RMTestModelBuilder;
import com.homefellas.rm.ValidationCodeEnum;
import com.homefellas.rm.calendar.Calendar;
import com.homefellas.rm.calendar.CalendarService;
import com.homefellas.rm.reminder.RepeatOccurance;
import com.homefellas.rm.repeatsetup.RepeatSetup;
import com.homefellas.rm.repeatsetup.RepeatSetup.RepeatSetupStatus;
import com.homefellas.rm.repeatsetup.RepeatSetupService;
import com.homefellas.rm.repeatsetup.TaskTemplate;
import com.homefellas.rm.share.Invite;
import com.homefellas.rm.share.Share;
import com.homefellas.rm.share.Share.ShareStatus;
import com.homefellas.rm.share.ShareApprovedStatus;
import com.homefellas.rm.share.ShareService;
import com.homefellas.rm.task.AbstractTask.ProgressEnum;
import com.homefellas.user.Member;
import com.homefellas.user.Profile;



public class TaskServiceTest extends AbstractRMTestDao {

	@Resource(name="calendarService")
	private CalendarService calendarService;
	
	@Resource(name="taskService")
	private TaskService taskService;
	
	@Resource(name="shareService")
	private ShareService shareService;
	
	@Resource(name="notificationService")
	private NotificationService notificationService;
	
	@Resource(name="repeatSetupService")
	private RepeatSetupService repeatSetupService;
//	
//	@Test
//	public void getAllDependantTaskObjects()
//	{
//		Profile owner = createProfile();
//		
//		Calendar calendar = createCalendar(owner);
//		Task task1 = task(owner);
//		task1.setEndTime(new DateTime().plusDays(1));
//		task1.addCalendar(calendar);
//		task1 = createTask(task1);
//	
//		Profile sharee1 = createGuest("sharee1@reminded.me");
//		Profile sharee2 = createGuest("guest1reminded.me");
//		
//		Share share1 = createShare(task1, sharee1);
//		Share share2 = createShare(task1, sharee2);
//		
//		List<Share> shares = shareService.getSharesForTaskTX(task1);
//		Assert.assertTrue(shares.contains(share1));
//		Assert.assertTrue(shares.contains(share2));
//		
//		Alarm alarm1 = createAlarm(task1, task1.getEndTime().minusMinutes(5));
//		Alarm alarm2 = createAlarm(task1, task1.getEndTime().minusHours(2));
//		
////		dao.refresh(task1);
//
//		
////		Task task = dao.loadByPrimaryKey(Task.class, task1.getId());
////		Assert.assertTrue(task.getShares().contains(share1));
////		Assert.assertTrue(task.getShares().contains(share2));
////		
////		Assert.assertTrue(task.getAlarms().contains(alarm1));
////		Assert.assertTrue(task.getAlarms().contains(alarm2));
//		try
//		{
//			List<Task> tasks = taskService.getUpcomingTasksTX(owner.getEmail(), 10, 0);
//			
//			Assert.assertTrue(tasks.contains(task1));
//			
//			Task taskUnderTest = tasks.get(0);
////			Assert.assertTrue(taskUnderTest.getShares().contains(share1));
////			Assert.assertTrue(taskUnderTest.getShares().contains(share2));
//			
//			Assert.assertTrue(taskUnderTest.getAlarms().contains(alarm1));
//			Assert.assertTrue(taskUnderTest.getAlarms().contains(alarm2));
//		}
//		catch (ValidationException e)
//		{
//			Assert.fail(e.getMessage());
//		}
//	}
	
	
	@Test
	public void getTodayTasksForMember()
	{
		
		Profile profile1 = createProfile();
		Task task1 = createTask(profile1);
		
		Task task2 = task(profile1);
		task2.setEndTime(new DateTime().plusMinutes(22));
		task2.setProgress(ProgressEnum.DELETE.ordinal());
		task2 = createTask(task2);
		
		Task task3 = task(profile1);
		task3.setEndTime(new DateTime().plusMinutes(22));
		task3 = createTask(task3);
		
		Task task4 = task(profile1);
		task4.setEndTime(new DateTime().withHourOfDay(11));
		task4 = createTask(task4);
		
		Task task5 = task(profile1);
		task5.setEndTime(new DateTime().plusDays(1));
		task5 = createTask(task5);
		
		Task task6 = task(profile1);
		task6.setEndTime(new DateTime().minusDays(1));
		task6 = createTask(task6);
		
		
		DateTime startRangeDateTime = new DateTime(DateMidnight.now().getMillis());
		DateTime endRangeDateTime = new DateTime(DateMidnight.now().getMillis()).plusDays(1).minus(1);
		
		Assert.assertTrue(task3.getEndTime().isAfter(startRangeDateTime));
		Assert.assertTrue(task3.getEndTime().isBefore(endRangeDateTime));
		Assert.assertTrue(task4.getEndTime().isAfter(startRangeDateTime));
		Assert.assertTrue(task4.getEndTime().isBefore(endRangeDateTime));
		
		try
		{
			List<Task> tasks = taskService.getTodayTasks(0, 0, profile1.getEmail(), 10, 0);
			
			Assert.assertTrue(tasks.contains(task3));
			Assert.assertTrue(tasks.contains(task4));
			Assert.assertFalse(tasks.contains(task1));
			Assert.assertFalse(tasks.contains(task2));
			Assert.assertFalse(tasks.contains(task5));
			Assert.assertFalse(tasks.contains(task6));
			
			tasks = taskService.getTodayTasks(new DateTime().minusDays(2).getMillis(),0, profile1.getEmail(), 10, 0);
			
			Assert.assertTrue(tasks.contains(task3));
			Assert.assertTrue(tasks.contains(task4));
			Assert.assertTrue(tasks.contains(task6));
			Assert.assertFalse(tasks.contains(task1));
			Assert.assertFalse(tasks.contains(task2));
			Assert.assertFalse(tasks.contains(task5));
			
			tasks = taskService.getTodayTasks(new DateTime().minusDays(2).getMillis(),new DateTime().plusDays(2).getMillis(), profile1.getEmail(), 10, 0);
			Assert.assertTrue(tasks.contains(task3));
			Assert.assertTrue(tasks.contains(task4));
			Assert.assertTrue(tasks.contains(task5));
			Assert.assertTrue(tasks.contains(task6));
			Assert.assertFalse(tasks.contains(task1));
			Assert.assertFalse(tasks.contains(task2));
			
			tasks = taskService.getTodayTasks(0,new DateTime().plusDays(2).getMillis(), profile1.getEmail(), 10, 0);
			Assert.assertTrue(tasks.contains(task3));
			Assert.assertTrue(tasks.contains(task4));
			Assert.assertFalse(tasks.contains(task6));
			Assert.assertFalse(tasks.contains(task1));
			Assert.assertFalse(tasks.contains(task2));
			Assert.assertTrue(tasks.contains(task5));
			
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void defaultCalendars()
	{
		Profile profile1 = createProfile();
		
		Collection<Calendar> calendars = calendarService.getAllCalendarsForMember(profile1.getMember());
		for (Calendar calendar:calendars)
		{
			System.out.println(calendar.getTitle()+" "+calendar.isGeneric());
		}
		
	}
	
//		@Test
//		public void calendarStore()
//		{
//			List<CalendarStoreCategory> calendarStoreCategories = calendarService.getCalendarStoreCategories();
//			
//			for (CalendarStoreCategory calendarStoreCategory : calendarStoreCategories)
//			{
//				Assert.assertTrue(calendarStoreCategory.getCalendarStoreSubCategories() != null && !calendarStoreCategory.getCalendarStoreSubCategories().isEmpty());
//			}
//			
//			Profile profile = createProfile();
//	//		CalendarStoreSubCategory calendarStoreSubCategory = dao.loadByPrimaryKey(CalendarStoreSubCategory.class, DefaultCalendarStoreSubCategoryEnum.bjj.ordinal());
//			CalendarStoreSubCategory calendarStoreSubCategory = calendarService.getCalendarStoreSubCategoryById(String.valueOf(DefaultCalendarStoreSubCategoryEnum.bjj.ordinal()));
//			List<CalendarStoreUserDefinedCategory> calendarStoreUserDefinedCategories = calendarService.getCalendarStoreUserDefiedCategories(calendarStoreSubCategory.getId(), profile.getEmail());
//	//		CalendarStoreUserDefinedCategory calendarStoreUserDefinedCategory = dao.loadByPrimaryKey(CalendarStoreUserDefinedCategory.class, DefaultCalendarStoreUserDefinedCategoryEnum.delawareBJJ.getId());	
//			CalendarStoreUserDefinedCategory calendarStoreUserDefinedCategory = calendarService.getCalendarStoreUserDefinedCategoryById(String.valueOf(DefaultCalendarStoreUserDefinedCategoryEnum.delawareBJJ.getId()));
//					
//			Assert.assertTrue(calendarStoreUserDefinedCategories.contains(calendarStoreUserDefinedCategory));
//			
//			CalendarStoreUserDefinedCategory newCalendarStoreUserDefinedCategory = new CalendarStoreUserDefinedCategory();
//			newCalendarStoreUserDefinedCategory.setMember(profile.getMember());
//			newCalendarStoreUserDefinedCategory.setCalendarStoreSubCategory(calendarStoreSubCategory);
//			newCalendarStoreUserDefinedCategory.setCategoryName("Paterson BJJ");
//			newCalendarStoreUserDefinedCategory.generateGUIDKey();
//			try
//			{
//				newCalendarStoreUserDefinedCategory = calendarService.createCalendarStoreUserDefinedCategory(newCalendarStoreUserDefinedCategory);
//				
//				calendarStoreUserDefinedCategories = calendarService.getCalendarStoreUserDefiedCategories(calendarStoreSubCategory.getId(), profile.getEmail());
//				Assert.assertTrue(calendarStoreUserDefinedCategories.contains(calendarStoreUserDefinedCategory));
//				Assert.assertTrue(calendarStoreUserDefinedCategories.contains(newCalendarStoreUserDefinedCategory));
//			}
//			catch (ValidationException e)
//			{
//				Assert.fail(e.getMessage());
//			}
//		}
	
	@Test
	public void taskWho()
	{
		Profile profile = createProfile();
		Profile sharee1 = createProfile("sharee1@reminded.me");
		Profile sharee2 = createProfile("sharee2@reminded.me");
		Task task = createTask(profile);
		
		Share share = createShare(task, sharee1);
		Share share2 = createShare(task, sharee2);
		
		try
		{
			Task taskUnderTest = taskService.getTaskByIDAndProcessShares(task.getId(), profile.getEmail());
			
			Assert.assertTrue(taskUnderTest.getWho().contains(sharee1.getName()));
			Assert.assertTrue(taskUnderTest.getWho().contains(sharee2.getName()));
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
		}
		
		
	}
	
	
//	@Test
//	public void cancelSubTasks()
//	{
//		Profile profile = createProfile();
//		Task parentTask = createTask(profile);
//		Task subtask1 = createTask(profile);
//		createSubTask(parentTask, subtask1);
//		
//		Task subtask2 = createTask(profile);
//		createSubTask(parentTask, subtask2);
//		
//		Task subtask3 = createTask(profile);
//		createSubTask(parentTask, subtask3);
//		
//		try
//		{
//			taskService.cancelTaskTX(parentTask, profile.getEmail());
//			
//			Assert.assertEquals(ProgressEnum.DELETE.ordinal(), dao.loadByPrimaryKey(Task.class, parentTask.getId()).getProgress());
//			Assert.assertEquals(ProgressEnum.DELETE.ordinal(), dao.loadByPrimaryKey(Task.class, subtask1.getId()).getProgress());
//			Assert.assertEquals(ProgressEnum.DELETE.ordinal(), dao.loadByPrimaryKey(Task.class, subtask2.getId()).getProgress());
//			Assert.assertEquals(ProgressEnum.DELETE.ordinal(), dao.loadByPrimaryKey(Task.class, subtask3.getId()).getProgress());
//		}
//		catch (ValidationException e)
//		{
//			Assert.fail(e.getMessage());
//		}
//		
//	}
//	
//	@Test
//	public void completeSubTasks()
//	{
//		Profile profile = createProfile();
//		Task parentTask = createTask(profile);
//		Task subtask1 = createTask(profile);
//		createSubTask(parentTask, subtask1);
//		
//		Task subtask2 = createTask(profile);
//		createSubTask(parentTask, subtask2);
//		
//		Task subtask3 = createTask(profile);
//		createSubTask(parentTask, subtask3);
//		
//		parentTask.setProgress(ProgressEnum.DONE.ordinal());
//		try
//		{
//			taskService.updateTaskTX(parentTask, profile.getEmail());
//			
//			Assert.assertEquals(ProgressEnum.DONE.ordinal(), dao.loadByPrimaryKey(Task.class, parentTask.getId()).getProgress());
//			Assert.assertEquals(ProgressEnum.DONE.ordinal(), dao.loadByPrimaryKey(Task.class, subtask1.getId()).getProgress());
//			Assert.assertEquals(ProgressEnum.DONE.ordinal(), dao.loadByPrimaryKey(Task.class, subtask2.getId()).getProgress());
//			Assert.assertEquals(ProgressEnum.DONE.ordinal(), dao.loadByPrimaryKey(Task.class, subtask3.getId()).getProgress());
//		}
//		catch (ValidationException e)
//		{
//			Assert.fail(e.getMessage());
//		}
//		
//	}
	
	@Test
	public void taskTimeReached()
	{
		Profile profile = createProfile();
		Task task = RMTestModelBuilder.task(profile);
		task.setStartTime(new DateTime().plusHours(1));
		task.setEndTime(new DateTime().plusHours(2));
		
		createTask(task);
		
		List<Notification> notifications = notificationService.getNotificationsForINotificationAndToEmailTX(task, profile.getEmail());
		Assert.assertEquals(task.getStartTime().getMillis(), notifications.get(0).getToSendTime());
		
		task = RMTestModelBuilder.task(profile);
		task.setEndTime(new DateTime().plusHours(2));
		
		createTask(task);
		
		notifications = notificationService.getNotificationsForINotificationAndToEmailTX(task, profile.getEmail());
		Assert.assertEquals(task.getEndTime().getMillis(), notifications.get(0).getToSendTime());
	}
	
	@Test
	public void updateRepeatSetupTemplateHibernateException()
	{
		Profile profile = createProfile();
		Task task = createTask(profile);
		
		RepeatSetup repeatSetup = new RepeatSetup();
		repeatSetup.generateGUIDKey();
		repeatSetup.setRepeatOccurance(RepeatOccurance.day.name());
		repeatSetup.setRepeatPeriod(1);
		repeatSetup.setMonthlyrule("monthday");
		repeatSetup.setClonedTask(new TaskTemplate(task.getId()));
		repeatSetup.setMember(profile.getMember());
		
		try
		{
			repeatSetupService.createRepeatSetup(repeatSetup);
			
			repeatSetup.setClonedTask(new TaskTemplate(task.getId()));
			repeatSetup.setRepeatPeriod(100);
			repeatSetupService.updateRepeatSetup(repeatSetup, profile.getEmail());
			
			Assert.assertEquals(100, repeatSetupService.getRepeatSetupById(repeatSetup.getId()).getRepeatPeriod());
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
		}
		
	}
	
	
	@Test
	public void getTasksByStatusAndMemberIdTX()
	{
		Profile profile = createProfile();
		Task task1 = task(profile);
//		buildSampleTask(true, null, null, false, null, profile, null, PriorityEnum.LOW, null, null);
		task1.setTitle("task1");
		task1.setProgress(ProgressEnum.ALMOST_THERE.ordinal());
		createTask(task1);
		
		Task task2 = task(profile);
//		Task task2 = buildSampleTask(true, null, null, false, null, profile, null, PriorityEnum.LOW, null, null);
		task2.setTitle("task1");
		task2.setProgress(ProgressEnum.DONE.ordinal());
		createTask(task2);
		
		Task task3 = task(profile);
//		Task task3 = buildSampleTask(true, null, null, false, null, profile, null, PriorityEnum.LOW, null, null);
		task3.setTitle("task1");
		task3.setProgress(ProgressEnum.DELETE.ordinal());
		createTask(task3);
		
		Task task4 = task(profile);
//		Task task4 = buildSampleTask(true, null, null, false, null, profile, null, PriorityEnum.LOW, null, null);
		task4.setTitle("task1");
		task4.setProgress(ProgressEnum.ALMOST_THERE.ordinal());
		createTask(task4);
		
		List<Task> tasks = taskService.getTasksByStatusAndMemberId(profile.getId(),ProgressEnum.ALMOST_THERE.ordinal());
		Assert.assertTrue(tasks.contains(task1));
		Assert.assertFalse(tasks.contains(task2));
		Assert.assertFalse(tasks.contains(task3));
		Assert.assertTrue(tasks.contains(task4));
		
	}
	
	@Test
	public void repeatSetupLifeCycle()
	{
		Member memeber1 = createProfile().getMember();
		
		RepeatSetup repeatSetup = new RepeatSetup();
		repeatSetup.setRepeatOccurance(RepeatOccurance.day.toString());
		repeatSetup.setRepeatPeriod(1);
		repeatSetup.generateGUIDKey();
		repeatSetup.setMember(memeber1);
		//set bogus id
		repeatSetup.setClonedTask(new TaskTemplate(UUID.randomUUID().toString()));
		try
		{
			repeatSetup = repeatSetupService.createRepeatSetup(repeatSetup);
			Assert.fail();
		}
		catch (ValidationException exception)
		{
			assertValidation(exception, ValidationCodeEnum.CLONED_TASK_ID_IS_NULL_OR_NOT_FOUND);
		}
		
		Profile profile = createProfile();
		Task task1 = createTask(profile);
		
		repeatSetup.setClonedTask(new TaskTemplate(task1.getId()));
		RepeatSetup classUnderTest = null;
		try
		{
			repeatSetup = repeatSetupService.createRepeatSetup(repeatSetup);
//			dao.flush();
			
			
			
			task1.setRepeatSetup(repeatSetup);
			taskService.updateTaskWithRepeatSetup(task1, profile.getEmail());
			
			assertRowCount(1, new RepeatSetup());
			assertRowCount(1, new TaskTemplate());
			
//			classUnderTest = dao.loadByPrimaryKey(RepeatSetup.class, repeatSetup.getId());
			classUnderTest = repeatSetupService.getRepeatSetupById(repeatSetup.getId());
			Assert.assertNotNull(classUnderTest);
			Assert.assertNotNull(repeatSetupService.getTaskTemplateByTaskId(task1.getId()));
			Assert.assertEquals(task1.getId(), classUnderTest.getClonedTask().getOrginalTaskId());
		}
		catch (ValidationException exception)
		{
			Assert.fail(exception.getMessage());
		}
		
		//now let's try to update
		try
		{
			repeatSetup.setModifiedDateZone(new DateTime());
			repeatSetup.setRepeatOccurance(RepeatOccurance.hour.toString());
			//try bad email so no update
			repeatSetupService.updateRepeatSetup(repeatSetup, memeber1.getEmail());
			
			classUnderTest = repeatSetupService.getRepeatSetupById(repeatSetup.getId());
			Assert.assertEquals(RepeatOccurance.hour.toString(), classUnderTest.getRepeatOccurance());
			
			classUnderTest = repeatSetupService.getRepeatSetupById(repeatSetup.getId());
			Assert.assertNotNull(classUnderTest);
			Assert.assertNotNull(repeatSetupService.getTaskTemplateByTaskId(task1.getId()));
			
			
		}
		catch (ValidationException exception)
		{
			Assert.fail(exception.getMessage());
		}
		
		task1.setTitle("new title");
		try
		{
			taskService.updateTaskTitle(task1, profile.getEmail());
			
			TaskTemplate taskTemplate = repeatSetupService.getTaskTemplateByTaskId(task1.getId());
			Assert.assertEquals("new title", taskTemplate.getTitle());
		}
		catch (ValidationException exception)
		{
			Assert.fail(exception.getMessage());
		}
		
		//final delete
		repeatSetupService.deleteRepeatSetup(repeatSetup, memeber1.getEmail());
			
		classUnderTest = repeatSetupService.getRepeatSetupById(repeatSetup.getId());
		Assert.assertEquals(RepeatSetupStatus.DELETE.ordinal(), classUnderTest.getRepeatSetupStatusOrdinal());
		//Assert.assertNull(dao.loadByPrimaryKey(TaskTemplate.class, task1.getId()));
		
		
		
	}
	
	@Test 
	public void createTaskWithInvalidCalendarId()
	{
		Profile profile3Guest1 = createGuest();
		
//		Task task = buildSampleTask(true, null, null, false, null, profile3Guest1, null, PriorityEnum.LOW, null, new DateTime().plusHours(1));
		Task task = task(profile3Guest1);
		task.setEndTime(new DateTime().plusHours(1));
		task.setTitle("Task");
		
		Calendar calendar = new Calendar();
		calendar.generateGUIDKey();
		
		task.addCalendar(calendar);
		
		try
		{
			taskService.createTask(task);
			
			taskService.getTaskForCalendar(calendar);
			
			Assert.fail();
		}
		catch (ValidationException exception)
		{
			Assert.assertTrue(exception.getValidationErrors().contains(ValidationCodeEnum.CALENDAR_NOT_FOUND));
		}
	}
	
	@Test
	public void reupNotifications()
	{
		Profile profile3Guest1 = createGuest();
		
		Task task = task(profile3Guest1);
		task.setEndTime( new DateTime().plusHours(1));
//		Task task = buildSampleTask(true, null, null, false, null, profile3Guest1, null, PriorityEnum.LOW, null, new DateTime().plusHours(1));
		task.setTitle("Task");
		
		try {taskService.createTask(task);}catch (ValidationException e) {}
		
		RepeatSetup repeatSetup = new RepeatSetup();
		repeatSetup.setRepeatOccurance(RepeatOccurance.day.toString());
		repeatSetup.setStartsRepeatingOn(task.getEndTime());
		repeatSetup.setClonedTask(new TaskTemplate(task.getId()));
//		repeatSetup.setClondedTaskAsTask(task);
		repeatSetup.generateGUIDKey();
		repeatSetup.setMember(profile3Guest1.getMember());
		try
		{
			repeatSetupService.createRepeatSetup(repeatSetup);
		}
		catch (ValidationException e1)
		{
			Assert.fail(e1.getMessage());
		}
//		dao.save(repeatSetup);
		
		task.setRepeatSetup(repeatSetup);
		try{taskService.updateTaskWithRepeatSetup(task, profile3Guest1.getEmail());}catch (ValidationException e) {}
		
		Task nextTask = task(profile3Guest1);
		task.setEndTime(task.getEndTime().plusDays(1));
//		Task nextTask = buildSampleTask(true, null, null, false, null, profile3Guest1, null, PriorityEnum.LOW, null, task.getEndTime().plusDays(1));
		nextTask.setTitle("nextTask");
		nextTask.setRepeatSetup(repeatSetup);
		
		
		try {taskService.createTask(nextTask);}catch (ValidationException e) {}
		Assert.assertFalse(task.isCallBack());
		
		List<Notification> notifications = notificationService.getNotificationsForINotificationTX(task);
		List<Notification> nextnotifications = notificationService.getNotificationsForINotificationTX(nextTask);
		Assert.assertTrue(notifications.size()==1);
//		Assert.assertTrue(nextnotifications.size()==1);
		
//		taskService.reupNotifications(task);
		
//		notifications = notificationService.getNotificationsForINotificationTX(task);
//		nextnotifications = notificationService.getNotificationsForINotificationTX(nextTask);
//		Assert.assertTrue(notifications.size()==1);
//		Assert.assertTrue(nextnotifications.size()==1);
//		Assert.assertEquals(nextTask.getEndTime().getMillis(), nextnotifications.get(0).getToSendTime());
	}
	
	@Test
	public void updateTaskModifyCalendars()
	{
//		Member memeber1 = createProfile().getMember();
		
		
		try
		{
			Profile profile = createProfile();
			Task task2 = createTask(profile);
			
			Profile profile3Guest1 = createGuest();
			Profile profile4Guest2 = createGuest();
			
			Calendar calendar = createCalendar(profile);
			
			task2.addCalendar(calendar);
			taskService.updateTaskWithCalendar(task2, profile.getEmail());
		 
			Invite invite1 = buildInvite(true, calendar.getId(), Calendar.class.getName());
			Map<String, String>emailAddresses = new HashMap<String, String>();
			emailAddresses.put(profile3Guest1.getMember().getEmail(), null);
			emailAddresses.put(profile4Guest2.getMember().getEmail(), null);
			invite1.setEmailAddresses(emailAddresses);
		
			invite1 = shareService.shareCalender(invite1);
			
			Assert.assertTrue(invite1.isPrimaryKeySet());
			Assert.assertNotNull(shareService.getCalenderShares(calendar));
			Assert.assertNotNull(shareService.getShareForUserAndTask(task2, profile3Guest1.getMember()));
			Assert.assertNotNull(shareService.getShareForUserAndTask(task2, profile4Guest2.getMember()));
		
		
			//task2.getCalendars().clear();
			Calendar calendar2 = buildCalendar(true, "test calendar 2", profile.getMember());
		
			calendarService.createCalendar(calendar2);
			
			task2.addCalendar(calendar2);
		
			taskService.updateTaskWithCalendar(task2, profile.getEmail());
			
//			Assert.assertNull(shareService.getShareForUserAndTask(task2, profile3Guest1.getMember()));
//			Assert.assertNull(shareService.getShareForUserAndTask(task2, profile4Guest2.getMember()));
			
//			Task taskUnderTest = dao.loadByPrimaryKey(Task.class, task2.getId());
			Task taskUnderTest = taskService.getTaskById(task2.getId());
			Assert.assertTrue(taskUnderTest.getCalendars().contains(calendar2));
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
//	@Test
//	public void getBulkTasksForAuthorization()
//	{
//		Profile profile1 = createProfile();
//		Task task1 = createTask(profile1, "1");
//		Task task2 = createTask(profile1, "2");
//		Task task6 = createTask(profile1, "6");
//		
//		Profile profile2 = createProfile();
//		Task task8 = createTask(profile2,"8");
//		Task task9 = createTask(profile2,"9");
//		
//		Profile profile3Guest1 = createGuest();
//		Profile profile4Guest2 = createGuest();
//		
//		Task task11 = createTask(profile3Guest1,"11");
//		
//		Share share1Profile3Task6 = createShare(task6, profile3Guest1);
//		Share share2Profile3Task9 = createShare(task9, profile3Guest1);
//		
//		//test 1 check to make sure unauthorized tasks are not present in list		
//		String passedId = task1.getId()+","+","+task8.getId()+","+task9.getId()+","+task2.getId();
//		List<Task> tasks;
//		try
//		{
//			tasks = taskService.getBulkTasksTX(passedId, profile1.getMember().getEmail());
//			taskService.getBulkTasksTX(passedId, profile1.getMember().getEmail());
//			
//			Assert.assertTrue(tasks.contains(task1));
//			Assert.assertTrue(tasks.contains(task2));
//			Assert.assertFalse(tasks.contains(task9));
//			Assert.assertFalse(tasks.contains(task8));
//			
//		}
//		catch (ValidationException exception)
//		{
//			Assert.fail(exception.getMessage());
//		}
//		
//		//test 2 make sure that task has correct share status
//		passedId = task6.getId()+","+","+task9.getId()+","+task11.getId()+","+task2.getId();
//		try
//		{
//			tasks = taskService.getBulkTasksTX(passedId, profile3Guest1.getMember().getEmail());
////			taskService.getBulkTasksTX(passedId, profile1.getMember().getEmail());
//			
//			Assert.assertTrue(tasks.contains(task6));
//			Assert.assertTrue(tasks.contains(task9));
//			Assert.assertTrue(tasks.contains(task11));
//			Assert.assertFalse(tasks.contains(task1));
//			
//			for (Task task: tasks)
//			{
//				if (task.getId().equals(task6))
//					Assert.assertEquals(share1Profile3Task6.getShareApprovedStatusOrdinal(), task.getShareStatus());
//				else if (task.getId().equals(task9))
//					Assert.assertEquals(share2Profile3Task9.getShareApprovedStatusOrdinal(), task.getShareStatus());
//				else if (task.getId().equals(task11))
//					Assert.assertEquals(ShareApprovedStatus.OWNER_TASK.ordinal(), task.getShareStatus());
//			}
//			
//			
//		}
//		catch (ValidationException exception)
//		{
//			Assert.fail(exception.getMessage());
//		}
//		
//		//test 3 make sure that unowned shares are not listed
//	
//		passedId = task9.getId()+","+","+task1.getId();
//		try
//		{
//			tasks = taskService.getBulkTasksTX(passedId, profile4Guest2.getMember().getEmail());
//			taskService.getBulkTasksTX(passedId, profile1.getMember().getEmail());
//			
//			Assert.assertTrue(tasks.isEmpty());
//			
//		}
//		catch (ValidationException exception)
//		{
//			Assert.fail(exception.getMessage());
//		}
//	}
	
	@Test
	public void cancelTaskAndCheckSharesAsMarkedForDelete()
	{
		Profile profile1 = createProfile();
		
		Profile profile2 = createProfile();
		Task task9 = createTask(profile2);
		
		Profile profile3 = createGuest();
		
		Share share2Profile3Task9 = createShare(task9, profile3);
		
		//cancel the task is the owner of hte task so it should notify the shares too that it changed
		try
		{
			taskService.cancelTask(task9, profile2.getMember().getEmail());
		}
		catch (ValidationException exception)
		{
			Assert.fail(exception.getMessage());
		}
		
		Share share = shareService.getShareById(share2Profile3Task9.getId());
		
		Assert.assertEquals(ShareStatus.DELETED.ordinal(), share.getStatus());
	}
	
	@Test
	public void cancelTaskAndCheckNotification()
	{
		Profile profile2 = createProfile();
		Task task9 = createTask(profile2);
		
		Profile profile3 = createProfile();
		
		Share share2Profile3Task9 = createShare(task9, profile3);
		
		share2Profile3Task9.setShareApprovedStatusOrdinal(ShareApprovedStatus.APPROVED.ordinal());
		try
		{
		shareService.updateShare(share2Profile3Task9);
//		dao.updateObject(share2Profile3Task9);
		
		//cancel the task is the owner of hte task so it should notify the shares too that it changed
			taskService.cancelTask(task9, profile2.getMember().getEmail());
		}
		catch (ValidationException exception)
		{
			Assert.fail(exception.getMessage());
		}
		
		
		
		List<Notification> notifications = notificationService.getNotificationsToEmailTX(profile3.getEmail());
		Assert.assertEquals(2, notifications.size());
	}
	
	@Test
	public void cancelTaskWithShare()
	{
		Profile profile1 = createProfile();
		Profile profile2 = createProfile();
		Task task9 = createTask(profile2);
		
		Profile profile3Guest1 = createGuest();

		Share share2Profile3Task9 = createShare(task9, profile3Guest1);
		Task taskUnderTest = null;
		try
		{
			taskService.cancelTask(task9, profile1.getMember().getEmail());
		
		
		taskUnderTest = taskService.getTaskById(task9.getId());
		//check to make sure status is unchanged
		Assert.assertEquals(task9.getProgress(), taskUnderTest.getProgress());
		Assert.assertNotNull(shareService.getShareById(share2Profile3Task9.getId()));
		
		}
		catch (ValidationException exception)
		{
			Assert.fail(exception.getMessage());
		}
		try
		{
			taskService.cancelTask(task9, profile3Guest1.getMember().getEmail());
		}
		catch (ValidationException exception)
		{
			Assert.fail(exception.getMessage());
		}
		
		//grab the task
		try {
			taskUnderTest = taskService.getTaskById(task9.getId());
		
		
		//check to make sure status is unchanged
		Assert.assertEquals(task9.getProgress(), taskUnderTest.getProgress());
		//make sure task is null
		Assert.assertEquals(ShareStatus.DELETED.ordinal(), shareService.getShareById(share2Profile3Task9.getId()).getStatus());
		} catch (ValidationException e) {
			Assert.fail(e.getMessage());
		}
	}
	
//	@Test
//	public void bulkTaskSpeedTest()
//	{
//		Profile profile1 = createProfile();
//		Task task1 = createTask(profile1);
//		Task task2 = createTask(profile1);
//		Task task3 = createTask(profile1);
//		Task task4 = createTask(profile1);
//		Task task5 = createTask(profile1);
//		Task task6 = createTask(profile1);
//		Task task7 = createTask(profile1);
//		
//		Profile profile2 = createProfile();
//		Task task8 = createTask(profile2);
//		Task task9 = createTask(profile2);
//		
//		String ids = task1.getId()+","+task2.getId()+","+task3.getId()+","+task4.getId()+","+task5.getId()+","+task6.getId()+","+task7.getId()+","+task8.getId()+","+task9.getId();
//		
//		List<Task> tasks;
//		try
//		{
//			tasks = taskService.getBulkTasksTX(ids, task1.getTaskCreator().getMember().getEmail());
//			Assert.assertTrue(tasks.contains(task1));
//			Assert.assertTrue(tasks.contains(task2));
//			Assert.assertTrue(tasks.contains(task3));
//			Assert.assertTrue(tasks.contains(task4));
//			Assert.assertTrue(tasks.contains(task5));
//			Assert.assertTrue(tasks.contains(task6));
//			Assert.assertTrue(tasks.contains(task7));
//			Assert.assertFalse(tasks.contains(task8));
//			Assert.assertFalse(tasks.contains(task9));
//		}
//		catch (ValidationException e)
//		{
//			Assert.fail(e.getMessage());
//		}
//		
//	}
	
	@Test 
	public void createTaskVerifyEndDateNotification()
	{
		Profile profile1 = createProfile();
		
		//test 1 make sure future date isn't picked up
		Task task = task(profile1);
		task.setEndTime(new DateTime().plusWeeks(1));
//		Task task = buildSampleTask(true, null, null, true, null, profile1, null, PriorityEnum.MEDIUM, null, new DateTime().plusWeeks(1));
		
		try
		{
			taskService.createTask(task);
//			dao.flush();
//			taskCounter++;
//			notificationCounter++;
			
			Assert.assertTrue(task.isPrimaryKeySet());
//			assertTableRowChecks();
			
			
		}
		catch (ValidationException validationException)
		{
			Assert.fail("No validation exception should be thrown");
		}
		
		List<Notification> notifications = notificationService.getNotificationsToBeSentTX(new DateTime().plusSeconds(60).getMillis(), 100);
		for (Notification notification:notifications)
		{
			if (notification.getiNotificationId().equals(task.getId()))
			{
				Assert.fail("should not find this alert in this list");
			}
		}
		
		//test 2 make sure past date is picked up
//		Task task2 = buildSampleTask(true, null, null, true, null, profile1, null, PriorityEnum.MEDIUM, new DateTime().minusWeeks(2), new DateTime().plusSeconds(20));
		Task task2 = task(profile1);
		task2.setStartTime(new DateTime().minusWeeks(2));
		task2.setEndTime( new DateTime().plusSeconds(20));
		try
		{
			taskService.createTask(task2);
//			dao.flush();
//			taskCounter++;
//			notificationCounter++;
			
			Assert.assertTrue(task2.isPrimaryKeySet());
//			assertTableRowChecks();
			
			
		}
		catch (ValidationException validationException)
		{
			Assert.fail("No validation exception should be thrown");
		}
		
		notifications = notificationService.getNotificationsToBeSentTX(new DateTime().plusSeconds(60).getMillis(), 100);
		boolean found=false;
		for (Notification notification:notifications)
		{
			if (notification.getiNotificationId().equals(task2.getId()))
			{
				found=true;
			}
		}
		
		Assert.assertTrue(found);
		
		//test 3 nothing should be found on null end date
//		Task task3 = buildSampleTask(true, null, null, true, null, profile1, null, PriorityEnum.MEDIUM, new DateTime(), n	ull);
		Task task3 = task(profile1);
		task3.setStartTime(new DateTime());
		try
		{
			taskService.createTask(task3);
//			dao.flush();
//			taskCounter++;
			
			Assert.assertTrue(task3.isPrimaryKeySet());
//			assertTableRowChecks();
			
			
		}
		catch (ValidationException validationException)
		{
			Assert.fail("No validation exception should be thrown");
		}
		
		notifications = notificationService.getNotificationsToBeSentTX(new DateTime().plusSeconds(60).getMillis(), 100);
		for (Notification notification:notifications)
		{
			if (notification.getiNotificationId().equals(task3.getId()))
			{
				Assert.fail("should not find this alert in this list");
			}
		}
	}
	
	
//	@Test
//	public void getBulkTasks()
//	{
//		Profile profile1 = createProfile();
//		Task task1 = createTask(profile1);
//		Task task2 = createTask(profile1);
//		Task task3 = createTask(profile1);
//		Task task4 = createTask(profile1);
//		Task task5 = createTask(profile1);
//		Task task6 = createTask(profile1);
//		Task task7 = createTask(profile1);
//		
//		String passedId = task1.getId()+","+","+task2.getId()+","+task3.getId()+","+task4.getId()+","+task5.getId()+","+task6.getId()+","+task7.getId();
//		List<Task> tasks;
//		try
//		{
//			tasks = taskService.getBulkTasksTX(passedId, task1.getTaskCreator().getMember().getEmail());
//			
//			Assert.assertTrue(tasks.contains(task1));
//			Assert.assertTrue(tasks.contains(task2));
//			Assert.assertTrue(tasks.contains(task3));
//			Assert.assertTrue(tasks.contains(task4));
//			Assert.assertTrue(tasks.contains(task5));
//			Assert.assertTrue(tasks.contains(task6));
//			Assert.assertTrue(tasks.contains(task7));
//		}
//		catch (ValidationException e)
//		{
//			Assert.fail(e.getMessage());
//		}
//		
//		
//	}
	
	
	
	@Test
	public void testGetGenericCalendar()
	{
		Profile profile1 = createProfile();
		Calendar calendarUserCreated = createCalendar(profile1);
		
		List<Calendar> calendars = calendarService.getGenericCalendar();
		
		Assert.assertTrue(calendars.contains(calendarDefaultPublic));
		Assert.assertFalse(calendars.contains(calendarUserCreated));
	}
	
	@Test
	public void testGetUserDefinedCalendars()
	{
		Profile profile1 = createProfile();
		Calendar calendarUserCreated = createCalendar(profile1);
		
		List<Calendar> calendars = calendarService.getUserDefinedCalendars(profile1.getMember());
		
//		Assert.assertFalse(calendars.contains(genericCalendarPublic));
		Assert.assertTrue(calendars.contains(calendarUserCreated));
	}
	
	@Test
	public void testGetAllCalendarsForMember()
	{
		Profile profile1 = createProfile();
		Calendar calendarUserCreated = createCalendar(profile1);
		
		Collection<Calendar> calendars = calendarService.getAllCalendarsForMember(profile1.getMember());
		
		Assert.assertTrue(calendars.contains(calendarDefaultPublic));
		Assert.assertTrue(calendars.contains(calendarUserCreated));
	}
	
	
	@Test
	public void testCreateTask()
	{
		Profile profile1 = createProfile();
		
		//test 1 base test....minimum amount of data entered
//		Task task = buildSampleTask(true, null, null, true, null, profile1, null, PriorityEnum.MEDIUM, new DateTime(), null);
		Task task = task(profile1);
		task.setStartTime(new DateTime());
		try
		{
			taskService.createTask(task);
//			taskCounter++;
			
			Assert.assertTrue(task.isPrimaryKeySet());
//			assertTableRowChecks();
			
			
		}
		catch (ValidationException validationException)
		{
			Assert.fail("No validation exception should be thrown");
		}
		
		//test 2 categories unsaved user added category.  Also selected a generic category so should have two rows in the category table
//		Category categoryUserCreatedPrivate = buildSampleCategory(true, true, basicUser);
		
//		task = buildSampleTask(true, categories, null, true, null, profile1, null, PriorityEnum.MEDIUM, new DateTime(), null);
		task = task(profile1);
		task.setStartTime(new DateTime());
		try
		{
			taskService.createTask(task);
//			taskCounter++;
//			taskCategoryCounter+=2;
			
			
			
			Assert.assertTrue(task.isPrimaryKeySet());
//			assertTableRowChecks();
			
			
		}
		catch (ValidationException validationException)
		{
			System.out.println(validationException.getMessage());
			Assert.fail("No validation exception should be thrown");
		}
		
		
		task = task(profile1);
		task.setStartTime(new DateTime());
//		task = buildSampleTask(true, null, null, false, null, profile1, null, PriorityEnum.MEDIUM, (new DateTime()), null);
		try
		{
			taskService.createTask(task);
//			taskCounter++;
//			taskEditorCounter+=2;
			
			Assert.assertTrue(task.isPrimaryKeySet());
//			assertTableRowChecks();
			
			
		}
		catch (ValidationException validationException)
		{
			Assert.fail("No validation exception should be thrown");
		}
		
		//test6 viewers
//		Set<User> viewers = new HashSet<User>();
//		viewers.add(user1);
//		viewers.add(user2);
//		task = buildSampleTask(true, null, null, false, null, profile1, null, PriorityEnum.MEDIUM, (new DateTime()), null);
		task = task(profile1);
		task.setStartTime(new DateTime());
		try
		{
			taskService.createTask(task);
//			taskCounter++;
//			taskViewerCounter+=2;
			
			Assert.assertTrue(task.isPrimaryKeySet());
//			assertTableRowChecks();
			
			
		}
		catch (ValidationException validationException)
		{
			Assert.fail("No validation exception should be thrown");
		}
		
		//test 7 reminders
	
		//test 9 fully loaded
		
		
	
		//test10 nulled out calednar and categories...passing id only
		task = task(profile1);
		task.addCalendar(new Calendar(calendarDefaultPublic.getId()));
		try
		{
			taskService.createTask(task);
//			taskCounter++;
//			taskCategoryCounter++;
//			taskCalendarCounter++;
			
			Assert.assertTrue(task.isPrimaryKeySet());
//			assertTableRowChecks();
			
			
		}
		catch (ValidationException validationException)
		{
			Assert.fail("No validation exception should be thrown");
		}
	}
	
	@Test
	public void testCreateDefinedCalander()
	{
		Profile profile1 = createProfile();
		Calendar calendarUserCreatedPublic = createCalendar(profile1);
		Calendar calendarUserCreatedPrivate = createCalendar(profile1);
		
		try
		{
			calendarService.createCalendar(calendarUserCreatedPublic);
//			taskService.cre(calendarUserCreatedPublic);
			
//			List<Calendar> calendars = dao.loadAllObjects(Calendar.class);
			List<Calendar> calendars = calendarService.getAllCalendarsForMember(profile1.getMember());
			Assert.assertTrue(calendars.contains(calendarUserCreatedPrivate));
			Assert.assertTrue(calendars.contains(calendarUserCreatedPublic));
		}
		catch (ValidationException exception)
		{
			Assert.fail();
		}
	
		//fail test set reminders
		calendarUserCreatedPrivate.setMember(null);
		try
		{
			calendarService.updateCalendar(calendarUserCreatedPrivate, profile1.getEmail());
			Assert.fail();
		}
		catch (ValidationException exception)
		{
			Assert.assertTrue(exception.getValidationErrors().contains(ValidationCodeEnum.MEMBER_ID_IS_NULL));
		}
		
	}
	

	@Test
	public void testCompleteTask()
	{
		Profile profile1 = createProfile();
		Task task1 = createTask(profile1);
		
		task1.setProgress(ProgressEnum.DONE.ordinal());
		try
		{
			taskService.updateTaskProgress(task1, profile1.getEmail());
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
		}
		
//		Task taskUnderTest = (Task)dao.loadObject(Task.class, task1.getId());
		Task taskUnderTest;
		try {
			taskUnderTest = taskService.getTaskById(task1.getId());
		
		Assert.assertEquals(ProgressEnum.DONE.ordinal(), taskUnderTest.getProgress());
		} catch (ValidationException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGetTaskById()
	{
		Profile profile1 = createProfile();
		Task task1 = createTask(profile1);
		Task task2 = createTask(profile1);
		Task task3 = createTask(profile1);
		Task task4 = createTask(profile1);
		Task task5 = createTask(profile1);
		Task task6 = createTask(profile1);
		Task task7 = createTask(profile1);
		
		try {Assert.assertEquals(task1, taskService.getTaskById(task1.getId()));}catch (ValidationException exception){Assert.fail(exception.getMessage());}
		try {Assert.assertEquals(task2, taskService.getTaskById(task2.getId()));}catch (ValidationException exception){Assert.fail(exception.getMessage());}
		try {Assert.assertEquals(task3, taskService.getTaskById(task3.getId()));}catch (ValidationException exception){Assert.fail(exception.getMessage());}
		try {Assert.assertEquals(task4, taskService.getTaskById(task4.getId()));}catch (ValidationException exception){Assert.fail(exception.getMessage());}
		try {Assert.assertEquals(task5, taskService.getTaskById(task5.getId()));}catch (ValidationException exception){Assert.fail(exception.getMessage());}
		try {Assert.assertEquals(task6, taskService.getTaskById(task6.getId()));}catch (ValidationException exception){Assert.fail(exception.getMessage());}
		try {Assert.assertEquals(task7, taskService.getTaskById(task7.getId()));}catch (ValidationException exception){Assert.fail(exception.getMessage());}
	}

	

	@Test
	public void cancelTask()
	{
		Profile profile1 = createProfile();
		Task task4 = createTask(profile1);
		
		List<Notification> notifications = notificationService.getNotificationsForINotificationTX(task4);
		try
		{
			taskService.cancelTask(task4, task4.getTaskCreator().getMember().getEmail());
		}
		catch (ValidationException exception)
		{
			Assert.fail(exception.getMessage());
		}
		
//		Task taskUnderTest = (Task)dao.loadObject(Task.class, task4.getId());
		Task taskUnderTest;
		try {
			taskUnderTest = taskService.getTaskById(task4.getId());
		
		Assert.assertEquals(ProgressEnum.DELETE.ordinal(), taskUnderTest.getProgress());
		
		Calendar calendar = createCalendar(profile1);
		
		Task task = RMTestModelBuilder.task(profile1);
		task.addCalendar(calendar);
		task = createTask(task);
		
		task = taskService.getTaskById(task.getId());
		Assert.assertEquals(1, countRowsInTable("t_taskCalendars"));
//		assertRowCount(1, new Task(), "calendars");

		try
		{
			taskService.cancelTask(task, task.getTaskCreator().getMember().getEmail());
		}
		catch (ValidationException exception)
		{
			Assert.fail(exception.getMessage());
		}
		
		task = taskService.getTaskById(task.getId());
		Assert.assertEquals(0, countRowsInTable("t_taskCalendars"));
		} catch (ValidationException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	
	
	
}
