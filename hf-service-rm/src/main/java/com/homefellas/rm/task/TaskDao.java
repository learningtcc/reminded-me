package com.homefellas.rm.task;

import java.util.List;

import org.hibernate.Query;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.homefellas.dao.hibernate.core.HibernateCRUDDao;
import com.homefellas.exception.ValidationException;
import com.homefellas.rm.RMValidator;
import com.homefellas.rm.ValidationCodeEnum;
import com.homefellas.rm.calendar.Calendar;
import com.homefellas.rm.repeatsetup.RepeatSetup;
import com.homefellas.rm.repeatsetup.TaskTemplate;
import com.homefellas.rm.task.AbstractTask.ProgressEnum;
import com.homefellas.user.Profile;

public class TaskDao extends HibernateCRUDDao implements ITaskDao {

	
//	public void updateTaskWithParent(Task task, boolean aParent)
//	{
//		Query query = getQuery("update Task t set t.aParent=? where t.id=?");
//		query.setBoolean(0, aParent);
//		query.setString(1, task.getId());
//		System.out.println(task.getId());
//		System.out.println(query.executeUpdate());
//	}
//	
	/* (non-Javadoc)
	 * @see com.homefellas.rm.task.ITaskDao#getGenericCalendars()
	 */
	
	@Autowired
	private RMValidator validator;
	
	
	
	@Override
	public List<Task> getUpcomingTasks(String email, int chunkSize, int startIndex)
	{
		String queryString = "from Task t " +
		"inner join fetch t.taskCreator tc " +
//		"left join fetch t.alarms " +
		"where tc.member.email=? and t.endTimeMilli is not null and t.endTimeMilli > ?)" +
		"order by t.priority, t.sortTimeStampMilli, t.createdDate, t.sortOrder";
		
		Query query = getQuery(queryString);query.setString(0,email);
		query.setLong(1, new DateTime().getMillis());
		
		query.setMaxResults(chunkSize);
		query.setFirstResult(startIndex);
		
		return query.list();
	}
	
	@Override
	public List<Task> getDatelessOverdueTasksForMember(String email, int chunkSize, int startIndex)
	{	
		String queryString = "from Task t " +
				"inner join fetch t.taskCreator tc " +
				"where tc.member.email=? and t.progress != ? and t.progress != ? and (t.endTimeMilli is null or ? > t.endTimeMilli)" +
				"order by t.priority, t.sortTimeStampMilli, t.createdDate, t.sortOrder";
		Query query = getQuery(queryString);
		
//		SQLQuery query = getSession().createSQLQuery("select task0_.id as id11_0_, profile1_.id as id2_1_, task0_.createdDate as createdD2_11_0_, task0_.createdDateZone as createdD3_11_0_, task0_.modifiedDate as modified4_11_0_, task0_.modifiedDateZone as modified5_11_0_, task0_.aParent as aParent11_0_, task0_.endTime as endTime11_0_, task0_.endTimezone as endTimez8_11_0_, task0_.modelName as modelName11_0_, task0_.taskPriority as taskPri10_11_0_, task0_.progress as progress11_0_, task0_.publicTask as publicTask11_0_, task0_.showTask as showTask11_0_, task0_.startTime as startTime11_0_, task0_.startTimezone as startTi15_11_0_, task0_.taskCreatorId as taskCre18_11_0_, task0_.whereLocation as whereLo16_11_0_, task0_.themeId as themeId11_0_, task0_.title as title11_0_, profile1_.createdDate as createdD2_2_1_, profile1_.createdDateZone as createdD3_2_1_, profile1_.modifiedDate as modified4_2_1_, profile1_.modifiedDateZone as modified5_2_1_, profile1_.memberId as memberId2_1_, profile1_.name as name2_1_ from t_tasks task0_ inner join u_profiles profile1_ on task0_.taskCreatorId=profile1_.id where profile1_.id=? and task0_.progress<>? order by task0_.modifiedDate desc, task0_.modifiedDateZone").addEntity(Task.class);
		
		query.setString(0,email);
		query.setInteger(1, ProgressEnum.DELETE.ordinal());
		query.setInteger(2, ProgressEnum.DONE.ordinal());
		query.setLong(3, new DateTime().getMillis());
		
		query.setMaxResults(chunkSize);
		query.setFirstResult(startIndex);
		
		return query.list();
//		
		
	}
	
