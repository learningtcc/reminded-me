package com.homefellas.rm.share;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.batch.NotificationService;
import com.homefellas.batch.NotificationTypeEnum;
import com.homefellas.batch.PushTypeEnum;
import com.homefellas.exception.DatabaseNotInitializedException;
import com.homefellas.exception.ValidationException;
import com.homefellas.metrics.CollectTimeMetrics;
import com.homefellas.rm.ISynchronizeable.SynchronizeableObject;
import com.homefellas.rm.RMValidator;
import com.homefellas.rm.ValidationCodeEnum;
import com.homefellas.rm.calendar.Calendar;
import com.homefellas.rm.calendar.CalendarService;
import com.homefellas.rm.notification.EmailService;
import com.homefellas.rm.reminder.Alarm;
import com.homefellas.rm.reminder.ReminderService;
import com.homefellas.rm.share.Share.ShareStatus;
import com.homefellas.rm.task.AbstractTask.ProgressEnum;
import com.homefellas.rm.task.ITaskDao;
import com.homefellas.rm.task.Task;
import com.homefellas.rm.task.TaskService;
import com.homefellas.rm.task.TaskUpdateOperation;
import com.homefellas.rm.user.Contact;
import com.homefellas.rm.user.ContactService;
import com.homefellas.service.core.AbstractService;
import com.homefellas.user.Member;
import com.homefellas.user.Profile;
import com.homefellas.user.UserService;
import com.homefellas.user.UserValidationCodeEnum;

public class ShareService extends AbstractService 
{

	@Autowired
	private IShareDao shareDao;
	
	@Autowired
	private RMValidator shareValidator;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private ITaskDao taskDao;
	
	@Autowired
	private CalendarService calendarService;
	
	@Autowired
	private ContactService contactService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private ReminderService reminderService;
	
	public void setShareDao(IShareDao shareDao)
	{
		this.shareDao = shareDao;
	}
	
	
	/*********************************************************************************
	 * 
	 * Service methods start
	 * 
	 *********************************************************************************/
	@Transactional
	public Calendar publishCalendarToStore(Calendar calendar, PushTypeEnum pushTypeEnum, String email) throws ValidationException
	{
		calendar.setPublicList(true);
		calendar.setPublishToAppStore(true);
		calendar.setPushTypeOrdinal(pushTypeEnum.ordinal());
		
		calendarService.updateCalendar(calendar, email);
		
		return calendar;
		
	}
	
	@Transactional
	public ShareCalendar acceptDeclineShareCaledarTX(String shareCalendarId, ShareApprovedStatus shareApprovedStatus, String loggedInEmail) throws ValidationException
	{
		//check to see if there is any action, cause if the status isn't accept or decline then just return
		if (shareApprovedStatus.ordinal() != ShareApprovedStatus.APPROVED.ordinal() && shareApprovedStatus.ordinal() != ShareApprovedStatus.DECLINED.ordinal())
			return null;

		ShareCalendar shareCalendar =shareDao.getShareCalendarById(shareCalendarId);
		if (shareCalendar == null)
			throw new ValidationException(ValidationCodeEnum.SHARE_CALENDAR_NOT_FOUND);
		
		Member sharee = shareCalendar.getUser();

		//check to see if the user has access to decide on this share
		if (!sharee.getEmail().equals(loggedInEmail))
		{
			throw new ValidationException(ValidationCodeEnum.MEMBER_DOES_NOT_HAVE_AUTHORIZATION);
		}
		//grab all the task id that need to be updated for the calendar that was shared
//		List<String> taskIds = taskService.getTaskIdsForCalendar(shareCalendar.getCalendar());
//		shareDao.updateShareStatusForTasks(taskIds, sharee, shareApprovedStatus);
		
		List<Task> tasks = taskService.getTaskForCalendar(shareCalendar.getCalendar());
		for (Task task: tasks)
		{
			Share share = getShareForUserAndTask(task, sharee);
			if (share==null)
				continue;
			
			if (shareApprovedStatus==ShareApprovedStatus.APPROVED)
				acceptShare(share.getId(), false);
			else
				declineShare(share.getId(), false);
		}
		
		Profile profile = userService.getProfileByEmail(sharee.getEmail());
		emailService.sendShareCalendarAcceptOrDeclineNotificationToTaskOwner(shareCalendar, profile, NotificationTypeEnum.EMAIL, shareApprovedStatus);
		
		return shareCalendar;
	}
	
	@Transactional
	public void updateOrCreateSubTaskSharesWithModifiedTimeAndNotifyUser(Task subTask, String userMakingChangeEmail, TaskUpdateOperation operation) throws ValidationException
	{
		if(subTask.isASubTask())
		{
			Task parentTask = taskService.getTaskById(subTask.getParentId());
			if (parentTask == null)
				return;
			
			if (!parentTask.isHasBeenShared())
				return;
			
			//now we need to get the shares for the parent and create one for the subtask
			List<Share> shares = getSharesForTask(parentTask);
			for (Share share:shares)
			{
				Member memberThatHasBeenSharedWith = share.getUser();
				Share subTaskShare = getShareForUserAndTask(subTask, memberThatHasBeenSharedWith);
				if (subTaskShare == null)
				{
					addSubTask(parentTask, subTask, memberThatHasBeenSharedWith);
				}
				else
				{
					//update the share
					updateShareWithModifiedTimeAndNotifyUser(subTask, subTaskShare, userMakingChangeEmail, operation);
				}
			}
		}
		
	}
	
