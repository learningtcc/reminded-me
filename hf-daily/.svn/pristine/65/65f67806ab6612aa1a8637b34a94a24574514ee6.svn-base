package me.reminded.launcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.homefellas.rm.notification.IEmailService;
import com.homefellas.rm.task.ITaskService;
import com.homefellas.rm.task.Task;

public abstract class AbstractDigestTasklet implements Tasklet
{

	protected ITaskService taskService;
	
	protected IEmailService emailService;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception
	{
//		taskService
		
		
		processTasks();
		
		
		return RepeatStatus.FINISHED;
	}
	
	public void processTasks()
	{
		List<Task> tasks = getTasks();
		Map<String, List<Task>> digestMap = new HashMap<String, List<Task>>();
		Map<String, DateTime> sendTimeMap = new HashMap<String, DateTime>();
		//loop through and build the list of tasks to send out per user
		for (Task task : tasks)
		{
			String ownerEmail = task.getTaskCreator().getEmail();
			List<Task> taskSubList = digestMap.get(ownerEmail);
			if (taskSubList == null || taskSubList.isEmpty())
				taskSubList = new ArrayList<Task>();
			
			taskSubList.add(task);
			
			digestMap.put(ownerEmail, taskSubList);
			
			DateTime toSendTime = sendTimeMap.get(ownerEmail);
			if (toSendTime == null)
			{
				toSendTime = getToSendTime(task);
				sendTimeMap.put(ownerEmail, toSendTime);
			}
			
			
		}
		
		//now that we have all that broken by user, we can compile the notification to send
		for (String key : digestMap.keySet())
		{
			List<Task> subTasks = digestMap.get(key);
			emailService.sendDigestEmail(subTasks, key, sendTimeMap.get(key));
		}
	}
	public abstract DateTime getToSendTime(Task task);
	public abstract List<Task> getTasks();

	public void setTaskService(ITaskService taskService)
	{
		this.taskService = taskService;
	}

	public void setEmailService(IEmailService emailService)
	{
		this.emailService = emailService;
	}
	
	
}
