package com.homefellas.rm;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.exception.ValidationException;
import com.homefellas.rm.ISynchronizeable.SynchronizeableObject;
import com.homefellas.rm.reminder.Alarm;
import com.homefellas.rm.task.AbstractTask.PriorityEnum;
import com.homefellas.rm.task.AbstractTask.ProgressEnum;
import com.homefellas.rm.task.Task;
import com.homefellas.user.Profile;

public class RemindedMeDaoTest extends AbstractRMTestDao
{

	@Resource(name="remindedMeDao")
	private IRemindedMeDao remindedMeDao;
	
	@Test
	@Transactional
	public void getTaskDependable()
	{
		Profile profile = createProfile(); 
		Task task = createTask(profile);
		
		Alarm alarm = createAlarm(task, new DateTime().plusDays(1));
		
		List<String> taskIds = new ArrayList<String>();
		taskIds.add(task.getId());
		List<Object> taskDependables = remindedMeDao.getTaskDependableObjects(alarm, taskIds, null);
		
		Assert.assertTrue(taskDependables.contains(alarm.getId()));
	}
	
	
	@Test
	@Transactional
	public void getSynchronizeableFilterObjectWithRange()
	{
		Profile profile1 = createProfile();
		
		remindedMeDao.getSynchronizeableFilterObject(new Task(), profile1.getId(), "12345", System.currentTimeMillis(), System.currentTimeMillis()-100000, System.currentTimeMillis()+100000);
	}
	
	@Test
	@Transactional
	public void getSynchronizeableObjectWithRange()
	{
		Profile profile1 = createProfile();
		
		remindedMeDao.getSynchronizeableObject(new Task(), profile1.getId(), "12345", System.currentTimeMillis(), System.currentTimeMillis()-100000, System.currentTimeMillis()+100000);
	}
	
	
	@Test
	@Transactional
	public void testGetRole()
	{
		Assert.assertEquals(roleUser, remindedMeDao.getRole(roleUser.getId()));
	}
	
//	@Test
//	@Transactional
//	public void testGetCategory()
//	{
//		Profile profile1 = createProfile();
//		Task task1 = createTask(profile1);
//		
//		Category category = task1.getCategories().iterator().next();
//		Assert.assertEquals(category, remindedMeDao.getCategory(category.getId()));
//	}
//	
//	@Test
//	@Transactional
//	public void testGetCalendar()
//	{
//		Profile profile1 = createProfile();
//		Task task1 = createTask(profile1);
//		
//		Calendar calendar = task1.getCalendars().iterator().next();
//		Assert.assertEquals(calendar, remindedMeDao.getCalendar(calendar.getId()));
//	}
//	
	