	@Transactional
	public void addSubTask(Task parentTask, Task subTask, Member memberThatHasBeenSharedWith) throws ValidationException
	{
		//this is a new subtask, so we need to create a share
		createShare(null, subTask, memberThatHasBeenSharedWith);
		
		//now we need to send an email that a task was updated.
		emailService.sendNotificationForSubTaskAdded(parentTask, subTask, memberThatHasBeenSharedWith);
	}
	
	@Transactional
	public void updateShareWithModifiedTimeAndNotifyUser(Task task, Share share, String userMakingChangeEmail, TaskUpdateOperation operation) throws ValidationException
	{
		//updatet the modified time and merge to db
		share.setModifiedDateZone(new DateTime());
		
		//set the share status to delete if the task is deleted or done
		if (task.getProgress()==ProgressEnum.DELETE.ordinal() || task.getProgress()==ProgressEnum.DONE.ordinal())
		{
			share.setStatus(ShareStatus.DELETED.ordinal());
		}
		
		shareDao.updateShare(share);
		
		//skip all shares that have not been approved
		if (share.getShareApprovedStatusOrdinal()!=ShareApprovedStatus.APPROVED.ordinal())
			return;
		
		if (operation.equals(TaskUpdateOperation.STATUS) || operation.equals(TaskUpdateOperation.CREATE))
		{
			//alert sharees of the status changes
			emailService.processNotificationsForTaskStatusChange(task, share.getTask(), share.getUser().getEmail(), userMakingChangeEmail);
		}
		
		else if (operation.equals(operation.equals(TaskUpdateOperation.END_DATE)) || operation.equals(TaskUpdateOperation.CREATE))
		{
			//we need to either setup new endtime notifications or update the existing ones
			emailService.createAndCancelTaskEndTimeNotifications(task, share.getTask(), share.getUser().getEmail(), true);
		}
	}
	
	
	
	
	/**
	 * This method will update the modifed time so that it is picked up during the sync.  This will also 
	 * process the notifications for this task update.
	 * @param task
	 */
	@Transactional
	public void updateSharesWithModifiedTimeAndNotifyUser(Task task, String userMakingChangeEmail, TaskUpdateOperation operation) throws ValidationException
	{
		List<Share> shares = getSharesForTask(task);
		for (Share share:shares)
		{
			updateShareWithModifiedTimeAndNotifyUser(task, share, userMakingChangeEmail, operation);
		}
		
	}
	
	/**
	 * This will update a share.  First it validates the share, then loads it and checks for syncs.
	 * @param share
	 * @return share
	 */
	@Transactional
	public Share updateShare(Share share) throws ValidationException
	{
		shareValidator.validateShare(share);
		
		Share shareFromDB = shareDao.getShareById(share.getId());
		
		shareValidator.validateSynchronizationUpdate(share, shareFromDB);
		shareFromDB.setModifiedDateZone(new DateTime());
		
		shareDao.updateShare(shareFromDB);
		return share;
	}
	
	/**
	 * This will update an invite.  If a sync is required it will throw an error.
	 * @param invite
	 * @return invite
	 */
	@Transactional
	public Invite updateInvite(Invite invite) throws ValidationException
	{
		Invite inviteFromDB = shareDao.getInviteById(invite.getId());
		
		shareValidator.validateSynchronizationUpdate(invite, inviteFromDB);
		inviteFromDB.setModifiedDateZone(new DateTime());
		
		shareDao.updateInvite(inviteFromDB);
		return invite;
	}

	


	/**
	 * This will retrieve invites that are separated by commas.  It will take a single invite as well.
	 * @param comma delimited list of invite ids
	 * @return List<Invite>
	 */
//	@Transactional(readOnly=true)
//	public List<Invite> getBulkInvitesTX(String taskDelimiter)
//	{
//		StringTokenizer stringTokenizer = new StringTokenizer(taskDelimiter, ",");
//		List<String> ids = new ArrayList<String>();
//		while (stringTokenizer.hasMoreTokens())
//		{
//			String id = stringTokenizer.nextToken();
//			ids.add(id);
//		}
//		
//		return shareDao.getInvitesByIds(ids);
//	}
//	
	/**
	 * This will get a list of shares by a comma separated list.  It will work for a single share as well
	 * @param comma delimted list of share ids
	 * @return List<Share>
	 */
//		@Transactional(readOnly=true)
//		public List<Share> getBulkSharesTX(String taskDelimiter)
//		{
//			StringTokenizer stringTokenizer = new StringTokenizer(taskDelimiter, ",");
//			List<String> ids = new ArrayList<String>();
//			while (stringTokenizer.hasMoreTokens())
//			{
//				String id = stringTokenizer.nextToken();
//				ids.add(id);
//			}
//			
//			return shareDao.getSharesByIds(ids);
//		}
	
	@Transactional(readOnly=true)
	public List<ShareCalendar> getBulkShareCalendarsTX(String taskDelimiter)
	{
		StringTokenizer stringTokenizer = new StringTokenizer(taskDelimiter, ",");
		List<String> ids = new ArrayList<String>();
		while (stringTokenizer.hasMoreTokens())
		{
			String id = stringTokenizer.nextToken();
			ids.add(id);
		}
		
		return shareDao.getShareCalendarsByIds(ids);
	}
	
