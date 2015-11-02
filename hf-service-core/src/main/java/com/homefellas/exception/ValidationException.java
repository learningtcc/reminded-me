package com.homefellas.exception;

import java.util.ArrayList;
import java.util.List;

import com.homefellas.model.core.AbstractModel;


public class ValidationException extends Exception  {

	private List<IValidationCode> validationErrors;
	private AbstractModel model;
	private String message;
	
	public ValidationException(IValidationCode code, String message)
	{
		validationErrors = new ArrayList<IValidationCode>(1);
		validationErrors.add(code);
		
		this.message = message;
		
	}
	public ValidationException(IValidationCode code, AbstractModel model)
	{
		validationErrors = new ArrayList<IValidationCode>(1);
		validationErrors.add(code);
		
		this.model = model;
	}
	
	public ValidationException(IValidationCode code)
	{
		validationErrors = new ArrayList<IValidationCode>(1);
		validationErrors.add(code);
	}
	public ValidationException(List<IValidationCode> validationErrors)
	{
		this.validationErrors = validationErrors;
	}
	
	public ValidationException(List<IValidationCode> validationErrors, AbstractModel model)
	{
		this.validationErrors = validationErrors;
		this.model = model;
	}

	public List<IValidationCode> getValidationErrors() {
		return validationErrors;
	}
	@Override
	public String getMessage() {
		StringBuffer buffer = new StringBuffer();
		for (IValidationCode code:validationErrors)
		{
			buffer.append("ValidationErrorCode:");
			buffer.append(code.ordinal());
			buffer.append(" - ");
			buffer.append(code.toString());
			if (message!=null)
			{
				buffer.append(" ");
				buffer.append(message);
			}
			buffer.append("\n");
			
			
		}
		
		return buffer.toString();
	}

	public AbstractModel getModel()
	{
		return model;
	}

	public void setModel(AbstractModel model)
	{
		this.model = model;
	}
	
	
}
