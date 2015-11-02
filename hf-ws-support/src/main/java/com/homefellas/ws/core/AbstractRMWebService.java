package com.homefellas.ws.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import com.homefellas.rm.reminder.Alarm;
import com.homefellas.rm.reminder.IReminderServiceTX;
import com.homefellas.rm.repeatsetup.TaskRepeat;
import com.homefellas.rm.share.IShareServiceTX;
import com.homefellas.rm.share.Share;
import com.homefellas.rm.task.ITaskServiceTX;
import com.homefellas.rm.task.Task;
import com.homefellas.ws.model.TaskUI;

public class AbstractRMWebService extends AbstractWebService
{

	private static String sessionEmail=null;
	
	@Autowired
	protected ITaskServiceTX taskService;
	
	@Autowired
	protected IShareServiceTX shareService;
	
	@Autowired
	protected IReminderServiceTX reminderService;
	
	protected List<TaskUI> convertTaskToUI(List<Task> tasks)
	{
		List<TaskUI> taskUIs = new ArrayList<TaskUI>(tasks.size());
		for (Task task : tasks)
		{
			List<Share> shares = shareService.getSharesForTaskTX(task);
			List<Alarm> alarms = reminderService.getAlarmdsyTaskTX(task);
			List<TaskRepeat> taskRepeats = taskService.getTaskRepeatsForTaskTX(task);
			
			taskUIs.add(new TaskUI(task, shares, alarms, taskRepeats));
			
		}
		
		return taskUIs;
	}
	
	public static void setMemberEmail(String email)
	{
		if ("".equals(email))
			sessionEmail =null;
		else
			sessionEmail = email;
	}
	
	protected String getEmailFromSecurityContext()
	{
		Authentication authentication = getAuthenicationFromSecurityContext();
		if (authentication == null)
			return sessionEmail;
		else
			return (String)authentication.getPrincipal();
	}
}
