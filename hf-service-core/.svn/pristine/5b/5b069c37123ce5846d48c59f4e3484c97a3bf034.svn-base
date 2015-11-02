package com.homefellas.batch;


/**
 * INotifiable is used to mark a model as being able to be sent as a notification.  To add a new object as this class you need to 
 * have it implement this interface.
 * 1.  Implement the following interfaces
 * 2.  Mark the methods as @JsonIgnore
 * @author Tim Delesio
 *
 */
public interface INotifiable
{

	//this is the id of the callback.  Typical implementation is return String.valueOf(getId());
	public String getNotificationId();
	
	//this is class name of the callback  Typical implementation is return getClass().getName();
	public String getClassName();  
	
	//this is used if more needs to be added to the queue.  It uses the class name and the id to retrieve the record from the DB. This should be set to false if its a one time notification
	public boolean isCallBack();
}
