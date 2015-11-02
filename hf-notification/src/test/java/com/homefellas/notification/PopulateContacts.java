package com.homefellas.notification;

import java.util.Random;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.homefellas.batch.INotificationService;
import com.homefellas.dao.core.IDao;
import com.homefellas.exception.DatabaseNotInitializedException;
import com.homefellas.exception.ValidationException;
import com.homefellas.rm.notification.IEmailService;
import com.homefellas.rm.reminder.IReminderServiceTX;
import com.homefellas.rm.share.IShareService;
import com.homefellas.rm.task.ITaskServiceTX;
import com.homefellas.user.IUserServiceTX;
import com.homefellas.user.Member;
import com.homefellas.user.Profile;
import com.homefellas.user.UserService;

public class PopulateContacts
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:com/homefellas/context/notification-context.xml");
//		INotificationLauncherService notificationLauncherService = (INotificationLauncherService)ctx.getBean("notificationLauncherService");
		final ITaskServiceTX taskService = (ITaskServiceTX)ctx.getBean("taskService");
		final IReminderServiceTX reminderService = (IReminderServiceTX)ctx.getBean("reminderService");
		final UserService userService = (UserService)ctx.getBean("userService");
		final IShareService shareService = (IShareService)ctx.getBean("shareService");
		final IEmailService emailService = (IEmailService)ctx.getBean("emailService");
		final INotificationService notificationService = (INotificationService)ctx.getBean("notificationService");
		final IDao dao = (IDao)ctx.getBean("dao");
		
		userService.setGenerateTGT(false);
		
		PlatformTransactionManager transactionManager = (PlatformTransactionManager)ctx.getBean("transactionManager");
		
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
		   @Override
		   protected void doInTransactionWithoutResult(TransactionStatus status) {
			   for (int i=10;i<10000;i++)
			   {
				   try
				{
					   Profile profile = new Profile();
						profile.setName("name"+String.valueOf(new Random().nextInt(100000000)));
						profile.generateUnquieId();
						Member member = new Member();
						member.setEmail("profile"+String.valueOf(new Random().nextInt(100000000))+"@reminded.me");
						member.setPassword("password");
						profile.setMember(member);
					userService.registerBasicMemberTX(profile);
				}
				catch (ValidationException e)
				{
					e.printStackTrace();
				}
				catch (DatabaseNotInitializedException e)
				{
					e.printStackTrace();
				}
			   }
		   }});
	}

}
