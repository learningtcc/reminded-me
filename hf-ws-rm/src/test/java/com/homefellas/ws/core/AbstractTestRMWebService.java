package com.homefellas.ws.core;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.junit.Rule;

import com.homefellas.rm.RMTestModelBuilder;
import com.homefellas.rm.RemindedMeWebService;
import com.homefellas.rm.calendar.Calendar;
import com.homefellas.rm.reminder.Alarm;
import com.homefellas.rm.reminder.ReminderWebService;
import com.homefellas.rm.share.Invite;
import com.homefellas.rm.share.Share;
import com.homefellas.rm.share.ShareWebService;
import com.homefellas.rm.task.AbstractTask.PriorityEnum;
import com.homefellas.rm.task.Category;
import com.homefellas.rm.task.Task;
import com.homefellas.rm.task.TaskWebService;
import com.homefellas.rm.user.UserWebService;
import com.homefellas.rule.AbstractEmbeddedJettyServer;
import com.homefellas.rule.EmbeddedTestServer;
import com.homefellas.user.Member;
import com.homefellas.user.Profile;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;

public abstract class AbstractTestRMWebService extends AbstractTestWebService
{
	@Rule
	public EmbeddedTestServer server = new EmbeddedTestServer();
	
//	@Rule
//	public SSOServer ssoServer = new SSOServer();
	
//	protected Task registerAndCreateTask()
//	{
//		Profile profile = registerAndRetrieveProfile();
//		Task task = createAndRetrieveTask(profile);
//		
//		Assert.assertTrue(task.isPrimaryKeySet());
//		
//		return task;
//		
//	}
	
	protected void subList(Task parent, Task child)
	{
//		parent.incrementSubTaskCount();
//		parent.setaParent(true);
//		
//		updateTask(parent);
//		
//		child.setParentId(parent.getId());
//		
//		updateTask(child);
		
		
		
		child.setParentId(parent.getId());
		updateTask(child);

		parent.incrementSubTaskCount();
		parent.setaParent(true);
		updateTask(parent); 
	}
	
	protected <T> T createGeneric(Class<T> clazz, T objectToConvert, String path)
	{
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "";
		try
		{
			json = objectMapper.writeValueAsString(objectToConvert);
		}
		catch (Exception exception)
		{
			Assert.fail(exception.getMessage());
		}
		
		return callWebService(RemindedMeWebService.class, "create", clazz, buildPathParms("{model}", path), json);
	}
	
	protected <T> T updateGeneric(Class<T> clazz, T objectToConvert, String path, String email)
	{
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "";
		try
		{
			json = objectMapper.writeValueAsString(objectToConvert);
		}
		catch (Exception exception)
		{
			Assert.fail(exception.getMessage());
		}
		
		return callSecuredWebService(RemindedMeWebService.class, "update", clazz, buildPathParms("{model}", path), json, email);
	
	}
	
	protected void deleteGeneric(Map<String, String> pathParms, String email)
	{
		callSecuredWebService(RemindedMeWebService.class, "delete", Boolean.class, pathParms, email);
	}
//	
//	protected <T> List<T> getGenericBulk(Class<T> clazz, String ids, String path, String email)
//	{
//		Map<String, String> pathParms = buildPathParms("{model}", path);
//		pathParms.put("{ids}", ids);
//		
//		List<T> list = callSecuredWebService(RemindedMeWebService.class, "getBulk", new GenericType<List<T>>(){}, pathParms, email);
//		return list;
//	}
	
	
	protected Map<String, List<Object>> syncRnage(String memberId, long syncTime, long startTime, long endTime, String deviceid)
	{
		Map<String, String> pathParms = new HashMap<String, String>();
		pathParms.put("{memberId}", memberId);
		pathParms.put("{lastModifiedTime}", String.valueOf(syncTime));
		pathParms.put("{deviceid}", deviceid);
		pathParms.put("{firstModifiedTime}", String.valueOf(startTime));
		pathParms.put("{lastModifiedTime}", String.valueOf(endTime));
		
		return callWebService(RemindedMeWebService.class, "synchronizeObjects", new GenericType<Map<String, List<Object>>>() {}, pathParms);
	}
	
