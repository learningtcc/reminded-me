package com.homefellas.notification;

import java.util.Date;
import java.util.List;
import java.util.Timer;

import org.joda.time.DateTime;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.homefellas.batch.INotifiable;
import com.homefellas.batch.INotifiableCallbackService;
import com.homefellas.batch.Notification;
import com.homefellas.batch.NotificationService;
import com.homefellas.queue.ConcreteQueue;
import com.homefellas.rm.notification.Device;
import com.homefellas.rm.reminder.IReminderService;
import com.homefellas.rm.task.ITaskService;

public class NotificationLauncherService extends NotificationService implements INotificationLauncherService
{
	private PropertiesLoader propertiesLoader;
	private NotificationProperties notificationProperties;
	private Timer propertiesReloadTask;
	private ConcreteQueue cq;
	private Timer dbReloadTask;
	private NotificationTask notificationTask;
	private JavaMailSenderImpl mailSender;
	private String defaultUserName;
	private String defaultPassword;
	private INotifiableCallbackService notifiableCallback;
	private IReminderService reminderService;
	private ITaskService taskService;
	
	
	
	
//	public NotificationLauncherService(NotificationService notificationService)
//	{
//		this.dao = notificationService.getDao();
//		this.notificationDao = notificationService.getNotificationDao();
//		this.transactionManager = notificationService.getTransactionManager();
//	}
	
	
	


	public void setReminderService(IReminderService reminderService)
	{
		this.reminderService = reminderService;
	}



	public void setTaskService(ITaskService taskService)
	{
		this.taskService = taskService;
	}



	public void setMailSender(JavaMailSenderImpl mailSender)
	{
		this.mailSender = mailSender;
	}



	public void setDefaultUserName(String defaultUserName)
	{
		this.defaultUserName = defaultUserName;
	}



	public void setDefaultPassword(String defaultPassword)
	{
		this.defaultPassword = defaultPassword;
	}







	public void startNotification(String propertiesFileLocation)
	{
		try {
			// pass the path to the properties file
			propertiesLoader = new PropertiesLoader(propertiesFileLocation, this);
			
			notificationProperties = propertiesLoader.getNotificationProperties();
			propertiesReloadTask = new Timer();
			propertiesReloadTask.schedule(propertiesLoader, // what to schedule
					notificationProperties.getPropertiesLoadTimer()*1000, 			//initial delay
					notificationProperties.getPropertiesLoadTimer()*1000);			// subsequent delay
			
		} catch (NotificationLauncherException exception) {
			System.out.println("Error while retreiving the properties file for the email services");
			System.out.println(exception.getErrorMessage());
			System.exit(10);
		}
		
		// check if tried to start while service is marked as non active in prop file
		if (!NotificationProperties.isServiceActive()) {
			System.out.println("WARNING ATTEMPTING TO START THE SERVICE BUT ACTIVE STATUS SET TO FALSE! EXITING NOW...");
			System.exit(20)	;
		}
		
		// looks like we are good to go, instantiate a concrete queue
		cq = new ConcreteQueue(notificationProperties.getNumberOfWorkerThreads(), notificationProperties.getMaxNumberOfTasksInMemory());
		
		//populate the list from db immediately (not using the timer task on the first time)
		this.addNotificationToQueue();
		
		//create notificationTask
		notificationTask = new NotificationTask(this);
		
		//schedule the task that will reload the records from db
		this.dbReloadTask = new Timer();
		this.dbReloadTask.schedule(this.notificationTask, notificationProperties.getDbCheckTimer()*1000, notificationProperties.getDbCheckTimer()*1000);
	}
	
