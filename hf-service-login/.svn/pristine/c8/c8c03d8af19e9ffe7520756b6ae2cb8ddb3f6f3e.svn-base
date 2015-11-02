package com.homefellas.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.homefellas.exception.IValidationCode;
import com.homefellas.exception.ValidationException;
import com.homefellas.service.core.AbstractValidator;

public class UserValidator extends AbstractValidator implements IUserValidator {




	@Override
	public void validateRoles(Set<Role> roles) throws ValidationException {
		if (roles.isEmpty())
			throw new ValidationException(UserValidationCodeEnum.ROLE_HF_USER_NOT_DEFINED_IN_DB);
	}

	
	public void validatePasswordReset(Member member) throws ValidationException
	{
		List<IValidationCode> codes = new ArrayList<IValidationCode>();
		
		if (isNullOrBlank(member.getTicket()))
			codes.add(UserValidationCodeEnum.NO_TICKET_SET);
				
		if (isNullOrBlank(member.getEmail()))
			codes.add(UserValidationCodeEnum.EMAIL_NULL);
		
		if (isNullOrBlank(member.getPassword()))
			codes.add(UserValidationCodeEnum.PASSWORD_NULL);
		
		throwException(codes);
	}

	public void validateMember(Profile member) throws ValidationException
	{
		List<IValidationCode> errors = new ArrayList<IValidationCode>();
		if (member.getMember()==null)
		{
			errors.add(UserValidationCodeEnum.EMAIL_NULL);
			errors.add(UserValidationCodeEnum.PASSWORD_NULL);
			throw new ValidationException(errors);
		}
		
		if (isNullOrBlank(member.getMember().getEmail()))
		{
			errors.add(UserValidationCodeEnum.EMAIL_NULL);
		}
		
		if (isNullOrBlank(member.getMember().getPassword()))
		{
			errors.add(UserValidationCodeEnum.PASSWORD_NULL);
		}
		
		//check to make sure there is a ticket passed
//		if (isSingleSignOn && member.getMember().getTicket()==null)
//			errors.add(UserValidationCodeEnum.NO_TICKET_SET);
		
		if (!errors.isEmpty())
			throw new ValidationException(errors);
		
	}
	
	public void validateExtendedProfile(ExtendedProfile extendedProfile) throws ValidationException
	{
		if (extendedProfile.getProfile()==null || extendedProfile.getProfile().getId()==null||extendedProfile.getProfile().getId().equals(""))
			throw new ValidationException(UserValidationCodeEnum.EXTENDED_PROFILE_HAS_NULL_PROFILE_ID);
	}
	
	public void validateProfileUpdate(Profile profile) throws ValidationException
	{
		List<IValidationCode> errors = new ArrayList<IValidationCode>();
		
		if (!profile.isPrimaryKeySet())
			throw new ValidationException(UserValidationCodeEnum.CANNOT_UPDATE_WITHOUT_PK);
		
		throwException(errors);
	}
}
