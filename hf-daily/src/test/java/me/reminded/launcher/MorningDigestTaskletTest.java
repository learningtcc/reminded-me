package me.reminded.launcher;

import static com.homefellas.user.UserTestModelBuilder.profile;
import static org.junit.Assert.assertEquals;
import static org.springframework.batch.core.BatchStatus.COMPLETED;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.exception.DatabaseNotInitializedException;
import com.homefellas.exception.ValidationException;
import com.homefellas.rm.RMTestModelBuilder;
import com.homefellas.rm.task.ITaskServiceTX;
import com.homefellas.rm.task.Task;
import com.homefellas.user.IUserServiceTX;
import com.homefellas.user.Profile;
import com.homefellas.user.UserTestModelBuilder;

public class MorningDigestTaskletTest
{

	
	JobLauncher jobLauncher;
	Job job;
	
	private MorningDigestTasklet morningDigestTasklet;
	private ITaskServiceTX taskService;
	private IUserServiceTX userService;
	
	@Before
	public void setUp(){
	ApplicationContext context = new ClassPathXmlApplicationContext("jobs/dailyDYK.xml");
	jobLauncher = (JobLauncher) context.getBean("jobLauncher");
	job = (Job) context.getBean("morningDigest");
	morningDigestTasklet = (MorningDigestTasklet)context.getBean("morningDigestTasklet");
	taskService = (ITaskServiceTX)context.getBean("taskService");
	userService = (IUserServiceTX)context.getBean("userService");
	}

//	@Test
//	@Transactional
//	public void jobShouldRunSuccessfully() 
//	{
//		Profile profile1 = createProfile();
//		
//		
//		Task p1task1 = RMTestModelBuilder.task(profile1);
//		p1task1.setEndTime(new DateTime().plusDays(1));
//		p1task1.setTimeLessTask(true);
//		p1task1 = createTask(p1task1);
//		
//		Task p1task2 = RMTestModelBuilder.task(profile1);
//		p1task2.setEndTime(new DateTime().plusDays(1).plusMinutes(22).plusHours(2));
//		p1task2.setTimeLessTask(true);
//		p1task2 = createTask(p1task2);
//		
//		Profile profile2 = createProfile();
//		
//		Task p2task1 = RMTestModelBuilder.task(profile2);
//		p2task1.setEndTime(new DateTime().plusDays(1));
//		p2task1.setTimeLessTask(true);
//		p2task1 = createTask(p2task1);
//		
//		Task p2task2 = RMTestModelBuilder.task(profile2);
//		p2task2.setEndTime(new DateTime().plusDays(1).plusMinutes(2).plusHours(1));
//		p2task2.setTimeLessTask(true);
//		p2task2 = createTask(p2task2);
//
//		
//		Task unTimelessTask = RMTestModelBuilder.task(profile1);
//		unTimelessTask.setEndTime(new DateTime().plusDays(1).plusMinutes(2).plusHours(1));
//		unTimelessTask = createTask(unTimelessTask);
//		
////		morningDigestTasklet.processTasks();
//		
//	}
	
	@Test
	public void testJob() throws JobInstanceAlreadyCompleteException, JobParametersInvalidException, JobRestartException, JobExecutionAlreadyRunningException 
	{
		JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
		
		
		assertEquals(COMPLETED, jobExecution.getStatus());
	}
	
	protected Task createTask(Task task)
	{
		try
		{
			taskService.createTaskTX(task);
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
		}
		 
		 return task;
	}
	
	protected Profile createProfile()
	{
		return createProfile(UserTestModelBuilder.profile());
	}
	
	protected Profile createProfile(Profile profile)
	{
		try
		{
			return userService.registerBasicMemberTX(profile);
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
			return null;
		}
		catch (DatabaseNotInitializedException e)
		{
			Assert.fail(e.getMessage());
			return null;
		}
	}
	protected Profile createProfile(String email)
	{
		try
		{
			return userService.registerBasicMemberTX(profile(email));
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
			return null;
		}
		catch (DatabaseNotInitializedException e)
		{
			Assert.fail(e.getMessage());
			return null;
		}
	}
}
