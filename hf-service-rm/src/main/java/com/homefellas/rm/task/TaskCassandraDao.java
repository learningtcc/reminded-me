package com.homefellas.rm.task;

import java.util.List;

import org.joda.time.DateTime;

import com.homefellas.dao.core.CassandraDao;
import com.homefellas.exception.ValidationException;
import com.homefellas.rm.calendar.Calendar;
import com.homefellas.rm.calendar.CalendarStoreUserDefinedCategory;
import com.homefellas.rm.repeatsetup.RepeatSetup;
import com.homefellas.rm.repeatsetup.TaskRepeat;
import com.homefellas.rm.repeatsetup.TaskTemplate;
import com.homefellas.user.Member;
import com.homefellas.user.Profile;



public class TaskCassandraDao extends CassandraDao<Task> implements ITaskDao {

	

	@Override
	public Task updateTask(Task task) throws ValidationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task createTask(Task task) throws ValidationException {
		save(task);
		
		return task;
	}

	@Override
	public List<Task> getTasksForMember(Profile profile) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Task> getTasksForMemberOrderByStartDate(Profile profile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getTasksForMemberOrderByDueDate(Profile profile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getTasksForMemberOrderByPriority(Profile profile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getTaskByIds(List<String> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getTaskForCalendar(Calendar calendar) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getTaskIdsForCalendar(Calendar calendar) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task getNextTaskByRepeatSetup(RepeatSetup repeatSetup) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getNextTasksByRepeatSetup(RepeatSetup repeatSetup) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RepeatSetup> getBulkRepeatSetup(List<String> ids, String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getTasksByStatusAndMemberId(String memberId, int status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getTaskChildren(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getTasksForSubTasks(Task parentTask) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getTaskWho(Task task) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getTimelessTasksEnding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getTimelessTasksEndingNotCompleted() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getDatelessOverdueTasksForMember(String email,
			int chunkSize, int startIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getCompletedCanceledTasksForMember(String email,
			int chunkSize, int startIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getTodayTasksForMember(DateTime startDateTimeRange,
			DateTime endDateTimeRange, String email, int chunkSize,
			int startIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getUpcomingTasks(String email, int chunkSize,
			int startIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task getTaskById(String Id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task save(Task model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task load(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task upadte(Task model) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
