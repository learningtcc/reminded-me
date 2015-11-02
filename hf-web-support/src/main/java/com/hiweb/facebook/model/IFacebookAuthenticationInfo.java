package com.hiweb.facebook.model;

public interface IFacebookAuthenticationInfo {
	void setAuthenticationParameters(String oauthToken, long expires);
	boolean isTokenUsable();
	String getOauthToken();
	long getExpires();
	
	FacebookUserDetail getUserDetail();
	void setUserDetail(FacebookUserDetail userDetail);
	public String getServiceRedirect();
}
 