	@Test
	@Transactional
	public void testGetSynchronizeableObject()
	{
		Profile profile1 = createProfile();
		Task task1 = RMTestModelBuilder.task(profile1);
		task1.setStartTime(new DateTime());
		task1.setEndTime((new DateTime()).plusMonths(1));
		task1.setPriority(PriorityEnum.MEDIUM.ordinal());
		task1 = createTask(task1);
		
		Task task2 = RMTestModelBuilder.task(profile1);
		task2.setProgress(ProgressEnum.RUNNING_LATE.ordinal());
		task2.setStartTime((new DateTime()).plusDays(1));
		task2.setEndTime( (new DateTime()).plusDays(2));
		task2.setPriority(PriorityEnum.HIGH.ordinal());
		task2 = createTask(task2);
		
		Task task3 = RMTestModelBuilder.task(profile1);
		task3.setProgress(ProgressEnum.DELETE.ordinal());
		task3.setStartTime((new DateTime()).plusDays(2));
		task3.setEndTime((new DateTime()).plusDays(3));
		task3.setPriority(PriorityEnum.HIGH.ordinal());
		task3 = createTask(task3);
		
		Task task4 = RMTestModelBuilder.task(profile1);
		task4.setProgress(ProgressEnum.ALMOST_THERE.ordinal());
		task4.setStartTime((new DateTime()).plusDays(3));
		task4.setEndTime((new DateTime()).plusDays(5));
		task4.setPriority(PriorityEnum.LOW.ordinal());
		task4 = createTask(task4);
		
		Task task5 = RMTestModelBuilder.task(profile1);
		task5.setStartTime((new DateTime()).plusDays(4));
		task5.setPriority(PriorityEnum.HIGH.ordinal());
		task5 = createTask(task5);
		
		Task task6 = RMTestModelBuilder.task(profile1);
		task6.setProgress(ProgressEnum.RUNNING_LATE.ordinal());
		task6.setPriority(PriorityEnum.HIGH.ordinal());
		task6 = createTask(task6);
		
		Task task7 = RMTestModelBuilder.task(profile1);
		task7.setProgress(ProgressEnum.DONE.ordinal());
		task7.setStartTime((new DateTime()).plusMinutes(5));
		task7.setEndTime((new DateTime()).plusHours(1));
		task7.setPriority(PriorityEnum.LOW.ordinal());
		task7 = createTask(task7);
		
		SynchronizeableObject[] synchronizeableObjects = SynchronizeableObject.values();
		boolean taskCheck=false, categoryCheck=true;
		
		for (int i=0;i<synchronizeableObjects.length;i++)
		{
			ISynchronizeable synchronizeable = synchronizeableObjects[i].getClassInstance();
			if (synchronizeable instanceof Task)
			{				
				List<Object> synchronizeables = remindedMeDao.getSynchronizeableObject(synchronizeable, profile1.getId());
				Assert.assertTrue(synchronizeables.contains(task1.getId()));
				Assert.assertTrue(synchronizeables.contains(task2.getId()));
				Assert.assertTrue(synchronizeables.contains(task3.getId()));
				Assert.assertTrue(synchronizeables.contains(task4.getId()));
				Assert.assertTrue(synchronizeables.contains(task5.getId()));
				Assert.assertTrue(synchronizeables.contains(task6.getId()));
				Assert.assertTrue(synchronizeables.contains(task7.getId()));
				taskCheck=true;
			}
//			else if (synchronizeable instanceof Category)
//			{
//				List<Object> synchronizeables = remindedMeDao.getSynchronizeableObject(synchronizeable, memeber1.getId());
//				Assert.assertTrue(synchronizeables.contains(categoryUserCreatedPrivate.getId()));
//				Assert.assertTrue(synchronizeables.contains(categoryUserCreatedPublic.getId()));
//				categoryCheck=true;
//			}
		}
		
		if (!taskCheck)
			Assert.fail("Did not check a synchronized class");
		
		
	}
	
	@Test
	@Transactional
	public void testGetSynchronizeableObjectByModified()
	{
		Profile profile1 = createProfile();
		Task task1 = RMTestModelBuilder.task(profile1);
		task1.setStartTime(new DateTime());
		task1.setEndTime((new DateTime()).plusMonths(1));
		task1.setPriority(PriorityEnum.MEDIUM.ordinal());
		task1 = createTask(task1);
		
		Task task2 = RMTestModelBuilder.task(profile1);
		task2.setProgress(ProgressEnum.RUNNING_LATE.ordinal());
		task2.setStartTime((new DateTime()).plusDays(1));
		task2.setEndTime( (new DateTime()).plusDays(2));
		task2.setPriority(PriorityEnum.HIGH.ordinal());
		task2 = createTask(task2);
		
		SynchronizeableObject[] synchronizeableObjects = SynchronizeableObject.values();
		long modifedTimeStamp = System.currentTimeMillis();
		
		try {Thread.sleep(50);} catch (Exception e){}
		task1.setModifiedDateZone(new DateTime());
		try {
			taskService.updateTaskTitle(task1, task1.getTaskCreator().getEmail());
		
//		dao.updateObject(task1);
		
		task2.setModifiedDateZone(new DateTime());
		taskService.updateTaskTitle(task2, task2.getTaskCreator().getEmail());
//		dao.updateObject(task2);
		
		try {Thread.sleep(50);} catch (Exception e){}
		} catch (ValidationException e1) {
			Assert.fail(e1.getMessage());
		}
		
//		categoryUserCreatedPublic.setModifiedDateZone(new DateTime().plusMillis(450532234));
//		dao.updateObject(categoryUserCreatedPublic);
//		
		boolean taskCheck=false, categoryCheck=false;
		
		for (int i=0;i<synchronizeableObjects.length;i++)
		{
			ISynchronizeable synchronizeable = synchronizeableObjects[i].getClassInstance();
			if (synchronizeable instanceof Task)
			{
				List<Object> synchronizeables = remindedMeDao.getSynchronizeableObject(synchronizeable, profile1.getId() ,null, modifedTimeStamp);
				Assert.assertTrue(synchronizeables.contains(task1.getId()));
				Assert.assertTrue(synchronizeables.contains(task2.getId()));
				taskCheck=true;
			}
//			else if (synchronizeable instanceof Category)
//			{
//				List<Object> synchronizeables = remindedMeDao.getSynchronizeableObject(synchronizeable, memeber1.getId(), null, modifedTimeStamp);
//				Assert.assertTrue(synchronizeables.contains(categoryUserCreatedPublic.getId()));
//				categoryCheck=true;
//			}
		}
		
		if (!taskCheck)
			Assert.fail("Did not check a synchronized class");
		
		
	}