	/**
	 * This will return all the invites that have been shared with the passed email.  This should be called
	 * when a sharee wants to see all the shares that have been sent to them.
	 * @param memberId (id of member)
	 * @return List<Invite>
	 */
	@Transactional(readOnly=true)
	public List<Invite> getInvitesSharedWithEmailTX(String memberId)
	{
		return shareDao.getInvitesSharedWithEmail(memberId);
	}
	
	/**
	 * This returns a list of invites that were sent out by the passed in member id.  This would be used
	 * for a task owner to find out who they have shared tasks with.
	 * @param memberId 
	 * @return List<Invite>
	 */
	@Transactional(readOnly=true)
	public List<Invite> getSentInvitesForMemberTX(String memberId)
	{
		return shareDao.getSentInvitesForMember(memberId);
	}
	
	/**
	 * This will delete a share.  The share is not actually deleted from the database but has the status updated
	 * and marked as delete.  We can not delete because of the sync framework.  The share is reloaded from the ds
	 * before it is deleted.
	 * @param share (only share id is required to be passed in the share)
	 */
	@Transactional
	public void deleteShare(Share share)
	{
		Share shareFromDb = shareDao.getShareById(share.getId());
		
		shareFromDb.setStatus(ShareStatus.DELETED.ordinal());
		shareDao.updateShare(shareFromDb);
	}

	/**
	 * This will create a share when a user registers. This method should be called from the registeration 
	 * process and will add the share after the profile is created.  This method will throw an error if
	 * there is no profile.member.sharedTaskId is not present or if that sharedTaskId does not resolve
	 * to a real task.    
	 * @param profile (fully loaded profile is required)
	 * @return Share
	 */
	@Transactional
	public Share createShareOnRegister(Profile profile) throws ValidationException
	{
		if (profile.getMember()==null||profile.getMember().getSharedTaskId()==null)
			throw new ValidationException(ValidationCodeEnum.TASK_NOT_FOUND, profile);
		
		Task task = taskService.getTaskById(profile.getMember().getSharedTaskId());
		
		if (task==null)
			throw new ValidationException(ValidationCodeEnum.TASK_NOT_FOUND, profile);
		
		Share share = new Share();
		share.setTask(task);
		share.setUser(profile.getMember());
		share.setShareApprovedStatusOrdinal(ShareApprovedStatus.APPROVED.ordinal());
		shareDao.createShare(share);
		
		return share;
	}
	
	/**
	 * This method will sync the shares for a member.  A share is actually synced in two different ways.  First
	 * the share itself is synced and second the task is retrieved from the share and is returned so that the user
	 * sees the shared tasks as part of their task list.
	 * @param synchronizeableObject must be an instance of IShareable else an empty list will be returned
	 * @param memberId a valid member id is required
	 */
	@Transactional(readOnly=true)
	public List getSyncShareIdsForMember(SynchronizeableObject synchronizeableObject, String memberId)
	{
		Object o = synchronizeableObject.getClassInstance();
		
		if (o == null || ! (o instanceof IShareable))
			return Collections.EMPTY_LIST;
		
		if (o instanceof Task)
		{
			return getAcceptedSharedTaskIdsForUser(memberId);
		}
		else
			return Collections.EMPTY_LIST;
	}

	/**
	 * This will return a list of share ids that have been accepted by the passed user id.  Shares that have 
	 * not been actioned will not be returned.
	 * @param userId a valid user id
	 * @return List<String>
	 */	
	@Transactional(readOnly=true)
	public List<String> getAcceptedSharedTaskIdsForUser(String userId)
	{
		return shareDao.getAcceptedSharedTaskIdsForUser(userId);
	}
	
	/**
	 * This will return a list of Task shares that have been accepted by the passed user.
	 * @param user user.id is required to be set
	 * @return List<Task> 
	 */
	@Transactional(readOnly=true)
	public List<Task> getAcceptedSharedTasksForUser(Member user)
	{
		return shareDao.getAcceptedSharedTasksForUser(user);
	}
	
	/**
	 * This will return a list of Task shares that have been accepted by the passed user.
	 * @param userId must be valid
	 * @return List<Task> 
	 */	
	@Transactional(readOnly=true)
	public List<Task> getAcceptedSharedTasksForUser(String userId)
	{
		Member user = new Member();
		user.setId(userId);
		return shareDao.getAcceptedSharedTasksForUser(user);
	}
	
	
	/**
	 * This will return a share based on a passed task and member.  task.id and user.id is required to be set
	 * @param task task.id is required to be valid
	 * @param user member.id is required to be valid
	 * @return Share 
	 */
	@Transactional(readOnly=true)
	public Share getShareForUserAndTask(Task task, Member user)
	{
		return shareDao.getShareForUserAndTask(task, user);
	}
	
	/**
	 * This will return a share based on the task.id and a member email
	 * @param task task.id is required to be valid
	 * @param email must be a valid email and in the system
	 * @return Share
	 */
	@Transactional(readOnly=true)
	public Share getShareForTaskAndEmail(Task task, String email)
	{
		return shareDao.getShareForTaskAndEmail(task, email);
	}
	
	/**
	 * This will return all the shares for a given task.
	 * @param task task.id is required to be valid
	 * @return List<Share>
	 */
	@Transactional(readOnly=true)
	public List<Share> getSharesForTask(Task task)
	{
		return shareDao.getSharesForTask(task);
	}
	
