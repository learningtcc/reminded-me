package com.hiweb.facebook.authenication;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hiweb.facebook.model.FacebookAuthenticationInfo;
import com.hiweb.facebook.service.IFacebookService;

public class FacebookAuthenticationProcessor {
	protected final Log log = LogFactory.getLog(getClass());
	
	private IFacebookService facebookService;
	
	// Facebook Constants
	private static final String FACEBOOK_CODE_PARAMETER = "code";
	
	// Configurable Parameters
	private String appSecret;
	private String appId;
	private String oauthRedirectUri;
	private String tokenRedirectUri;

	/*
	 * Default Constructor
	 */
	public FacebookAuthenticationProcessor() {
		super();
	}

	public void retrieveAccessToken(HttpServletRequest request, HttpServletResponse response, 
			FacebookAuthenticationInfo facebookAuthenticationInfo) {
		
		// Gets the code that Facebook sends us.
		String code = request.getParameter(FACEBOOK_CODE_PARAMETER);
		
		log.info("code="+code);
		// If the code parameter does not exist we need to authenticate.
		if (code == null) {
			handleAuthenticationRedirect(response);
			return;
		}
		
//		FacebookAccessTokenRequestResponse facebookResponse 
//				= getFacebookService().requestAccessToken(code, appSecret, appId, tokenRedirectUri);
//		
//		
//		if (facebookResponse.isAuthenticationError()) {
//			System.out.println("Authentication Error");
//		} else {
//			String oauthToken = facebookResponse.getAccessToken();
//			long tokenExpirationTime = facebookResponse.getExpiration();
//			
//			// The token is expired so we need to authenticate again.
//			if ((new Date().getTime()) > tokenExpirationTime) {
//				handleAuthenticationRedirect(response);
//				return;
//			}
//			
//			facebookAuthenticationInfo.setAuthenticationParameters(oauthToken, tokenExpirationTime);
//		}
	}
	
	private void handleAuthenticationRedirect(HttpServletResponse response) {
		try {
			log.info("redirect to oauthRedirectUri="+oauthRedirectUri);
			response.sendRedirect(oauthRedirectUri);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		
	}

	public IFacebookService getFacebookService() {
		return this.facebookService;
	}

	public void setFacebookService(IFacebookService facebookService) {
		this.facebookService = facebookService;
	}

	public String getAppSecret() {
		return this.appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getOauthRedirectUri() {
		return this.oauthRedirectUri;
	}

	public void setOauthRedirectUri(String oauthRedirectUri) {
		this.oauthRedirectUri = oauthRedirectUri;
	}

	public String getTokenRedirectUri() {
		return this.tokenRedirectUri;
	}

	public void setTokenRedirectUri(String tokenRedirectUri) {
		this.tokenRedirectUri = tokenRedirectUri;
	}
}
