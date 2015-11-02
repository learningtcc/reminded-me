package com.homefellas.user;

import static com.homefellas.model.core.TestModelBuilder.convertObjectToJSON;
import static com.homefellas.user.UserTestModelBuilder.buildBasicMember;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.homefellas.exception.ValidationException;


public class UserValidatorTest
{

	private UserValidator userValidator = new UserValidator();
	
	@Before
	public void init()
	{
	}
	
	@Test
	public void testValidateMember()
	{
		//test sunny day
		Profile member = buildBasicMember(true, "user@homefellas.com",null);
		
		
		try
		{
			userValidator.validateMember(member);
			assertTrue(true);
		}
		catch (ValidationException exception)
		{
			fail(exception.getMessage());
		}
		
		
		//test sunnday non-sso
		member = buildBasicMember(false,"user@homefellas.com",null);
		member.getMember().setTicket(null);
		try
		{
			userValidator.validateMember(member);
			assertTrue(true);
		
		}
		catch (ValidationException exception)
		{
			fail();
		}
		
		
		//test null authenication
		member = buildBasicMember(false,"user@homefellas.com",null);
		member.setMember(null);
		try
		{
			userValidator.validateMember(member);
			fail();
		}
		catch (ValidationException exception)
		{
			assertTrue(exception.getValidationErrors().contains(UserValidationCodeEnum.EMAIL_NULL));
			assertTrue(exception.getValidationErrors().contains(UserValidationCodeEnum.PASSWORD_NULL));
			
		}
		
		
		//test blank email
		member = buildBasicMember(false,"user@homefellas.com",null);
		member.getMember().setEmail("");
		try
		{
			userValidator.validateMember(member);
			fail();
		}
		catch (ValidationException exception)
		{
			assertTrue(exception.getValidationErrors().contains(UserValidationCodeEnum.EMAIL_NULL));
			
		}
		
		//test null email
		member = buildBasicMember(false,"user@homefellas.com",null);
		member.getMember().setEmail(null);
		try
		{
			userValidator.validateMember(member);
			fail();
		}
		catch (ValidationException exception)
		{
			assertTrue(exception.getValidationErrors().contains(UserValidationCodeEnum.EMAIL_NULL));
			
		}
		
		//test blank password
		member = buildBasicMember(false,"user@homefellas.com",null);
		member.getMember().setPassword("");
		try
		{
			userValidator.validateMember(member);
			fail();
		}
		catch (ValidationException exception)
		{
			assertTrue(exception.getValidationErrors().contains(UserValidationCodeEnum.PASSWORD_NULL));
			
		}
		
		//test null password
		member = buildBasicMember(false,"user@homefellas.com",null);
		member.getMember().setPassword(null);
		try
		{
			userValidator.validateMember(member);
			fail();
		}
		catch (ValidationException exception)
		{
			assertTrue(exception.getValidationErrors().contains(UserValidationCodeEnum.PASSWORD_NULL));
			
		}
		
//		//test dub id
//		member = buildBasicMember(true,"user@homefellas.com");
//		
//		try
//		{
//			userValidator.validateMember(member, true);
//			fail();
//			
//		}
//		catch (ValidationException exception)
//		{
//			assertTrue(exception.getValidationErrors().contains(UserValidationCodeEnum.MEMBER_ID_TAKEN));
//		}
	}
}
