package com.homefellas.rm.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.batch.NotificationService;
import com.homefellas.exception.ValidationException;
import com.homefellas.metrics.CollectTimeMetrics;
import com.homefellas.rm.RMValidator;
import com.homefellas.rm.ValidationCodeEnum;
import com.homefellas.rm.calendar.Calendar;
import com.homefellas.rm.notification.IEmailService;
import com.homefellas.rm.reminder.ReminderService;
import com.homefellas.rm.repeatsetup.RepeatSetup;
import com.homefellas.rm.repeatsetup.RepeatSetupService;
import com.homefellas.rm.repeatsetup.TaskTemplate;
import com.homefellas.rm.share.Share;
import com.homefellas.rm.share.ShareApprovedStatus;
import com.homefellas.rm.share.ShareService;
import com.homefellas.rm.task.AbstractTask.ProgressEnum;
import com.homefellas.service.core.AbstractService;
import com.homefellas.user.Profile;
import com.homefellas.user.UserService;
import com.homefellas.user.UserValidationCodeEnum;

public class TaskService extends AbstractService {

	@Autowired
	private ITaskDao taskDao;
	
	@Autowired
	private RMValidator validator;
				
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private ShareService shareService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ReminderService reminderService;
	
	@Autowired
	private IEmailService emailService;
	
	@Autowired
	private RepeatSetupService repeatSetupService;
	

//	public List<Task> getTimelessTasksEnding()
//	{
//		return taskDao.getTimelessTasksEnding();
//	}
	

	
	/************************************************
	 * *          Start Business Methods
	 * ***********************************************
	 */
	
	@Transactional 
	@CollectTimeMetrics
	public List<Task> getUpcomingTasks(String loggedInUserEmail, int chunkSize, int startIndex) throws ValidationException
	{
		List<Task> tasks = taskDao.getUpcomingTasks(loggedInUserEmail, chunkSize, startIndex);
		return postTaskRetrieval(tasks, loggedInUserEmail);
	}
	
	@Transactional 
	@CollectTimeMetrics
	public List<Task> getDatelessOverdueTasks(String loggedInUserEmail, int chunkSize, int startIndex) throws ValidationException
	{
		if (loggedInUserEmail == null || "".equals(loggedInUserEmail.trim()))
			throw new ValidationException(UserValidationCodeEnum.USER_MUST_BE_LOGGED_IN);
		
		List<Task> tasks = taskDao.getDatelessOverdueTasksForMember(loggedInUserEmail, chunkSize, startIndex);
		
		return postTaskRetrieval(tasks, loggedInUserEmail);
	}
	
	
	@Transactional 
	@CollectTimeMetrics
	public List<Task> getCompletedCanceledTasks(String loggedInUserEmail, int chunkSize, int startIndex) throws ValidationException
	{
		if (loggedInUserEmail == null || "".equals(loggedInUserEmail.trim()))
			throw new ValidationException(UserValidationCodeEnum.USER_MUST_BE_LOGGED_IN);
		
		List<Task> tasks = taskDao.getCompletedCanceledTasksForMember(loggedInUserEmail, chunkSize, startIndex);
		return postTaskRetrieval(tasks, loggedInUserEmail);
	}
	
	
	@Transactional 
	@CollectTimeMetrics
	public List<Task> getTodayTasks(long startRange, long endRange, String loggedInUserEmail, int chunkSize, int startIndex) throws ValidationException
	{
		if (loggedInUserEmail == null || "".equals(loggedInUserEmail.trim()))
			throw new ValidationException(UserValidationCodeEnum.USER_MUST_BE_LOGGED_IN);
		
		DateTime startRangeDateTime;
		DateTime endRangeDateTime;
		final DateTime now = new DateTime();
		
		if (startRange == 0 && endRange == 0)
		{
			//we should use today.
			startRangeDateTime = new DateTime(DateMidnight.now().getMillis());
			endRangeDateTime = new DateTime(DateMidnight.now().getMillis()).plusDays(1).minus(1);
		}
		else if (startRange > 0 && endRange == 0)
		{
			//check to see if start is before now
			if (startRange > now.getMillis())
				throw new ValidationException(ValidationCodeEnum.INVALID_DATE_RANGE);
			
			//from start to today
			startRangeDateTime = new DateTime(startRange);
			endRangeDateTime = new DateTime(DateMidnight.now().getMillis()).plusDays(1).minus(1);
		}
		else if (startRange == 0 && endRange > 0)
		{
			//check to see that end is after now
			if (endRange < now.getMillis())
				throw new ValidationException(ValidationCodeEnum.INVALID_DATE_RANGE);
			
			//from today to end
			startRangeDateTime = new DateTime(DateMidnight.now().getMillis());
			endRangeDateTime = new DateTime(endRange);
		}
		else
		{
			if (endRange < startRange)
				throw new ValidationException(ValidationCodeEnum.INVALID_DATE_RANGE);
			startRangeDateTime = new DateTime(startRange);
			endRangeDateTime = new DateTime(endRange);
		}
		
		List<Task> tasks = taskDao.getTodayTasksForMember(startRangeDateTime, endRangeDateTime, loggedInUserEmail, chunkSize, startIndex);
		return postTaskRetrieval(tasks, loggedInUserEmail);
	}
	
	
	
	
	
	
	
	
//	public List<Task> getTimelessTasksEndingNotCompleted()
//	{ 
//		return taskDao.getTimelessTasksEndingNotCompleted();
//	}
//	
	
