package com.homefellas.rm.notification;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.homefellas.exception.ValidationException;
import com.homefellas.rm.AbstractRMTestDao;
import com.homefellas.rm.RMTestModelBuilder;
import com.homefellas.rm.notification.ClientNotification.ClientNotificationStatusEnum;
import com.homefellas.rm.task.Task;
import com.homefellas.user.Profile;

public class ClientNotificationServiceTest extends AbstractRMTestDao
{

	@Autowired
	private ClientNotificationService clientNotificationService;
	
	@Test 
	public void createDevice()
	{
		Profile profile = createProfile();
		Device device = RMTestModelBuilder.device(profile);
		
		try
		{
			clientNotificationService.createDevice(device);
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
		}
		
		Assert.assertEquals(device, clientNotificationService.getDeviceById(device.getId()));
	}
	
//	@Test 
//	public void getBulkDevice()
//	{
//		Profile profile = createProfile();
//		Device device = RMTestModelBuilder.device(profile);
//		Device deviceUnderTest = null;
//		try
//		{
//			clientNotificationService.createDevice(device);
//			
//			deviceUnderTest = clientNotificationService.getBulkDevicesTX(device.getId(), profile.getEmail()).get(0);
//		}
//		catch (ValidationException e)
//		{
//			Assert.fail(e.getMessage());
//		}
//		
//		Assert.assertEquals(device, deviceUnderTest);
//	}
	
	
//	@Test
//	public void getBulkClientNotificationsTX()
//	{
//		Profile profile1 = createProfile();
//		Task task1 = createTask(profile1);
//		
//		ClientNotification clientNotification = new ClientNotification();
//		clientNotification.setMember(profile1.getMember());
//		clientNotification.setReferenceClassName(Task.class.getName());
//		clientNotification.setReferenceId(task1.getId());
//		clientNotification.generateGUIDKey();
//		
//		try
//		{
//			clientNotificationService.createClientNotification(clientNotification);
//			
//			Assert.assertNotNull(dao.loadByPrimaryKey(ClientNotification.class, clientNotification.getId()));
//			
//			List<ClientNotification> clientNotifications = clientNotificationService.getBulkClientNotificationsTX(clientNotification.getId(), profile1.getEmail());
//			Assert.assertTrue(clientNotifications.contains(clientNotification));
//		}
//		catch (ValidationException e)
//		{
//			Assert.fail(e.getMessage());
//		}
//	}

	@Test
	public void updateClientNotificationTX()
	{
		Profile profile1 = createProfile();
		Task task1 = createTask(profile1);
		
		ClientNotification clientNotification = new ClientNotification();
		clientNotification.setMember(profile1.getMember());
		clientNotification.setReferenceClassName(Task.class.getName());
		clientNotification.setReferenceId(task1.getId());
		clientNotification.generateGUIDKey();
		
		try
		{
			clientNotificationService.createClientNotification(clientNotification);
			
			Assert.assertNotNull(clientNotificationService.getClientNotificationById(clientNotification.getId()));
			
			clientNotification.setNotificationType(ClientNotificationStatusEnum.read.toString());
			clientNotificationService.updateClientNotification(clientNotification, profile1.getEmail());
			
			Assert.assertEquals(ClientNotificationStatusEnum.read.toString(), clientNotificationService.getClientNotificationById(clientNotification.getId()).getNotificationType());
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
		}
		
		
	}

	@Test
	public void createClientNotificationTX()
	{
		Profile profile1 = createProfile();
		Task task1 = createTask(profile1);
		
		ClientNotification clientNotification = new ClientNotification();
		clientNotification.setMember(profile1.getMember());
		clientNotification.setReferenceClassName(Task.class.getName());
		clientNotification.setReferenceId(task1.getId());
		clientNotification.generateGUIDKey();
		
		try
		{
			clientNotificationService.createClientNotification(clientNotification);
			
			Assert.assertNotNull(clientNotificationService.getClientNotificationById(clientNotification.getId()));
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
		}
		
		
	}

}