	protected Map<String, List<Object>> sync(String memberId, long syncTime, String deviceid)
	{
		Map<String, String> pathParms = new HashMap<String, String>();
		pathParms.put("{memberId}", memberId);
		pathParms.put("{lastModifiedTime}", String.valueOf(syncTime));
		pathParms.put("{deviceid}", deviceid);
		return callWebService(RemindedMeWebService.class, "synchronizeObjects", new GenericType<Map<String, List<Object>>>() {}, pathParms);
	}
	
	protected Map<String, List<Object>> sync(String memberId, long syncTime)
	{
		Map<String, String> pathParms = new HashMap<String, String>();
		pathParms.put("{memberId}", memberId);
		pathParms.put("{lastModifiedTime}", String.valueOf(syncTime));
		return callWebService(RemindedMeWebService.class, "synchronizeObjects", new GenericType<Map<String, List<Object>>>() {}, pathParms);
	}
	
	protected Invite createShare(Task task, String emailToShareWith, String emailAliasToShareWith)
	{
		//create shares
		Map<String, String> emailAddresses = new HashMap<String, String>();
		emailAddresses.put(emailToShareWith, emailAliasToShareWith);
		return createShares(task, emailAddresses);
	}
	
	protected Invite createShareTaskAndOwner(String emailToShareWith, String emailAliasToShareWith)
	{
		Profile owner = createAndRetrieveProfile();
		
		//create two tasks
		Task task1 = createAndRetrieveTask(owner);
		
		return createShare(task1, emailToShareWith, emailAliasToShareWith);
	}
	
	protected Task createAndRetrieveTask()
	{
		
		Profile profile = createAndRetrieveProfile();
		return createAndRetrieveTask(profile);
	}
	
	protected Alarm createAndRetrieveAlarm(Task task)
	{
//		Alarm alarm = RMTestModelBuilder.alarm(task, new DateTime().plusHours(1));
//		return callWebService(ReminderWebService.class, "createAlarm", Alarm.class, alarm);
		DateTime alarmTime;
		if (task.getEndTime()!=null)
			alarmTime = task.getEndTime().plusHours(1);
		else
			alarmTime = new DateTime().plusHours(1);
		return createAndRetrieveAlarm(task, alarmTime);
	}
	
	protected Alarm createAndRetrieveAlarm(Task task, DateTime alarmTime)
	{
		Alarm alarm = RMTestModelBuilder.alarm(task, alarmTime);
		return callWebService(ReminderWebService.class, "createAlarm", Alarm.class, alarm);
	}
	
	protected Task createAndRetrieveTask(Profile profile)
	{
//		Task task = RMTestModelBuilder.buildSampleTask(true, null, null, true, null, profile, null, PriorityEnum.HIGH, null,null);
		Task task = RMTestModelBuilder.task(profile);
		task.setPriority(PriorityEnum.HIGH.ordinal());
		task.setPublicTask(true);
		return createAndRetrieveTask(task);
	}
	
	protected Task createAndRetrieveTask(Task task)
	{
		ClientResponse response = callWebService(TaskWebService.class, "createTask", ClientResponse.class, task);
		assertOKStatus(response);
		
		task = response.getEntity(Task.class);
		
		Assert.assertTrue(task.isPrimaryKeySet());
		return task;
	}
	
	
	protected Profile createAndRetrieveProfile()
	{
		Profile profile = RMTestModelBuilder.buildBasicMember(true, passwordEncoder);	
		return createAndRetrieveProfile(profile);
	}
	
	protected Profile createAndRetrieveProfile(String emailAddress)
	{
		Profile profile = RMTestModelBuilder.buildBasicMember(true, passwordEncoder);
		profile.getMember().setEmail(emailAddress);
		return createAndRetrieveProfile(profile);
	}
	
	protected Profile createAndRetrieveProfile(Profile profile)
	{
		ClientResponse response = callWebService(UserWebService.class, "registerMember", ClientResponse.class, profile);
		
		assertOKStatus(response);
		profile = response.getEntity(Profile.class);
		assertTrue(profile.isPrimaryKeySet());

		return profile;
	}
	
	protected List<Task> retrieveTaskes(Profile profile)
	{
		Map<String, String> pathParms = new HashMap<String, String>();
		pathParms.put("{profileId}", String.valueOf(profile.getId()));  
		return callWebService(TaskWebService.class, "getTasksForMember",new GenericType<List<Task>>() {}, pathParms);
	}
	
