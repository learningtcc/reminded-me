package com.homefellas.rm;

import com.homefellas.exception.ValidationException;
import com.homefellas.model.core.AbstractModel;
import com.homefellas.user.Member;

public interface IRMValidator
{
	public void validateSynchronizationUpdate(ISynchronizeable objectToUpdate, ISynchronizeable objectFromDataSource) throws ValidationException;
	public void validationMemberIsSet(Member member) throws ValidationException;
	public void validatePrimaryKeyIsSet(AbstractModel model) throws ValidationException;
}
