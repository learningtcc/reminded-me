package com.homefellas.batch;

import java.util.List;

import org.joda.time.DateTime;

public interface INotificationDao
{

	
	
	//create
	public Notification createNotification(Notification notification);
	
	//read
	public List<Notification> getNotificationsForINotification(INotifiable notification);
	public List<Notification> getNotificationsForINotification(INotifiable notification, String toEmail);
	public Notification getNotificationToBeSent(long pollTime);
	public List<Notification> getNotificationsToBeSent(long pollTime, int maxResults);
	public List<String> getDeviceIdsByEmail(String email);
	public List<String> getDeviceIdsForDailyDYKNotification(PushTypeEnum pushTypeEnum);
	public List<Notification> getNotificationsToEmail(String toEmail);
	public Notification getNotificationBySendToAndDateTime(String toEmail, DateTime dateTime);
	
	//update
	public Notification updateNotification(Notification notification);
}
