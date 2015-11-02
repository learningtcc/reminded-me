package me.reminded.launcher;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.homefellas.rm.task.Task;

public class MorningDigestTasklet extends AbstractDigestTasklet
{

	@Override
	public DateTime getToSendTime(Task task)
	{
//		String timeZone = task.getEndTimeZone();
//		DateTimeZone dateTimeZone = DateTimeZone.forID(timeZone);
//		return new DateTime(task.getEndTimeMilli(), dateTimeZone).withHourOfDay(5);
		return task.getEndTime().withHourOfDay(5);
	}

	@Override
	public List<Task> getTasks()
	{
		return taskService.getTimelessTasksEnding();
	}
	
	
	

}