	protected Category createAndRetrieveCategory(String categoryName, Member member)
	{
		Category category = new Category();
		category.generateGUIDKey();
		category.setCategoryName(categoryName);
		category.setMember(member);
		
		//create category
		ClientResponse clientResponse = callWebService(TaskWebService.class, "createCategory", ClientResponse.class, category);
		assertOKStatus(clientResponse);
		category = clientResponse.getEntity(Category.class);
		Assert.assertTrue(category.isPrimaryKeySet());
		
		return category;
	}
	
	protected Calendar createAndRetrieveCalendar(String calendarName, Member member)
	{
		return createAndRetrieveCalendar(calendarName, member, true);
	}
	
	
	protected Calendar createAndRetrieveCalendar(String calendarName, Member member, boolean ispublic)
	{
		Profile profile = new Profile(member.getId());
		profile.setMember(member);
		
		Calendar calendar = new Calendar();
		calendar.generateGUIDKey();
		calendar.setCalendarName(calendarName);
		calendar.setMember(profile);
		calendar.setPublicList(ispublic);
		
		//create category
		ClientResponse clientResponse = callWebService(TaskWebService.class, "createCalendar", ClientResponse.class, calendar);
		assertOKStatus(clientResponse);
		calendar = clientResponse.getEntity(Calendar.class);
		Assert.assertTrue(calendar.isPrimaryKeySet());
		
		return calendar;
	}
	
	@Override
	protected AbstractEmbeddedJettyServer getServer()
	{
		return server;
	}
	
	



	@Override
	protected String getContextRoot()
	{
		return "/ws";
	}


	protected Invite createAndRetrieveShares(Task task)
	{
		Invite invite = new Invite();
		invite.setMessage("Hey, <br/><br/>I would like to invite you to...<br/><br/>Dfsdf<br/>On : Sat Feb 02 2013 Sat Feb 2 11:59 pm IST<br/><br/> <a href='${link}'>Click here</a> to see more details and to let me know if you are In or OUT<br/><br/> Shared by Sabith via <a href='http://reminded.me'>REMINDED.me</a><br/>");
		invite.setSubject("Invitation for : dfsdf");
		invite.setDirectLink("http://localhost");
		
		Map<String, String> emailAddresses = new HashMap<String, String>();
		emailAddresses.put("tdelesio@gmail.com", "Tim Delesio");
		invite.setEmailAddresses(emailAddresses);
		invite.setShareId(task.getId());
		invite = callWebService(ShareWebService.class, "shareTask", Invite.class, invite);
		
		return invite;
	}
	
	protected Invite createShares(Task task, Profile profile)
	{
		Map<String, String> emailAddresses = new HashMap<String, String>();
		emailAddresses.put(profile.getMember().getEmail(), profile.getName());
		return createShares(task, emailAddresses);
		
	}
	
	
	protected Share createAndRetrieveShares(Task task, Profile profile)
	{
		Map<String, String> emailAddresses = new HashMap<String, String>();
		emailAddresses.put(profile.getMember().getEmail(), profile.getName());
		createShares(task, emailAddresses);
		
		Share share = retrieveShare(task, profile);
		
		Assert.assertNotNull(share);
		
		return share;
		
	}
	
	protected Invite createAndRetrieveCalendarShares(Calendar calendar, Profile profile)
	{
		Map<String, String> emailAddresses = new HashMap<String, String>();
		emailAddresses.put(profile.getMember().getEmail(), profile.getName());
		return createAndRetrieveCalendarShares(calendar, emailAddresses);
		
	}
	
	protected Invite createShares(Task task, Map<String, String> emailAddresses)
	{
		Invite invite = new Invite();
		invite.setMessage("Hey, <br/><br/>I would like to invite you to...<br/><br/>Dfsdf<br/>On : Sat Feb 02 2013 Sat Feb 2 11:59 pm IST<br/><br/> <a href='${link}'>Click here</a> to see more details and to let me know if you are In or OUT<br/><br/> Shared by Sabith via <a href='http://reminded.me'>REMINDED.me</a><br/>");
		invite.setSubject("new task");
		invite.setDirectLink("http://localhost");
		
		invite.setEmailAddresses(emailAddresses);
		invite.setShareId(task.getId());
		invite = callWebService(ShareWebService.class, "shareTask", Invite.class, invite);
		
		return invite;
	}
	
