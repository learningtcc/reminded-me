package com.homefellas.rm;

import java.util.ArrayList;
import java.util.List;

import com.homefellas.batch.NotificationTypeEnum;
import com.homefellas.batch.PushTypeEnum;
import com.homefellas.exception.IValidationCode;
import com.homefellas.exception.ValidationException;
import com.homefellas.model.core.AbstractModel;
import com.homefellas.rm.calendar.Calendar;
import com.homefellas.rm.notification.ClientNotification;
import com.homefellas.rm.notification.Device;
import com.homefellas.rm.reminder.Alarm;
import com.homefellas.rm.repeatsetup.RepeatSetup;
import com.homefellas.rm.share.Invite;
import com.homefellas.rm.share.Share;
import com.homefellas.rm.task.Task;
import com.homefellas.service.core.AbstractValidator;
import com.homefellas.user.Member;
import com.homefellas.user.Profile;
import com.homefellas.user.UserValidationCodeEnum;

public class RMValidator extends AbstractValidator
{
	
	public void validateSynchronizationUpdate(ISynchronizeable objectToUpdate, ISynchronizeable objectFromDataSource) throws ValidationException
	{
		if (objectFromDataSource==null || objectToUpdate==null)
			throw new ValidationException(ValidationCodeEnum.SYNC_UPDATE_FAILED_BECAUSE_PASSED_OBJECT_IS_NULL_OR_OBJECT_IN_DB_IS_NULL);
		
		if (objectFromDataSource.getModifiedDate()>objectToUpdate.getModifiedDate())
		{
			if (objectToUpdate.getClientUpdateTimeStamp()==null || objectFromDataSource.getModifiedDate()>objectToUpdate.getClientUpdateTimeStamp().getMillis())
				throw new ValidationException(ValidationCodeEnum.SYNCHRONIZATION_REQUIRED);
		}
		
//		if (isNullOrBlank(objectToUpdate.getLastModifiedDeviceId()))
//			throw new ValidationException(ValidationCodeEnum.NO_DEVICE_ID_PASSED);
		
	}
	
	public void validateSynchronizationUpdate(long dateBeingUpdated, long dateFromDataSource) throws ValidationException
	{
		
		if (dateFromDataSource>dateBeingUpdated)
		{
			throw new ValidationException(ValidationCodeEnum.SYNCHRONIZATION_REQUIRED);
		}
		
//		if (isNullOrBlank(objectToUpdate.getLastModifiedDeviceId()))
//			throw new ValidationException(ValidationCodeEnum.NO_DEVICE_ID_PASSED);
		
	}
	
	protected void validateMemberId(List<IValidationCode> codes, Member member)
	{
		if (member==null||!member.isPrimaryKeySet())
			codes.add(ValidationCodeEnum.MEMBER_ID_IS_NULL);
	}
	
	
	protected void validateMemberId(List<IValidationCode> codes, Profile profile)
	{
		if (profile==null)
			codes.add(ValidationCodeEnum.MEMBER_ID_IS_NULL);
		else
		{
			if (!profile.isPrimaryKeySet())
			{
				if (profile.getMember()==null||!profile.getMember().isPrimaryKeySet())
					codes.add(ValidationCodeEnum.MEMBER_ID_IS_NULL);
			}
		}
	}
	
	protected void validatePrimaryKey(List<IValidationCode> codes, AbstractModel model)
	{
		if (!model.isPrimaryKeySet())
			codes.add(ValidationCodeEnum.PK_NOT_SET);
	}
	
	public void validationMemberIsSet(Member member) throws ValidationException
	{
		List<IValidationCode> codes = new ArrayList<IValidationCode>();
		
		validateMemberId(codes, member);
		
		throwException(codes);
	}
	
	public void validatePrimaryKeyIsSet(AbstractModel model) throws ValidationException
	{
		List<IValidationCode> codes = new ArrayList<IValidationCode>();
		
		validatePrimaryKey(codes, model);
		
		throwException(codes);
	}
	
	public void validateRepeatSetup(RepeatSetup repeatSetup) throws ValidationException
	{
		List<IValidationCode> codes = new ArrayList<IValidationCode>();
		
		validatePrimaryKey(codes, repeatSetup);
		validateMemberId(codes, repeatSetup.getMember());
		
		if (isNullOrBlank(repeatSetup.getRepeatOccurance()))
			codes.add(ValidationCodeEnum.REPEAT_OCCURANCE_IS_NOT_VALID);
		
		if (repeatSetup.getClonedTask()==null || !repeatSetup.getClonedTask().isPrimaryKeySet()) 
			codes.add(ValidationCodeEnum.CLONED_TASK_ID_IS_NULL_OR_NOT_FOUND);
		
		throwException(codes, repeatSetup);
	}
	
