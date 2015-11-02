package com.homefellas.register;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.CredentialsToPrincipalResolver;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.SimplePrincipal;

public final class RegisterCredentialsToPrincipalResolver implements CredentialsToPrincipalResolver {
	protected final Log log = LogFactory.getLog(getClass());
	public Principal resolvePrincipal(Credentials credentials) {
		final RegisterCredentials registerCredentials = (RegisterCredentials)credentials;
		
		log.info(registerCredentials.getTokenId());
		return new SimplePrincipal(registerCredentials.getUsername());
	}

	public boolean supports(Credentials credentials) {
		return credentials != null
        && RegisterCredentials.class.isAssignableFrom(credentials
            .getClass());
	}

	
}
