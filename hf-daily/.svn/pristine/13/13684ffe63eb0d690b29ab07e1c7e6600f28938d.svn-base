package me.reminded.launcher;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.homefellas.rm.task.Task;

public class NightlyDigestTasklet extends AbstractDigestTasklet
{

	@Override
	public DateTime getToSendTime(Task task)
	{
//		String timeZone = task.getEndTimeZone();
//		DateTimeZone dateTimeZone = DateTimeZone.forID(timeZone);
//		return new DateTime(task.getEndTimeMilli(), dateTimeZone).withHourOfDay(23).withMinuteOfHour(59);
		return task.getEndTime().withHourOfDay(23).withMinuteOfHour(59);
	}

	@Override
	public List<Task> getTasks()
	{
		return taskService.getTimelessTasksEndingNotCompleted();
	}

	

}
