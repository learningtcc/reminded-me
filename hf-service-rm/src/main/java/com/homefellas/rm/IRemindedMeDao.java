package com.homefellas.rm;

import java.util.List;

import com.homefellas.metrics.WebRequestMetric;
import com.homefellas.model.core.AbstractGUIDModel;
import com.homefellas.model.core.AbstractModel;
import com.homefellas.model.core.IGenericSynchroinzedLifeCycle;
import com.homefellas.rm.calendar.Calendar;
import com.homefellas.user.Role;

public interface IRemindedMeDao
{

	public Role getRole(long id);
	public Calendar getCalendar(String calendarId);
	public List<Object> getSynchronizeableObject(ISynchronizeable synchronizeable, String memberId, String deviceId, long modifedTimeStamp);
	public List<Object> getSynchronizeableObject(ISynchronizeable synchronizeable, String memberId);
	public List<Object> getSynchronizeableFilterObject(ISynchronizeableFiltered synchronizeable, String memberId);
	public List<Object> getSynchronizeableFilterObject(ISynchronizeableFiltered synchronizeable, String memberId, String deviceId, long modifedTimeStamp);
	public List<Object> getSynchronizeableFilterObject(ISynchronizeableFilteredDateRange synchronizeable, String memberId, String deviceId, long modifedTimeStamp, long startTimeStamp, long endTimeStamp);
	public List<Object> getSynchronizeableObject(ISynchronizeable synchronizeable, String memberId, String deviceId, long modifedTimeStamp, long startTimeStamp, long endTimeStamp);
	public List<Object> getDateLessSynchronizeableFilterObject(ISynchronizeableFilteredDateRange synchronizeable, String memberId, String deviceId, long modifedTimeStamp);
	public List<Object> getTaskDependableObjects(ITaskDependable taskDependable, List<String> taskIds, String deviceId);
	public List<? extends AbstractModel> getBulkByIds(List<String> ids, String className);
	public List<WebRequestMetric> getHistory(String tgt);
	
	//create
	public Role createRole(Role role);
	public IGenericSynchroinzedLifeCycle createIGenericSynchroinzedLifeCycle(IGenericSynchroinzedLifeCycle model);
	
	//read
	public AbstractGUIDModel getAbstractGUIDModelById(Class<?> clazz, String id);
	public IGenericSynchroinzedLifeCycle getIGenericSynchroinzedLifeCycleByID(Class<?> clazz, String id);
	//update
	public IGenericSynchroinzedLifeCycle updateIGenericSynchroinzedLifeCycle(IGenericSynchroinzedLifeCycle model);
}