	@Override
	public List<Task> getCompletedCanceledTasksForMember(String email, int chunkSize, int startIndex)
	{	
		String queryString = "from Task t " +
				"inner join fetch t.taskCreator tc " +
				"where tc.member.email=? and (t.progress = ? or t.progress = ?)" +
				"order by t.priority, t.sortTimeStampMilli, t.createdDate, t.sortOrder";
		Query query = getQuery(queryString);
		
//		SQLQuery query = getSession().createSQLQuery("select task0_.id as id11_0_, profile1_.id as id2_1_, task0_.createdDate as createdD2_11_0_, task0_.createdDateZone as createdD3_11_0_, task0_.modifiedDate as modified4_11_0_, task0_.modifiedDateZone as modified5_11_0_, task0_.aParent as aParent11_0_, task0_.endTime as endTime11_0_, task0_.endTimezone as endTimez8_11_0_, task0_.modelName as modelName11_0_, task0_.taskPriority as taskPri10_11_0_, task0_.progress as progress11_0_, task0_.publicTask as publicTask11_0_, task0_.showTask as showTask11_0_, task0_.startTime as startTime11_0_, task0_.startTimezone as startTi15_11_0_, task0_.taskCreatorId as taskCre18_11_0_, task0_.whereLocation as whereLo16_11_0_, task0_.themeId as themeId11_0_, task0_.title as title11_0_, profile1_.createdDate as createdD2_2_1_, profile1_.createdDateZone as createdD3_2_1_, profile1_.modifiedDate as modified4_2_1_, profile1_.modifiedDateZone as modified5_2_1_, profile1_.memberId as memberId2_1_, profile1_.name as name2_1_ from t_tasks task0_ inner join u_profiles profile1_ on task0_.taskCreatorId=profile1_.id where profile1_.id=? and task0_.progress<>? order by task0_.modifiedDate desc, task0_.modifiedDateZone").addEntity(Task.class);
		
		query.setString(0,email);
		query.setInteger(1, ProgressEnum.DELETE.ordinal());
		query.setInteger(2, ProgressEnum.DONE.ordinal());

		query.setMaxResults(chunkSize);
		query.setFirstResult(startIndex);
		
		return query.list();
	}
	
	@Override
	public List<Task> getTodayTasksForMember(DateTime startDateTimeRange, DateTime endDateTimeRange, String email, int chunkSize, int startIndex)
	{	
		
		
		String queryString = "from Task t " +
				"inner join fetch t.taskCreator tc " +
				"where tc.member.email=? and t.progress != ? and t.progress != ? and t.endTimeMilli is not null and t.endTimeMilli > ? and t.endTimeMilli < ? " +
				"order by t.priority, t.sortTimeStampMilli, t.createdDate, t.sortOrder";
		Query query = getQuery(queryString);
		
//		SQLQuery query = getSession().createSQLQuery("select task0_.id as id11_0_, profile1_.id as id2_1_, task0_.createdDate as createdD2_11_0_, task0_.createdDateZone as createdD3_11_0_, task0_.modifiedDate as modified4_11_0_, task0_.modifiedDateZone as modified5_11_0_, task0_.aParent as aParent11_0_, task0_.endTime as endTime11_0_, task0_.endTimezone as endTimez8_11_0_, task0_.modelName as modelName11_0_, task0_.taskPriority as taskPri10_11_0_, task0_.progress as progress11_0_, task0_.publicTask as publicTask11_0_, task0_.showTask as showTask11_0_, task0_.startTime as startTime11_0_, task0_.startTimezone as startTi15_11_0_, task0_.taskCreatorId as taskCre18_11_0_, task0_.whereLocation as whereLo16_11_0_, task0_.themeId as themeId11_0_, task0_.title as title11_0_, profile1_.createdDate as createdD2_2_1_, profile1_.createdDateZone as createdD3_2_1_, profile1_.modifiedDate as modified4_2_1_, profile1_.modifiedDateZone as modified5_2_1_, profile1_.memberId as memberId2_1_, profile1_.name as name2_1_ from t_tasks task0_ inner join u_profiles profile1_ on task0_.taskCreatorId=profile1_.id where profile1_.id=? and task0_.progress<>? order by task0_.modifiedDate desc, task0_.modifiedDateZone").addEntity(Task.class);
		
		query.setString(0,email);
		query.setInteger(1, ProgressEnum.DELETE.ordinal());
		query.setInteger(2, ProgressEnum.DONE.ordinal());
		query.setLong(3, startDateTimeRange.getMillis());
		query.setLong(4, endDateTimeRange.getMillis());
		
		query.setMaxResults(chunkSize);
		query.setFirstResult(startIndex);
		
		return query.list();
	}
	
	
	