	@Transactional(readOnly=true)
	public List<Share> getAcceptedSharesForTask(Task task)
	{
		return shareDao.getAcceptedSharesForTask(task);
	}
	
	
	/**
	 * This will set a share to be marked as accepted.  A notification will be send out to the owner when a
	 * share is accepted
	 * @param shareId a valid share.id is required
	 * @return Share
	 */
	@Transactional
	public Share acceptShareTX(String shareId) throws ValidationException
	{
		return acceptShare(shareId, true);
	}
	
	/**
	 * This will make the share accepted.  It requires the share id as well as a flag that indicates whether or not to
	 * send a notfication to the share owner.  This is mostly used for sharing of calendars as you dont want to send
	 * all those notifications and should be consolidated. 
	 * @param shareId
	 * @param sendNotification
	 * @return
	 * @throws ValidationException
	 */
	@Transactional
	public Share acceptShare(String shareId, boolean sendNotification) throws ValidationException
	{
		Share share = shareDao.getShareById(shareId);
		if (share == null) 
			throw new ValidationException(ValidationCodeEnum.SHARE_IS_NULL);
		
		//if its already approved...then lets not process this extra stuff
		if (ShareApprovedStatus.APPROVED.ordinal() == share.getShareApprovedStatusOrdinal())
			return share;
		
		share.setShareApprovedStatusOrdinal(ShareApprovedStatus.APPROVED.ordinal());
		
		share.setModifiedDateZone(new DateTime());
		shareDao.updateShare(share);
	
		//we need to set the alarms for the task as well...but only grab the ones from the task owner
		List<Alarm> alarms = reminderService.getAlarmByTaskAndMember(share.getTask(), share.getTask().getTaskCreator().getMember());
		for (Alarm alarm : alarms)
		{
			//check to see if alarm has already been set
			Alarm newAlarmn = new Alarm(alarm);
			newAlarmn.setTask(share.getTask());
			newAlarmn.generateGUIDKey();
			reminderService.createAlarmTX(newAlarmn, share.getUser());			
		}
		
		//we need to schedule the notification for end date too
		emailService.createAndCancelTaskEndTimeNotifications(share.getTask(), share.getTask(), share.getUser().getEmail(), false);
		
		if (sendNotification)
		{
			Profile profile = userService.getProfileByEmail(share.getUser().getEmail());
			emailService.sendShareAcceptOrDeclineNotificationToTaskOwner(share, profile, NotificationTypeEnum.EMAIL, ShareApprovedStatus.APPROVED);
		}
	
		return share;
	}
	
	/**
	 * This is the method that will accept for a public share.  A valid taskId and memberId are required to be passed
	 * and an error will be thrown if they are not.  If the task is private an error will be thrown.
	 * @param taskId must be valid
	 * @param memberId must be valud
	 * @return Share
	 */
	@Transactional
	public Share acceptPublicShareTX(String taskId, String memberId) throws ValidationException
	{
		Task task = taskService.getTaskById(taskId);
		if (task==null)
			throw new ValidationException(ValidationCodeEnum.TASK_NOT_FOUND, new Task(taskId));
		
		Member member = userService.loadMemberById(memberId);
		if (member==null)
			throw new ValidationException(UserValidationCodeEnum.MEMBER_NOT_FOUND);
		
		//get the share
		Share share = getShareForUserAndTask(task, member);
		if (share==null)
		{
			//chck if the task is public
			if (task.isPublicTask())
			{
				share = new Share();
				share.setTask(task);
				share.setUser(member);
				share.setShareApprovedStatusOrdinal(ShareApprovedStatus.APPROVED.ordinal());
				shareDao.createShare(share);
			}
			else
			{
				throw new ValidationException(ValidationCodeEnum.CANNOT_ACCEPT_PRIVATE_TASK);
			}		
		}
		else
		{
			share.setShareApprovedStatusOrdinal(ShareApprovedStatus.APPROVED.ordinal());
			
			share.setModifiedDateZone(new DateTime());
			
			shareDao.updateShare(share);
		}
		
		//we need to schedule the notification for end date too
		emailService.createAndCancelTaskEndTimeNotifications(share.getTask(), share.getTask(), share.getUser().getEmail(), false);
		
		Profile profile = userService.getProfileByEmail(share.getUser().getEmail());
		emailService.sendShareAcceptOrDeclineNotificationToTaskOwner(share, profile, NotificationTypeEnum.EMAIL, ShareApprovedStatus.APPROVED);

		return share;
	}
	
	/**
	 * This is called when a sharee declines a share.  A valid shareId must be passed.  A notification will 
	 * be sent out to the owner as the result of this action.
	 * @param shareId must be valid
	 * @return Share
	 */
	@Transactional
	public Share declineShare(String shareId, boolean sendNotification)
	{
		Share share =shareDao.getShareById(shareId);
		share.setShareApprovedStatusOrdinal(ShareApprovedStatus.DECLINED.ordinal());
		
		share.setModifiedDateZone(new DateTime());
		
		shareDao.updateShare(share);
		
		if (sendNotification)
		{
			Profile profile = userService.getProfileByEmail(share.getUser().getEmail());
			emailService.sendShareAcceptOrDeclineNotificationToTaskOwner(share, profile, NotificationTypeEnum.EMAIL, ShareApprovedStatus.DECLINED);
		}
		
		return share;
	}

