package com.homefellas.notification;

/**
 * @author prc7037
 *
 *	holds all the properties for the email app
 * 	should be refreshed periodically
 */
public class NotificationProperties {
	
	// used to shutdown the app
	private static boolean serviceActive = false;
	
	// host for email
	private static String emailServerHostName;
	
	// number of threads used to consume emailtasks
	private int numberOfWorkerThreads;
	
	// number of db records retrieved at a time
	private int dbNumberOfRecordsRetrieved;
	
	// max number of email tasks awaiting execution
	private int maxNumberOfTasksInMemory;
	
	// number of seconds between each load from db should be a least 30 seconds
	private int dbCheckTimer;
	
	// number of tries maximum in case of failure sending an email
	private int numberOfTries;
	
	// number of seconds between reloading the properties file
	private int propertiesLoadTimer;
	
	// used to test app in simulation mode (without sending any emails)
	private static boolean isSimulationMode = false;
	
	// in simulation mode, time by which to simulate the execution time of an emailTask
	private static int pauseTime;
	
	private String database;
	private String username;
	private String password;
//gpalk - Table name is now EMAIL_QUEUE, which is a constant in EmailConstants
//	private String tableName;
	private String schemaName;
	
	private String apnsKeystore;
	private String apnsKeyStorePassword;
	private boolean productionMode;
	
	private static String emailClientsReceivingHTMLBody;
	

	/**
	 * Method getEmailServerHostName.
	 * Returns the emailServerHostName.
	 * @return String
	 *  static because required in part of the app 
	 */
	public static String getEmailServerHostName() {
		return emailServerHostName;
	}


	/**
	 * Method setEmailServerHostName.
	 * Sets the emailServerHostName.
	 * @param emailServerHostName The emailServerHostName to set
	 */
	public static void setEmailServerHostName(String emailServerHostName) {
		NotificationProperties.emailServerHostName = emailServerHostName;
	}

	/**
	 * Method getNumberOfWorkerThreads.
	 * Returns the numberOfWorkerThreads.
	 * @return int
	 */
	public int getNumberOfWorkerThreads() {
		return numberOfWorkerThreads;
	}


	/**
	 * Method setNumberOfWorkerThreads.
	 * Sets the numberOfThreads.
	 * @param numberOfThreads The numberOfThreads to set
	 */
	public void setNumberOfWorkerThreads(int aNumberOfWorkerThreads) {
		this.numberOfWorkerThreads = aNumberOfWorkerThreads;
	}

	/**
	 * Method isServiceActive.
	 * Returns the serviceActive.
	 * @return boolean
	 */
	public static boolean isServiceActive() {
		return NotificationProperties.serviceActive;
	}

	/**
	 * Method setServiceActive.
	 * Sets the serviceActive.
	 * @param serviceActive The serviceActive to set
	 */
	public static void setServiceActive(boolean serviceActive) {
		NotificationProperties.serviceActive = serviceActive;
	}
	
	/**
	 * Method getDbCheckTimer.
	 * Returns the dbCheckTimer.
	 * @return int
	 */
	public int getDbCheckTimer() {
		return dbCheckTimer;
	}

	/**
	 * Method getDbNumberOfRecordsRetrieved.
	 * Returns the dbNumberOfRecordsRetrieved.
	 * @return int
	 */
	public int getDbNumberOfRecordsRetrieved() {
		return dbNumberOfRecordsRetrieved;
	}

	/**
	 * Method getNumberOfTries.
	 * Returns the numberOfTries.
	 * @return int
	 */
	public int getNumberOfTries() {
		return numberOfTries;
	}

	/**
	 * Method getPropertiesLoadTimer.
	 * Returns the propertiesLoadTimer.
	 * @return int
	 */
	public int getPropertiesLoadTimer() {
		return propertiesLoadTimer;
	}

	/**
	 * Method setDbCheckTimer.
	 * Sets the dbCheckTimer.
	 * @param dbCheckTimer The dbCheckTimer to set
	 */
	public void setDbCheckTimer(int dbCheckTimer) {
		this.dbCheckTimer = dbCheckTimer;
	}

	/**
	 * Method setDbNumberOfRecordsRetrieved.
	 * Sets the dbNumberOfRecordsRetrieved.
	 * @param dbNumberOfRecordsRetrieved The dbNumberOfRecordsRetrieved to set
	 */
	public void setDbNumberOfRecordsRetrieved(int dbNumberOfRecordsRetrieved) {
		this.dbNumberOfRecordsRetrieved = dbNumberOfRecordsRetrieved;
	}

