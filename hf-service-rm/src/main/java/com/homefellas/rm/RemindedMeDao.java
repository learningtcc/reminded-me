package com.homefellas.rm;

import java.util.List;

import org.hibernate.Query;

import com.homefellas.dao.hibernate.core.HibernateCRUDDao;
import com.homefellas.metrics.WebRequestMetric;
import com.homefellas.model.core.AbstractGUIDModel;
import com.homefellas.model.core.AbstractModel;
import com.homefellas.model.core.IGenericSynchroinzedLifeCycle;
import com.homefellas.rm.calendar.Calendar;
import com.homefellas.user.Role;

public class RemindedMeDao extends HibernateCRUDDao implements IRemindedMeDao
{
	@Override
	public Role getRole(long id)
	{
		Query query = getQuery("from Role r where r.id=?");
		query.setLong(0, id);
		
		return (Role)query.uniqueResult();
	}
	
	public List<WebRequestMetric> getHistory(String tgt)
	{
		Query query = getQuery("from WebRequestMetric w where w.tgt=? order by w.systemTime desc");
		query.setString(0, tgt);
		
		return query.list();
	}

	/* (non-Javadoc)
	 * @see com.homefellas.rm.IRemindedMeDao#getCalendar(long)
	 */
	@Override
	public Calendar getCalendar(String calendarId)
	{
		Query query = getQuery("from Calendar c where c.id=?");
		query.setString(0, calendarId);
		
		return (Calendar)query.uniqueResult();
	}



	public List<Object> getTaskDependableObjects(ITaskDependable taskDependable, List<String> taskIds, String deviceId)
	{
		if (deviceId == null)
			deviceId = "INITIAL_SYNC";
		
		String queryString = "select tableAlias." +taskDependable.getSyncId() +" from " +taskDependable.getModelName() +" tableAlias where " +
				"(tableAlias." +taskDependable.getTaskAttributeName() +".id in (:ids)) " +
				"and " +
				"(tableAlias.lastModifiedDeviceId is null or tableAlias.lastModifiedDeviceId != (:device)) " +
				"and " +
				"( " +
					"(tableAlias." +taskDependable.getDeleteStatusField() +((taskDependable.isStatusExclusive()) ? " != " : " = ") +"(:deletevalue1)) " +
					"or " +
					"(tableAlias." +taskDependable.getDeleteStatusField() +((taskDependable.isStatusExclusive()) ? " = " : " != ") +"(:deletevalue2)) " +
				") " ;
		
		Query query = getQuery(queryString);
		query.setParameterList("ids", taskIds);
		query.setString("device", deviceId);
		query.setInteger("deletevalue1", taskDependable.getDeletedTrueValue());
		query.setInteger("deletevalue2", taskDependable.getDeletedTrueValue());
		
		return query.list();

	
	}
	
	
	@Override
	/**
	 * This method handles the case when there is no datetime passed.
	 */
	public List<Object> getSynchronizeableFilterObject(ISynchronizeableFiltered synchronizeable, String memberId)
	{
		String queryString = "select tableAlias." +
				synchronizeable.getSyncId() +
				" from " +
				synchronizeable.getModelName() +
				" tableAlias where tableAlias." +
				synchronizeable.getMemberAttributeName() +
				".id=? and tableAlias." +
				synchronizeable.getDeleteStatusField()+
				((synchronizeable.isStatusExclusive()) ? " != " : " = ") +
				"?";
//			synchronizeable.getSynchronizedFilterClause();
				
		
		Query query = getQuery(queryString);
		query.setString(0, memberId);
		query.setInteger(1, synchronizeable.getDeletedTrueValue());
		
		return query.list();
	}
	
