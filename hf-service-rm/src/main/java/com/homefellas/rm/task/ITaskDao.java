package com.homefellas.rm.task;

import java.util.List;

import org.hibernate.Query;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.exception.ValidationException;
import com.homefellas.rm.calendar.Calendar;
import com.homefellas.rm.calendar.CalendarStoreUserDefinedCategory;
import com.homefellas.rm.repeatsetup.RepeatSetup;
import com.homefellas.rm.repeatsetup.TaskRepeat;
import com.homefellas.rm.repeatsetup.TaskTemplate;
import com.homefellas.rm.task.AbstractTask.ProgressEnum;
import com.homefellas.user.Member;
import com.homefellas.user.Profile;

public interface ITaskDao {
	
	public List<Task> getTasksForMember(Profile profile);
//	public List<Task> getTasksForMemberOrderByCategory(Profile profile);
	public List<Task> getTasksForMemberOrderByStartDate(Profile profile);
	public List<Task> getTasksForMemberOrderByDueDate(Profile profile);
	public List<Task> getTasksForMemberOrderByPriority(Profile profile);
	
	public List<Task> getTaskByIds(List<String> ids);
	public List<Task> getTaskForCalendar(Calendar calendar);
	public List<String> getTaskIdsForCalendar(Calendar calendar);
	public Task getNextTaskByRepeatSetup(RepeatSetup repeatSetup);
	public List<Task> getNextTasksByRepeatSetup(RepeatSetup repeatSetup);
	public List<RepeatSetup> getBulkRepeatSetup(List<String> ids, String email);
	public List<Task> getTasksByStatusAndMemberId(String memberId, int status);
//	public void updateTaskWithParent(Task task, boolean aParent);
	public List<Task> getTaskChildren(String taskId);
	public List<Task> getTasksForSubTasks(Task parentTask);
	public List<String> getTaskWho(Task task);
	public List<Task> getTimelessTasksEnding();
	public List<Task> getTimelessTasksEndingNotCompleted();
	
	public List<Task> getDatelessOverdueTasksForMember(String email, int chunkSize, int startIndex);
	public List<Task> getCompletedCanceledTasksForMember(String email, int chunkSize, int startIndex);
	public List<Task> getTodayTasksForMember(DateTime startDateTimeRange, DateTime endDateTimeRange, String email, int chunkSize, int startIndex);
	public List<Task> getUpcomingTasks(String email, int chunkSize, int startIndex);
	
	
	//keepers
	//create
	public Task createTask(Task task) throws ValidationException;
	
	//get
	public Task getTaskById(String Id);
	
	//update
	public Task updateTask(Task task) throws ValidationException;
	

	
	
	
	
}
