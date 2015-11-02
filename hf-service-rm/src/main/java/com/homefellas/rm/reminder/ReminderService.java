package com.homefellas.rm.reminder;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.batch.INotifiable;
import com.homefellas.batch.INotifiableCallbackService;
import com.homefellas.batch.NotificationService;
import com.homefellas.batch.NotificationTypeEnum;
import com.homefellas.exception.ValidationException;
import com.homefellas.metrics.CollectTimeMetrics;
import com.homefellas.rm.RMValidator;
import com.homefellas.rm.ValidationCodeEnum;
import com.homefellas.rm.notification.IEmailService;
import com.homefellas.rm.share.ShareService;
import com.homefellas.rm.task.Task;
import com.homefellas.rm.task.TaskService;
import com.homefellas.service.core.AbstractService;
import com.homefellas.user.Member;
import com.homefellas.user.UserValidationCodeEnum;

public class ReminderService extends AbstractService implements INotifiableCallbackService
{
	@Autowired
	private IReminderDao reminderDao;
	
	@Autowired
	private RMValidator reminderValidator;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private IEmailService emailService;
	
	@Autowired
	private ShareService shareService;
	
	int defaulltNotificationQueueSize=2;

	public void setDefaulltNotificationQueueSize(int defaulltNotificationQueueSize)
	{
		this.defaulltNotificationQueueSize = defaulltNotificationQueueSize;
	}
	
	
	/*
	 * ***********************************************
	 * *          Start Business Methods
	 * ***********************************************
	 */

	@Transactional
	public void scheduleAlarm(Alarm alarm)
	{
		if (alarm.getNotificationType()== NotificationTypeEnum.NONE.ordinal())
			return;
		
		if (alarm.getAlarmTime().isBeforeNow())
			return;
			
		emailService.sendAlarm(alarm);
		
	}
		
	@Override
	public INotifiable reupNotifications(INotifiable notifiable)
	{
		return notifiable;
	}

	@Transactional
	public void unScheduleAlarm(Alarm alarm)
	{
		notificationService.cancelNotification(alarm);
	}
	
	@Transactional
	public Alarm getAlarmByTaskMemberAndTime(Task task, Member member, DateTime fireTime)
	{
		return reminderDao.getAlarmByTaskMemberAndTime(task, member, fireTime);
	}
	
	@Transactional
	@CollectTimeMetrics
	public List<Alarm> getAlarmdsyTaskTX(Task task)
	{
		return getAlarmdsyTask(task);
	}
	
	@Transactional
	public List<Alarm> getAlarmdsyTask(Task task)
	{
		return reminderDao.getAlarmdsyTask(task);
	}
	
	@Transactional
	@CollectTimeMetrics
	public Alarm createAlarmTX(Alarm alarm) throws ValidationException
	{
		return createAlarm(alarm, null);
	}
	
	@Transactional
	@CollectTimeMetrics
	public Alarm createAlarmTX(Alarm alarm, Member member) throws ValidationException
	{
		return createAlarm(alarm, member);
	}
	
	@Transactional
	public List<Alarm> getAlarmByTaskAndMember(Task task, Member member)
	{
		return reminderDao.getAlarmByTaskAndMember(task, member);
	}
		
	@Transactional
	public Alarm createAlarm(Alarm alarm, Member member) throws ValidationException
	{
		reminderValidator.validateAlarm(alarm);
		
		Task task = taskService.getTaskById(alarm.getTask().getId());
		if (task == null)
			throw new ValidationException(ValidationCodeEnum.TASK_NOT_FOUND);
		alarm.setTask(task);
		
		if (member == null)
			alarm.setMember(task.getTaskCreator().getMember());
		else
			alarm.setMember(member);
	
		reminderDao.createAlarm(alarm);
		
		scheduleAlarm(alarm);
		
		return alarm;
	}
	