	@Override
	public List<Object> getSynchronizeableFilterObject(ISynchronizeableFiltered synchronizeable, String memberId, String deviceId, long modifedTimeStamp)
	{
		if (deviceId == null)
			deviceId = "INITIAL_SYNC";
		
		String queryString = "select tableAlias." +synchronizeable.getSyncId() +" from " +synchronizeable.getModelName() +" tableAlias where " +
				"(tableAlias." +synchronizeable.getMemberAttributeName() +".id=?) " +
				"and " +
				"(tableAlias.lastModifiedDeviceId is null or tableAlias.lastModifiedDeviceId != ?) " +
				"and " +
				"( " +
					"( tableAlias.modifiedDate > ? and tableAlias." +synchronizeable.getDeleteStatusField() +((synchronizeable.isStatusExclusive()) ? " != " : " = ") +"?) " +
					"or " +
					"(tableAlias.createdDate < ? and tableAlias.modifiedDate > ? and tableAlias." +synchronizeable.getDeleteStatusField() +((synchronizeable.isStatusExclusive()) ? " = " : " != ") +"?) " +
				")";
//			synchronizeable.getSynchronizedFilterClause();
		
		Query query = getQuery(queryString);
		query.setString(0, memberId);
		query.setString(1, deviceId);
		query.setLong(2, modifedTimeStamp);
		query.setInteger(3, synchronizeable.getDeletedTrueValue());
		query.setLong(4, modifedTimeStamp);
		query.setLong(5, modifedTimeStamp);
		query.setInteger(6, synchronizeable.getDeletedTrueValue());
		
		return query.list();
	}
	