	public void validateTask(Task task) throws ValidationException
	{
		List<IValidationCode> codes = new ArrayList<IValidationCode>();
		
		if (task == null)
		{
			codes.add(ValidationCodeEnum.TASK_NOT_FOUND);
			throwException(codes, task);
			return;
		}
		
		validateMemberId(codes, task.getTaskCreator());
		validatePrimaryKey(codes, task);
		
		
		//make sure that task has a title
		if (isNullOrBlank(task.getTitle()))
			codes.add(ValidationCodeEnum.TASK_TITLE_IS_NULL);
		
		//make sure that the end time is after the start time
		if (task.getStartTime()!=null && task.getEndTime()!=null)
		{
			if (task.getEndTime().isBefore(task.getStartTime()))
				codes.add(ValidationCodeEnum.END_TIME_BEFORE_START_TIME);
		}
		
		if (task.getAppleIOSCalEvent() != null)
		{
			try
			{
				task.getAppleIOSCalEvent().validate();
			}
			catch (ValidationException exception)
			{
				codes.addAll(exception.getValidationErrors());
			}
		}
		throwException(codes, task);
	}
	
	public void validateCalendar(Calendar calendar) throws ValidationException
	{
		List<IValidationCode> codes = new ArrayList<IValidationCode>();
		
		validateMemberId(codes, calendar.getMember());
		validatePrimaryKey(codes, calendar);
		
		if (isNullOrBlank(calendar.getCalendarName()))
			codes.add(ValidationCodeEnum.CALENDAR_TITLE_IS_NULL);
		
		throwException(codes, calendar);
		
	}
	
	public void validateAlarm(Alarm alarm) throws ValidationException
	{
		List<IValidationCode> codes = new ArrayList<IValidationCode>();
		
		validatePrimaryKey(codes, alarm);
		if (alarm.getTask()==null || alarm.getTask().getId()==null || "".equals(alarm.getTask().getId()))
			throw new ValidationException(ValidationCodeEnum.TASK_ID_IS_NULL);
		
		if ((alarm.getNotificationType()==NotificationTypeEnum.EMAIL_PUSH.ordinal()
				||alarm.getNotificationType()==NotificationTypeEnum.SMS_PUSH.ordinal()
				||alarm.getNotificationType()==NotificationTypeEnum.ALL.ordinal()
				||alarm.getNotificationType()==NotificationTypeEnum.PUSH.ordinal()
				)&&alarm.getPushType()==PushTypeEnum.NONE.ordinal())
				codes.add(ValidationCodeEnum.PUSH_TYPE_IS_NULL);
		
		throwException(codes, alarm);
	}
	
	public void validateShare(Share share) throws ValidationException
	{
		List<IValidationCode> codes = new ArrayList<IValidationCode>();
		
		if (share == null)
		{
			codes.add(ValidationCodeEnum.SHARE_IS_NULL);
			throwException(codes);
			return;
		}
		
		if (isNullOrBlank(share.getId()))
			codes.add(ValidationCodeEnum.SHARE_ID_IS_NULL);
		
		if (share.getUser() == null || isNullOrBlank(share.getUser().getId()))
			codes.add(ValidationCodeEnum.USER_ID_IS_NULL);
		
		if (share.getTask() == null || isNullOrBlank(share.getTask().getId()))
			codes.add(ValidationCodeEnum.TASK_ID_IS_NULL);
		
		if (isNullOrBlank(share.getId()))
			codes.add(ValidationCodeEnum.SHARE_ID_IS_NULL);
		throwException(codes);
	}
	
	public void validateInvite(Invite invite) throws ValidationException
	{
		List<IValidationCode> codes = new ArrayList<IValidationCode>();
		
		if (invite == null)
		{
			codes.add(ValidationCodeEnum.INVITE_IS_NULL);
			throwException(codes);
			return;
		}
		
		if (invite.getDirectLink()==null)
			codes.add(ValidationCodeEnum.LINK_IS_NULL);
		
		if (isNullOrBlank(invite.getSubject()))
			codes.add(ValidationCodeEnum.SUBJECT_IS_NULL);
		
		if (isNullOrBlank(invite.getMessage()))
			codes.add(ValidationCodeEnum.MESSAGE_IS_NULL);
		
		if (isNullOrBlank(invite.getShareId()))
			codes.add(ValidationCodeEnum.SHARE_ID_IS_NULL);
		
		if (invite.getEmailAddresses()==null||invite.getEmailAddresses().isEmpty()||invite.getEmailAddresses().size()<1)
			codes.add(ValidationCodeEnum.ONE_EMAIL_ADDRESS_IS_REQUIRED_TO_SHARE);
		
		
		throwException(codes);
	}
	
	public void validateClientNotification(ClientNotification clientNotification)
			throws ValidationException
	{
		List<IValidationCode> codes = new ArrayList<IValidationCode>();
		
		validateMemberId(codes, clientNotification.getMember());
		validatePrimaryKey(codes, clientNotification);
		
		if (isNullOrBlank(clientNotification.getReferenceId()))
			codes.add(ValidationCodeEnum.REFERENCE_ID_IS_NULL);
			
		if (isNullOrBlank(clientNotification.getReferenceClassName()))
			codes.add(ValidationCodeEnum.REFERENCE_CLASS_NAME_IS_NULL);
		
		throwException(codes, clientNotification);
		
	}
	
	public void validateDevice(Device device) throws ValidationException
	{
		List<IValidationCode> codes = new ArrayList<IValidationCode>();
		
		validateMemberId(codes, device.getProfile());
		validatePrimaryKey(codes, device);
			
		if (device.getPushTypeOrdinal()<1 || device.getPushTypeOrdinal()>3)
			codes.add(UserValidationCodeEnum.INVALID_PUSH_TYPE);
		throwException(codes);
	}
}