	@Transactional
	public List<Task> getTaskChildren(String taskId)
	{
		return taskDao.getTaskChildren(taskId);
	}
	
	@Transactional
	public List<Task> getTasksByStatusAndMemberId(String memberId, int status)
	{
		return taskDao.getTasksByStatusAndMemberId(memberId, status);
	}
	
	
	
//	@Transactional(readOnly=true)
//	@CollectTimeMetrics
//	public List<RepeatSetup> getBulkRepeatSetupTX(String taskDelimiter, String email)
//	{
//		return taskDao.getBulkRepeatSetup(tokenize(taskDelimiter), email);
//	}
	
	
	@Transactional
	public List<Task> getTaskForCalendar(Calendar calendar)
	{
		return taskDao.getTaskForCalendar(calendar);
	}
	
	public Task getTaskByIDAndProcessShares(String Id, String loggedInUserEmail) throws ValidationException  
	{
		Task task = getTaskById(Id);
		List<Task> tasks = new ArrayList<Task>(1);
		tasks.add(task);
		return postTaskRetrieval(tasks , loggedInUserEmail).get(0);
	}
	
	private List<Task> postTaskRetrieval(List<Task> tasks, String loggedInUserEmail) throws ValidationException
	{
		if (loggedInUserEmail == null || "".equals(loggedInUserEmail.trim()))
			throw new ValidationException(UserValidationCodeEnum.USER_MUST_BE_LOGGED_IN);
				
		
		List<Task> unauthorizedTasks = new ArrayList<Task>();
		//now we need to loop through the tasks and mark the ones that as shared with the status
		for (Task task : tasks)
		{
//			task.getTaskCreator().clearAttributes();
			
			//check to see if the task has been shared.  This is set for performance
			if  (task.isHasBeenShared())
			{
				//it has been, so we need to check to see if it is a share or not
				if (task.getTaskCreator().getMember().getEmail().equalsIgnoreCase(loggedInUserEmail))
				{
					//task belongs to the owner...so this means it was shared with someone else
					task.setShareStatus(ShareApprovedStatus.SHARED_TASK.ordinal());
					
					List<String> profileIds = taskDao.getTaskWho(task);
					StringBuilder builder = new StringBuilder();
					for (String id:profileIds)
					{
						Profile profile = userService.loadProfileById(id);
						if (profile==null||profile.getName()==null||profile.getName().equals(""))
							continue;
						
						builder.append(profile.getName());
						builder.append(",");
					}
					
					task.setWho(builder.toString());
				}
				else
				{
					//it doesn't match so it must be a share.  Grab the share.
					Share share = shareService.getShareForTaskAndEmail(task, loggedInUserEmail);
					if (share==null)
					{
						//the task isn't owned by user and the task isn't shared with user, so don't return it
						unauthorizedTasks.add(task);
					}
					else
					{
						task.setShareStatus(share.getShareApprovedStatusOrdinal());
					}
				}
			}
			else
			{
				//task has not been subtasked so the task must be the owner in order for it to be returned.
				if (!task.getTaskCreator().getMember().getEmail().equalsIgnoreCase(loggedInUserEmail))
				{
					//we are going to do an extra check to make sure that the record should be marked as shared.
					//it doesn't match so it must be a share.  Grab the share.
					Share share = shareService.getShareForTaskAndEmail(task, loggedInUserEmail);
					if (share==null)
					{
						//the task isn't owned by user and the task isn't shared with user, so don't return it
						unauthorizedTasks.add(task);
					}
					else
					{
						task.setShareStatus(share.getShareApprovedStatusOrdinal());
						
						//also lets update the task so that it is marked as shared for future use
						task.setHasBeenShared(true);
						taskDao.updateTask(task);
					}					
				}
			}
		}
		
		//check to see if we need to remove any tasks
		if (!unauthorizedTasks.isEmpty())
		{
			tasks.removeAll(unauthorizedTasks);
		}
		
		return tasks;
	}
	

	
//	@Transactional(readOnly=true)
//	@CollectTimeMetrics
//	public List<Calendar> getBulkCalendarTX(String taskDelimiter)
//	{
//		return getBulk(Calendar.class, taskDelimiter);
//	}
	
	
	
//	/**
//	 * This will create or update a user defined Calendar.  It will validate the calendar.
//	 * @param calendar
//	 * @see Calendar
//	 * @see CalendarValidator
//	 */
//	@Override
//	
//	@Deprecated
//	public void updateUserDefinedCalendar(Calendar calendar) throws ValidationException
//	{
//		taskValidator.validateCalendar(calendar);
//		
//		
//		Calendar calendarFromDb = dao.loadByPrimaryKey(Calendar.class, calendar.getId());
//		taskValidator.validateSynchronizationUpdate(calendar, calendarFromDb);
//		calendar.setModifiedDateZone(new DateTime());
//		
//		dao.merge(calendar);
//		
//	}


	
	
	
	/**
	 * This method will create a task. 
	 * @param task
	 */
	@Transactional
	public Task createTask(Task task) throws ValidationException
	{
		validator.validateTask(task);
		
		taskDao.createTask(task);
		
		//call the email service to add reminders and end date notifications
		emailService.processNotificationsForTaskCreateOrUpdate(task, task, false);
		
		//we need to check to see if the task has any calendars that are shared
		shareService.processSharesAddForSharedCalendars(task);
		
		//we need to process subtasks
		shareService.updateOrCreateSubTaskSharesWithModifiedTimeAndNotifyUser(task, task.getTaskCreator().getEmail(), TaskUpdateOperation.CREATE);
		
		return task;
	}
	
	




//	/**
//	 * This will return all the tasks for a member.  This also takes in a sort order.  The ordinal of the TaskOrderEnum is 
//	 * used to define which sort order to use. 
//	 * @param profile
//	 * @param taskOrder
//	 * @return List<Task>
//	 * @see TaskOrderEnum
//	 */
//	@Transactional(readOnly=true)
//	@CollectTimeMetrics
//	@Override	
//	public List<Task> getTasksForMemberTX(Profile profile, int taskOrder) throws ValidationException
//	{
//		if (taskOrder==TaskOrderEnum.CATEGORY.ordinal())
//			return taskDao.getTasksForMemberOrderByCategory(profile);
//		else if (taskOrder==TaskOrderEnum.DUE_DATE.ordinal())
//			return taskDao.getTasksForMemberOrderByDueDate(profile);
//		else if (taskOrder==TaskOrderEnum.PRIORITY.ordinal())
//			return taskDao.getTasksForMemberOrderByPriority(profile);
//		else if (taskOrder==TaskOrderEnum.START_DATE.ordinal())
//			return taskDao.getTasksForMemberOrderByStartDate(profile);
//		else
//			throw new ValidationException(ValidationCodeEnum.TASK_ORDER_NOT_SUPPORTED);
//	}

	
//	/**
//	 * This is the business method that handles both show and clear completed tasks.  It will change the status of a task
//	 * if the task is marked as done.
//	 * @param profile
//	 * @param shouldBeShown
//	 * @see ProgressEnum
//	 */
//	private void setShowStatus(Profile profile, boolean shouldBeShown)
//	{
//		List<Task> tasks = getTasksForMember(profile);
//		for (Task task:tasks)
//		{
//			//check to see if the status is done or delete and if so dont show
//			if (task.getProgress()==ProgressEnum.DONE.ordinal())
//			{
//				task.setShow(shouldBeShown);
//				dao.saveOrUpdate(task);
//			}
//		}
//	}


