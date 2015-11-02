package com.homefellas.rm.notification;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.homefellas.batch.INotificationServiceTX;
import com.homefellas.batch.Notification;
import com.homefellas.batch.NotificationService;
import com.homefellas.dao.core.IDao;
import com.homefellas.rm.RMTestModelBuilder;
import com.homefellas.rm.calendar.Calendar;
import com.homefellas.rm.calendar.GenericCalendarEnum;
import com.homefellas.rm.task.Category;
import com.homefellas.rm.task.GenericCategoryEnum;
import com.homefellas.rm.task.Task;
import com.homefellas.user.Profile;
import com.homefellas.ws.core.AbstractTestRMWebService;
import com.sun.jersey.api.client.GenericType;

public class ClientNotificationWebServiceTest extends AbstractTestRMWebService
{
	private IClientNotificationServiceTX clientNotificationServiceTX;
	protected Category categoryDefaultWork;
	protected Category categoryDefaultPersonal;
	protected Calendar calendarDefaultWork;
	
	private INotificationServiceTX notificationService;
	@Before
	public void setupDefaultDatabaseValues()
	{
		super.setupDefaultDatabaseValues();
		
		dao = (IDao)getServer().getSpringBean("dao");
		transactionManager = (PlatformTransactionManager)getServer().getSpringBean("transactionManager");
		clientNotificationServiceTX = (IClientNotificationServiceTX)getServer().getSpringBean("clientNotificationService");
		notificationService = (INotificationServiceTX)getServer().getSpringBean("notificationService");
		
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
		   @Override
		   protected void doInTransactionWithoutResult(TransactionStatus status) {
				
				categoryDefaultWork = GenericCategoryEnum.Work.getCategory();
				dao.save(categoryDefaultWork);
				
				categoryDefaultPersonal = GenericCategoryEnum.Personal.getCategory();
				dao.save(categoryDefaultPersonal);
				
				calendarDefaultWork = GenericCalendarEnum.Work.getCalendar();
				dao.save(calendarDefaultWork);
		   }});
		
	}
	
//	@Test
//	public void clientNotificationTaskEndReached()
//	{
//		Profile profile = createAndRetrieveProfile();
//		Task task = RMTestModelBuilder.task(profile);
//		task.setEndTime(new DateTime().plusDays(10));
//		task = createAndRetrieveTask(task);
//		
//		List<Notification> notifications = notificationService.getNotificationsForINotificationAndToEmailTX(task, profile.getEmail());
//		Assert.assertEquals(1, notifications.size());
//		
//		
//		
//		Map<String, List<Object>> syncs = sync(profile.getId(), 0);
//		List<Object> clientNotificationsIds = syncs.get(ClientNotification.class.getName());
//		
//		List<ClientNotification> clientNotifications = callSecuredWebService(ClientNotificationWebService.class, "getBulkClientNotifications", new GenericType<List<ClientNotification>>() {}, buildPathParms("{ids}", (String)clientNotificationsIds.get(0)), profile.getEmail()); 
//			
//		Assert.assertEquals(notifications.get(0).getId(), clientNotifications.get(0).getNotification().getId());
//		
//		
//	}
	
	@Test
	public void testCreateDevice()
	{
		Profile profile = createAndRetrieveProfile(); 
		Device device = RMTestModelBuilder.device(profile);
		
		device = callWebService(ClientNotificationWebService.class, "createDevice", Device.class, device);
		
		List<Device> devices = callSecuredWebService(ClientNotificationWebService.class, "getBulkDevices", new GenericType<List<Device>>() {}, buildPathParms("{ids}", device.getId()), profile.getEmail());
		
		Assert.assertEquals(device, devices.get(0));
	}

	

}
