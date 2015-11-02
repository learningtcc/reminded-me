package com.homefellas.ws.core;

import java.net.URI;

import com.homefellas.ws.core.AbstractTestWebService.HTTPMethod;

public class WebServiceCall
{

	private HTTPMethod httpMethod;
	private Class<? extends AbstractWebService> service;
	private String methodName;
	private String jsonInput;
	private URI action;
	
	public HTTPMethod getHttpMethod()
	{
		return httpMethod;
	}
	public void setHttpMethod(HTTPMethod httpMethod)
	{
		this.httpMethod = httpMethod;
	}
	public Class<? extends AbstractWebService> getService()
	{
		return service;
	}
	public void setService(Class<? extends AbstractWebService> service)
	{
		this.service = service;
	}
	public String getMethodName()
	{
		return methodName;
	}
	public void setMethodName(String methodName)
	{
		this.methodName = methodName;
	}
	public String getJsonInput()
	{
		return jsonInput;
	}
	public void setJsonInput(String jsonInput)
	{
		this.jsonInput = jsonInput;
	}
	public URI getAction()
	{
		return action;
	}
	public void setAction(URI action)
	{
		this.action = action;
	}
	
	public String getServiceName()
	{
		return service.getName();
	}
}
