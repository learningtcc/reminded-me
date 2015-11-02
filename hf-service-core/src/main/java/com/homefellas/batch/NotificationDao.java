package com.homefellas.batch;

import java.util.List;

import org.hibernate.Query;
import org.joda.time.DateTime;

import com.homefellas.dao.hibernate.core.HibernateCRUDDao;

public class NotificationDao extends HibernateCRUDDao implements INotificationDao
{
	public Notification getNotificationBySendToAndDateTime(String toEmail, DateTime dateTime)
	{
		Query query = getQuery("from Notification n where n.sendTo=? and n.toSendTime=? and n.sentStatusOrdinal=?");

		query.setString(0, toEmail);
		query.setLong(1, dateTime.getMillis());
		query.setInteger(2, NotificationStatusEnum.QUEUED.ordinal());
		
		return (Notification)query.uniqueResult();
	}

	public List<String> getDeviceIdsForDailyDYKNotification(PushTypeEnum pushTypeEnum)
	{
		Query query = getQuery("select d.id from Device d where profile.dailyDYKNotification = true and d.pushTypeOrdinal="+pushTypeEnum.ordinal());
		
		return query.list();
	}
	
	public List<String> getDeviceIdsByEmail(String email)
	{
		Query query = getQuery("select d.id from Device d where profile.member.email = ?");
		query.setString(0, email);
		
		return query.list();
	}
	
	public List<Notification> getNotificationsForINotification(INotifiable notification)
	{
		Query query = getQuery("from Notification n where n.iNotificationId=? and n.iNotificationClassName=? and n.sentStatusOrdinal=?");

		query.setString(0, (String)notification.getNotificationId());
		query.setString(1, notification.getClassName());
		query.setInteger(2, NotificationStatusEnum.QUEUED.ordinal());
		
		return query.list();
	}
	
	public List<Notification> getNotificationsForINotification(INotifiable notification, String toEmail)
	{
		Query query = getQuery("from Notification n where n.iNotificationId=? and n.iNotificationClassName=? and n.sentStatusOrdinal=? and n.sendTo=?");

		query.setString(0, (String)notification.getNotificationId());
		query.setString(1, notification.getClassName());
		query.setInteger(2, NotificationStatusEnum.QUEUED.ordinal());
		query.setString(3, toEmail);
		
		return query.list();
	}
	
	public Notification getNotificationToBeSent(long pollTime)
	{
//		Query query = getQuery("from Notification n where (n.sentStatusOrdinal=? or n.sentStatusOrdinal=?) and n.toSendTime <= ? order by n.priority, n.toSendTime, n.id");
		Query query = getQuery("from Notification n where (n.sentStatusOrdinal=?) and n.toSendTime <= ? order by n.priority, n.toSendTime, n.id");
		query.setInteger(0, NotificationStatusEnum.QUEUED.ordinal());
//		query.setInteger(1, NotificationStatusEnum.FAILED.ordinal());
		query.setLong(1, pollTime);
		query.setMaxResults(1);
		return (Notification)query.uniqueResult();
	}
	
	public List<Notification> getNotificationsToBeSent(long pollTime, int maxResults)
	{
//		Query query = getQuery("from Notification n where (n.sentStatusOrdinal=? or n.sentStatusOrdinal=?) and n.toSendTime <= ? order by n.priority, n.toSendTime, n.id");
		Query query = getQuery("from Notification n where (n.sentStatusOrdinal=?) and n.toSendTime <= ? order by n.priority, n.toSendTime, n.id");
		query.setInteger(0, NotificationStatusEnum.QUEUED.ordinal());
//		query.setInteger(1, NotificationStatusEnum.FAILED.ordinal());
		query.setLong(1, pollTime);
		query.setMaxResults(maxResults);
		return query.list();
	}
	
	public List<Notification> getNotificationsToEmail(String toEmail)
	{
		Query query = getQuery("from Notification n where n.sentStatusOrdinal=? and n.sendTo=?");

		query.setInteger(0, NotificationStatusEnum.QUEUED.ordinal());
		query.setString(1, toEmail);
		
		return query.list();
	}

	@Override
	public Notification createNotification(Notification notification) {
		save(notification);
		
		return notification;
	}

	@Override
	public Notification updateNotification(Notification notification) {
		updateObject(notification);
		
		return notification;
	}
	
	
}
