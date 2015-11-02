package com.homefellas.authentication.principal;

import org.jasig.cas.authentication.principal.AbstractPersonDirectoryCredentialsToPrincipalResolver;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;

public class FacebookCredentialsToPrincipalResolver extends AbstractPersonDirectoryCredentialsToPrincipalResolver
{

	@Override
	public boolean supports(Credentials credentials)
	{
		return credentials != null
        && FacebookCredentials.class.isAssignableFrom(credentials.getClass());
	}

	@Override
	protected String extractPrincipalId(Credentials credentials)
	{
		 final FacebookCredentials facebookCredentials = (FacebookCredentials) credentials;
	        return facebookCredentials.getId();
	}

}
