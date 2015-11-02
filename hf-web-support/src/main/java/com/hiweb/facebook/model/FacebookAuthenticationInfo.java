package com.hiweb.facebook.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class FacebookAuthenticationInfo implements IFacebookAuthenticationInfo, Serializable {

	private static final long serialVersionUID = 7244117469152505960L;
	 
	public static final String SESSION_NAME = "FacebookAuthenticationInfo";
	
	// The following variables represent the object's state.
	private String oauthToken; 			// A string that you can pass to the Graph API or the Legacy REST API.
	private long expires;				// A number containing the Unix timestamp when the oauth_token expires.
	
	private FacebookUserDetail userDetail;
	private String serviceRedirect;
	
	public FacebookAuthenticationInfo() { }
	
	public static FacebookAuthenticationInfo getInstance(HttpSession session) {
		FacebookAuthenticationInfo facebookAuthenticationInfo = 
			(FacebookAuthenticationInfo) session.getAttribute(SESSION_NAME);
		
		if (facebookAuthenticationInfo == null) {
			facebookAuthenticationInfo = new FacebookAuthenticationInfo();
			session.setAttribute(SESSION_NAME, facebookAuthenticationInfo);
		}

		return facebookAuthenticationInfo;
	}
	
	public static FacebookAuthenticationInfo getInstance(Map<String, Object> sessionMap) {
		FacebookAuthenticationInfo facebookAuthenticationInfo = 
			(FacebookAuthenticationInfo) sessionMap.get(SESSION_NAME);
		
		if (facebookAuthenticationInfo == null) {
			facebookAuthenticationInfo = new FacebookAuthenticationInfo();
			sessionMap.put(SESSION_NAME, facebookAuthenticationInfo);
		}

		return facebookAuthenticationInfo;
	}
	
	
	public void setAuthenticationParameters(String oauthToken, long expires) {
		this.oauthToken = oauthToken;
		this.expires = expires;
	}
	
	public boolean isTokenUsable() {
		if (this.oauthToken == null) {
			return false;
		}
		
		long currentTime = new Date().getTime();
		return currentTime < this.expires;
	}
	
	

	public String getOauthToken() {
		return this.oauthToken;
	}

	public long getExpires() {
		return this.expires;
	}

	public FacebookUserDetail getUserDetail() {
		return this.userDetail;
	}

	public void setUserDetail(FacebookUserDetail userDetail) {
		this.userDetail = userDetail;
	}

	public String getServiceRedirect() {
		return serviceRedirect;
	}

	public void setServiceRedirect(String serviceRedirect) {
		this.serviceRedirect = serviceRedirect;
	}
	
	
}
