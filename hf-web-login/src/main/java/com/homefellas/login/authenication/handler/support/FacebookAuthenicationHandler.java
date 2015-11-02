package com.homefellas.login.authenication.handler.support;

import javax.validation.constraints.NotNull;

import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;

import com.homefellas.authentication.principal.FacebookCredentials;
import com.homefellas.user.IUserService;
import com.homefellas.user.IUserServiceTX;

public class FacebookAuthenicationHandler extends AbstractPreAndPostProcessingAuthenticationHandler
{
	 /** Default class to support if one is not supplied. */
    private static final Class<FacebookCredentials> DEFAULT_CLASS = FacebookCredentials.class;

    /** Class that this instance will support. */
    @NotNull
    private Class< ? > classToSupport = DEFAULT_CLASS;
    
    private boolean supportSubClasses = true;

    private IUserServiceTX userService;
    
    public void setUserService(IUserServiceTX userService)
	{
		this.userService = userService;
	}
    
	@Override
	 public final boolean supports(final Credentials credentials) {
        return credentials != null
            && (this.classToSupport.equals(credentials.getClass()) || (this.classToSupport
                .isAssignableFrom(credentials.getClass()))
                && this.supportSubClasses);
    }

	@Override
	protected boolean doAuthentication(Credentials credentials)
			throws AuthenticationException
	{
		
		return userService.getProfileByFaceBookIdTX(((FacebookCredentials)credentials).getId())==null?false:true;
//		loginMember(credentials.getUsername(), credentials.getPassword());
	}

}
