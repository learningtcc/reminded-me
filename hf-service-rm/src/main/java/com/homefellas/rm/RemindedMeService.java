package com.homefellas.rm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.exception.ValidationException;
import com.homefellas.metrics.WebRequestMetric;
import com.homefellas.model.core.AbstractGUIDModel;
import com.homefellas.model.core.AbstractModel;
import com.homefellas.model.core.IGenericSynchroinzedLifeCycle;
import com.homefellas.rm.ISynchronizeable.SynchronizeableObject;
import com.homefellas.rm.ISynchronizeableFilteredDateRange.SynchronizeableFilteredDateRangeObject;
import com.homefellas.rm.ISynchronizeableInitialized.SynchronizeableInitializedObject;
import com.homefellas.rm.ITaskDependable.TaskDependableObject;
import com.homefellas.rm.calendar.Calendar;
import com.homefellas.rm.calendar.CalendarStoreCategory;
import com.homefellas.rm.calendar.CalendarStoreCategory.DefaultCalendarStoreCategoryEnum;
import com.homefellas.rm.calendar.CalendarStoreSubCategory;
import com.homefellas.rm.calendar.CalendarStoreSubCategory.DefaultCalendarStoreSubCategoryEnum;
import com.homefellas.rm.calendar.CalendarStoreUserDefinedCategory;
import com.homefellas.rm.calendar.GenericCalendarEnum;
import com.homefellas.rm.calendar.ICalendarDao;
import com.homefellas.service.core.AbstractService;
import com.homefellas.user.Role;
import com.homefellas.user.RoleEnum;
import com.homefellas.user.UserService;
import com.homefellas.user.UserValidationCodeEnum;

/**
 * This class contains helper services for the reminded.me project.
 * @author Tim Delesio
 *
 */
public class RemindedMeService extends AbstractService implements IRemindedMeService
{

	@Autowired
	private IRemindedMeDao remindedMeDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ICalendarDao calendarDao;
	
	
	public void setRemindedMeDao(IRemindedMeDao remindedMeDao)
	{
		this.remindedMeDao = remindedMeDao;
	}


	@Transactional
	public List<WebRequestMetric> getHistory(String tgt)
	{
		return remindedMeDao.getHistory(tgt);
	}
	
	/**
	 * This will create the default user roles in the database.  If the roles exist, then it will not re-add them.  The
	 * roles need to be added to the AuthorizationEnum as that is what drives the default roles.
	 * @see AuthorizationEnum
	 */
	@Transactional
	void createDefaultRoles()
	{
	
		for (RoleEnum authorizationEnum : RoleEnum.values())
		{
			//check if role exists
			long id =authorizationEnum.getId();
			Role role = remindedMeDao.getRole(id);
			if (role == null)
			{
				//does not so create a new one
				role = new Role();
				role.setRoleName(authorizationEnum.getRole());
				role.setId(authorizationEnum.getId());
				remindedMeDao.createRole(role);
			}
		} 
	}
		
	/**
	 * This will create the default list of calendars.  If hte calendar exists in the database it will not re-add it.
	 * The default calendars are stored in the GenericCalendarEnum
	 * @see GenericCalendarEnum
	 */
	@Transactional
	void createDefaultCalendars()
	{
		for (GenericCalendarEnum genericCalendarEnum:GenericCalendarEnum.values())
		{
//			Category category = (Category)dao.loadObject(Category.class, genericCategoryEnum.getCategory().getId());
			Calendar calendar = remindedMeDao.getCalendar(genericCalendarEnum.getCalendar().getId());
			if (calendar == null)
			{
				calendar = genericCalendarEnum.getCalendar();
				calendarDao.updateCalendar(calendar);
			}
		}
	}
	
	/**
	 * This will setup the database with all the default values.  It does this by calling the other defaulting methods
	 * in the class.
	 */
	@Transactional
	public void createDefaultDatabaseEntries()
	{
		createDefaultRoles();
//		createDefaultCategories();
		createDefaultCalendars();
		createDefaultCalendarStoreCategoriesAndSubCategories();
	}
	
