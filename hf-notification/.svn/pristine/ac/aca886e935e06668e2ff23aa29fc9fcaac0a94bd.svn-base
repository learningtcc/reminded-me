package com.homefellas.notification;

import java.io.File;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NotificationLauncher
{

//	private static ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:com/homefellas/context/notification-context.xml"); 
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

		if (args.length < 1) {
			args = new String[1];
			args[0] = "notification.properties";
			if (!(new File(args[0]).exists()))
			{
				System.out.println("Invalid syntax!\nThis application needs to be invoked this way: \nEmailServices <property file location>\nYou can add this parameter <displaySQL> after the properties file location if you want to display SQL exception on console" );
				System.exit(30);
			}
		}
		
		if (args.length > 1) {
			if (((String)args[1]).equals("displaySQL")) {
//				EmailServices.printSQLException = true;
				System.out.println("WARNING: You have selected to display SQL Exception thrown in the dbEmailRecordsAccessor. This may generate a lot of logs.");
			} else {
				System.out.println("Invalid syntax! Unrecognized 2nd parameter!:<<" + (String)args[1] + ">> \nThis application needs to be invoked this way: \nEmailServices <property file location>\nYou can add this parameter <displaySQL> after the properties file location if you want to display SQL exception on console" );
				System.exit(30);
			}
		}
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:com/homefellas/context/notification-context.xml");
		
		INotificationLauncherService notificationLauncherService = (INotificationLauncherService)ctx.getBean("notificationLauncherService");
//		Object object = ctx.getBean("notificationLauncherService");
//		System.out.println(object);
		notificationLauncherService.startNotification((String)args[0]);
//		NotificationLauncher launcher = new NotificationLauncher();
//		launcher.startNotification((String)args[0]);
	}

//	public static <T>T getBean(String beanName)
//	{
//		return (T)ctx.getBean(beanName);
//	}
	
	
}
