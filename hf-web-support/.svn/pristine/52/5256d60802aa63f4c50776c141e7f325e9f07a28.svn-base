package com.hiweb.facebook.model;

import java.io.Serializable;

/**
 * Holds the information associated with a Facebook Access Token
 * request. This class is used by the service layer to relay
 * authentication info to other tiers of the application
 * in a unified manner.
 *  
 */
public class FacebookAccessTokenRequestResponse implements Serializable {

	private static final long serialVersionUID = 4191785331562801084L;
	
	private String accessToken;
	private long expiration;
	
	private boolean authenticationError;
	private String errorType;
	private String errorMessage;

	public FacebookAccessTokenRequestResponse(String accessToken, long expiration) {
		this.accessToken = accessToken;
		this.expiration = expiration;
	}
	
	public FacebookAccessTokenRequestResponse(String errorType, String errorMessage) {
		this.authenticationError = true;
		this.errorType = errorType;
		this.errorMessage = errorMessage;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public long getExpiration() {
		return this.expiration;
	}

	public boolean isAuthenticationError() {
		return this.authenticationError;
	}

	public String getErrorType() {
		return this.errorType;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