	@Transactional
	public void createDefaultCalendarStoreCategoriesAndSubCategories()
	{
		Map<DefaultCalendarStoreCategoryEnum, CalendarStoreCategory> categoryMap = new HashMap<DefaultCalendarStoreCategoryEnum, CalendarStoreCategory>();
		Map<DefaultCalendarStoreSubCategoryEnum, CalendarStoreSubCategory> subCategoryMap = new HashMap<CalendarStoreSubCategory.DefaultCalendarStoreSubCategoryEnum, CalendarStoreSubCategory>();
		for (DefaultCalendarStoreCategoryEnum categoryEnum:DefaultCalendarStoreCategoryEnum.values())
		{
			
			CalendarStoreCategory category = new CalendarStoreCategory();
			category.setId(categoryEnum.getId());
			category.setCategoryName(categoryEnum.getCategoryName());
			
			categoryMap.put(categoryEnum, category);
			
		}
		
		createDefaultStoreCategories(categoryMap);
		
		for (DefaultCalendarStoreSubCategoryEnum subCategoryEnum:DefaultCalendarStoreSubCategoryEnum.values())
		{
			DefaultCalendarStoreCategoryEnum calendarStoreCategoryEnum = subCategoryEnum.getCalendarStoreCategory();
			CalendarStoreCategory category = categoryMap.get(calendarStoreCategoryEnum);
			
			CalendarStoreSubCategory subCategory = new CalendarStoreSubCategory();	
			subCategory.setId(subCategoryEnum.getId());
			subCategory.setCalendarStoreCategory(category);
			subCategory.setCategoryName(subCategoryEnum.getCategoryName());
			category.addSubCategory(subCategory);
	
			subCategoryMap.put(subCategoryEnum, subCategory);
		}
		
		createDefaultStoreSubCategories(subCategoryMap);
		
//		dao.flush();
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void createDefaultStoreCategories(Map<DefaultCalendarStoreCategoryEnum, CalendarStoreCategory> categoryMap)
	{
		for (CalendarStoreCategory calendarStoreCategory : categoryMap.values())
		{
			calendarDao.createCalendarStoreCategory(calendarStoreCategory);
		}
		
		
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void createDefaultStoreSubCategories(Map<DefaultCalendarStoreSubCategoryEnum, CalendarStoreSubCategory> subCategoryMap)
	{
		for (CalendarStoreSubCategory calendarStoreSubCategory : subCategoryMap.values())
		{
			calendarDao.createCalendarStoreSubCategory(calendarStoreSubCategory);
		}
//		for (DefaultCalendarStoreSubCategoryEnum subCategory : DefaultCalendarStoreSubCategoryEnum.values())
//		{
//			CalendarStoreUserDefinedSubCategory calendarStoreUserDefinedCategory =  new CalendarStoreUserDefinedCategory();
//			CalendarStoreSubCategory storeSubCategory = subCategoryMap.get(subCategory.getCalendarStoreSubCategoryEnum());
//			calendarStoreUserDefinedCategory.setCalendarStoreSubCategory(storeSubCategory);
//			calendarStoreUserDefinedCategory.setCategoryName(calendarStoreUserDefinedCategoryEnum.getName());
//			calendarStoreUserDefinedCategory.setId(calendarStoreUserDefinedCategoryEnum.getId());
//			calendarDao.createCalendarStoreUserDefinedCategory(calendarStoreUserDefinedCategory);
////			dao.save(calendarStoreUserDefinedCategory);
//			
//		}
	}
	
//	@Transactional(readOnly=true)
//	@CollectTimeMetrics
//	public Map<String, List> synchronizeObjects(String memberId, String deviceId, DateTime modifiedTime) throws ValidationException
//	{
//		return synchronizeObjects(memberId, deviceId, modifiedTime, null, null);
//	}
	
	@Transactional(readOnly=true)
	public Map<String, List> synchronizeInitialize(String memberId) throws ValidationException
	{
		UserDetails userDetails = userService.loadMemberById(memberId);
		if (userDetails == null)
			throw new ValidationException(UserValidationCodeEnum.MEMBER_NOT_FOUND);
		
		//grab all the synchronized objects
		SynchronizeableInitializedObject[] synchronizeableObjects = SynchronizeableInitializedObject.values();
		Map<String, List> returnList = new HashMap<String, List>(synchronizeableObjects.length);
		
		//loop over the enum to get the objects
		for (SynchronizeableInitializedObject synchronizeableObject:synchronizeableObjects)
		{
			if (!synchronizeableObject.isRetreiveSynchronized())
				continue;
			//grab all the sync objects of that type 
			ISynchronizeableInitialized synchronizeableInitialized = synchronizeableObject.getClassInstance();
			List sublist = remindedMeDao.getSynchronizeableObject(synchronizeableInitialized, memberId);
			
			//need to check to see if there is already a value set to the key
			returnList.put(synchronizeableObject.getClassName(), sublist);
		}
		
		return returnList;
	}
	
	/**
	 * This will synchronize the objects in the local data store with the data in the database.  An object will be returned in 
	 * this list if exists in the SynchronizeableObject enum.  In order to add an object to that enum, the object must also
	 * implement the ISynchronizeable interface.  A memberId is required for this operation to complete.  If you want all the 
	 * object (fresh set) then pass 0 as the modified time.  The key of the map that is returned is the modelName which is defined
	 * in the ISynchronizeable interface.
	 * @param memberId
	 * @param modifiedTime
	 * @return Map<String, List<Object>>
	 * @see SynchronizeableObject
	 * @see ISynchronizeable
	 */
	@Transactional(readOnly=true)
//	public Map<String, List> synchronizeObjects(String memberId, String deviceId, DateTime modifiedTime, DateTime startTime, DateTime endTime) throws ValidationException
	public Map<String, List> synchronizeObjects(String memberId, String deviceId, DateTime modifiedTime) throws ValidationException
	{
//		if (deviceId==null||"".equals(deviceId))
//			modifiedTime = null;
					
		
		//validate member
		UserDetails userDetails = userService.loadMemberById(memberId);
		if (userDetails == null)
			throw new ValidationException(UserValidationCodeEnum.MEMBER_NOT_FOUND);
		
		//grab all the synchronized objects
		SynchronizeableObject[] synchronizeableObjects = SynchronizeableObject.values();
		Map<String, List> returnList = new HashMap<String, List>(synchronizeableObjects.length);
		
		//loop over the enum to get the objects
		for (SynchronizeableObject synchronizeableObject:synchronizeableObjects)
		{
			//grab all the sync objects of that type
			List sublist = getSynchronizeableObject(synchronizeableObject, memberId, deviceId, modifiedTime);
			
			//need to check to see if there is already a value set to the key
			List existingList = returnList.get(synchronizeableObject.getMapKeyClass());
			if (existingList==null)
				existingList = new ArrayList();
			
			existingList.addAll(sublist);
			
			returnList.put(synchronizeableObject.getMapKeyClass(), existingList);
		}
		
		List<Object> time = new ArrayList<Object>(1);
		time.add(System.currentTimeMillis());
		returnList.put(ISynchronizeable.SYSTEM_TIME, time);
		
		//check to see if there was a passed deviceid, if not then generate a new one
		if (deviceId == null||"".equals(deviceId))
		{
			UUID uuid = UUID.randomUUID();
			deviceId = String.valueOf(uuid.getLeastSignificantBits())+String.valueOf(uuid.getMostSignificantBits());
		}
		
		//add the device id back to the list
		List<Object> deviceids = new ArrayList<Object>(1);
		deviceids.add(deviceId);
		returnList.put(ISynchronizeable.DEVICE_ID,deviceids);
		
		return returnList;
	}
	
	@Transactional(readOnly=true)
	public Map<String, List> synchronizeFilteredDateRangeTX(String memberId, String deviceId, DateTime modifiedDateTime, DateTime startTimeStamp, DateTime endTimeStamp) throws ValidationException
	{
		SynchronizeableFilteredDateRangeObject[] synchronizeableObjects = SynchronizeableFilteredDateRangeObject.values();
		Map<String, List> returnList = new HashMap<String, List>(synchronizeableObjects.length);
		
		final long upperBoundEndTime = new DateTime().plusYears(10).getMillis();
		long startTime=0, endTime=upperBoundEndTime, modifiedTime = 0;
		boolean dateless = false;
		//short circuit if there is no range
		if (startTimeStamp != null)
			startTime = startTimeStamp.getMillis();
		
		if (endTimeStamp != null)
			endTime = endTimeStamp.getMillis();
		
		if (modifiedDateTime != null)
			modifiedTime = modifiedDateTime.getMillis();
		
		UserDetails userDetails = userService.loadMemberById(memberId);
		if (userDetails == null)
			throw new ValidationException(UserValidationCodeEnum.MEMBER_NOT_FOUND);
		
		//loop over the enum to get the objects
		for (SynchronizeableFilteredDateRangeObject synchronizeableObject:synchronizeableObjects)
		{
			ISynchronizeableFilteredDateRange synchronizeable = synchronizeableObject.getClassInstance();
			List sublist=null;
			//grab all the sync objects of that type
			if (startTime==0 && endTime==upperBoundEndTime)
			{
				sublist = remindedMeDao.getDateLessSynchronizeableFilterObject(synchronizeable, memberId, deviceId, modifiedTime);
			}
			else
			{
				sublist = remindedMeDao.getSynchronizeableFilterObject(synchronizeable, memberId, deviceId, modifiedTime, startTime, endTime);
			}
			
			//need to check to see if there is already a value set to the key
			List existingList = returnList.get(synchronizeableObject.getMapKeyClass());
			if (existingList==null)
				existingList = new ArrayList();
			
			existingList.addAll(sublist);
			returnList.put(synchronizeableObject.getMapKeyClass(), existingList);
			
			if (existingList.isEmpty())
				continue;
			
			//handle dependancies of the SynchronizeableFilteredDateRangeObject
			TaskDependableObject[] taskDependableObjects = TaskDependableObject.values();
			for (TaskDependableObject taskDependableObject:taskDependableObjects)
			{
				ITaskDependable iTaskDependable = taskDependableObject.getClassInstance();
				returnList.put(taskDependableObject.getMapKeyClass(), remindedMeDao.getTaskDependableObjects(iTaskDependable, existingList, deviceId));
			}
		}
		
		List<Object> time = new ArrayList<Object>(1);
		time.add(System.currentTimeMillis());
		returnList.put(ISynchronizeable.SYSTEM_TIME, time);
		
		//check to see if there was a passed deviceid, if not then generate a new one
		if (deviceId == null||"".equals(deviceId))
		{
			UUID uuid = UUID.randomUUID();
			deviceId = String.valueOf(uuid.getLeastSignificantBits())+String.valueOf(uuid.getMostSignificantBits());
		}
		
		//add the device id back to the list
		List<Object> deviceids = new ArrayList<Object>(1);
		deviceids.add(deviceId);
		returnList.put(ISynchronizeable.DEVICE_ID,deviceids);
		
		return returnList;
//			return getSynchronizeableObject(synchronizeableObject, memberId, deviceId, modifiedDateTime);
//		
//			
//		
//		ISynchronizeable synchronizeable = synchronizeableObject.getClassInstance();
//		if (!(synchronizeable instanceof ISynchronizeableFilteredRange))
//			return Collections.EMPTY_LIST;
//		else 
//			

	}
	
	/**
	 * This is just a helper method to decide whether or not to load all the object or do some business processing against 
	 * the time.
	 * @param synchronizeable
	 * @param memberId
	 * @param modifedTimeStamp
	 * @return List<Object>
	 */
	private List<Object> getSynchronizeableObject(SynchronizeableObject synchronizeableObject, String memberId, String deviceId, DateTime modifedTimeStamp)
	{
		ISynchronizeable synchronizeable = synchronizeableObject.getClassInstance();
		if (synchronizeable instanceof ISynchronizeableFiltered)
		{
			if (modifedTimeStamp == null)
			{
				return remindedMeDao.getSynchronizeableFilterObject((ISynchronizeableFiltered)synchronizeable, memberId);
			}
			else
			{
				return remindedMeDao.getSynchronizeableFilterObject((ISynchronizeableFiltered)synchronizeable, memberId, deviceId, modifedTimeStamp.getMillis());
			}
		}
		else
		{
			if (modifedTimeStamp == null)
			{
				return remindedMeDao.getSynchronizeableObject(synchronizeable, memberId);
			}
			else
				return remindedMeDao.getSynchronizeableObject(synchronizeable, memberId, deviceId, modifedTimeStamp.getMillis());
		}
		
	}
	
	@Transactional
	public AbstractGUIDModel getModel(String fqClassName, String id) throws ValidationException
	{
		Class<?> clazz=null;
		try
		{
			clazz = Class.forName(fqClassName);
		}
		catch (ClassNotFoundException e)
		{
			throw new ValidationException(ValidationCodeEnum.MODEL_DOES_NOT_EXIST);
		}
		
//		AbstractGUIDModel model = (AbstractGUIDModel)dao.loadByPrimaryKey(clazz, id);
		AbstractGUIDModel model = remindedMeDao.getAbstractGUIDModelById(clazz, id);
		if (model==null)
			throw new ValidationException(ValidationCodeEnum.MODEL_DOES_NOT_EXIST);
		else
			return model;
	}
	
	@Transactional(readOnly = true)
	public List<? extends AbstractModel> getBulkTX(String taskDelimiter, String loggedInUserEmail, String modelPath) throws ValidationException
	{
		if (loggedInUserEmail == null || "".equals(loggedInUserEmail.trim()))
			throw new ValidationException(UserValidationCodeEnum.USER_MUST_BE_LOGGED_IN);
		
		StringTokenizer stringTokenizer = new StringTokenizer(taskDelimiter, ",");
		List<String> ids = new ArrayList<String>();
		while (stringTokenizer.hasMoreTokens())
		{
			String id = stringTokenizer.nextToken();
			ids.add(id);
		}
		
		String className=null; 
		try
		{
			SynchronizeableObject object = SynchronizeableObject.valueOf(modelPath);
			className = object.getClassSimpleName();
		}
		catch (IllegalArgumentException argumentException)
		{
			try
			{
				SynchronizeableInitializedObject object = SynchronizeableInitializedObject.valueOf(modelPath);
				className = object.getClassSimpleName();
			}
			catch (IllegalArgumentException argumentException2)
			{
				throw new ValidationException(ValidationCodeEnum.INVALID_BULK_PATH);
			}
		}
		
		if (className==null)
			throw new ValidationException(ValidationCodeEnum.INVALID_BULK_PATH);
		
		return remindedMeDao.getBulkByIds(ids, className);
		
		
	}
	
	@Transactional
	public void deleteTX(String id, String modelPath, String loggedInUserEmail) throws ValidationException
	{
		Class clazz = getClassFromJson(modelPath);
//		IGenericSynchroinzedLifeCycle model = (IGenericSynchroinzedLifeCycle)dao.loadByPrimaryKey(clazz, id);
		IGenericSynchroinzedLifeCycle model = (IGenericSynchroinzedLifeCycle)remindedMeDao.getAbstractGUIDModelById(clazz, id);
		if (model == null)
			throw new ValidationException(ValidationCodeEnum.MODEL_NOT_FOUND);
		
		//we need to mark it as inactive
		model.markForDeletion();

//		dao.merge(model);
		remindedMeDao.updateIGenericSynchroinzedLifeCycle(model);
	}
	
	
	@Transactional
	public IGenericSynchroinzedLifeCycle updateTX(String json, String modelPath, String loggedInUserEmail) throws ValidationException
	{
		if (loggedInUserEmail == null || "".equals(loggedInUserEmail.trim()))
			throw new ValidationException(UserValidationCodeEnum.USER_MUST_BE_LOGGED_IN);
		
		Class clazz = getClassFromJson(modelPath);
		IGenericSynchroinzedLifeCycle model = buildModel(json, clazz);
		
		//validate
		model.validate();
		
//		final IGenericSynchroinzedLifeCycle fromDB = (IGenericSynchroinzedLifeCycle)dao.loadByPrimaryKey(clazz, model.getId());
		final IGenericSynchroinzedLifeCycle fromDB = remindedMeDao.getIGenericSynchroinzedLifeCycleByID(clazz, model.getId());
		if (fromDB==null || model==null)
			throw new ValidationException(ValidationCodeEnum.SYNC_UPDATE_FAILED_BECAUSE_PASSED_OBJECT_IS_NULL_OR_OBJECT_IN_DB_IS_NULL);
		
		if (!fromDB.getEmailForAuthorization().equals(loggedInUserEmail))
			throw new ValidationException(ValidationCodeEnum.MEMBER_DOES_NOT_HAVE_AUTHORIZATION);
		
		if (fromDB.getModifiedDate()>model.getModifiedDate())
		{
			if (fromDB.getClientUpdateTimeStamp()==null || fromDB.getModifiedDate()>model.getClientUpdateTimeStamp().getMillis())
				throw new ValidationException(ValidationCodeEnum.SYNCHRONIZATION_REQUIRED);
		}
		
		
		//check to see if the modified date from the db is before the one being passed
		model.setModifiedDateZone(new DateTime());
		
		//update the task in the db
//		dao.merge(model);
		remindedMeDao.updateIGenericSynchroinzedLifeCycle(model);
		return model;
	}
	
	@Transactional
	public IGenericSynchroinzedLifeCycle createTX(String json, String modelPath) throws ValidationException
	{
		
		IGenericSynchroinzedLifeCycle model = buildModel(json, modelPath);
		
		//validate
		model.validate();
		
		//save
//		dao.save(model);
		remindedMeDao.createIGenericSynchroinzedLifeCycle(model);
		return model;
		
		//return
	}
	
	private Class getClassFromJson(String modelPath) throws ValidationException
	{
		Class clazz = null;
		try
		{
			SynchronizeableObject object = SynchronizeableObject.valueOf(modelPath);
			clazz= Class.forName(object.getClassName());
		}
		catch (ClassNotFoundException classNotFoundException)
		{
			clazz = getClassFromJsonTry2(modelPath);
		}
		catch (IllegalArgumentException illegalArgumentException)
		{
			clazz = getClassFromJsonTry2(modelPath);
		}
		
		return clazz;
	}
	
	private Class getClassFromJsonTry2(String modelPath) throws ValidationException
	{
		Class clazz = null;
		try
		{
			SynchronizeableInitializedObject object = SynchronizeableInitializedObject.valueOf(modelPath);
			clazz= Class.forName(object.getClassName());
		}
		catch (ClassNotFoundException classNotFoundException2)
		{
			throw new ValidationException(ValidationCodeEnum.INVALID_BULK_PATH);
		}
		
		return clazz;
	}
	private IGenericSynchroinzedLifeCycle buildModel(String json, String modelPath) throws ValidationException
	{
		return buildModel(json, getClassFromJson(modelPath));
	}
	
	private IGenericSynchroinzedLifeCycle buildModel(String json, Class clazz) throws ValidationException
	{
		ObjectMapper objectMapper = new ObjectMapper();
		IGenericSynchroinzedLifeCycle model;
		try
		{
			model = (IGenericSynchroinzedLifeCycle)objectMapper.readValue(json, clazz);
		}
		catch (JsonParseException e)
		{
			throw new ValidationException(ValidationCodeEnum.INVALID_BULK_PATH);
		}
		catch (JsonMappingException e)
		{
			throw new ValidationException(ValidationCodeEnum.INVALID_BULK_PATH);
		}
		catch (IOException e)
		{
			throw new ValidationException(ValidationCodeEnum.INVALID_BULK_PATH);
		}
		
		return model;
	}
}