	protected Invite createAndRetrieveCalendarShares(Calendar calendar, Map<String, String> emailAddresses)
	{
		Invite invite = new Invite();
		invite.setMessage("Hey, <br/><br/>I would like to invite you to...<br/><br/>Dfsdf<br/>On : Sat Feb 02 2013 Sat Feb 2 11:59 pm IST<br/><br/> <a href='${link}'>Click here</a> to see more details and to let me know if you are In or OUT<br/><br/> Shared by Sabith via <a href='http://reminded.me'>REMINDED.me</a><br/>");
		invite.setSubject("new task");
		invite.setDirectLink("http://localhost");
		
		invite.setEmailAddresses(emailAddresses);
		invite.setShareId(calendar.getId());
		invite = callWebService(ShareWebService.class, "shareCalendar", Invite.class, invite);
		
		return invite;
	}
	
	protected Share retrieveShareDirectLink(Task task, String email)
	{
		Map<String, String> pathParms = new HashMap<String, String>();
		pathParms.put("{taskId}", task.getId());
		pathParms.put("{email}", email);
		Share share = callWebService(ShareWebService.class, "getPrivateShareDirectLink", Share.class, pathParms);
		
		Assert.assertNotNull(share);
		Assert.assertTrue(share.isPrimaryKeySet());
		
		return share;
	}
	protected Share retrieveShare(Task task, Profile profile)
	{
		Map<String, String> pathParms = buildPathParms("{taskid}", task.getId());
		pathParms.put("{memberid}", profile.getId());
		ClientResponse response = callWebService(ShareWebService.class, "getShareForUserAndTask", ClientResponse.class, pathParms);
		if (response.getStatus()==400)
			return null;
		
		Share share = response.getEntity(Share.class);
		Assert.assertTrue(share.isPrimaryKeySet());
		
		return share;
	}

	protected <T>T callSecuredWebService(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, Class<T> returnClass, Object inputObject, String loggedInEmailAddress)
	{
		Method m=null;
		try
		{
			m = webServiceClass.getSuperclass().getDeclaredMethod("setMemberEmail", String.class);
		
		
			m.invoke(null, loggedInEmailAddress);
//			webService.setMemberEmail(loggedInEmailAddress);
			
			return callWebService(webServiceClass, webServiceMethodName, returnClass, inputObject);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			return null;
		}
		finally
		{
			try
			{
				//reset the session email back to null
				m.invoke(null, "");
			}
			catch (Exception exception2)
			{
				exception2.printStackTrace();
				return null;
			}
		}
	}
	protected <T>T callSecuredWebService(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, GenericType<T> returnClass, Map<String, String> pathParms, String loggedInEmailAddress)
	{
		Method m=null;
		try
		{
			m = webServiceClass.getMethod("setMemberEmail", String.class);
		
		
			m.invoke(null, loggedInEmailAddress);
//			webService.setMemberEmail(loggedInEmailAddress);
			
			return callWebService(webServiceClass, webServiceMethodName, returnClass, pathParms);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			return null;
		}
		finally
		{
			try
			{
				m.invoke(null, "");
			}
			catch (Exception exception2)
			{
				exception2.printStackTrace();
				return null;
			}
		}
	}
	
	
	protected <T>T callSecuredWebService(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, Class<T> returnClass, Map<String, String> pathParms, String loggedInEmailAddress)
	{
		return callSecuredWebService(webServiceClass, webServiceMethodName, returnClass, pathParms, null, loggedInEmailAddress);
	}
	
	
	protected <T>T callSecuredWebService(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, Class<T> returnClass, Map<String, String> pathParms, Object posteddata, String loggedInEmailAddress)
	{
		
		Method m=null;
		try
		{
			m = webServiceClass.getMethod("setMemberEmail", String.class);
		
		
			m.invoke(null, loggedInEmailAddress);
//			webService.setMemberEmail(loggedInEmailAddress);
			
			return callWebService(webServiceClass, webServiceMethodName, returnClass, pathParms, posteddata);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			return null;
		}
		finally
		{
			try
			{
				m.invoke(null, "");
			}
			catch (Exception exception2)
			{
				exception2.printStackTrace();
				return null;
			}
		}
	}
	