	/**
	 * This method should be called after a share has been viewed.  This will mark the share as being shared, 
	 * but it is the callers responsiblity to block and/or handle the result of this action.  If the shareId is 
	 * invalid an error will be thrown.
	 * @param shareId must be valid
	 * @return Share
	 */
	@Transactional
	public Share shareViewed(String shareId) throws ValidationException
	{
		Share share = shareDao.getShareById(shareId);
		if (share == null)
			throw new ValidationException(ValidationCodeEnum.SHARE_IS_NULL);
		
		share.setViewed(true);
		shareDao.updateShare(share);
		
		return share;
	}
	
	/**
	 * This will get all the shares for the passed user and calendar.  calendar.id and user.id must be set.  
	 * @param calendar a valid calendar.id is required
	 * @param user a valid user.id is required
	 * @return ShareCalendar
	 */
	@Transactional
	public ShareCalendar getShareForUserAndCalendar(Calendar calendar, Member user)
	{
		return shareDao.getShareForUserAndCalendar(calendar, user);
	}
	
	/**
	 * This checks ishareable to find the implementation.  An error is thrown if the implementation is not implemented.
	 * @param iShareable
	 * @param member
	 * @return
	 * @throws ValidationException
	 */
	IShare getShareForUserAndIShare(IShareable iShareable, Member member) throws ValidationException
	{
		if (iShareable instanceof Task)
		{
			return getShareForUserAndTask((Task)iShareable, member);
		}
		else if (iShareable instanceof Calendar)
		{
			return getShareForUserAndCalendar((Calendar)iShareable, member);
		}
		else
		{
			throw new ValidationException(ValidationCodeEnum.NOT_IMPLEMENTED_ISHAREABLE);
		}
	}
	
	/**
	 * This will share a calendar (list).  An error is thrown if the invite does not have all the required information
	 * Notifications will be processed as the result of the calendar being shared.
	 * @param invite invite.message, invite.subject, invite.shareId are all required.
	 * @return Invite
	 * @see ShareValidator
	 */
	@Transactional
	public Invite shareCalender(Invite invite) throws ValidationException
	{
		//check to make sure that the invite is valid
		shareValidator.validateInvite(invite);
		
		Calendar calendar = calendarService.getCalendarById(invite.getShareId());
		if (calendar==null)
			throw new ValidationException(ValidationCodeEnum.CALENDAR_NOT_FOUND, invite);
		
		invite.setShareClassName(Calendar.class.getName());
		invite.setInviter(calendar.getMember().getMember());
		
		shareDao.createInvite(invite);
		
		//create the share at the calendar level
		createShares(invite, calendar, true);
		
		//we need to grab all the tasks for shared calendar and create shares for them
		List<Task> tasks = taskService.getTaskForCalendar(calendar);
		for (Task task : tasks)
		{
			createShares(invite, task, false);
		}
		
		return invite;
	}
	
	
	/**
	 * This will share a task.  If the invite does not have the required information, then a error will be thrown.
	 * Notifications will also be sent out to list of sharees.
	 * @param invite invite.message, invite.subject, invite.shareId are required.
	 * @see ShareValidator 
	 */
	@Transactional
	public void shareTask(Invite invite) throws ValidationException
	{
		//check to make sure that the invite is valid
		shareValidator.validateInvite(invite);
		
		Task task = taskService.getTaskById(invite.getShareId());
		if (task==null)
			throw new ValidationException(ValidationCodeEnum.TASK_NOT_FOUND, invite);
		
		
		
		//lets save the invite
		invite.setShareClassName(Task.class.getName());
		invite.setInviter(task.getTaskCreator().getMember());
		
		//persist the invite to the db
		shareDao.createInvite(invite);
			
		//check to see if this is a parent list
		if (task.getSubTasksCount()>0)
		{
			//create the top level share and send an email
			createShares(invite, task, true);
			
			//task has children
			shareSubList(invite, task);
		}
		else
		{
			//now we need to create shares and send out share emails
			createShares(invite, task, true);
		}
		
		//we need to mark the task as shared for quicker lookups in the future
		task.setHasBeenShared(true);
		taskDao.updateTask(task);
		return;
	}
	
	
	private Task shareSubList(Invite invite, Task parentTask) throws ValidationException
	{
		
		
		if (parentTask.getSubTasksCount() == 0)
		{	
			return parentTask;
		}
		else
		{
			List<Task> taskChildren =  taskService.getTaskChildren(parentTask.getId());
			for (Task childTask : taskChildren)
			{
				createShares(invite, childTask, false);
				
				shareSubList(invite, childTask);
			}
			
			return parentTask;
		}
	}
	
	/**
	 * This method should handle specific implementation logic for a share.  This should be executed once a share is 
	 * created.
	 * @param iShare
	 */
	void postShareCreate(IShare iShare)
	{
		//handle contacts
		createOrUpdateContacts(iShare);
	}
	
