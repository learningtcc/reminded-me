package com.homefellas.cas.client.authentication;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.TicketValidationException;

public class OAuthReceivingTicketValidationFilter 
//implements Filter

extends AbstractCasFilter
{

	private String clientId;
	private String secrectCode;
	private String oauthAccessURL;
	private boolean exceptionOnValidationFailure = true;
	

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException
	{
		
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        
        final String ticket = CommonUtils.safeGetParameter(request, "code");
        final String service = CommonUtils.safeGetParameter(request, "service");
        
        if (CommonUtils.isNotBlank(ticket)) {
            if (log.isDebugEnabled()) {
                log.debug("Attempting to validate ticket: " + ticket);
            }
            
            //build url to accessToken.  https://localhost/login/oauth2.0/accessToken?code=ST-3-aXR9EfblD2WDdxl70RNP-cas&client_id=the_key_for_caswrapper1&client_secret=the_secrect_for_caswrapper1&redirect_uri=http://localhost/ws-core/index.html    
	        StringBuffer oauthAccessUrl = new StringBuffer();
	        oauthAccessUrl.append(oauthAccessURL);
	        oauthAccessUrl.append("?code=");
	        oauthAccessUrl.append(ticket);
	        oauthAccessUrl.append("&client_id=");
	        oauthAccessUrl.append(clientId);
	        oauthAccessUrl.append("&client_secret=");
	        oauthAccessUrl.append(secrectCode);
	        oauthAccessUrl.append("&redirect_uri=");
	        oauthAccessUrl.append(service);
        
	        try
	        {
	        	//call accessToken on cas
	        	String serverResponse = CommonUtils.getResponseFromServer(oauthAccessUrl.toString(), null);
	        
	        	//if there is no response we need to error.
		        if (serverResponse == null) {
		            throw new TicketValidationException("The CAS server returned no response.");
		        }
		        
		        //if the response contains error in it, die too
		        final String error = parseValue(serverResponse, "error");
		
		        //check to see if there was an error
		        if (CommonUtils.isNotBlank(error)) {
		        	throw new TicketValidationException(error);
		        }
		
		        //extract the tgt from the response
		        final String tgt = parseValue(serverResponse, "access_token");
		        
		        //build the response url
		        StringBuffer responseURL = new StringBuffer(service);
		        if (service.contains("?"))
		        	responseURL.append("&");
		        else
		        	responseURL.append("?");
		        
		        responseURL.append("tgt=");
		        responseURL.append(tgt);
		        
		        //send the user back to the service
		        response.sendRedirect(responseURL.toString());
		        
		        
	        }
		    catch (final TicketValidationException e) 
		    {
		    	//some ticket exception hsa occured...handle best we can
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                log.warn(e, e);

                if (this.exceptionOnValidationFailure) {
                    throw new ServletException(e);
                }

                return;
            }
        }
        
        

	}

	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}

	public void setSecrectCode(String secrectCode)
	{
		this.secrectCode = secrectCode;
	}

	public void setOauthAccessURL(String oauthAccessURL)
	{
		this.oauthAccessURL = oauthAccessURL;
	}

	public void setExceptionOnValidationFailure(boolean exceptionOnValidationFailure)
	{
		this.exceptionOnValidationFailure = exceptionOnValidationFailure;
	}
	
	
	public static String parseValue(String serverResponse, String key)
	{
		key += "=";
		if (!serverResponse.contains(key))
			return null;
		
		StringTokenizer stringTokenizer = new StringTokenizer(serverResponse);
		while (stringTokenizer.hasMoreElements())
		{
			String token = stringTokenizer.nextToken();
			int index = token.indexOf(key);
			if (index==-1)
				continue;

			return token.substring(index+key.length());
		}
		
		return null;
	}
	
//	https://localhost/login/oauth2.0/accessToken?code=ST-3-aXR9EfblD2WDdxl70RNP-cas&client_id=the_key_for_caswrapper1&client_secret=the_secrect_for_caswrapper1&redirect_uri=http://localhost/ws-core/index.html
}
