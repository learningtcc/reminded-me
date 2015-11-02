package com.homefellas.rm.repeatsetup;

import java.util.List;

import org.hibernate.Query;

import com.homefellas.rm.task.Task;

public interface IRepeatSetupDao {

	//creat
	public RepeatSetup createRepeatSetup(RepeatSetup repeatSetup);
	public TaskTemplate createTaskTemplate(TaskTemplate taskTemplate);
	
	//read
	public RepeatSetup getRepeatSetupByID(String id);
	public TaskTemplate getTaskTemplateByTaskId(String taskId);
	public List<TaskRepeat> getTaskRepeatsForTask(Task task);
	
	//update
	public RepeatSetup updateRepeatSetup(RepeatSetup repeatSetup);
	public TaskTemplate updateTaskTemplate(TaskTemplate taskTemplate);
}