	public void createOrUpdateContacts(IShare ishare)
	{
		if (ishare instanceof Share)
		{ 
			Share share = (Share)ishare;
			Contact contact = shareDao.getContactByProfileId(share.getUser().getId(), share.getTask().getTaskCreator().getId());
			if (contact == null)
			{
				//this is a new contact and user has not been shared before
				Profile profile = userService.loadProfileById(share.getUser().getId());
				contact = new Contact();
				contact.generateGUIDKey();
				contact.setContact(profile);
				contact.setContactCounter(1);
				contact.setContactOwner(share.getTask().getTaskCreator().getMember());
				contactService.createContact(contact);
			}
			else
			{
				contact.incrementCounter();
				contact.setModifiedDateZone(new DateTime());
				contactService.updateContact(contact);
			}
		}
	}
	
	/**
	 * This method will create a single share based on the implementaiton of the IShareable.  An error will be thrown
	 * if the implementing class can not be created.  
	 * @param invite
	 * @param iShareable
	 * @param user
	 * @return IShare
	 * @throws ValidationException
	 */
	IShare createShare(Invite invite, IShareable iShareable, Member user) throws ValidationException
	{
		//no share, so lets create one. 
		try
		{
			IShare share;
			share = (IShare)Class.forName(iShareable.getIShareImplClassName()).newInstance();
		
			share.setIShareable(iShareable);
			share.setUser(user);
			share.setInvite(invite);		
			shareDao.createShare(share);
			
			postShareCreate(share);
			
			return share;
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
			throw new ValidationException(ValidationCodeEnum.NOT_IMPLEMENTED_ISHAREABLE);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
			throw new ValidationException(ValidationCodeEnum.NOT_IMPLEMENTED_ISHAREABLE);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			throw new ValidationException(ValidationCodeEnum.NOT_IMPLEMENTED_ISHAREABLE);
		}			
	}
	
	/**
	 * This will create all the shares for the email addresses passed in the invite.  There is a flag where you
	 * can disable or enable notifications to be sent out.
	 * @param invite
	 * @param iShareable
	 * @param sendOutNotification
	 * @throws ValidationException
	 */
	
	void createShares(Invite invite, IShareable iShareable, boolean sendOutNotification) throws ValidationException
	{
		Map<String, String> emailMap = invite.getEmailAddresses();
		Set<String> emailAddresses = emailMap.keySet();
		List<IShare> shares = new ArrayList<IShare>(emailAddresses.size());
		
		//loop over the email address
		for (String emailAddress : emailAddresses)
		{
			//need to grab the member profile.
			Profile profile = userService.getProfileByEmail(emailAddress);
			Member user;
			 
			//no profile yet, so we will need ot register the user
			if (profile == null)
			{
				//user does not exist in our database...we will have to create a user account
				try
				{
					profile = userService.reigsterGuest(emailAddress, emailMap.get(emailAddress));
				}
				catch (DatabaseNotInitializedException e)
				{
					//almost should never come here
					throw new ValidationException(ValidationCodeEnum.DATABASE_NOT_SETUP);
				}
				
			}

			//this is a bad error...should never come here either and it si just a catch all
			if (profile==null || profile.getMember()==null)
				throw new ValidationException(ValidationCodeEnum.REGISTER_GUEST_ERROR);
			
			//grab the member
			user = profile.getMember();
			
			//need to check to see if the share already exists.
			IShare share = getShareForUserAndIShare(iShareable, user);
			if (share==null)
			{
				share = createShare(invite, iShareable, user);
			}

			//add the share to be resent even though it already existed
			shares.add(share);
		}
		
		//now we loo
		if (sendOutNotification)
		{
			for (IShare share:shares)
			{
				String alias = emailMap.get(share.getUser().getEmail());
				if (alias == null || "".equals(alias))
					alias = share.getUser().getEmail();
				
				emailService.sendShareNotificationToSharee(invite, share, NotificationTypeEnum.EMAIL, alias, share.isPublic());
//				sendIShareNotification(invite, share, alias, share.isPublic());
			}
		}

	}
	
	/**
	 * This method is used when someone wants to message and notifiy all the sharees.  Typically this would
	 * be used when a share owner wants to send some message to the sharee.  
	 * @param invite.message, invite.subject, invite.shareId are all required.
	 */
	@Transactional
	public void messageShares(Invite invite) throws ValidationException
	{
		if (invite.getShareId()==null||invite.getShareId().equals(""))
			throw new ValidationException(ValidationCodeEnum.TASK_ID_IS_NULL);
		
//		Task task = taskService.getTaskById(invite.getShareId());
		Task task = new Task(invite.getShareId());
		List<Share> shares = getSharesForTask(task);
		for (Share share:shares)
		{
			emailService.sendMessageToSharee(invite, share);
//			Notification notification = buildNotificationToMessageShares(invite, share);
//			notificationService.scheduleNotification(notification);
		}
	}
	
	/**
	 * This method is a short cut method to the main method and removes the flag from the caller.
	 * @param task The task that has been updated
	 * @param oldCalendarKeys a list of the old calendars that belonged to the task before the task was updated
	 */
	
	 
	public void processSharesUpdateForSharedCalendars(Task task, List<String> oldCalendarKeys) throws ValidationException
	{
		processSharesForSharedCalendars(task, true, oldCalendarKeys);
	}
	
	/**
	 * This method is a short cut method ot the main method and set the isUpdate flag to false for you so
	 * the caller does not have to worry about it.
	 * @param task the task that has been added
	 */
	
	 
	public void processSharesAddForSharedCalendars(Task task) throws ValidationException
	{
		processSharesForSharedCalendars(task, false, null);
	}
	
