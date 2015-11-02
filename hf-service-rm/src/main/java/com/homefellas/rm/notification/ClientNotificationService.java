package com.homefellas.rm.notification;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.batch.Notification;
import com.homefellas.batch.NotificationService;
import com.homefellas.batch.NotificationTypeEnum;
import com.homefellas.batch.PushTypeEnum;
import com.homefellas.exception.ValidationException;
import com.homefellas.rm.RMValidator;
import com.homefellas.rm.ValidationCodeEnum;
import com.homefellas.service.core.AbstractService;
import com.homefellas.user.UserValidationCodeEnum;

public class ClientNotificationService extends AbstractService 
{
	@Autowired
	private RMValidator validator;
	
	@Autowired
	private IClientNotificationDao clientNotificationDao;
	
	@Autowired
	private NotificationService notificationService;
	
	@Transactional
	public Notification pushAppleNotificationTX(Notification notification)
	{
		notification.setPushTypeOrdinal(PushTypeEnum.APPLE.ordinal());
		notification.setNotificationTypeOrdinal(NotificationTypeEnum.PUSH.ordinal());
		notification.setToSendTime(new DateTime().plusSeconds(30).getMillis());
		notification.setSendFrom("neverforget@reminded.me");
		
		notificationService.scheduleNotification(notification);
		
		return notification;
	}
	
//	@Transactional
//	@CollectTimeMetrics
//	public List<Device> getDevicesByEmailTX(String email)
//	{
//		return clientNotificationDao.getDevicesByEmail(email);
//	}
	
//	@Override
//	@Transactional
//	@CollectTimeMetrics
//	public List<ClientNotification> getBulkClientNotificationsX(String taskDelimiter, String loggedInUserEmail) throws ValidationException
//	{
//		if (loggedInUserEmail == null || "".equals(loggedInUserEmail.trim()))
//			throw new ValidationException(UserValidationCodeEnum.USER_MUST_BE_LOGGED_IN);
//		
//		StringTokenizer stringTokenizer = new StringTokenizer(taskDelimiter, ",");
//		List<ClientNotification> list = new ArrayList<ClientNotification>(11);
//		List<String> ids = new ArrayList<String>();
//		while (stringTokenizer.hasMoreTokens())
//		{
//			String id = stringTokenizer.nextToken();
//			ids.add(id);
//		}
//	
//		list = clientNotificationDao.getClientNotificationByIds(ids);
//		return list;
//	}

	@Transactional
	public ClientNotification updateClientNotification( ClientNotification clientNotification, String loggedInUserEmail) throws ValidationException
	{
		if (loggedInUserEmail == null || "".equals(loggedInUserEmail.trim()))
			throw new ValidationException(UserValidationCodeEnum.USER_MUST_BE_LOGGED_IN);
		
		validator.validateClientNotification(clientNotification);
		
		final ClientNotification clientNotificationFromDB = clientNotificationDao.getClientNotificaitonById(clientNotification.getId());
		
		if (!clientNotificationFromDB.getMember().getEmail().equals(loggedInUserEmail))
			throw new ValidationException(ValidationCodeEnum.MEMBER_DOES_NOT_HAVE_AUTHORIZATION);
		
		//check to see if the modified date from the db is before the one being passed
		validator.validateSynchronizationUpdate(clientNotification, clientNotificationFromDB);
		clientNotification.setModifiedDateZone(new DateTime());
		
		//update the task in the db
		clientNotificationDao.updateClientNotification(clientNotificationFromDB);
		
		return clientNotification;
	}

	@Transactional
	public ClientNotification createClientNotification(ClientNotification clientNotification) throws ValidationException
	{
		validator.validateClientNotification(clientNotification);
		
		clientNotificationDao.createClientNotification(clientNotification);
		
		return clientNotification;
	}

//	@Transactional(readOnly = true)
//	@CollectTimeMetrics
//	public List<Device> getBulkDevicesTX(String taskDelimiter, String loggedInUserEmail) throws ValidationException
//	{
//		if (loggedInUserEmail == null || "".equals(loggedInUserEmail.trim()))
//			throw new ValidationException(UserValidationCodeEnum.USER_MUST_BE_LOGGED_IN);
//		
//		StringTokenizer stringTokenizer = new StringTokenizer(taskDelimiter, ",");
//		List<String> ids = new ArrayList<String>();
//		while (stringTokenizer.hasMoreTokens())
//		{
//			String id = stringTokenizer.nextToken();
//			ids.add(id);
//		}
//		
//		return clientNotificationDao.getBulkDevices(ids, loggedInUserEmail);
//		
//	}
	
	@Transactional
	public Device createDevice(Device device) throws ValidationException
	{
		validator.validateDevice(device);
	
		Device deviceFromDb = clientNotificationDao.getDeviceById(device.getId());
		if (deviceFromDb == null)
			clientNotificationDao.createDevice(device);
		
		return device;
	}
	
	@Transactional
	public ClientNotification getClientNotificationById(String id)
	{
		return clientNotificationDao.getClientNotificaitonById(id);
	}
	
	@Transactional
	public Device getDeviceById(String id)
	{
		return clientNotificationDao.getDeviceById(id);
	}
	
	@Transactional
	public List<Notification> getNotificationQueue()
	{
		return clientNotificationDao.getNotificationQueue();
	}
}
