package com.homefellas.service.core;

import java.util.List;

import com.homefellas.exception.IValidationCode;
import com.homefellas.exception.ValidationException;
import com.homefellas.model.core.AbstractModel;

public abstract class AbstractValidator {


	public static boolean isNullOrBlank(String string)
	{
		if (string == null || "".equals(string))
			return true;
		else
			return false;
	}	
	
	protected void throwException(List<IValidationCode> codes) throws ValidationException
	{
		if (!codes.isEmpty())
			throw new ValidationException(codes);
	}
	
	protected void throwException(List<IValidationCode> codes, AbstractModel model) throws ValidationException
	{
		if (!codes.isEmpty())
			throw new ValidationException(codes, model);
	}
	
	protected ValidationException getValidationException(IValidationCode validationCodeEnum)
	{
		return new ValidationException(validationCodeEnum);
	}
	
	
	
//	protected <T> List<IValidationCode> validateValidationAnnonations(T object) 
//	{
//		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//		Validator validator = factory.getValidator();
//		Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);
//		
//		if (!constraintViolations.isEmpty())
//		{
//			List<IValidationCode> codes = new ArrayList<IValidationCode>(constraintViolations.size());
//		
//			for (final ConstraintViolation<T> constraintViolation:constraintViolations)
//			{
//				codes.add(new IValidationCode() {
//					
//					@Override
//					public int ordinal()
//					{
//						return -1;
//					}
//					
//					@Override
//					public String toString()
//					{
//						return constraintViolation.getMessage();
//					}
//				});
//			}
//			return (codes);
//		}
//		else
//		{
//			return new ArrayList<IValidationCode>();
//		}
//	}
	

}