	@Override
	public List<Task> getTimelessTasksEnding()
	{
		Query query = getQuery("from Task t where t.timeLessTask = true and t.endTimeMilli >= ? and t.endTimeMilli <= ?");
		DateMidnight tomorrow = new DateMidnight().plusDays(1);
		DateMidnight dayAfterTomorrow = new DateMidnight().plusDays(2);
		
		query.setLong(0, tomorrow.getMillis());
		query.setLong(1, dayAfterTomorrow.getMillis());
		
		return query.list();
		
	}
	
	@Override
	public List<Task> getTimelessTasksEndingNotCompleted()
	{
		Query query = getQuery("from Task t where (t.progress = ? or t.progress = ? or t.progress = ? or t.progress = ? or t.progress = ?) and t.timeLessTask = true and t.endTimeMilli >= ? and t.endTimeMilli <= ?");
		DateMidnight tomorrow = new DateMidnight().plusDays(1);
		DateMidnight dayAfterTomorrow = new DateMidnight().plusDays(2);
		
		query.setInteger(0, ProgressEnum.OPEN.ordinal());
		query.setInteger(1, ProgressEnum.ALMOST_THERE.ordinal());
		query.setInteger(2, ProgressEnum.STARTED.ordinal());
		query.setInteger(3, ProgressEnum.RUNNING_LATE.ordinal());
		query.setInteger(4, ProgressEnum.WORKING_ON_IT.ordinal());
		query.setLong(5, tomorrow.getMillis());
		query.setLong(6, dayAfterTomorrow.getMillis());
		
		return query.list();
		
	}
	
	@Override
	public List<String> getTaskWho(Task task)
	{
		Query query = getQuery("select s.user.id from Share s where s.task.id = ?");
		query.setMaxResults(4);
		query.setString(0, task.getId());
		
		return query.list();
	}
	
	@Override
	public List<Task> getTasksForSubTasks(Task parentTask)
	{
//		Query query = getQuery("select st.childTask from SubTask st where st.parentTask.id=?");
		Query query = getQuery("from Task t where t.parentId=?");
		query.setString(0, parentTask.getId());
		
		return query.list();
	}
	
	@Override
	public List<Task> getTaskChildren(String taskId)
	{
		Query query = getQuery("from Task t where t.parentId = ?");
		query.setString(0, taskId);
		
		List<Task> tasks = query.list();
		return tasks;
	}
	
	@Override
	public List<Task> getNextTasksByRepeatSetup(RepeatSetup repeatSetup)
	{
		Query query = getQuery("from Task t where t.repeatSetup.id = ? order by t.createdDate desc");
		query.setString(0, repeatSetup.getId());
		
		List<Task> tasks = query.list();
		return tasks;
	}
	
	@Override
	public List<Task> getTasksByStatusAndMemberId(String memberId, int status)
	{
		Query query = getQuery("from Task t where t.taskCreator.id = ? and t.progress = ? order by t.modifiedDate desc");
		query.setString(0, memberId); 
		query.setInteger(1, status);
		
		List<Task> tasks = query.list();
		return tasks;
	}

	@Override
	public Task getNextTaskByRepeatSetup(RepeatSetup repeatSetup)
	{
		List<Task> tasks = getNextTasksByRepeatSetup(repeatSetup);
		if (tasks.isEmpty())
			return null;
		else 
			return tasks.get(0);
	}
	
	@Override
	public List<String> getTaskIdsForCalendar(Calendar calendar)
	{
		Query query = getQuery("select t.id from Task t left join t.calendars tc where tc.id=?");
		query.setString(0, calendar.getId());
		
		return query.list();
	}
	
	@Override
	public List<Task> getTaskForCalendar(Calendar calendar)
	{
		Query query = getQuery("select t from Task t left join t.calendars tc where tc.id=?");
		query.setString(0, calendar.getId());
		
		return query.list();
	}
	
	@Override
	public Task getTaskById(String id)
	{
//		Query query = getQuery("from Task t " +
//				"left outer join fetch t.repeatSetup rs " +
//				"inner join fetch t.taskCreator tc " +
////				"left outer join fetch t.memos memo " +
////				"left outer join fetch t.reminders r " +
////				"left outer join fetch t.goals g " +
////				"left outer join fetch t.categories cat " +
//				"left outer join fetch t.calendars cal " +
////				"left outer join fetch t.theme th " +
//				"where t.id=?");
//		query.setString(0, id);
//		
//		return (Task)query.uniqueResult();
		flush();
		return loadByPrimaryKey(Task.class, id);
	}
	