	@Override
	public List<Object> getSynchronizeableObject(ISynchronizeable synchronizeable, String memberId, String deviceId, long modifedTimeStamp)
	{
		if (deviceId == null)
			deviceId = "INITIAL_SYNC";
		
		String queryString = "select tableAlias." +synchronizeable.getSyncId() +" from " +synchronizeable.getModelName() +" tableAlias where " +
				"(tableAlias." +synchronizeable.getMemberAttributeName() +".id=? )" +
				"and (tableAlias.lastModifiedDeviceId is null or tableAlias.lastModifiedDeviceId != ?) " +
				"and (tableAlias.modifiedDate>?) ";
		
		Query query = getQuery(queryString);
		query.setString(0, memberId);
		query.setString(1, deviceId);
		query.setLong(2, modifedTimeStamp);
		
		return query.list();
	}
	
//	@Override
//	public List<Object> getSynchronizeableFilterObject(ISynchronizeableFiltered synchronizeable, String memberId, String deviceId, long modifedTimeStamp, long startTimeStamp, long endTimeStamp)
//	{
//		if (deviceId == null)
//			deviceId = "INITIAL_SYNC";
//		
//		String queryString = "select tableAlias." +synchronizeable.getSyncId() + " from " + synchronizeable.getModelName() +
//			" tableAlias where " +
//				"(tableAlias." +synchronizeable.getMemberAttributeName() +".id=?) " +
//				"and " +
//				"(tableAlias.lastModifiedDeviceId is null or tableAlias.lastModifiedDeviceId != ?) " +
//				"and " +
//				"( " +
//					"(tableAlias.modifiedDate > ? and tableAlias.modifiedDate < ? and tableAlias." +synchronizeable.getDeleteStatusField() +((synchronizeable.isStatusExclusive()) ? " != " : " = ") +"?) " +
//				" ) " +
//				"and " +
//				"(" +
//					"( " +
//						"tableAlias.startTimeMilli is not null and ? > tableAlias.startTimeMilli and tableAlias.startTimeMilli < ? or tableAlias.endTimeMilli is not null and ? > tableAlias.endTimeMilli and tableAlias.endTimeMilli < ? " +
//					") " +
//					"OR (tableAlias.startTimeMilli < ? AND tableAlias.endTimeMilli > ?) " +
//				")";
//
//		
//		Query query = getQuery(queryString);
//		query.setString(0, memberId);
//		query.setString(1, deviceId);
//		query.setLong(2, modifedTimeStamp);
//		query.setLong(3, modifedTimeStamp);
//		query.setInteger(4, synchronizeable.getDeletedTrueValue());
//		query.setLong(5, startTimeStamp);
//		query.setLong(6, endTimeStamp);
//		query.setLong(7, startTimeStamp);
//		query.setLong(8, endTimeStamp);
//		query.setLong(9, startTimeStamp);
//		query.setLong(10, endTimeStamp);
//		
////		String queryString = "select tableAlias." +
////		synchronizeable.getSyncId() +
////			" from " +
////			synchronizeable.getModelName() +
////			" tableAlias where tableAlias." +
////			synchronizeable.getMemberAttributeName() +
////			".id=? " +
////			"and " +
////			"(tableAlias.lastModifiedDeviceId is null or tableAlias.lastModifiedDeviceId != ?) " +
////			"and " +
////			"( " +
////				"( " +
////					"tableAlias.startTimeMilli is not null and ? > tableAlias.startTimeMilli and tableAlias.startTimeMilli < ? or tableAlias.endTimeMilli is not null and ? > tableAlias.endTimeMilli and tableAlias.endTimeMilli < ? " +
////				") " +
////				"OR (tableAlias.startTimeMilli < ? AND tableAlias.endTimeMilli > ?) " +
////			") " +
////			"and " +
////			"( " +
////				"tableAlias." +
////				synchronizeable.getDeleteStatusField() +
////				((synchronizeable.isStatusExclusive()) ? " != " : " = ") +
////				"?" +
////			") ";
////
////		
////		Query query = getQuery(queryString);
////		query.setString(0, memberId);
////		query.setString(1, deviceId);
////		query.setLong(2, startTimeStamp);
////		query.setLong(3, endTimeStamp);
////		query.setLong(4, startTimeStamp);
////		query.setLong(5, endTimeStamp);
////		query.setLong(6, startTimeStamp);
////		query.setLong(7, endTimeStamp);
////		query.setInteger(8, synchronizeable.getDeletedTrueValue());
//	
//		return query.list();
//	}

//	@Override
//	public List<Object> getSynchronizeableObject(ISynchronizeable synchronizeable, String memberId, String deviceId, long startTimeStamp, long endTimeStamp)
//	{
//		if (deviceId == null)
//			deviceId = "INITIAL_SYNC";
//		
//		String queryString = "select tableAlias." +
//				synchronizeable.getSyncId() +
//				" from " +
//				synchronizeable.getModelName() +
//				" tableAlias where tableAlias." +
//				synchronizeable.getMemberAttributeName() +
//				".id=? " +
//				"and (tableAlias.lastModifiedDeviceId is null or tableAlias.lastModifiedDeviceId != ?) " +
//				"and ( ( " +
//				"tableAlias.startTimeMilli is not null and ? > tableAlias.startTimeMilli and tableAlias.startTimeMilli < ? " +
//				"or " +
//				"tableAlias.endTimeMilli is not null and ? > tableAlias.endTimeMilli and tableAlias.endTimeMilli < ? " +
//				") " +
//				"OR (tableAlias.startTimeMilli < ? AND tableAlias.endTimeMilli > ?) )";
//				
//					     
//						 
////				"and (tableAlias.lastModifiedDeviceId is null or tableAlias.lastModifiedDeviceId != ?) " +
////				"and tableAlias.modifiedDate > ? " +
////				"and tableAlias.modifiedDate < ?";
////		Query query = getQuery(queryString);
////		query.setString(0, memberId);
////		query.setString(1, deviceId);
////		query.setLong(2, startTimeStamp);
////		query.setLong(3, endTimeStamp);
//		
//		
//		Query query = getQuery(queryString);
//		query.setString(0, memberId);
//		query.setString(1, deviceId);
//		query.setLong(2, startTimeStamp);
//		query.setLong(3, endTimeStamp);
//		query.setLong(4, startTimeStamp);
//		query.setLong(5, endTimeStamp);
//		query.setLong(6, startTimeStamp);
//		query.setLong(7, endTimeStamp);
//		
//		return query.list();
//	}
	