	/**
	 * Method setNumberOfTries.
	 * Sets the numberOfTries.
	 * @param numberOfTries The numberOfTries to set
	 */
	public void setNumberOfTries(int numberOfTries) {
		this.numberOfTries = numberOfTries;
	}

	/**
	 * Method setPropertiesLoadTimer.
	 * Sets the propertiesLoadTimer.
	 * @param propertiesLoadTimer The propertiesLoadTimer to set
	 */
	public void setPropertiesLoadTimer(int propertiesLoadTimer) {
		this.propertiesLoadTimer = propertiesLoadTimer;
	}

	/**
	 * Method getMaxNumberOfTasksInMemory.
	 * Returns the maxNumberOfTasksInMemory.
	 * @return int
	 */
	public int getMaxNumberOfTasksInMemory() {
		return maxNumberOfTasksInMemory;
	}

	/**
	 * Method setMaxNumberOfTasksInMemory.
	 * Sets the maxNumberOfTasksInMemory.
	 * @param maxNumberOfTasksInMemory The maxNumberOfTasksInMemory to set
	 */
	public void setMaxNumberOfTasksInMemory(int maxNumberOfTasksInMemory) {
		this.maxNumberOfTasksInMemory = maxNumberOfTasksInMemory;
	}

	/**
	 * Method setSimulationIndicator.
	 * @param aSimulationIndicator
	 */
	public static void setSimulationIndicator(boolean aSimulationIndicator) {
		NotificationProperties.isSimulationMode = aSimulationIndicator;
	}
	
	/**
	 * Method isSimulationRun.
	 * @return boolean
	 */

	public static boolean isSimulationRun() {
		return NotificationProperties.isSimulationMode;
	}
	

	/**
	 * Method getPauseTime.
	 * Returns the pauseTime.
	 * @return int
	 */
	public static int getPauseTime() {
		return pauseTime;
	}

	/**
	 * Method setPauseTime.
	 * Sets the pauseTime in seconds
	 * @param pauseTime The pauseTime to set
	 */
	public static void setPauseTime(int pauseTime) {
		NotificationProperties.pauseTime = pauseTime*1000;
	}

	/**
	 * Method getDatabase.
	 * Returns the database.
	 * @return String
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * Method getPassword.
	 * Returns the password.
	 * @return String
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Method getUsername.
	 * Returns the username.
	 * @return String
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Method setDatabase.
	 * Sets the database.
	 * @param database The database to set
	 */
	public void setDatabase(String database) {
		this.database = database;
	}

	/**
	 * Method setPassword.
	 * Sets the password.
	 * @param password The password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Method setUsername.
	 * Sets the username.
	 * @param username The username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

/**
 * Method getSchemaName.
 * @return String
 */
//	/**
//	 * Returns the tableName.
//	 * @return String
//	 */
//	public String getTableName() {
//		return tableName;
//	}
//
//	/**
//	 * Sets the tableName.
//	 * @param tableName The tableName to set
//	 */
//	public void setTableName(String tableName) {
//		this.tableName = tableName;
//	}

	/**
	 * Returns the schemaName.
	 * @return String
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * Method setSchemaName.
	 * Sets the schemaName.
	 * @param schemaName The schemaName to set
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	/**
	 * @return Returns the emailClientsReceivingHTMLBody.
	 */
	public static String getEmailClientsReceivingHTMLBody() {
		return emailClientsReceivingHTMLBody;
	}
	/**
	 * @param emailClientsReceivingHTMLBody The emailClientsReceivingHTMLBody to set.
	 */
	public static void setEmailClientsReceivingHTMLBody(
			String emailClients) {
		emailClientsReceivingHTMLBody = emailClients;
	}


	public String getApnsKeystore()
	{
		return apnsKeystore;
	}


	public void setApnsKeystore(String apnsKeystore)
	{
		this.apnsKeystore = apnsKeystore;
	}


	public String getApnsKeyStorePassword()
	{
		return apnsKeyStorePassword;
	}


	public void setApnsKeyStorePassword(String apnsKeyStorePassword)
	{
		this.apnsKeyStorePassword = apnsKeyStorePassword;
	}


	public boolean isProductionMode()
	{
		return productionMode;
	}


	public void setProductionMode(boolean productionMode)
	{
		this.productionMode = productionMode;
	}
	
	
}
