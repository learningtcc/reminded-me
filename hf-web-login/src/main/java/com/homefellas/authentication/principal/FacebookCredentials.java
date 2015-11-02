package com.homefellas.authentication.principal;

import org.jasig.cas.authentication.principal.Credentials;

public class FacebookCredentials implements Credentials
{

	private String id;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}
	
	
}
