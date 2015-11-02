package com.homefellas.rm.share;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import com.homefellas.exception.ValidationException;
import com.homefellas.rm.RMValidator;
import com.homefellas.rm.ValidationCodeEnum;

public class ShareValidatorTest
{

	@Test
	public void validateInvite()
	{
		//check for null
		RMValidator shareValidator = new RMValidator();
		try
		{
			shareValidator.validateInvite(null);
			Assert.fail();
		}
		catch (ValidationException exception)
		{
			Assert.assertTrue(exception.getValidationErrors().contains(ValidationCodeEnum.INVITE_IS_NULL));
		}
		
		Invite invite = new Invite();
		try
		{
			shareValidator.validateInvite(invite);
			Assert.fail();
		}
		catch (ValidationException exception)
		{
			Assert.assertTrue(exception.getValidationErrors().contains(ValidationCodeEnum.SUBJECT_IS_NULL));
			Assert.assertTrue(exception.getValidationErrors().contains(ValidationCodeEnum.MESSAGE_IS_NULL));
			Assert.assertTrue(exception.getValidationErrors().contains(ValidationCodeEnum.SHARE_ID_IS_NULL));
			Assert.assertTrue(exception.getValidationErrors().contains(ValidationCodeEnum.LINK_IS_NULL));
			Assert.assertTrue(exception.getValidationErrors().contains(ValidationCodeEnum.ONE_EMAIL_ADDRESS_IS_REQUIRED_TO_SHARE));
		}
		
	}

}