	@Test
	@Transactional
	public void testGetSynchronizeableObjectByModifiedWithDeviceId()
	{
		Profile profile1 = createProfile();
		Task task1 = RMTestModelBuilder.task(profile1);
		task1.setStartTime(new DateTime());
		task1.setEndTime((new DateTime()).plusMonths(1));
		task1.setPriority(PriorityEnum.MEDIUM.ordinal());
		task1 = createTask(task1);
		
		Task task2 = RMTestModelBuilder.task(profile1);
		task2.setProgress(ProgressEnum.RUNNING_LATE.ordinal());
		task2.setStartTime((new DateTime()).plusDays(1));
		task2.setEndTime( (new DateTime()).plusDays(2));
		task2.setPriority(PriorityEnum.HIGH.ordinal());
		task2 = createTask(task2);
		
		String deviceId1 = "1";
		String deviceId2 = "2";
		
		SynchronizeableObject[] synchronizeableObjects = SynchronizeableObject.values();
		long modifedTimeStamp = System.currentTimeMillis();
		
		try {Thread.sleep(50);} catch (Exception e){}
		
		task1.setModifiedDateZone(new DateTime());
		task1.setLastModifiedDeviceId(deviceId1);
		try {
			taskService.updateTaskTitle(task1, task1.getTaskCreator().getEmail());
		
//		dao.updateObject(task1);
		
		try {Thread.sleep(50);} catch (Exception e){}
		
		task2.setModifiedDateZone(new DateTime());
		task2.setLastModifiedDeviceId(deviceId2);
		taskService.updateTaskTitle(task2, task2.getTaskCreator().getEmail());
//		dao.updateObject(task2);
		
		try {Thread.sleep(50);} catch (Exception e){}
		
		} catch (ValidationException e1) {
			Assert.fail(e1.getMessage());
		}
		
//		categoryUserCreatedPublic.setModifiedDateZone(new DateTime().plusMillis(450532234));
//		dao.updateObject(categoryUserCreatedPublic);
		
		boolean taskCheck=false, categoryCheck=false;
		
		for (int i=0;i<synchronizeableObjects.length;i++)
		{
			ISynchronizeable synchronizeable = synchronizeableObjects[i].getClassInstance();
			if (synchronizeable instanceof Task)
			{
				List<Object> synchronizeables = remindedMeDao.getSynchronizeableObject(synchronizeable, profile1.getId() ,deviceId2, modifedTimeStamp);
				Assert.assertTrue(synchronizeables.contains(task1.getId()));
				Assert.assertFalse(synchronizeables.contains(task2.getId()));
				taskCheck=true;
			}
//			else if (synchronizeable instanceof Category)
//			{
//				List<Object> synchronizeables = remindedMeDao.getSynchronizeableObject(synchronizeable, memeber1.getId(), deviceId2, modifedTimeStamp);
//				Assert.assertTrue(synchronizeables.contains(categoryUserCreatedPublic.getId()));
//				categoryCheck=true;
//			}
		}
		
		if (!taskCheck)
			Assert.fail("Did not check a synchronized class");
		
		
	}
	
