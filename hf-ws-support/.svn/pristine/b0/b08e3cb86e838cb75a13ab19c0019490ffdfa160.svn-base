package com.homefellas.ws.core;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.homefellas.exception.IValidationCode;

public class ValidationError
{

	private long occurance;
	private String error;
	private int errorCode;
	
	public ValidationError()
	{
		this.occurance = System.currentTimeMillis();
	}
	
	public ValidationError(IValidationCode code)
	{
		this.occurance = System.currentTimeMillis();
		this.error = code.toString();
		this.errorCode = code.ordinal();
		
	}
	
	public String getError()
	{
		return error;
	}
	public void setError(String error)
	{
		this.error = error;
	}
	public int getErrorCode()
	{
		return errorCode;
	}
	public void setErrorCode(int errorCode)
	{
		this.errorCode = errorCode;
	}

	public long getOccurance()
	{
		return occurance;
	}

	public void setOccurance(long occurance)
	{
		this.occurance = occurance;
	}
	
	

	public String toString()
	{
//		return "errorCode:"+errorCode+" error:"+error;
		try
		{
			return new ObjectMapper().writeValueAsString(this);
		}
		catch (Exception e)
		{
			return "errorCode:"+errorCode+",error:"+error+",occurance:"+occurance;
		}
	}
}
