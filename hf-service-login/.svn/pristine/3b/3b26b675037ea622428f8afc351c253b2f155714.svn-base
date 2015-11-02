package com.homefellas.user;

import java.util.Set;

import com.homefellas.exception.ValidationException;

public interface IUserValidator {
	
	public void validateMember(Profile member) throws ValidationException;
	public void validateRoles(Set<Role> roles) throws ValidationException;
	public void validateExtendedProfile(ExtendedProfile extendedProfile) throws ValidationException;
	public void validatePasswordReset(Member member) throws ValidationException;
	public void validateProfileUpdate(Profile profile) throws ValidationException;
}