	@Transactional
	public Alarm updateAlarm(Alarm alarm) throws ValidationException
	{
		reminderValidator.validateAlarm(alarm);
		
		Alarm alarmFromDB = reminderDao.getAlarmById(alarm.getId());
		if (alarmFromDB == null)
			throw new ValidationException(ValidationCodeEnum.ALARM_NOT_FOUND);

		Task task = taskService.getTaskById(alarm.getTask().getId());
		if (task == null)
			throw new ValidationException(ValidationCodeEnum.TASK_NOT_FOUND);
		alarm.setTask(task);
		
		//check to see if the modified date from the db is before the one being passed
		reminderValidator.validateSynchronizationUpdate(alarm, alarmFromDB);
		alarm.setModifiedDateZone(new DateTime());
		
		if (alarm.getMember()==null)
			alarm.setMember(task.getTaskCreator().getMember());
		
		shareService.processSharesForAlarms(alarm, alarmFromDB);

		//there is too much varration of the notifcations so we need to remove them all and readd them
		unScheduleAlarm(alarm);
		
		//add the reminder to the list
		reminderDao.updateAlarm(alarm);
		
		scheduleAlarm(alarm);
		
		return alarm;
	}
	
	@Transactional	
	public void deleteAlarm(String id) throws ValidationException
	{
		Alarm alarmFromDB = reminderDao.getAlarmById(id);
		if (alarmFromDB == null)
			throw new ValidationException(ValidationCodeEnum.ALARM_NOT_FOUND);
		
		//we need to mark it as inactive
		alarmFromDB.setActiveReminder(false);
		alarmFromDB.setAlarmStatus(ReminderStatusEnum.DELETED.ordinal());
		
//		
//		reminderDao.deleteReminderNotificationsByReminder(reminder);
		
		reminderDao.updateAlarm(alarmFromDB);
		
		unScheduleAlarm(alarmFromDB);
	}
	
	@Transactional
	public void deleteAlarmsForDeletedTask(Task task) throws ValidationException
	{
		List<Alarm> alarms = getAlarmdsyTask(task);
		
		for (Alarm alarm : alarms)
		{
			deleteAlarm(alarm.getId());
		}
	}
	
	@Transactional
	public List<Alarm> getBulkAlarmsTX(String alarmDelimiter, String loggedInUserEmail) throws ValidationException
	{
		if (loggedInUserEmail == null || "".equals(loggedInUserEmail.trim()))
			throw new ValidationException(UserValidationCodeEnum.USER_MUST_BE_LOGGED_IN);
		
		List<Alarm> list = new ArrayList<Alarm>(11);
		list = reminderDao.getBulkAlarms(tokenize(alarmDelimiter), loggedInUserEmail);
		
		return list;
	}
	
	/**
	 * This method desides whether or not to ignore the notification based on the parmaters passed in the reminder.  This is
	 * a helper mehod and should only be called inside the package.
	 * @param notificationTime
	 * @param reminder
	 * @return boolean
	 */
	boolean ignoreNotification(DateTime notificationTime, IRepeatSetup reminder)
	{
		if (notificationTime.isBefore(new DateTime()))
			return true;
			
		//check to see if the time has passed the ending date
		if (reminder.getEndsRepeatingOn()!=null&&notificationTime.isAfter(reminder.getEndsRepeatingOn()))
			return true;
		
		//check to see if the except times are set
		if (reminder.shouldIgnoreTime(notificationTime))
			return true;
		
		//need to check the days of the week
		if (notificationTime.getDayOfWeek()==DateTimeConstants.MONDAY && !reminder.isRepeatsOnMonday())
			return true;
		
		if (notificationTime.getDayOfWeek()==DateTimeConstants.TUESDAY && !reminder.isRepeatsOnTuesday())
			return true;
		
		if (notificationTime.getDayOfWeek()==DateTimeConstants.WEDNESDAY && !reminder.isRepeatsOnWednesday())
			return true;
		
		if (notificationTime.getDayOfWeek()==DateTimeConstants.THURSDAY && !reminder.isRepeatsOnThursday())
			return true;
		
		if (notificationTime.getDayOfWeek()==DateTimeConstants.FRIDAY && !reminder.isRepeatsOnFriday())
			return true;
		
		if (notificationTime.getDayOfWeek()==DateTimeConstants.SATURDAY && !reminder.isRepeatsOnSaturday())
			return true;
		
		if (notificationTime.getDayOfWeek()==DateTimeConstants.SUNDAY && !reminder.isRepeatsOnSunday())
			return true;
		
		
		return false;
		
	}
	
	@Transactional
	public Alarm getAlarmById(String id)
	{
		return reminderDao.getAlarmById(id);
	}
}