package me.reminded.launcher;

import java.sql.Date;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.homefellas.batch.INotificationService;
import com.homefellas.rm.dyk.DidYouKnow;
import com.homefellas.rm.dyk.IDidYouKnowService;
import com.homefellas.rm.notification.Device;

public class DailyDidYouKnowTasklet implements Tasklet
{

	private INotificationService notificationService;
	private IDidYouKnowService didYouKnowService;
	
	
	
	public void setDidYouKnowService(IDidYouKnowService didYouKnowService)
	{
		this.didYouKnowService = didYouKnowService;
	}



	public void setNotificationService(INotificationService notificationService)
	{
		this.notificationService = notificationService;
	}



	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception
	{
		
		List<DidYouKnow> didYouKnows = didYouKnowService.getValidDidYouKnowMessages(new Date(System.currentTimeMillis()));
		if (didYouKnows==null || didYouKnows.isEmpty())
			return RepeatStatus.FINISHED;
		
		DidYouKnow didYouKnow = didYouKnows.get(0);
		if (didYouKnow == null)
			return RepeatStatus.FINISHED;
		
		 notificationService.sendDailyAppleDYKNotificaiton(didYouKnow.getMessage(), Device.class.getName());
	    
		 return RepeatStatus.FINISHED;
	}

}