	@Test
	@Transactional
	public void getSynchronizeableFilterObject()
	{

		Profile profile1 = createProfile();
		Task task1 = RMTestModelBuilder.task(profile1);
		task1.setStartTime(new DateTime());
		task1.setEndTime((new DateTime()).plusMonths(1));
		task1.setPriority(PriorityEnum.MEDIUM.ordinal());
		task1 = createTask(task1);
		
		Task task2 = RMTestModelBuilder.task(profile1);
		task2.setProgress(ProgressEnum.RUNNING_LATE.ordinal());
		task2.setStartTime((new DateTime()).plusDays(1));
		task2.setEndTime( (new DateTime()).plusDays(2));
		task2.setPriority(PriorityEnum.HIGH.ordinal());
		task2 = createTask(task2);
		
		Task task3 = RMTestModelBuilder.task(profile1);
		task3.setProgress(ProgressEnum.DELETE.ordinal());
		task3.setStartTime((new DateTime()).plusDays(2));
		task3.setEndTime((new DateTime()).plusDays(3));
		task3.setPriority(PriorityEnum.HIGH.ordinal());
		task3 = createTask(task3);
		
		Task task4 = RMTestModelBuilder.task(profile1);
		task4.setProgress(ProgressEnum.ALMOST_THERE.ordinal());
		task4.setStartTime((new DateTime()).plusDays(3));
		task4.setEndTime((new DateTime()).plusDays(5));
		task4.setPriority(PriorityEnum.LOW.ordinal());
		task4 = createTask(task4);
		
		Task task5 = RMTestModelBuilder.task(profile1);
		task5.setStartTime((new DateTime()).plusDays(4));
		task5.setPriority(PriorityEnum.HIGH.ordinal());
		task5 = createTask(task5);
		
		Task task6 = RMTestModelBuilder.task(profile1);
		task6.setProgress(ProgressEnum.RUNNING_LATE.ordinal());
		task6.setPriority(PriorityEnum.HIGH.ordinal());
		task6 = createTask(task6);
		
		Task task7 = RMTestModelBuilder.task(profile1);
		task7.setProgress(ProgressEnum.DONE.ordinal());
		task7.setStartTime((new DateTime()).plusMinutes(5));
		task7.setEndTime((new DateTime()).plusHours(1));
		task7.setPriority(PriorityEnum.LOW.ordinal());
		task7 = createTask(task7);
		
		SynchronizeableObject[] synchronizeableObjects = SynchronizeableObject.values();
		boolean taskCheck=false, categoryCheck=true;
	
		try {
			task2.setProgress(ProgressEnum.DELETE.ordinal());
			taskService.updateTaskProgress(task2, task2.getTaskCreator().getEmail());
//			dao.updateObject(task2);
			
			task4.setProgress(ProgressEnum.DELETE.ordinal());
			taskService.updateTaskProgress(task4, task4.getTaskCreator().getEmail());
//			dao.updateObject(task6);
			
			task6.setProgress(ProgressEnum.DELETE.ordinal());
			taskService.updateTaskProgress(task6, task6.getTaskCreator().getEmail());
//			dao.updateObject(task6);
		} catch (ValidationException e1) {
			Assert.fail(e1.getMessage());
		}
		
//			dao.flush();
		
		for (int i=0;i<synchronizeableObjects.length;i++)
		{
			ISynchronizeable synchronizeable = synchronizeableObjects[i].getClassInstance();
			if (synchronizeable instanceof Task)
			{				
				List<Object> synchronizeables = remindedMeDao.getSynchronizeableFilterObject((ISynchronizeableFiltered)synchronizeable, profile1.getId());
				Assert.assertTrue(synchronizeables.contains(task1.getId()));
				Assert.assertTrue(synchronizeables.contains(task5.getId()));
				Assert.assertTrue(synchronizeables.contains(task7.getId()));
				taskCheck=true;
			}
//			else if (synchronizeable instanceof Category)
//			{
//				List<Object> synchronizeables = remindedMeDao.getSynchronizeableObject(synchronizeable, memeber1.getId());
//				Assert.assertTrue(synchronizeables.contains(categoryUserCreatedPrivate.getId()));
//				Assert.assertTrue(synchronizeables.contains(categoryUserCreatedPublic.getId()));
//				categoryCheck=true;
//			}
		}
		
		if (!taskCheck)
			Assert.fail("Did not check a synchronized class");
	}
	
	
	@Test
	@Transactional
	public void getSynchronizeableFilterObjectWithModifiedTimeAndDeviceId()
	{
		Profile profile1 = createProfile();
		Task task1 = RMTestModelBuilder.task(profile1);
		task1.setStartTime(new DateTime());
		task1.setEndTime((new DateTime()).plusMonths(1));
		task1.setPriority(PriorityEnum.MEDIUM.ordinal());
		task1 = createTask(task1);
		
		String deviceId1 = "1";
		String deviceId2 = "2";
		SynchronizeableObject[] synchronizeableObjects = SynchronizeableObject.values();
		long modifedTimeStamp = System.currentTimeMillis();
		
		try {Thread.sleep(50);} catch (Exception e){}
		
		//test 1, sync some deleted tasks with different ids
		task1.setCreatedDateZone(new DateTime().minusDays(20));
		task1.setModifiedDateZone(new DateTime());
		task1.setProgress(ProgressEnum.DELETE.ordinal());
		task1.setLastModifiedDeviceId(deviceId2);
		
		try {Thread.sleep(50);} catch (Exception e){}
		try {
		taskService.updateTaskProgress(task1, task1.getTaskCreator().getEmail());
//		dao.updateObject(task1);
//		dao.flush();
		} catch (ValidationException e1) {
			Assert.fail(e1.getMessage());
		}
		
		boolean taskCheck=false;
		boolean categoryCheck=false;
		for (int i=0;i<synchronizeableObjects.length;i++)
		{
			ISynchronizeable synchronizeable = synchronizeableObjects[i].getClassInstance();
			if (synchronizeable instanceof Task)
			{				
				List<Object> synchronizeables = remindedMeDao.getSynchronizeableFilterObject((ISynchronizeableFiltered)synchronizeable, profile1.getId(), deviceId1, modifedTimeStamp);
				Assert.assertTrue(synchronizeables.contains(task1.getId()));
				taskCheck=true;
			}
//			else if (synchronizeable instanceof Category)
//			{
//				List<Object> synchronizeables = remindedMeDao.getSynchronizeableObject(synchronizeable, memeber1.getId());
//				Assert.assertTrue(synchronizeables.contains(categoryUserCreatedPrivate.getId()));
//				Assert.assertTrue(synchronizeables.contains(categoryUserCreatedPublic.getId()));
//				categoryCheck=true;
//			}
		}
		
		if (!taskCheck)
			Assert.fail("Did not check a synchronized class");
		
		//test 2, sync some deleted tasks with same ids
		task1.setCreatedDateZone(new DateTime().minusDays(20));
		task1.setModifiedDateZone(new DateTime().plusDays(5));
		task1.setProgress(ProgressEnum.DELETE.ordinal());
		task1.setLastModifiedDeviceId(deviceId2);
		try {
		taskService.updateTaskProgress(task1, task1.getTaskCreator().getEmail());
		} catch (ValidationException e1) {
			Assert.fail(e1.getMessage());
		}
//		dao.updateObject(task1);
//		dao.flush();
		
		taskCheck=false;
		categoryCheck=false;
		for (int i=0;i<synchronizeableObjects.length;i++)
		{
			ISynchronizeable synchronizeable = synchronizeableObjects[i].getClassInstance();
			if (synchronizeable instanceof Task)
			{				
				List<Object> synchronizeables = remindedMeDao.getSynchronizeableFilterObject((ISynchronizeableFiltered)synchronizeable, profile1.getId(), deviceId2, System.currentTimeMillis());
				Assert.assertFalse(synchronizeables.contains(task1.getId()));
				taskCheck=true;
			}
//			else if (synchronizeable instanceof Category)
//			{
//				List<Object> synchronizeables = remindedMeDao.getSynchronizeableObject(synchronizeable, memeber1.getId());
//				Assert.assertTrue(synchronizeables.contains(categoryUserCreatedPrivate.getId()));
//				Assert.assertTrue(synchronizeables.contains(categoryUserCreatedPublic.getId()));
//				categoryCheck=true;
//			}
		}
		
		if (!taskCheck)
			Assert.fail("Did not check a synchronized class");
		
		
	}
	
