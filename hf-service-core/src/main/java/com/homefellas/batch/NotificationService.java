package com.homefellas.batch;

import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.metrics.CollectTimeMetrics;
import com.homefellas.service.core.AbstractService;

public class NotificationService extends AbstractService
{

	protected INotificationDao notificationDao;

	public void setNotificationDao(INotificationDao notificationDao)
	{
		this.notificationDao = notificationDao;
	}
	
	public INotificationDao getNotificationDao()
	{
		return notificationDao;
	}	
	
	
	public Notification getNotificationBySendToAndDateTime(String toEmail, DateTime dateTime) {
		return notificationDao.getNotificationBySendToAndDateTime(toEmail, dateTime);
	}

	public void sendDailyAppleDYKNotificaiton(String message, String iNotificaitonClassName)
	{
		List<String> ids = notificationDao.getDeviceIdsForDailyDYKNotification(PushTypeEnum.APPLE);
		
		if (message!=null && message.length()>100)
		{
			message = message.substring(0, 99).concat("...");
		}
		
		DateTime dateTime = new DateTime();
		dateTime = dateTime.withHourOfDay(13).withMinuteOfHour(0);
		for (String id:ids)
		{
			Notification notification = new Notification();
			notification.setPushTypeOrdinal(PushTypeEnum.APPLE.ordinal());
			notification.setNotificationTypeOrdinal(NotificationTypeEnum.PUSH.ordinal());
			notification.setToSendTime(dateTime.getMillis());
			notification.setSendFrom("neverforget@reminded.me");
			notification.setSendTo(id);
			notification.setBody(message);
			notification.setiNotificationId(id);
			notification.setiNotificationClassName(iNotificaitonClassName);
			scheduleNotification(notification);
			
		}
	}


	public void cancelNotification(Notification notification)
	{
		notification.setSentStatusOrdinal(NotificationStatusEnum.CANCELED.ordinal());
		notification.setDateSent(new DateTime().getMillis());
		notificationDao.updateNotification(notification);
	}
	
	/**
	 * This will return all the notifications that are in the queued status, and that match the inotification class and id.
	 * @param inotification This is the classt that implements the inotification
	 */
	public void cancelNotification(INotifiable inotification)
	{
		List<Notification> notifications = notificationDao.getNotificationsForINotification(inotification);
		
		if (notifications==null || notifications.isEmpty())
			return;
		
		for (Notification notification:notifications)
		{
			notification.setSentStatusOrdinal(NotificationStatusEnum.CANCELED.ordinal());
			notification.setDateSent(new DateTime().getMillis());
			notificationDao.updateNotification(notification);
		}
	}
	
	public void scheduleNotification(Notification iNotification)
	{
		notificationDao.createNotification(iNotification);
		
	}
	
	public void scheduleNotifications(List<? extends Notification> iNotifications)
	{
		for (Notification notification :iNotifications)
			scheduleNotification(notification);
	}
	
	@Transactional
	@CollectTimeMetrics
	public List<Notification> getNotificationsForINotificationTX(INotifiable inotifiable)
	{
		return notificationDao.getNotificationsForINotification(inotifiable);
	}
	
	@Transactional
	@CollectTimeMetrics
	public List<Notification> getNotificationsForINotificationAndToEmailTX(INotifiable inotifiable, String toEmail)
	{
		return notificationDao.getNotificationsForINotification(inotifiable, toEmail);
	}
	
	@Transactional
	@CollectTimeMetrics
	public List<Notification> getNotificationsToEmailTX(String toEmail)
	{
		return notificationDao.getNotificationsToEmail(toEmail);
	}

	@Transactional
	@CollectTimeMetrics
	public Notification getNotificationToBeSentTX(long pollTime)
	{
		return notificationDao.getNotificationToBeSent(pollTime);
	}


	@Transactional
	@CollectTimeMetrics
	public List<Notification> getNotificationsToBeSentTX(long pollTime, int maxResults)
	{
		return notificationDao.getNotificationsToBeSent(pollTime, maxResults);
	}
	
	
//	public Object pollForNotification() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException
//	{
//		return notificationDao.getNotificationToBeSent(System.currentTimeMillis()+pollTimeInSeconds);
//	}
	
//	public void processNotifications(List<Notification> notifications)
//	{
//		boolean failure=true;
//		
//	}
	
	
	
//	public void markAsFailed(Notification notification)
//	{
//		notification.setSentStatusOrdinal(NotificationStatusEnum.FAILED.ordinal());
//		
//		dao.updateObject(notification);
//	}
	
//	public Notification markAsSent(Notification notification)
//	{
//		
//	}


//	@Override
//	public Notification process(Notification notification) throws Exception
//	{
//		
//		
//		return notification;
//	}





	


	
	
}