	public void processSharesForAlarms(Alarm alarm, Alarm alarmFromDB) throws ValidationException
	{
		//get a list of all the shares
		List<Share> shares = getSharesForTask(alarm.getTask());
		for (Share share : shares)
		{
			//loop over the shares and get the alarm that was set for each user
//			Alarm sharedAlarm = reminderServiceTX.getAlarmByTaskMemberAndTime(alarm.getTask(), share.getUser(), alarmFromDB.getAlarmTime());
			List<Alarm> alarms = reminderService.getAlarmByTaskAndMember(alarm.getTask(), share.getUser());
			for (Alarm sharedAlarm : alarms)
			{
				if (sharedAlarm.getAlarmTime().isEqual(alarmFromDB.getAlarmTime()))
				{
					//cancel that alarm
					reminderService.unScheduleAlarm(sharedAlarm);
					
					//schedule a new one
					sharedAlarm.setAlarmTime(new DateTime(alarm.getAlarmTime()));
					reminderService.updateAlarm(sharedAlarm);
					return;
				}
			}
			
		}
	}
	
	
	/**
	 * This method will manage the tasks and shares that are part of a calendarShare.  So when a ShareCalendar
	 * is created, all the tasks that belong to that ShareCalendar will be shared as well.  So when a user adds
	 * a task to a shared calendar, the newly added task will have a share created as well.  This is also true
	 * when a task has the calendar removed from the list, the share will be deleted.  Since references are used
	 * this method requires that you pass in thet oldCalendarKeys that way we can compare to see what has changed
	 * before and after this call.  To simplify things too, we ask for whether or not this is a new task or not.
	 * @param task
	 * @param isUpdate
	 * @param oldCalendarKeys
	 * @throws ValidationException
	 */
	void processSharesForSharedCalendars(Task task, boolean isUpdate,List<String> oldCalendarKeys) throws ValidationException
	{
		List<Member> membersThatBelongToShareLis= new ArrayList<Member>();
		Set<Calendar> calendars = task.getCalendars();
		//if its an add and there are no calendars we can return
		if (!isUpdate && (calendars == null || calendars.isEmpty()))
			return;
		
		List<Share> shares = new ArrayList<Share>();
		//get a list of shares.  This will only be used on update to remove any shares from the list
		if (isUpdate)
		{
			shares = getSharesForTask(task);
			
			//we need to loop over the old list of calendar to propagate share removals
			if (oldCalendarKeys != null && !oldCalendarKeys.isEmpty())
			{
				List<Member> members = shareDao.getMembersFromCalendarShares(oldCalendarKeys);
				membersThatBelongToShareLis.addAll(members);
			}
			
			//if it is an update and there are no calendars and no existing shares then we can return
			if (isUpdate&&(shares==null||shares.isEmpty())&&(calendars == null || calendars.isEmpty()))
				return;
			
			
		}
		
		if (calendars != null)
		{
			for (Calendar calendar: calendars)
			{
				//calendar is marked as shared...
				List<ShareCalendar> calendarShares = getCalenderShares(calendar);
				if (calendarShares == null || calendarShares.isEmpty())
					continue;
				
				//check to see if this is a new task or an updated one
				if (isUpdate)
				{ 
					
					//caller of this method should check to make sure something has changed.
					for (ShareCalendar calendarShare:calendarShares)
					{
//						//we need to maintain a list of shareCalendarUsers
						membersThatBelongToShareLis.add(calendarShare.getUser());
						
						//check to see if a share already exists
						Share share = getShareForUserAndTask(task, calendarShare.getUser());
						if (share == null)
						{
							//no share exists so we need to generate one
							Invite invite = calendarShare.getInvite();
							createShare(invite, task, calendarShare.getUser());
						}
						else
						{
							//there is a share so lets remove it from the list
							shares.remove(share);
						}
					}
					
				}
				//new task
				else
				{
					//this is a new task so we need to create shares 
					for (ShareCalendar calendarShare:calendarShares)
					{
						//for every member we want to create a new share
//						Invite invite = calendarShare.getInvite();
//						createShare(invite, task, calendarShare.getUser());
						Share share = getShareForUserAndTask(task, calendarShare.getUser());
						if (share == null)
						{
							//no share exists so we need to generate one
							Invite invite = calendarShare.getInvite();
							createShare(invite, task, calendarShare.getUser());
						}
						else
						{
							//there is a share so lets remove it from the list
							shares.remove(share);
						}
					}
				}
			}

			//lets see if the shares is empty.  It should be...if its not that means a task has been removed from a list that was shared so we need to remve the share
			if (isUpdate&&shares!=null&&!shares.isEmpty())
			{
				for (Share share:shares)
				{
					//we need to make sure that the share is part of the shared list
					if (membersThatBelongToShareLis.contains(share.getUser()))
					{
						//for every share that remains, we need ot make it as delete
						deleteShare(share);
					}
				}
			}
		}
	}
	
	/**
	 * This method will retrieve the ShareCalendars that belong to the calendar.  calendar.id is required to be 
	 * passed.
	 * @param calendar calendar.id is required to be set
	 * @return List<ShareCalendar>
	 */
	@Transactional 
	public List<ShareCalendar> getCalenderShares(Calendar calendar)
	{
		return shareDao.getCalenderShares(calendar);
	}
	