	@Override
	public List<RepeatSetup> getBulkRepeatSetup(List<String> ids, String email)
	{
		Query query = getQuery("from RepeatSetup rs where rs.member.email=:member and rs.id in (:ids)");
		query.setString("member", email);
		query.setParameterList("ids", ids);
		
		return query.list();		
	}
	
	@Override
	public List<Task> getTaskByIds(List<String> ids)
	{
		Query query = getQuery("from Task t " +
				"left outer join fetch t.repeatSetup rs " +
				"inner join fetch t.taskCreator tc " +
				"inner join fetch tc.member m " +
				"left outer join fetch m.roles roles " +
//				"left outer join fetch t.memos m " +
//				"left outer join fetch t.reminders r " +
//				"left outer join fetch t.goals g " +
//				"left outer join fetch t.categories cat " +
				"left outer join fetch t.calendars cal " +
//				"left outer join fetch t.theme th " +
				"where t.id in (:ids)");
		query.setParameterList("ids", ids);
		
		return query.list();		
		
		
		
	}
	



	private String buildTaskHQL(String prefix, String suffix)
	{
		StringBuffer buffer = new StringBuffer("from Task t ");
		buffer.append("inner join fetch t.taskCreator tc ");
//		buffer.append("left outer join fetch t.categories cat ");
		
		return buildHQL(prefix, buffer.toString(), suffix);
		
	}
	
	@Override
	public List<Task> getTasksForMember(Profile profile)
	{	
		String queryString = "from Task t " +
				"inner join fetch t.taskCreator tc " +
				"where tc.id=? and t.progress != ? " +
				"order by t.modifiedDate desc";
		Query query = getQuery(queryString);
		
//		SQLQuery query = getSession().createSQLQuery("select task0_.id as id11_0_, profile1_.id as id2_1_, task0_.createdDate as createdD2_11_0_, task0_.createdDateZone as createdD3_11_0_, task0_.modifiedDate as modified4_11_0_, task0_.modifiedDateZone as modified5_11_0_, task0_.aParent as aParent11_0_, task0_.endTime as endTime11_0_, task0_.endTimezone as endTimez8_11_0_, task0_.modelName as modelName11_0_, task0_.taskPriority as taskPri10_11_0_, task0_.progress as progress11_0_, task0_.publicTask as publicTask11_0_, task0_.showTask as showTask11_0_, task0_.startTime as startTime11_0_, task0_.startTimezone as startTi15_11_0_, task0_.taskCreatorId as taskCre18_11_0_, task0_.whereLocation as whereLo16_11_0_, task0_.themeId as themeId11_0_, task0_.title as title11_0_, profile1_.createdDate as createdD2_2_1_, profile1_.createdDateZone as createdD3_2_1_, profile1_.modifiedDate as modified4_2_1_, profile1_.modifiedDateZone as modified5_2_1_, profile1_.memberId as memberId2_1_, profile1_.name as name2_1_ from t_tasks task0_ inner join u_profiles profile1_ on task0_.taskCreatorId=profile1_.id where profile1_.id=? and task0_.progress<>? order by task0_.modifiedDate desc, task0_.modifiedDateZone").addEntity(Task.class);
		
		query.setString(0, profile.getId());
		query.setInteger(1, ProgressEnum.DELETE.ordinal());

		
		return query.list();
	}
	
//	@Override
//	public List<Task> getTasksForMemberOrderByCategory(Profile profile)
//	{	
//		String queryString = "from Task t " +
//				"inner join fetch t.taskCreator tc " +
////				"left outer join fetch t.categories cat " +
//				"where tc.id=? and t.progress != ? " +
//				"order by CASE WHEN cat.categoryName IS NULL THEN 1 ELSE 0 END, cat.categoryName, t.modifiedDate desc";
//		Query query = getQuery(queryString);
//		
////		SQLQuery query = getSession().createSQLQuery("select task0_.id as id11_0_, profile1_.id as id2_1_, category3_.id as id7_2_, task0_.createdDate as createdD2_11_0_, task0_.createdDateZone as createdD3_11_0_, task0_.modifiedDate as modified4_11_0_, task0_.modifiedDateZone as modified5_11_0_, task0_.aParent as aParent11_0_, task0_.endTime as endTime11_0_, task0_.endTimezone as endTimez8_11_0_, task0_.modelName as modelName11_0_, task0_.taskPriority as taskPri10_11_0_, task0_.progress as progress11_0_, task0_.publicTask as publicTask11_0_, task0_.showTask as showTask11_0_, task0_.startTime as startTime11_0_, task0_.startTimezone as startTi15_11_0_, task0_.taskCreatorId as taskCre18_11_0_, task0_.whereLocation as whereLo16_11_0_, task0_.themeId as themeId11_0_, task0_.title as title11_0_, profile1_.createdDate as createdD2_2_1_, profile1_.createdDateZone as createdD3_2_1_, profile1_.modifiedDate as modified4_2_1_, profile1_.modifiedDateZone as modified5_2_1_, profile1_.memberId as memberId2_1_, profile1_.name as name2_1_, category3_.createdDate as createdD2_7_2_, category3_.createdDateZone as createdD3_7_2_, category3_.modifiedDate as modified4_7_2_, category3_.modifiedDateZone as modified5_7_2_, category3_.categoryName as category6_7_2_, category3_.generic as generic7_2_, category3_.memberId as memberId7_2_, category3_.modelName as modelName7_2_, category3_.privateCategory as privateC9_7_2_, category3_.sortOrder as sortOrder7_2_, categories2_.taskId as taskId11_0__, categories2_.categoryId as categoryId0__ from t_tasks task0_ inner join u_profiles profile1_ on task0_.taskCreatorId=profile1_.id left outer join t_taskCategories categories2_ on task0_.id=categories2_.taskId left outer join t_categories category3_ on categories2_.categoryId=category3_.id where profile1_.id=? and task0_.progress<>? order by case when category3_.categoryName is null then 1 else 0 end, category3_.categoryName, task0_.modifiedDate desc, task0_.modifiedDateZone").addEntity(Task.class);
//		
//		query.setString(0, profile.getId());
//		query.setInteger(1, ProgressEnum.DELETE.ordinal());
//		
//		return query.list();
//	}
	