	protected Task getBulkTasks(String taskid, String loggedInEmail)
	{
		return callSecuredWebService(TaskWebService.class, "getBulkTasks", new GenericType<List<Task>>(){}, buildPathParms("{ids}", taskid), loggedInEmail).get(0);
	}
	
	protected List<Task> getBulkTasks(List<Object> taskids, String loggedInEmailAddress)
	{
		StringBuffer buffer = new StringBuffer();		
		for (int i=0;i<taskids.size();i++)
		{
			buffer.append(taskids.get(i));
			if (i!=taskids.size()-1)
			{
				buffer.append(",");
			}
		}
		return callSecuredWebService(TaskWebService.class, "getBulkTasks", new GenericType<List<Task>>(){}, buildPathParms("{ids}", buffer.toString()), loggedInEmailAddress);
		
	}
	
	
	
	protected List<Alarm> getBulkAlarms(List<Object> alarmIds, String loggedInEmail)
	{
		StringBuffer buffer = new StringBuffer();		
		for (int i=0;i<alarmIds.size();i++)
		{
			buffer.append(alarmIds.get(i));
			if (i!=alarmIds.size()-1)
			{
				buffer.append(",");
			}
		}
		
		return callSecuredWebService(ReminderWebService.class, "getBulkAlarms", new GenericType<List<Alarm>>(){}, buildPathParms("{ids}", buffer.toString()), loggedInEmail);
	}
	
	protected Alarm getBulkAlarm(String alarmId, String loggedInEmail)
	{
		return callSecuredWebService(ReminderWebService.class, "getBulkAlarms", new GenericType<List<Alarm>>(){}, buildPathParms("{ids}", alarmId), loggedInEmail).get(0);
	}
	
	protected Share getBulkShare(String shareid, String loggedInEmail)
	{
		return callSecuredWebService(ShareWebService.class, "getBulkShares", new GenericType<List<Share>>(){}, buildPathParms("{ids}", shareid), loggedInEmail).get(0);
	}
	
	protected List<Share> getBulkShare(List<String> taskids)
	{
		StringBuffer buffer = new StringBuffer();		
		for (int i=0;i<taskids.size();i++)
		{
			buffer.append(taskids.get(i));
			if (i!=taskids.size()-1)
			{
				buffer.append(",");
			}
		}
		return callWebService(ShareWebService.class, "getBulkShares", new GenericType<List<Share>>(){}, buildPathParms("{ids}", buffer.toString()));
		
	}

//	protected Task sendUpdateTask(Task task, long timestamp)
//	{
//		task.setModifiedDateZone(new DateTime(timestamp));
//		return callSecuredWebService(TaskWebService.class, "updateTask", Task.class, task, task.getTaskCreator().getMember().getEmail());
//	}
	
	protected Task updateTask(Task task)
	{
//		task.setModifiedDateZone(new DateTime());
		return callSecuredWebService(TaskWebService.class, "updateTask", Task.class, task, task.getTaskCreator().getMember().getEmail());
	}
	
	protected Alarm updateAlarm(Alarm alarm)
	{
		return callWebService(ReminderWebService.class, "updateAlarm", Alarm.class, alarm);
	}
	
	protected Task updateTask(Task task, String userMakingChange)
	{
//		task.setModifiedDateZone(new DateTime());
		return callSecuredWebService(TaskWebService.class, "updateTask", Task.class, task, userMakingChange);
	}
	
	protected Share acceptShare(Task task, Member sharee)
	{
		Map<String, String> pathParms = buildPathParms("{taskid}", String.valueOf(task.getId()));
		pathParms.put("{memberid}", sharee.getId());
		return callWebService(ShareWebService.class, "acceptShare", Share.class, pathParms);
	}
	
	protected Share acceptShare(Share share)
	{
		Map<String, String> pathParms = buildPathParms("{shareid}", share.getId());
		return callWebService(ShareWebService.class, "acceptShareOld", Share.class, pathParms);
	}

	protected Share declineShare(Share share)
	{
		Map<String, String> pathParms = buildPathParms("{shareid}", share.getId());
		return callWebService(ShareWebService.class, "declineShare", Share.class, pathParms);
	}
	
	
	
	
}
