package com.homefellas.batch;

import java.util.List;

/**
 * This interface should be implemented by a service for refilling a queue.  The basic idea is that when a notification
 * is pulled from the queue, it needs to grab the next one.  This method instucts the notification service hold to 
 * populate the next record.  The isCallBack method must return true in order for this method to be called.  The
 * model must also implement INotifiable which gives you the isCallBack method.  The callback method in 
 * NotificationLauncherService must be programmed to handle the implementation.  
 * @author Tim Delesio
 * @see INotifiable
 * @see NotificationLauncherService
 *
 */
public interface INotifiableCallbackService
{

	public INotifiable reupNotifications(INotifiable notifiable);
}