	@Override
	public List<Task> getTasksForMemberOrderByStartDate(Profile profile)
	{	
		String queryString = "from Task t " +
				"inner join fetch t.taskCreator tc " +
				"where tc.id=? and t.progress != ? " +
				"order by t.startTime desc NULLS LAST";
		Query query = getQuery(queryString);
		query.setString(0, profile.getId());
		query.setInteger(1, ProgressEnum.DELETE.ordinal());
		
		return query.list();
	}
	
	@Override
	public List<Task> getTasksForMemberOrderByDueDate(Profile profile)
	{	
		String queryString = "from Task t " +
			"inner join fetch t.taskCreator tc " +
			"where tc.id=?  and t.progress != ? " +
			"order by CASE WHEN t.endTime IS NULL THEN 1 ELSE 0 END, t.endTime, t.modifiedDate desc";
		Query query = getQuery(queryString);
		
//		SQLQuery query = getSession().createSQLQuery("select task0_.id as id11_0_, profile1_.id as id2_1_, task0_.createdDate as createdD2_11_0_, task0_.createdDateZone as createdD3_11_0_, task0_.modifiedDate as modified4_11_0_, task0_.modifiedDateZone as modified5_11_0_, task0_.aParent as aParent11_0_, task0_.endTime as endTime11_0_, task0_.endTimezone as endTimez8_11_0_, task0_.modelName as modelName11_0_, task0_.taskPriority as taskPri10_11_0_, task0_.progress as progress11_0_, task0_.publicTask as publicTask11_0_, task0_.showTask as showTask11_0_, task0_.startTime as startTime11_0_, task0_.startTimezone as startTi15_11_0_, task0_.taskCreatorId as taskCre18_11_0_, task0_.whereLocation as whereLo16_11_0_, task0_.themeId as themeId11_0_, task0_.title as title11_0_, profile1_.createdDate as createdD2_2_1_, profile1_.createdDateZone as createdD3_2_1_, profile1_.modifiedDate as modified4_2_1_, profile1_.modifiedDateZone as modified5_2_1_, profile1_.memberId as memberId2_1_, profile1_.name as name2_1_ from t_tasks task0_ inner join u_profiles profile1_ on task0_.taskCreatorId=profile1_.id where profile1_.id=? and task0_.progress<>? order by case when (task0_.endTime is null) and (task0_.endTimezone is null) then 1 else 0 end, task0_.endTime, task0_.endTimezone, task0_.modifiedDate desc, task0_.modifiedDateZone").addEntity(Task.class);
		query.setString(0, profile.getId());
		query.setInteger(1, ProgressEnum.DELETE.ordinal());
		
		return query.list();
	}
	
