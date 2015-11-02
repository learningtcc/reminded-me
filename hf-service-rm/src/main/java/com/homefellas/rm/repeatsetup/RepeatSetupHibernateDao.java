package com.homefellas.rm.repeatsetup;

import java.util.List;

import org.hibernate.Query;

import com.homefellas.dao.hibernate.core.HibernateCRUDDao;
import com.homefellas.rm.task.Task;

public class RepeatSetupHibernateDao extends HibernateCRUDDao implements
		IRepeatSetupDao {

	@Override
	public RepeatSetup createRepeatSetup(RepeatSetup repeatSetup) {
		save(repeatSetup);
		
		return repeatSetup;
	}

	@Override
	public TaskTemplate createTaskTemplate(TaskTemplate taskTemplate) {
		save(taskTemplate);
		
		return taskTemplate;
	}

	@Override
	public RepeatSetup getRepeatSetupByID(String id) {
		return loadByPrimaryKey(RepeatSetup.class, id);
	}

	@Override
	public RepeatSetup updateRepeatSetup(RepeatSetup repeatSetup) {
		updateObject(repeatSetup);
		
		return repeatSetup;
	}
	
	@Override
	public TaskTemplate updateTaskTemplate(TaskTemplate taskTemplate) {
		updateObject(taskTemplate);
		
		return taskTemplate;
	}
	
	@Override
	public TaskTemplate getTaskTemplateByTaskId(String taskId)
	{
		Query query = getQuery("from TaskTemplate tt where tt.orginalTaskId=?");
		query.setString(0, taskId);
		
		return (TaskTemplate)query.uniqueResult();
	}

	@Override
	public List<TaskRepeat> getTaskRepeatsForTask(Task task)
	{
		String queryString = "from TaskRepeat tr where tr.task.id = ?";
		Query query = getQuery(queryString);
		query.setString(0,task.getId());
		
		return query.list();
	}
	
}