	/**
	 * This is to retrieve a task by taskId.
	 * @param taskId
	 * @return Task
	 */
	@Transactional
	public Task getTaskById(String taskId) throws ValidationException
	{
		return taskDao.getTaskById(taskId);
	}
	
	
	
	@Transactional
	public Task updateTaskProgress(Task task, String loggedInMemberEmail) throws ValidationException
	{
		if (task.getProgress()==ProgressEnum.DELETE.ordinal())
		{
			cancelTask(task.getId(), loggedInMemberEmail);
			return null;
		}
		
		Task taskFromDB = loadTaskForUpdate(task.getId(), task.getModifiedDate(), loggedInMemberEmail);
		
		taskFromDB.setProgress(task.getProgress());
		
		//if the task was marked as done, we need to update the subtasks with the done status
		if (task.getProgress()==ProgressEnum.DONE.ordinal())
		{
			//update the subTasksToo for task completion
			updateSubTasksProgress(taskFromDB, ProgressEnum.DONE);
		}
		
		postTaskUpdate(taskFromDB, loggedInMemberEmail, TaskUpdateOperation.STATUS);
		
		return taskFromDB;
	}
	
	@Transactional
	public Task updateTaskTitle(Task task, String loggedInMemberEmail) throws ValidationException
	{
		if (task.getTitle()==null||task.getTitle().equals(""))
			throw new ValidationException(ValidationCodeEnum.TASK_TITLE_IS_NULL);
		
		Task taskFromDB = loadTaskForUpdate(task.getId(), task.getModifiedDate(), loggedInMemberEmail);
		
		updateTaskTemplateForTaskUpdate(taskFromDB);
		
		taskFromDB.setTitle(task.getTitle());
		
		postTaskUpdate(taskFromDB, loggedInMemberEmail, TaskUpdateOperation.TITLE);
		
		return taskFromDB;
	}
	
