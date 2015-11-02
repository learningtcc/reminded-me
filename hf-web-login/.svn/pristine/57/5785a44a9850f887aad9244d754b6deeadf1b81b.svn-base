package com.homefellas.register;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.NamedAuthenticationHandler;
import org.jasig.cas.authentication.principal.Credentials;

import com.homefellas.user.IUserServiceTX;
import com.homefellas.user.RegisterTicket;

public class RegisterAuthenticationHandler implements
		NamedAuthenticationHandler {

	/** Instance of logging for subclasses. */
    protected Log log = LogFactory.getLog(this.getClass());
    
    /** The name of the authentication handler. */
    private String name = getClass().getName();
    
    /** Default class to support if one is not supplied. */
    private static final Class<RegisterCredentials> DEFAULT_CLASS = RegisterCredentials.class;

    /** Class that this instance will support. */
    private Class< ? > classToSupport = DEFAULT_CLASS;
    
    private boolean supportSubClasses = false;
    
   
    private IUserServiceTX userService;
    
	public void setUserService(IUserServiceTX userService)
	{
		this.userService = userService;
	}
    
    public final void setName(final String name) {
        this.name = name;
    }
    
    public final String getName() {
        return this.name;
    }

	public boolean authenticate(Credentials credentials) throws AuthenticationException {
		
		log.info("In RegisterAuthenticationHandler");
		RegisterCredentials registerCredentials = (RegisterCredentials)credentials;
//		ILoginService loginService = (ILoginService)userDetailsService;
		log.info("id="+registerCredentials.getTokenId()+" user="+registerCredentials.getUsername());
		RegisterTicket casRegisterTO = userService.getCasRegisterTX(registerCredentials.getTokenId(), registerCredentials.getUsername());
		if (casRegisterTO!=null)
		{
			log.info("Register match!");
			userService.deleteCasRegisterTO(casRegisterTO);
			return true; 
		}
		else
		{
			return false;
		}
	}

	 public final boolean supports(final Credentials credentials) {
	        return credentials != null
	            && (this.classToSupport.equals(credentials.getClass()) || (this.classToSupport
	                .isAssignableFrom(credentials.getClass()))
	                && this.supportSubClasses);
	    }

}