	@Override
	public List<Object> getSynchronizeableObject(ISynchronizeable synchronizeable, String memberId)
	{
		String queryString = "select tableAlias." +
			synchronizeable.getSyncId() +
				" from " +
			synchronizeable.getModelName() +
				" tableAlias where tableAlias." +
				synchronizeable.getMemberAttributeName() +
				".id=?";
			
		Query query = getQuery(queryString);
		query.setString(0, memberId);
		
		return query.list();
	}

  
	public List<Object> getDateLessSynchronizeableFilterObject(ISynchronizeableFilteredDateRange synchronizeable, String memberId, String deviceId, long modifedTimeStamp)
	{
		if (deviceId == null)
			deviceId = "INITIAL_SYNC";
		
		String queryString = "select tableAlias." +synchronizeable.getSyncId() +" from " +synchronizeable.getModelName() +" tableAlias where " +
				"(tableAlias." +synchronizeable.getMemberAttributeName() +".id=?) " +
				"and " +
					"(tableAlias.lastModifiedDeviceId is null or tableAlias.lastModifiedDeviceId != ?) " +
				"and " +
				"( " +
					"( tableAlias.modifiedDate > ? and tableAlias." +synchronizeable.getDeleteStatusField() +((synchronizeable.isStatusExclusive()) ? " != " : " = ") +"?) " +
					"or " +
					"(tableAlias.createdDate < ? and tableAlias.modifiedDate > ? and tableAlias." +synchronizeable.getDeleteStatusField() +((synchronizeable.isStatusExclusive()) ? " = " : " != ") +"?) " +
				") " +
				"and " +
				"( " +
					"tableAlias." + synchronizeable.getTimeLessTaskAttributeName() + " is true " +
					"or " +
					"(" +
						"tableAlias." + synchronizeable.getStartTimeAttributeName() + " is null " +
						"and " +
						"tableAlias." + synchronizeable.getEndTimeAttributeName() + " is null " +
					")"+
				")";
		
		Query query = getQuery(queryString);
		query.setString(0, memberId);
		query.setString(1, deviceId);
		query.setLong(2, modifedTimeStamp);
		query.setInteger(3, synchronizeable.getDeletedTrueValue());
		query.setLong(4, modifedTimeStamp);
		query.setLong(5, modifedTimeStamp);
		query.setInteger(6, synchronizeable.getDeletedTrueValue());
		
		return query.list();
	}

	@Override
	public List<Object> getSynchronizeableFilterObject(ISynchronizeableFilteredDateRange synchronizeable, String memberId, String deviceId, long modifedTimeStamp, long startTimeStamp, long endTimeStamp)
	{
		if (deviceId == null)
			deviceId = "INITIAL_SYNC";
		
		String queryString = "select tableAlias." +synchronizeable.getSyncId() +" from " +synchronizeable.getModelName() +" tableAlias where " +
				"(tableAlias." +synchronizeable.getMemberAttributeName() +".id=?) " +
				"and " +
				"(tableAlias.lastModifiedDeviceId is null or tableAlias.lastModifiedDeviceId != ?) " +
				"and " +
				"( " +
					"( tableAlias.modifiedDate > ? and tableAlias." +synchronizeable.getDeleteStatusField() +((synchronizeable.isStatusExclusive()) ? " != " : " = ") +"?) " +
					"or " +
					"(tableAlias.createdDate < ? and tableAlias.modifiedDate > ? and tableAlias." +synchronizeable.getDeleteStatusField() +((synchronizeable.isStatusExclusive()) ? " = " : " != ") +"?) " +
				")" +
				"and " +
				"( " +
					"( " +
						"tableAlias."+synchronizeable.getStartTimeAttributeName()+" is not null and tableAlias."+synchronizeable.getStartTimeAttributeName()+" > ? and tableAlias."+synchronizeable.getStartTimeAttributeName()+" < ? " +
						"or " +
						"tableAlias."+synchronizeable.getEndTimeAttributeName()+" is not null and tableAlias."+synchronizeable.getEndTimeAttributeName()+" > ? and tableAlias."+synchronizeable.getEndTimeAttributeName()+" < ? " +
					") " +
					"OR " +
					"(tableAlias."+synchronizeable.getStartTimeAttributeName()+" < ? AND tableAlias."+synchronizeable.getEndTimeAttributeName()+" > ?) " +
				")";
//			synchronizeable.getSynchronizedFilterClause();
		
		Query query = getQuery(queryString);
		query.setString(0, memberId);
		query.setString(1, deviceId);
		query.setLong(2, modifedTimeStamp);
		query.setInteger(3, synchronizeable.getDeletedTrueValue());
		query.setLong(4, modifedTimeStamp);
		query.setLong(5, modifedTimeStamp);
		query.setInteger(6, synchronizeable.getDeletedTrueValue());
		query.setLong(7, startTimeStamp);
		query.setLong(8, endTimeStamp);
		query.setLong(9, startTimeStamp);
		query.setLong(10, endTimeStamp);
		query.setLong(11, startTimeStamp);
		query.setLong(12, endTimeStamp);
		
		return query.list();
	}



