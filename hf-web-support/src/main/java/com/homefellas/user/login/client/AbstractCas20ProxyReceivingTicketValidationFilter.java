package com.homefellas.user.login.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public abstract class AbstractCas20ProxyReceivingTicketValidationFilter extends
		Cas20ProxyReceivingTicketValidationFilter {

	protected UserDetailsService loginService;
	
	@Override
	protected void onSuccessfulValidation(HttpServletRequest request, HttpServletResponse response, Assertion assertion)
	{
		super.onSuccessfulValidation(request, response, assertion);
		
		Object sessionObject = getSessionObject(request, response);
		if (sessionObject == null)
		{
			//this is the first time we are hitting this, so lets store the object into session
			UserDetails userTO = loginService.loadUserByUsername(assertion.getPrincipal().getName());
			IUserSession userSession = createSessionObject();
			userSession.setLoggedInUser(userTO);
			loadSessionObject(request, response, userSession);
		}
	}
	
	protected abstract IUserSession getSessionObject(HttpServletRequest request, HttpServletResponse response);
	protected abstract void loadSessionObject(HttpServletRequest request, HttpServletResponse response, IUserSession userSession);
	protected abstract IUserSession createSessionObject();
	
	public void setLoginService(UserDetailsService loginService) {
		this.loginService = loginService;
	}
	
	
	
}