	@Transactional
	public Task updateTaskLocation(Task task, String loggedInMemberEmail) throws ValidationException
	{
		Task taskFromDB = loadTaskForUpdate(task.getId(), task.getModifiedDate(), loggedInMemberEmail);
		
		//update the template
		updateTaskTemplateForTaskUpdate(taskFromDB);
		
		taskFromDB.setTaskLocation(task.getTaskLocation());
		
		postTaskUpdate(taskFromDB, loggedInMemberEmail, TaskUpdateOperation.LOCATION);
		
		return taskFromDB;
	}
	
	
	private void postTaskUpdate(Task taskFromDB, String loggedInMemberEmail, TaskUpdateOperation operation) throws ValidationException
	{
		//write the record to the db
		taskDao.updateTask(taskFromDB);
		
		//update the shares
		shareService.updateSharesWithModifiedTimeAndNotifyUser(taskFromDB, loggedInMemberEmail, operation);
		
		//we need to get the old calendars from the task.  We need a new object because of references
		Set<Calendar> oldListOfCalendars = taskFromDB.getCalendars();
		List<String> oldCalendarKeys = new ArrayList<String>();
		if (oldListOfCalendars!=null&&!oldListOfCalendars.isEmpty())
		{
			for (Calendar calendar:oldListOfCalendars)
				oldCalendarKeys.add(calendar.getId());
		}
				
		//we need to check to see if the task has any calendars that are shared.  If they are the same, nothing has changed
		shareService.processSharesUpdateForSharedCalendars(taskFromDB, oldCalendarKeys);				
				
		//we need to process subtasks
		shareService.updateOrCreateSubTaskSharesWithModifiedTimeAndNotifyUser(taskFromDB, loggedInMemberEmail, operation);
		
	}
	
