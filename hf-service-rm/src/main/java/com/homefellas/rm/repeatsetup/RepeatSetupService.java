package com.homefellas.rm.repeatsetup;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.exception.ValidationException;
import com.homefellas.rm.RMValidator;
import com.homefellas.rm.ValidationCodeEnum;
import com.homefellas.rm.repeatsetup.RepeatSetup.RepeatSetupStatus;
import com.homefellas.rm.task.ITaskDao;
import com.homefellas.rm.task.Task;
import com.homefellas.rm.task.TaskService;
import com.homefellas.service.core.AbstractService;

public class RepeatSetupService extends AbstractService {

	@Autowired
	private RMValidator validator;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private IRepeatSetupDao repeatSetupDao;
	
	@Autowired
	private ITaskDao taskDao;
	
	@Transactional
	public RepeatSetup createRepeatSetup(RepeatSetup repeatSetup) throws ValidationException
	{
		//validator to make sure required fields are set
		validator.validateRepeatSetup(repeatSetup);
		
		//need to load the task
		Task task = taskService.getTaskById(repeatSetup.getClonedTask().getId());
		if (task == null)
			throw new ValidationException(ValidationCodeEnum.CLONED_TASK_ID_IS_NULL_OR_NOT_FOUND);
		
		//now clone the task
//		repeatSetup.setClondedTaskAsTask(task);
		
		TaskTemplate taskTemplate = new TaskTemplate(task);
		repeatSetupDao.createTaskTemplate(taskTemplate);
		
		repeatSetup.setClonedTask(taskTemplate);
		
		//now we can save the object
		repeatSetupDao.createRepeatSetup(repeatSetup);
		
		
		//check to see if the task has the revese repeat setup attached.  This is done because of how the sync framework sends repeats
		if (task.getRepeatSetup()==null || !task.getRepeatSetup().getId().equals(repeatSetup.getId()))
		{
			task.setRepeatSetup(repeatSetup);
			taskDao.updateTask(task);
		}
				
		return repeatSetup;
		
	}
	
	@Transactional
	public RepeatSetup updateRepeatSetup(RepeatSetup repeatSetup, String profileId) throws ValidationException
	{
		//check to make sure required fields are set
		validator.validateRepeatSetup(repeatSetup);
		
		RepeatSetup repeatSetupFromDB = repeatSetupDao.getRepeatSetupByID(repeatSetup.getId());
		if (repeatSetupFromDB == null)
			return createRepeatSetup(repeatSetupFromDB);
		
		if (profileId==null || !repeatSetupFromDB.getMember().getEmail().equalsIgnoreCase(profileId))
			return repeatSetup;
		
		validator.validateSynchronizationUpdate(repeatSetup, repeatSetupFromDB);
		repeatSetup.setModifiedDateZone(new DateTime());
		
		repeatSetupDao.updateRepeatSetup(repeatSetupFromDB);	
		
		return repeatSetup;
	}
	
	@Transactional
	public void deleteRepeatSetup(RepeatSetup repeatSetup, String email)
	{
		RepeatSetup repeatSetupFromDB = repeatSetupDao.getRepeatSetupByID(repeatSetup.getId());
		if (repeatSetupFromDB==null || !repeatSetupFromDB.getMember().getEmail().equals(email))
			return;
		
		repeatSetupFromDB.setRepeatSetupStatusOrdinal(RepeatSetupStatus.DELETE.ordinal());
		
		repeatSetupDao.updateRepeatSetup(repeatSetupFromDB);
	}
	
	@Transactional
	public TaskTemplate getTaskTemplateByTaskId(String taskId)
	{
		return repeatSetupDao.getTaskTemplateByTaskId(taskId);
	}
	
	@Transactional
	public List<TaskRepeat> getTaskRepeatsForTask(Task task)
	{
		return repeatSetupDao.getTaskRepeatsForTask(task);
	}
	
	@Transactional
	public RepeatSetup getRepeatSetupById(String id)
	{
		return repeatSetupDao.getRepeatSetupByID(id);
	}
	
	@Transactional
	public TaskTemplate updateTaskTemplate(TaskTemplate taskTemplate)
	{
		return repeatSetupDao.updateTaskTemplate(taskTemplate);
	}
}
