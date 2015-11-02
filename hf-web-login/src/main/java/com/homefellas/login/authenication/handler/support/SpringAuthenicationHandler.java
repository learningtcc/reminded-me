package com.homefellas.login.authenication.handler.support;

import java.util.Collection;

import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.springframework.security.authentication.dao.SaltSource;
import org.jasypt.spring.security3.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.homefellas.user.IUserService;

public class SpringAuthenicationHandler extends AbstractUsernamePasswordAuthenticationHandler
{

	private IUserService userService;

	@Override
	protected boolean authenticateUsernamePasswordInternal(
			UsernamePasswordCredentials credentials) throws AuthenticationException
	{
		return userService.loginMember(credentials.getUsername(), credentials.getPassword());
		
		
	}

	public void setUserService(IUserService userService)
	{
		this.userService = userService;
	}

		
}