	@Test
	@Transactional
	public void getSynchronizeableFilterObjectWithModifiedTime()
	{
		Profile profile1 = createProfile();
		Task task1 = RMTestModelBuilder.task(profile1);
		task1.setStartTime(new DateTime());
		task1.setEndTime((new DateTime()).plusMonths(1));
		task1.setPriority(PriorityEnum.MEDIUM.ordinal());
		task1 = createTask(task1);
		
		Task task2 = RMTestModelBuilder.task(profile1);
		task2.setProgress(ProgressEnum.RUNNING_LATE.ordinal());
		task2.setStartTime((new DateTime()).plusDays(1));
		task2.setEndTime( (new DateTime()).plusDays(2));
		task2.setPriority(PriorityEnum.HIGH.ordinal());
		task2 = createTask(task2);
		
		Task task3 = RMTestModelBuilder.task(profile1);
		task3.setProgress(ProgressEnum.DELETE.ordinal());
		task3.setStartTime((new DateTime()).plusDays(2));
		task3.setEndTime((new DateTime()).plusDays(3));
		task3.setPriority(PriorityEnum.HIGH.ordinal());
		task3 = createTask(task3);
		
		Task task4 = RMTestModelBuilder.task(profile1);
		task4.setProgress(ProgressEnum.ALMOST_THERE.ordinal());
		task4.setStartTime((new DateTime()).plusDays(3));
		task4.setEndTime((new DateTime()).plusDays(5));
		task4.setPriority(PriorityEnum.LOW.ordinal());
		task4 = createTask(task4);
		
		Task task5 = RMTestModelBuilder.task(profile1);
		task5.setStartTime((new DateTime()).plusDays(4));
		task5.setPriority(PriorityEnum.HIGH.ordinal());
		task5 = createTask(task5);
		
		Task task6 = RMTestModelBuilder.task(profile1);
		task6.setProgress(ProgressEnum.RUNNING_LATE.ordinal());
		task6.setPriority(PriorityEnum.HIGH.ordinal());
		task6 = createTask(task6);
		
		Task task7 = RMTestModelBuilder.task(profile1);
		task7.setProgress(ProgressEnum.DONE.ordinal());
		task7.setStartTime((new DateTime()).plusMinutes(5));
		task7.setEndTime((new DateTime()).plusHours(1));
		task7.setPriority(PriorityEnum.LOW.ordinal());
		task7 = createTask(task7);
		
		//test 1 make sure that it doesn't return task3 as its already deleted
		SynchronizeableObject[] synchronizeableObjects = SynchronizeableObject.values();
		boolean taskCheck=false, categoryCheck=false;
			
		for (int i=0;i<synchronizeableObjects.length;i++)
		{
			ISynchronizeable synchronizeable = synchronizeableObjects[i].getClassInstance();
			if (synchronizeable instanceof Task)
			{				
				List<Object> synchronizeables = remindedMeDao.getSynchronizeableFilterObject((ISynchronizeableFiltered)synchronizeable, profile1.getId(), null, System.currentTimeMillis()-100000);
				Assert.assertTrue(synchronizeables.contains(task1.getId()));
				Assert.assertTrue(synchronizeables.contains(task2.getId()));
				Assert.assertTrue(synchronizeables.contains(task4.getId()));
				Assert.assertTrue(synchronizeables.contains(task5.getId()));
				Assert.assertTrue(synchronizeables.contains(task6.getId()));
				Assert.assertTrue(synchronizeables.contains(task7.getId()));
				taskCheck=true;
			}
//			else if (synchronizeable instanceof Category)
//			{
//				List<Object> synchronizeables = remindedMeDao.getSynchronizeableObject(synchronizeable, memeber1.getId());
//				Assert.assertTrue(synchronizeables.contains(categoryUserCreatedPrivate.getId()));
//				Assert.assertTrue(synchronizeables.contains(categoryUserCreatedPublic.getId()));
//				categoryCheck=true;
//			}
		}
		
		long modifiedTime = System.currentTimeMillis();
		
		if (!taskCheck)
			Assert.fail("Did not check a synchronized class");
		
		try {Thread.sleep(50);} catch (Exception e){}
		
		//test 2, sync some deleted tasks
		task1.setCreatedDateZone(new DateTime().minusDays(20));
		task1.setModifiedDateZone(new DateTime());
		task1.setProgress(ProgressEnum.DELETE.ordinal());
//		dao.updateObject(task1);
		try
		{
		taskService.updateTaskProgress(task1, task1.getTaskCreator().getEmail());
//		dao.flush();
		
		try {Thread.sleep(50);} catch (Exception e){}
		
		taskCheck=false;
		categoryCheck=false;
		for (int i=0;i<synchronizeableObjects.length;i++)
		{
			ISynchronizeable synchronizeable = synchronizeableObjects[i].getClassInstance();
			if (synchronizeable instanceof Task)
			{				
				List<Object> synchronizeables = remindedMeDao.getSynchronizeableFilterObject((ISynchronizeableFiltered)synchronizeable, profile1.getId(), null, modifiedTime);
				Assert.assertTrue(synchronizeables.contains(task1.getId()));
				taskCheck=true;
			}
//			else if (synchronizeable instanceof Category)
//			{
//				List<Object> synchronizeables = remindedMeDao.getSynchronizeableObject(synchronizeable, memeber1.getId());
//				Assert.assertTrue(synchronizeables.contains(categoryUserCreatedPrivate.getId()));
//				Assert.assertTrue(synchronizeables.contains(categoryUserCreatedPublic.getId()));
//				categoryCheck=true;
//			}
		}
		
		if (!taskCheck)
			Assert.fail("Did not check a synchronized class");
		
		//test 3 going to create and delete before sync
		task1.setCreatedDateZone(new DateTime().minusDays(20));
		task1.setModifiedDateZone(new DateTime().minusDays(25));
		task1.setProgress(ProgressEnum.DELETE.ordinal());
//		dao.updateObject(task1);
		taskService.updateTaskProgress(task1, task1.getTaskCreator().getEmail());
//		dao.flush();
		
		
		taskCheck=false;
		categoryCheck=false;
		for (int i=0;i<synchronizeableObjects.length;i++)
		{
			ISynchronizeable synchronizeable = synchronizeableObjects[i].getClassInstance();
			if (synchronizeable instanceof Task)
			{				
				List<Object> synchronizeables = remindedMeDao.getSynchronizeableFilterObject((ISynchronizeableFiltered)synchronizeable, profile1.getId(), null, System.currentTimeMillis()-60000);
				Assert.assertTrue(synchronizeables.contains(task1.getId()));
				Assert.assertTrue(synchronizeables.contains(task2.getId()));
				Assert.assertTrue(synchronizeables.contains(task4.getId()));
				Assert.assertTrue(synchronizeables.contains(task5.getId()));
				Assert.assertTrue(synchronizeables.contains(task6.getId()));
				Assert.assertTrue(synchronizeables.contains(task7.getId()));
				taskCheck=true;
			}
//			else if (synchronizeable instanceof Category)
//			{
//				List<Object> synchronizeables = remindedMeDao.getSynchronizeableObject(synchronizeable, memeber1.getId());
//				Assert.assertTrue(synchronizeables.contains(categoryUserCreatedPrivate.getId()));
//				Assert.assertTrue(synchronizeables.contains(categoryUserCreatedPublic.getId()));
//				categoryCheck=true;
//			}
		}
		
		if (!taskCheck)
			Assert.fail("Did not check a synchronized class");
		} catch (ValidationException e1) {
			Assert.fail(e1.getMessage());
		}
	}



}