	@Override
	public List<Object> getSynchronizeableObject(ISynchronizeable synchronizeable, String memberId, String deviceId, long modifedTimeStamp, long startTimeStamp, long endTimeStamp)
	{
		if (deviceId == null)
			deviceId = "INITIAL_SYNC";
		
		String queryString = "select tableAlias." +synchronizeable.getSyncId() +" from " +synchronizeable.getModelName() +" tableAlias where " +
				"(tableAlias." +synchronizeable.getMemberAttributeName() +".id=? )" +
				"and (tableAlias.lastModifiedDeviceId is null or tableAlias.lastModifiedDeviceId != ?) " +
				"and (tableAlias.modifiedDate>?) " +
				"and " +
				"( " +
					"( " +
						"tableAlias.startTimeMilli is not null and ? > tableAlias.startTimeMilli and tableAlias.startTimeMilli < ? " +
						"or " +
						"tableAlias.endTimeMilli is not null and ? > tableAlias.endTimeMilli and tableAlias.endTimeMilli < ? " +
					") " +
					"OR (tableAlias.startTimeMilli < ? AND tableAlias.endTimeMilli > ?) " +
				")";
		
		Query query = getQuery(queryString);
		query.setString(0, memberId);
		query.setString(1, deviceId);
		query.setLong(2, modifedTimeStamp);
		query.setLong(3, startTimeStamp);
		query.setLong(4, endTimeStamp);
		query.setLong(5, startTimeStamp);
		query.setLong(6, endTimeStamp);
		query.setLong(7, startTimeStamp);
		query.setLong(8, endTimeStamp);
		
		return query.list();
	}

	public List<? extends AbstractModel> getBulkByIds(List<String> ids, String className)
	{
		Query query = getQuery("from "+className+" cn " +
				"where cn.id in (:ids)");
		query.setParameterList("ids", ids);
		
		return query.list();		
	}
	
	public Role createRole(Role role)
	{
		save(role);
		
		return role;
	}

	@Override
	public IGenericSynchroinzedLifeCycle createIGenericSynchroinzedLifeCycle(
			IGenericSynchroinzedLifeCycle model) {
		save(model);
		return model;
	}

	@Override
	public AbstractGUIDModel getAbstractGUIDModelById(Class<?> clazz, String id) {
		return (AbstractGUIDModel)loadByPrimaryKey(clazz, id);
	}

	@Override
	public IGenericSynchroinzedLifeCycle getIGenericSynchroinzedLifeCycleByID(
			Class<?> clazz, String id) {
		return (IGenericSynchroinzedLifeCycle)loadByPrimaryKey(clazz, id);
	}

	@Override
	public IGenericSynchroinzedLifeCycle updateIGenericSynchroinzedLifeCycle(
			IGenericSynchroinzedLifeCycle model) {
		updateObject(model);
		return model;
	}
	
	
	
}
