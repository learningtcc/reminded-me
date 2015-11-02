package com.homefellas.rm.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.exception.ValidationException;
import com.homefellas.rm.share.ShareService;
import com.homefellas.user.Profile;
import com.homefellas.user.UserService;

public class RMUserService extends UserService
{

	@Autowired
	private ShareService shareService;

	

	@Override
	@Transactional
	protected void postGuestUpgrade(Profile profile)
	{
		createShareForProfile(profile);
	}

	@Override
	protected void postMemberRegister(Profile profile)
	{
		createShareForProfile(profile);
	}
	
	private void createShareForProfile(Profile profile)
	{
		String taskId = profile.getMember().getSharedTaskId();
		if (taskId!=null&&!"".equals(taskId))
		{
			try
			{
				shareService.createShareOnRegister(profile);
			}
			catch (ValidationException exception)
			{
				logger.error(exception.getMessage());
			}
		}
	}
	
	
	
}