	@Override
	public List<Task> getTasksForMemberOrderByPriority(Profile profile)
	{	
		String queryString = "from Task t " +
			"inner join fetch t.taskCreator tc " +
			"where tc.id=?  and t.progress != ? " +
			"order by t.priority, t.modifiedDate desc";		
		Query query = getQuery(queryString);
//		SQLQuery query = getSession().createSQLQuery("select task0_.id as id11_0_, profile1_.id as id2_1_, task0_.createdDate as createdD2_11_0_, task0_.createdDateZone as createdD3_11_0_, task0_.modifiedDate as modified4_11_0_, task0_.modifiedDateZone as modified5_11_0_, task0_.aParent as aParent11_0_, task0_.endTime as endTime11_0_, task0_.endTimezone as endTimez8_11_0_, task0_.modelName as modelName11_0_, task0_.taskPriority as taskPri10_11_0_, task0_.progress as progress11_0_, task0_.publicTask as publicTask11_0_, task0_.showTask as showTask11_0_, task0_.startTime as startTime11_0_, task0_.startTimezone as startTi15_11_0_, task0_.taskCreatorId as taskCre18_11_0_, task0_.whereLocation as whereLo16_11_0_, task0_.themeId as themeId11_0_, task0_.title as title11_0_, profile1_.createdDate as createdD2_2_1_, profile1_.createdDateZone as createdD3_2_1_, profile1_.modifiedDate as modified4_2_1_, profile1_.modifiedDateZone as modified5_2_1_, profile1_.memberId as memberId2_1_, profile1_.name as name2_1_ from t_tasks task0_ inner join u_profiles profile1_ on task0_.taskCreatorId=profile1_.id where profile1_.id=? and task0_.progress<>? order by task0_.taskPriority, task0_.modifiedDate desc, task0_.modifiedDateZone").addEntity(Task.class);
		query.setString(0, profile.getId());
		query.setInteger(1, ProgressEnum.DELETE.ordinal());
		
		return query.list();
	}
	
	
	/**
	 * This method will prepare a task.  First it will validate the task to make sure no parameters were missed.  Next
	 * it will go through and set all the call back references to bi-directional references.  It will also call the validators
	 * on the sub children
	 * @param task
	 * @see TaskValidator
	 * @see ReminderValidator
	 * @throws ValidationException
	 */
	private void prepareTask(Task task) throws ValidationException
	{
		
		if (task.getCalendars()!=null)
		{
			for (Calendar calendar:task.getCalendars())
			{
				validator.validatePrimaryKeyIsSet(calendar);
				
				if (loadByPrimaryKey(Calendar.class,calendar.getId())==null)
					throw new ValidationException(ValidationCodeEnum.CALENDAR_NOT_FOUND);

			}
		}
		
		
	}
	
	@Override
	public Task createTask(Task task) throws ValidationException
	{
		prepareTask(task);
		
		//check to see if this should be an update instead of a create
		Task taskFromDB = getTaskById(task.getId());
		if (taskFromDB != null)
		{
//			updateTask(taskFromDB, taskFromDB.getTaskCreator().getEmail());
			throw new ValidationException(ValidationCodeEnum.TASK_WITH_SAME_ID_ALREADY_EXISTS);
		}
		
		if (task.getRepeatSetup()!=null && task.getRepeatSetup().getId()!=null)
		{
			RepeatSetup repeatSetup = loadByPrimaryKey(RepeatSetup.class, task.getRepeatSetup().getId());
			if (repeatSetup != null)
			{
				TaskTemplate taskTemplate = repeatSetup.getClonedTask();
			
			
				taskTemplate.setTitle(task.getTitle());
				taskTemplate.setTaskLocation(task.getTaskLocation());
				updateObject(taskTemplate);
			}
			else
			{
				task.setRepeatSetup(null);
			}
		}
		
		save(task);
		
		return task;
	}


	

	@Override
	public Task updateTask(Task task) throws ValidationException {
		prepareTask(task);
		
		updateObject(task);
		
//		flush();
		return task;
	}
	
	
	
}
