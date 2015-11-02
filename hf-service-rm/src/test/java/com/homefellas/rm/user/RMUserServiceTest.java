package com.homefellas.rm.user;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import com.homefellas.exception.DatabaseNotInitializedException;
import com.homefellas.exception.ValidationException;
import com.homefellas.rm.AbstractRMTestDao;
import com.homefellas.rm.task.Task;
import com.homefellas.user.IUserDao;
import com.homefellas.user.Profile;
import com.homefellas.user.UserService;


public class RMUserServiceTest extends AbstractRMTestDao
{

	@PostConstruct
	protected void disableTGT()
	{
		userService.setGenerateTGT(false);
	}
	
	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="userDao")
	private IUserDao userDao;
	
	@Test
	public void testUpgradeFromGuest()
	{
		Profile profile1 = createProfile();
		Profile profile3Guest1 = createGuest();
		Task task1 = createTask(profile1);
		
		String newPassword = "newpasswordthatrocks";
		profile3Guest1.getMember().setPassword(newPassword);
		
		try
		{
			profile3Guest1.getMember().setSharedTaskId(task1.getId());
			userService.registerBasicMember(profile3Guest1);
			
//			Profile classUnderTest = dao.loadByPrimaryKey(Profile.class, profile3Guest1.getId());
			Profile classUnderTest = userDao.getProfileByID(profile3Guest1.getId());
			Assert.assertTrue(passwordEncoder.isPasswordValid(classUnderTest.getMember().getCreditials(), newPassword, null));
			Assert.assertFalse(classUnderTest.getMember().isGuest());
			
//			shareCounter++;
//			notificationCounter+=2;
//			assertTableRowChecks();
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
		}
		catch (DatabaseNotInitializedException e)
		{
			Assert.fail(e.getMessage());
		}

	}
}
