package com.homefellas.notification;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.TimerTask;

import com.homefellas.batch.INotificationService;

/**
 * @author prc7037
 *
 *	loads the properties from properties file
 */
public class PropertiesLoader extends TimerTask {
	
	private String path;
	private NotificationLauncherService notificationService;
	
	/**
	 * Method PropertiesLoader.
	 * @param aPath
	 * @param aStatusManager
	 */
	public PropertiesLoader(String aPath, NotificationLauncherService aStatusManager) {
		this.path = aPath;
		this.notificationService = aStatusManager; // used for callback when timer ticks
	}
	
	
	/**
	 * 	called by the Timer to reload the properties file
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			NotificationProperties notificationProperties = this.getNotificationProperties();
			this.notificationService.setProperties(notificationProperties);
		} catch (NotificationLauncherException exception) {
			exception.printStackTrace();	
		}
		
	}
	
	
	
	/**
	 * Method getEmailProperties.
	 * 	return an NotificationProperties instace populated from the properties
	 *  file located at path
	 * @return NotificationProperties
	 * @throws NotificationLauncherException
	 */
	public NotificationProperties getNotificationProperties() throws NotificationLauncherException {
		ResourceBundle rb = null;
		NotificationProperties notificationProperties = new NotificationProperties();
		
		try {
			rb = new PropertyResourceBundle(new FileInputStream(path));
		}
		catch (FileNotFoundException fileNotFound) {
			NotificationLauncherException e = new NotificationLauncherException();
			e.setErrorMessage(fileNotFound.toString());
			throw e;
		}

		catch (IOException ioexception) {
			NotificationLauncherException e = new NotificationLauncherException();
			e.setErrorMessage(ioexception.toString());
			throw e;
		} 
		
		// get host
		NotificationProperties.setEmailServerHostName(rb.getString("host"));
		// get active status
		NotificationProperties.setServiceActive(rb.getString("active").equals("Y"));
		// number of worker threads
		notificationProperties.setNumberOfWorkerThreads(Integer.parseInt(rb.getString("numberOfWorkerThreads")));
		
		notificationProperties.setDbNumberOfRecordsRetrieved(Integer.parseInt(rb.getString("dbNumberOfRecordsRetrieved")));
		
		
		notificationProperties.setMaxNumberOfTasksInMemory(Integer.parseInt(rb.getString("maxNumberOfTasksInMemory")));


		notificationProperties.setDbCheckTimer(Integer.parseInt(rb.getString("dbCheckTimer")));

		notificationProperties.setNumberOfTries(Integer.parseInt(rb.getString("numberOfTries")));

		notificationProperties.setPropertiesLoadTimer(Integer.parseInt(rb.getString("propertiesLoadTimer")));
		
		// note that this is a static call as the app checking for this does NOT have an
		// instance of NotificationProperties class to get this information
		NotificationProperties.setSimulationIndicator(rb.getString("simulationIndicator").equals("Y")? true: false);

		NotificationProperties.setPauseTime(Integer.parseInt(rb.getString("taskPauseTime")));
		
//		notificationProperties.setDatabase(rb.getString("database"));
//		notificationProperties.setUsername(rb.getString("dbusername"));
//		notificationProperties.setPassword(rb.getString("dbpassword"));
// gpalk - Table name is EMAIL_QUEUE in all schemas now.
//		notificationProperties.setTableName(rb.getString("dbtablename"));
//		notificationProperties.setSchemaName(rb.getString("dbschemaname"));
		NotificationProperties.setEmailClientsReceivingHTMLBody(rb.getString("emailClientsReceivingHTMLBody"));
		
		notificationProperties.setApnsKeystore(rb.getString("apn_keystore"));
		notificationProperties.setApnsKeyStorePassword(rb.getString("apn_keystore_password"));
		notificationProperties.setProductionMode(Boolean.valueOf(rb.getString("apn_production_mode")));
		
		
//		if (NotificationProperties.isSimulationRun()) {
//		} else {
//		}
		return notificationProperties;
	}

}