	@Transactional(readOnly=true)
	@CollectTimeMetrics
	public List<Task> getTasksByCalendarDirectLinkTX(String calendarId, String email) throws ValidationException
	{
		Calendar calendar = calendarService.getCalendarById(calendarId);
		
		if (calendar == null)
			throw new ValidationException(ValidationCodeEnum.CALENDAR_NOT_FOUND, new Calendar(calendarId));
		
		if (calendar.isPublicList())
		{
			return taskService.getTaskForCalendar(calendar);
		}
		else
		{
			Member user=null;
			Profile profile = userService.getProfileByEmail(email);
			if (profile!=null)
				user = profile.getMember(); 
			
			//not a valid user so error
			if (user == null)
			{
				throw new ValidationException(ValidationCodeEnum.LOGGED_IN_MEMBER_DOES_NOT_EXIST);		
			}
			
			//check to see if the user trying to access is the owner
			if (calendar.getMember().getId().equals(user.getId()))
			{
				//you are the owner, so there will be no share setup so lets create a mock one			
				return taskService.getTaskForCalendar(calendar);
			}
			
			//grab the share...
			ShareCalendar shareCalendar = getShareCalendarByCalendarAndMember(calendar, user);
			
			//share is not valid so they don't have access
			if (shareCalendar == null)
			{
				throw new ValidationException(ValidationCodeEnum.MEMBER_DOES_NOT_HAVE_AUTHORIZATION);
			}
			
			return taskService.getTaskForCalendar(calendar);
		}
	}
	
	
	/**
	 * This will return the share when it is clicked on.  When a share is created, a notification is sent out
	 * to the sharee with a link.  When the sharee clicks on that link, this method should be called.  It's 
	 * main purpose is to check to see if the person who clicked on the link is allowed to view the share or not.
	 * This done by checking the public status of the task as well as the email address of the user to see if the 
	 * user has access to the share.  
	 * and handle it accordingly.  
	 * @param taskId a valid taskid is required
	 * @param the email address of the user clicking on the link.  This most likely will be pulled from the security context
	 * @return share
	 */
	@Transactional(readOnly=true)
	@CollectTimeMetrics
	public Share getShareDirectLink(String taskId, String email) throws ValidationException
	{
		//get the task by id
		Task task = taskService.getTaskById(taskId);
		
		//if you can't find the task, then error out
		if (task == null)
			throw new ValidationException(ValidationCodeEnum.TASK_NOT_FOUND, new Task(taskId));
		
		Share share;
		Member user=null;
		
		//if the task is public, then wrap it in a share
		if (task.isPublicTask())
		{
			//we need to check if there is a share
			if (email == null || "".equals(email))
			{
				return buildShareWrapper(task, null, ShareApprovedStatus.NO_ACTION);
			}
			
			//try to grab a share
			share = getShareForTaskAndEmail(task, email);
			if (share==null)
				return buildShareWrapper(task, null, ShareApprovedStatus.NO_ACTION);
			else
				return share;
		}
		
		//see if we have a valid user
		if (email == null || "".equals(email))
		{
			return buildShareWrapper(task, null, ShareApprovedStatus.NO_ACTION);
//			throw new ValidationException(ValidationCodeEnum.USER_NOT_LOGGED_IN);
		}
			
			
		Profile profile = userService.getProfileByEmail(email);
		if (profile!=null)
			user = profile.getMember(); 
		
		//not a valid user so error
		if (user == null)
		{
			return buildShareWrapper(task, null, ShareApprovedStatus.NO_ACTION);
//			throw new ValidationException(ValidationCodeEnum.LOGGED_IN_MEMBER_DOES_NOT_EXIST);		
		}

		//check to see if the user trying to access is the owner
		if (task.getTaskCreator().getId().equals(user.getId()))
		{
			//you are the owner, so there will be no share setup so lets create a mock one			
			return buildShareWrapper(task, task.getTaskCreator().getMember(), ShareApprovedStatus.APPROVED);
		}
		
		//grab the share...
		share = getShareForUserAndTask(task, user);
		
		//share is not valid so they don't have access
		if (share == null)
		{
			throw new ValidationException(ValidationCodeEnum.MEMBER_DOES_NOT_HAVE_AUTHORIZATION);
		}
		
		//if the user is a member, then turn off the blur, else it is based on viewed
		if (!user.isGuest())
			share.setBlurred(false);
		else
		{
			share.setBlurred(share.isViewed());
		}
		
		return share;
	}
	
	/**
	 * This is used to wrap the share
	 * @param task
	 * @param member
	 * @param shareApprovedStatus
	 * @return Share
	 */
	private Share buildShareWrapper(Task task, Member member, ShareApprovedStatus shareApprovedStatus)
	{
		Share share = new Share();
		share.setTask(task);
		share.setUser(member);
		share.setShareApprovedStatusOrdinal(shareApprovedStatus.ordinal());
		share.setViewed(false);
		return share;
	}
	
	@Transactional
	public ShareCalendar getShareCalendarByCalendarAndMemberTX(Calendar calendar, Member member)
	{
		return getShareCalendarByCalendarAndMember(calendar, member);
	}
	
	@Transactional
	public ShareCalendar getShareCalendarByCalendarAndMember(Calendar calendar, Member member)
	{
		return shareDao.getShareCalendarByCalendarAndMember(calendar, member);
	}
	
	
	@Transactional
	public Share getShareById(String shareId)
	{
		return shareDao.getShareById(shareId);
	}

}
