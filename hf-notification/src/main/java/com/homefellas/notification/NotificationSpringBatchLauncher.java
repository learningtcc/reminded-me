package com.homefellas.notification;

import org.springframework.batch.core.launch.support.CommandLineJobRunner;

public class NotificationSpringBatchLauncher
{

	public static void main(String[] args) {
		try
		{
			CommandLineJobRunner.main(new String[]{"classpath:com/homefellas/context/notification-context.xml", "notifyJob"});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