	private void updateTaskTemplateForTaskUpdate(Task taskFromDB) throws ValidationException
	{
		if (taskFromDB.getRepeatSetup()!=null && taskFromDB.getRepeatSetup().getId()!=null)
		{
			RepeatSetup repeatSetup = repeatSetupService.getRepeatSetupById(taskFromDB.getRepeatSetup().getId());
			
			if (repeatSetup != null)
			{
				TaskTemplate taskTemplate = repeatSetup.getClonedTask();
			
			
				taskTemplate.setTitle(taskFromDB.getTitle());
				taskTemplate.setTaskLocation(taskFromDB.getTaskLocation());
				repeatSetupService.updateTaskTemplate(taskTemplate);
			}
			else
			{
				taskFromDB.setRepeatSetup(null);
				taskDao.updateTask(taskFromDB);
			}			
		}
	}
	
	public Task updateTaskWithCalendar(Task task, String loggedInMemberEmail) throws ValidationException
	{
		Task taskFromDB = loadTaskForUpdate(task.getId(), task.getModifiedDate(), loggedInMemberEmail);
		
		taskFromDB.setCalendars(task.getCalendars());

		postTaskUpdate(taskFromDB, loggedInMemberEmail, TaskUpdateOperation.CALEDNAR);
		
		return taskFromDB;
	}
	
	public Task updateTaskEndTime(Task task, String loggedInMemberEmail) throws ValidationException
	{
		Task taskFromDB = loadTaskForUpdate(task.getId(), task.getModifiedDate(), loggedInMemberEmail);
		
		taskFromDB.setStartTime(task.getStartTime());
		
		postTaskUpdate(taskFromDB, loggedInMemberEmail, TaskUpdateOperation.END_DATE);
				
		//check the reminders that are out there and if not valid remove them.  also schedule new ones
		if (!taskFromDB.getTaskCreator().getEmail().equals(loggedInMemberEmail))
		{
			emailService.processNotificationsForTaskCreateOrUpdate(task, taskFromDB, true);
		}		
		
		return taskFromDB;
		
	}
	
	public Task updateTaskStartTime(Task task, String loggedInMemberEmail) throws ValidationException
	{
		Task taskFromDB = loadTaskForUpdate(task.getId(), task.getModifiedDate(), loggedInMemberEmail);
		
		taskFromDB.setStartTime(task.getEndTime());
		
		postTaskUpdate(taskFromDB, loggedInMemberEmail, TaskUpdateOperation.START_DATE);
		
		return taskFromDB;
	}
	