	public void deleteDevice(final String deviceId)
	{
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
		   @Override
		   protected void doInTransactionWithoutResult(TransactionStatus status) {
			   Device deviceToDelete = dao.loadByPrimaryKey(Device.class, deviceId);
               if (deviceToDelete!=null)
             	  dao.deleteObject(deviceToDelete);
		
		   }});
	}
	
	public List<String> getDevicesByEmailTX(final String email)
	{
		List<String> devices;
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		devices = transactionTemplate.execute(new TransactionCallback<List<String>>() {
			 @Override
			   public List<String> doInTransaction(TransactionStatus status) {
				   return notificationDao.getDeviceIdsByEmail(email);
			   }
		});
		
		return devices;
	}
	
	/**
	 * @see com.hertz.irac.emailservices.EmailStatusManager#addEmailsToQueue(ArrayList)
	 */
	/**IMPLEMENTATION OF THE INTERFACE EmailStatusManager
	 *  call back for the timer task
	 *  put emails retrieved from db and create email task from them and put them
	 *  in the queue to be consumed by worker threads
	 * 
	 */
	public void addNotificationToQueue() 
	{
		final NotificationLauncherService launcher = this;
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
		   @Override
		   protected void doInTransactionWithoutResult(TransactionStatus status) {
				
		List<Notification> notifications = notificationDao.getNotificationsToBeSent(new DateTime().plusSeconds(notificationProperties.getDbCheckTimer()).getMillis(), notificationProperties.getDbNumberOfRecordsRetrieved());	
			for (Notification notification:notifications) 
			{
				NotificationTask notificationTask = new NotificationTask(notification, launcher); // pass self for call back through interface
				cq.put(notificationTask);
			}
		   }});
	}
	
	
	public void callback(final Notification notification)
	{
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
		   @Override
		   protected void doInTransactionWithoutResult(TransactionStatus status) {
			   
			   Class clazz;
			try
			{
//System.out.println("in call back");				
				clazz = Class.forName(notification.getiNotificationClassName());
				INotifiable notifiable=null;
				try
				{
					notifiable = (INotifiable)dao.loadByPrimaryKey(clazz, notification.getiNotificationId());
				}
				catch (Exception exception)
				{
					logger.error(clazz+" with id of "+notification.getiNotificationId()+" does not exist in the db.");
					exception.printStackTrace();
				}
//System.out.println("notifiable="+notifiable);				
				if (notifiable!=null&&notifiable.isCallBack())
				{
					System.out.println("ATEEMPTING RE-UP"+notifiable.getClass().getName()+" with id:"+notifiable.getNotificationId());
//					if (notification.getiNotificationClassName().equals(Reminder.class.getName()))
//						notifiableCallback = reminderService;
//					else if (notification.getiNotificationClassName().equals(Task.class.getName()))
//						notifiableCallback = taskService;

					notifiable = notifiableCallback.reupNotifications(notifiable);
//System.out.println("successfull called the reup!");					
				}
				else
				{
//System.out.println("no callback required");
				}
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			   
		   }});
	}
	
	public void updateNotification(final Notification notification)
	{
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
		   @Override
		   protected void doInTransactionWithoutResult(TransactionStatus status) {
			   dao.updateObject(notification);
//System.out.println("updated the notification");			   
		   }});
	}
	
	public void setProperties(NotificationProperties properties)
	{
		System.out.println("**" + new Date(System.currentTimeMillis()).toString() + " call back from the properties loader timer task");
		if (properties != null) {
			System.out.println("switching the reference of emailProperties now");
			this.notificationProperties = properties;
			System.out.println("is system active?: " +  NotificationProperties.isServiceActive());
			if (! NotificationProperties.isServiceActive()) {
				this.initiateShutdown();
			}
		}
	}
	
	/**
	 * @see com.hertz.irac.emailservices.EmailStatusManager#isAddingEmailsAllowed()
	 */
	/**IMPLEMENTATION OF THE INTERFACE EmailStatusManager
	 *  protects agains too aggressive timing of db loads which could lead to running out of memory
	 *  should check number of awaiting records in queue if greater than some number (read from properties file?)
	 *  don't reload
	 *  check the size of the vector
	 */
	public boolean isAddingEmailsAllowed() {

		return ! cq.isQueueFull();
		
	}
	
	/**
	 * Method initiateShutdown.
	 *  shuts down the app
	 */
	private void initiateShutdown() {
		System.out.println("Canceling the timer tasks... going down.. ");
		
		this.cq.setShutdown(true);
		this.propertiesReloadTask.cancel();
		this.dbReloadTask.cancel();
		
	}
	
	public void sendEmail(MimeMessagePreparator mimeMessagePreparator, String replyToEmailAddress)
	{
		this.mailSender.setUsername(defaultUserName);
		this.mailSender.setPassword(defaultPassword);
		this.mailSender.send(mimeMessagePreparator);
		
	}



	public NotificationProperties getNotificationProperties()
	{
		return notificationProperties;
	}

}
