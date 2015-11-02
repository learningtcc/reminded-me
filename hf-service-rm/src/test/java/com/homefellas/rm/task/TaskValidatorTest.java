package com.homefellas.rm.task;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Test;

import com.homefellas.exception.ValidationException;
import com.homefellas.rm.RMTestModelBuilder;
import com.homefellas.rm.RMValidator;
import com.homefellas.rm.ValidationCodeEnum;
import com.homefellas.rm.calendar.Calendar;
import com.homefellas.rm.task.AbstractTask.PriorityEnum;
import com.homefellas.user.Member;

public class TaskValidatorTest {

	@Test
	public void testValidateCalendar() {
		RMValidator taskValidator = new RMValidator();		
		Calendar calendar = RMTestModelBuilder.buildCalendar(false, "test calendar", null);
		
		try
		{
			taskValidator.validateCalendar(calendar);
			fail();
		}
		catch (ValidationException exception)
		{
			Assert.assertTrue(exception.getValidationErrors().contains(ValidationCodeEnum.MEMBER_ID_IS_NULL));
		}
		
		Member member = new Member();
		member.setId(null);
		calendar = RMTestModelBuilder.buildCalendar(false, "test calendar", member);
		
		try
		{
			taskValidator.validateCalendar(calendar);
			fail();
		}
		catch (ValidationException exception)
		{
			Assert.assertTrue(exception.getValidationErrors().contains(ValidationCodeEnum.MEMBER_ID_IS_NULL));
		}
		
		member = new Member();
		member.generateGUIDKey();
		calendar = RMTestModelBuilder.buildCalendar(false, "test calendar", member);
		
		try
		{
			taskValidator.validateCalendar(calendar);
			Assert.assertTrue(true);
		}
		catch (ValidationException exception)
		{
			fail();
		}
	}
	
	

	@Test
	public void testValidateTask() {
		RMValidator taskValidator = new RMValidator();
//		Task task = RMTestModelBuilder.buildSampleTask(true, null, null, true, null, null, null, PriorityEnum.MEDIUM, null, null);
		Task task = RMTestModelBuilder.task(null);
		task.setTitle(null);
		try
		{
			taskValidator.validateTask(task);
			fail();
		}
		catch (ValidationException exception)
		{
			Assert.assertTrue(exception.getValidationErrors().contains(ValidationCodeEnum.TASK_TITLE_IS_NULL));
		}
		
//		task = RMTestModelBuilder.buildSampleTask(true, null, null, true, null, null, null, PriorityEnum.MEDIUM, (new DateTime()).plusMillis(1000000), new DateTime());
		task = RMTestModelBuilder.task(null);
		task.setStartTime((new DateTime()).plusMillis(1000000));
		task.setEndTime(new DateTime());
		try
		{
			taskValidator.validateTask(task);
			fail();
		}
		catch (ValidationException exception)
		{
			Assert.assertTrue(exception.getValidationErrors().contains(ValidationCodeEnum.END_TIME_BEFORE_START_TIME));
		}
	}

	

}
