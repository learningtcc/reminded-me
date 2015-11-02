package com.homefellas.batch;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.dao.core.AbstractTestDao;
import com.homefellas.model.core.TestModelBuilder;

public class NotificationServiceTest extends AbstractTestDao
{

	@Resource(name="notificationService")
	private NotificationService notificationService;
	
	@Resource(name="notificationDao")
	private INotificationDao notificationDao;

	@Override
	protected void createDatabaseDefaults()
	{
		
	}

	@Test
	public void testCancelNotification()
	{
		INotifiable iNotification1 = new INotifiable() {
			
			@Override
			public String getNotificationId()
			{
				return "1";
			}
			
			@Override
			public String getClassName()
			{
				return "AnnymousInnerClass";
			}

			@Override
			public boolean isCallBack()
			{
				return true;
			}
			
			
		};
		
		INotifiable iNotification2 = new INotifiable() {
			
			@Override
			public String getNotificationId()
			{
				return "2";
			}
			
			@Override
			public String getClassName()
			{
				return "AnnymousInnerClass";
			}

			@Override
			public boolean isCallBack()
			{
				return false;
			}
			
			
		};
		
		Notification notification1 = TestModelBuilder.buildNotification(true, NotificationTypeEnum.EMAIL_SMS, PushTypeEnum.NONE, System.currentTimeMillis(), iNotification1);
		createNotification(notification1);
		
		Notification notification2 = TestModelBuilder.buildNotification(true, NotificationTypeEnum.EMAIL_SMS, PushTypeEnum.NONE, System.currentTimeMillis(), iNotification1);
		notification2.setSentStatusOrdinal(NotificationStatusEnum.SENT.ordinal());
		createNotification(notification2);
		
		Notification notification3 = TestModelBuilder.buildNotification(true, NotificationTypeEnum.EMAIL_SMS, PushTypeEnum.NONE, System.currentTimeMillis(), iNotification2);
		createNotification(notification3);
		
		notificationService.cancelNotification(iNotification1);
		
		List<Notification> notifications = notificationService.getNotificationsToBeSentTX(0, 100);
		for (Notification notification:notifications)
		{
			if (notification.getiNotificationClassName().equals(iNotification1.getClassName()) && notification.getiNotificationId().equals(iNotification1.getNotificationId()) && NotificationStatusEnum.QUEUED.ordinal()==notification.getSentStatusOrdinal())
			{
				Assert.assertEquals(NotificationStatusEnum.CANCELED.ordinal(), notification.getSentStatusOrdinal());
			}
		}
	}
	
	@Transactional
	private void createNotification(Notification notification)
	{
		notificationDao.createNotification(notification);
	}

}