		/**
	 * This method will update a task.  There will be a check performed to see if the task is out of date.  It the task
	 * is out of date, then a validation exception will be thrown.
	 * @param task
	 * @throws ValidationException
	 */
	private Task loadTaskForUpdate(String taskId, long modifiedDate, String loggedInMemberEmail) throws ValidationException
	{
		final Task taskFromDB = getTaskById(taskId);
		if (taskFromDB == null)
			throw new ValidationException(ValidationCodeEnum.TASK_NOT_FOUND);
		
		//see if it needs to be synced
		validator.validateSynchronizationUpdate(modifiedDate, taskFromDB.getModifiedDate());
		taskFromDB.setModifiedDateZone(new DateTime());
		
		//check to see if you have access
		if (!loggedInMemberEmail.equals(taskFromDB.getTaskCreator().getEmail()))
		{
			throw new ValidationException(ValidationCodeEnum.UNAUTHORIZED);
		}
		
		return taskFromDB;
	
	}
	
	
	public void cancelTask(String id, String loggedInMemberEmail) throws ValidationException
	{
		Task task = new Task(id);
		cancelTask(task, loggedInMemberEmail);
	}


	/**
	 * This is called to cancel a task.  It does not delete it from the database and only marks it as delete.
	 * @param task
	 * @see ProgressEnum
	 */
	@Transactional
	public void cancelTask(Task task, String loggedInMemberEmail) throws ValidationException
	{
		if (loggedInMemberEmail==null||"".equals(loggedInMemberEmail))
			throw new ValidationException(UserValidationCodeEnum.USER_MUST_BE_LOGGED_IN);
		
		//grab the task
		Task taskFromDB = getTaskById(task.getId());
		
		//if the task is null, throw an error
		if (taskFromDB == null)
			throw new ValidationException(ValidationCodeEnum.INVALID_TASK_ID);
		
	
		
		//check to see if logged in user is the owner
		if (taskFromDB.getTaskCreator().getMember().getEmail().equalsIgnoreCase(loggedInMemberEmail))
		{		
			//check to see if the modified date from the db is before the one being passed
			validator.validateSynchronizationUpdate(task, taskFromDB);
			taskFromDB.setModifiedDateZone(new DateTime());
			
			taskFromDB.setProgress(ProgressEnum.DELETE.ordinal());
			taskFromDB.setShow(false);
			
			//cancel the notifications for the tasks
			notificationService.cancelNotification(taskFromDB);
			
			//update the share modified times
			shareService.updateSharesWithModifiedTimeAndNotifyUser(task, loggedInMemberEmail, TaskUpdateOperation.STATUS);
			
			//update the subTasksToo
			updateSubTasksProgress(taskFromDB, ProgressEnum.DELETE);
			
			//we need to clear the references to the join table
			taskFromDB.removeAllCalendars();
			
			//cancel reminders
			reminderService.deleteAlarmsForDeletedTask(taskFromDB);
			
			//update the database
			taskDao.updateTask(taskFromDB);			
			
			//call email service to cancel the tasks
//			emailService.sendTaskStatusChangeNotification(taskFromDB, loggedInMemberEmail, TaskStatusChangeEvent.Canceled);
		}
		//the delete is not the task owner, so we should remove the share instead
		else
		{
			//need to retrieve the orginal share from this delete
			Share share = shareService.getShareForTaskAndEmail(taskFromDB, loggedInMemberEmail);
			if (share != null)
				shareService.deleteShare(share);
		}
	}

	
	
	public Task updateTaskWithRepeatSetup(Task task, String loggedInMemberEmail) throws ValidationException
	{
		Task taskFromDB = loadTaskForUpdate(task.getId(), task.getModifiedDate(), loggedInMemberEmail);
		
		taskFromDB.setStartTime(task.getEndTime());
		
		postTaskUpdate(taskFromDB, loggedInMemberEmail, TaskUpdateOperation.REPEAT_SETUP);
		
		return taskFromDB;
	}
	

	
	
	
	
	private void updateSubTasksProgress(Task parentTask, ProgressEnum progressEnum) throws ValidationException
	{
		if (!parentTask.isaParent())
			return;
		
		List<Task> subTasks = taskDao.getTasksForSubTasks(parentTask);
		for (Task subTask : subTasks)
		{
			subTask.setProgress(progressEnum.ordinal());
			taskDao.updateTask(subTask);
		}
	}
	
}
