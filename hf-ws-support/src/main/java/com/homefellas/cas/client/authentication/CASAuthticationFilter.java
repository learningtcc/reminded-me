package com.homefellas.cas.client.authentication;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.jasig.cas.client.authentication.AttributePrincipalImpl;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.homefellas.user.IUserService;
import com.homefellas.user.IUserServiceTX;
import com.homefellas.user.Profile;
import com.homefellas.user.UserService;

public class CASAuthticationFilter extends AbstractCasFilter
{

//	private String clientId;
//	private String oauthAuthorizeUrl;
	
	/**
     * Whether to send the renew request or not.
     */
//    private boolean renew = false;
//    private boolean gateway = false;
//    private String authorizeResponseURI;
    private String oauthProfileUrl;
    private String loginURL;
    private IUserServiceTX userService;
//    private String serverNameInstance;
    /**
     * Whether to send the gateway request or not.
     */
    
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException
	{
		 final HttpServletRequest request = (HttpServletRequest) servletRequest;
	     final HttpServletResponse response = (HttpServletResponse) servletResponse;
	     
	     //pull the tgt from the url   
	     final String tgt = CommonUtils.safeGetParameter(request,"tgt");
	     
	     if (CommonUtils.isNotBlank(tgt)) 
	     {
	    	 //we have a ticket so we need to try and grab the creditals from session
	    	 HttpSession session = request.getSession();
	    	 Assertion assertion=null;
	    	 
	    	 //check session to see if this is another request
	    	 
//	    	 if (session.getAttribute(CONST_CAS_ASSERTION)==null)
	    	 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    	 if (authentication == null)
	    	 {
	    		 //first hit we need to call cas and retrieve the profile
	    		 StringBuffer profileRequestUrl = new StringBuffer(oauthProfileUrl);
	    		 profileRequestUrl.append("/");
	    		 profileRequestUrl.append(tgt);
	    		 
	    		 //call cas
	    		 String serverResponse = CommonUtils.getResponseFromServer(profileRequestUrl.toString(), null);
	    		 
	    		 //need to check the response...if it has an error that probably means the TGT is expired so redirect to login page
	    		 if (!CommonUtils.isNotBlank(serverResponse) || serverResponse.contains("error"))
	    		 {
//	    			 redirectToLogin(request, response);
//	    			 response.sendRedirect(loginURL);
	    			 chain.doFilter(request, response);
	    			 return;
	    		 }
	    		 
	    		 //no error...convert the json to a temp varialbe to create teh assertion
	    		 ObjectMapper mapper = new ObjectMapper();
	    		 ProfileJson profileJson  = mapper.readValue(serverResponse, ProfileJson.class);
//	    		 assertion = new AssertionImpl(new AttributePrincipalImpl(profileJson.getId(), profileJson.getAttributes()));
	    		 assertion = new AssertionImpl(new AttributePrincipalImpl(profileJson.getId()));
	    		 
	    		 if (profileJson.getId().contains("@"))
	    		 	authentication = userService.getProfileByEmailTX(profileJson.getId());
	    		 else
	    			 authentication = userService.getProfileByFaceBookIdTX(profileJson.getId());
	    		 authentication.setAuthenticated(true);
	    		 //set it back in session
//	    		 session.setAttribute(CONST_CAS_ASSERTION, assertion);
	    		 SecurityContextHolder.getContext().setAuthentication(authentication);
	    	 }
//	    	 else
//	    	 {
//	    		 //retrieve the assertion from session
//	    		 assertion = (Assertion)session.getAttribute(CONST_CAS_ASSERTION);
//	    	 }
	    	 
	    	 //set it in the requet for downstream filters
//	    	 request.setAttribute(CONST_CAS_ASSERTION, assertion);
	    	 
	    	 //continue on
	    	 chain.doFilter(request, response);
	     }
	     else
	     {
	    	//no tgt, so we need to have them login 
//	    	redirectToLogin(request, response);
//	    	 respon?se.sendRedirect(loginURL);
	    	 chain.doFilter(request, response);
	    	return;
	     }
	}
	
//	private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
//	{
//		StringBuffer buffer = new StringBuffer();
//	     if (!serverNameInstance.startsWith("https://") && !serverNameInstance.startsWith("http://")) {
//	    	 buffer.append(request.isSecure() ? "https://" : "http://");
//	        }
//
//	        buffer.append(serverNameInstance);
//	        buffer.append(request.getContextPath());
//	        buffer.append(authorizeResponseURI);
//	        buffer.append("?service=");
//	        buffer.append(constructServiceUrl(request, response));
//	        
//		//we need to call oauth
//    	String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.oauthAuthorizeUrl, getServiceParameterName(), buffer.toString(), this.renew, this.gateway);
//    	urlToRedirectTo+="&client_id="+clientId;
//         if (log.isDebugEnabled()) {
//             log.debug("redirecting to \"" + urlToRedirectTo + "\"");
//         }
//
//         response.sendRedirect(urlToRedirectTo);
//    	 return;
//	}

//	public void setClientId(String clientId)
//	{
//		this.clientId = clientId;
//	}
//
//	public void setOauthAuthorizeUrl(String oauthAuthorizeUrl)
//	{
//		this.oauthAuthorizeUrl = oauthAuthorizeUrl;
//	}
//
//	public void setAuthorizeResponseURI(String authorizeResponseURI)
//	{
//		this.authorizeResponseURI = authorizeResponseURI;
//	}
//
//	public void setServerNameInstance(String serverNameInstance)
//	{
//		this.serverNameInstance = serverNameInstance;
//	}
//
	public void setOauthProfileUrl(String oauthProfileUrl)
	{
		this.oauthProfileUrl = oauthProfileUrl;
	}

	public void setLoginURL(String loginURL)
	{
		this.loginURL = loginURL;
	}

	public void setUserService(IUserServiceTX userService)
	{
		this.userService = userService;
	}

	
	


	
}
