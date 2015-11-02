package com.homefellas.register;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.web.flow.AbstractNonInteractiveCredentialsAction;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.webflow.execution.RequestContext;

import com.homefellas.user.IUserService;

public class RegisterController extends AbstractNonInteractiveCredentialsAction {

	private IUserService userService;
	
	
	
	public void setUserService(IUserService userService)
	{
		this.userService = userService;
	}

	@Override
	protected Credentials constructCredentialsFromRequest(
	        final RequestContext context) {
		logger.info("In RegisterController");
		String tokenId = null;
		String username = null;
		final HttpServletRequest request = WebUtils.getHttpServletRequest(context);
		final HttpServletResponse response = WebUtils.getHttpServletResponse(context);
		
		tokenId = request.getParameter("ticket");
		username = request.getParameter("email");
//		Cookie[] cookies = request.getCookies();
//		if (cookies== null)
//		{
//			logger.error("Cookies is null in RegisterController");
//			return null;
//		}
//		for (Cookie cookie:cookies)
//		{
//			if ("cas-register-id".equals(cookie.getName()))
//			{
//				try
//				{
//					tokenId = URLDecoder.decode(cookie.getValue(), "UTF-8");
//				}
//				catch (UnsupportedEncodingException encodingException)
//				{
//					tokenId = cookie.getValue();
//				}
//				logger.info("Found cas-register-id:"+tokenId+" Going to delete it now.");
//				cookie.setMaxAge(0);
//				cookie.setValue("");
//				cookie.setPath("/");
//				response.addCookie(cookie);
//				
//			}
//			if ("cas-register-email".equals(cookie.getName()))
//			{
//				try
//				{
//					username = URLDecoder.decode(cookie.getValue(), "UTF-8");
//				}
//			
//				catch (UnsupportedEncodingException encodingException)
//				{
//					username = cookie.getValue();
//				}
//				logger.info("cas-register-email:"+username+" Going to delete it now.");
//				cookie.setMaxAge(0);
//				cookie.setValue("");
//				cookie.setPath("/");
//				response.addCookie(cookie);
//			}
//		}
//		
//	        
//	        if (tokenId == null || username == null) {
//	            if (logger.isDebugEnabled()) {
//	                logger.debug("Certificates not found in request.");
//	            }
//	            return null;
//	        }
//
//	        if (logger.isDebugEnabled()) {
//	            logger.debug("Certificate found in request.");
//	        }
	        
	        return new RegisterCredentials(tokenId, username);
	    }

	@Override
	protected void onSuccess(RequestContext context, Credentials credentials) {
		
		RegisterCredentials registerCredentials = (RegisterCredentials)credentials;
		
		final HttpServletResponse response = WebUtils.getHttpServletResponse(context);
		
		UserDetails userDetails = userService.loadUserByUsername(registerCredentials.getUsername());
		String cookieValue=registerCredentials.getUsername()+"^"+userDetails.getPassword();
		
//		Cookie rememberMeCookie = new Cookie("cas-rememberme", cookieValue);
//		rememberMeCookie.setMaxAge(2147483647);
//		rememberMeCookie.setPath("/");
//		response.addCookie(rememberMeCookie);
	}



	

	
	
	
}
