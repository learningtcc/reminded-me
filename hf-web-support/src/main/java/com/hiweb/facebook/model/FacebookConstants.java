package com.hiweb.facebook.model;

public class FacebookConstants {

	// OAuth
	public static final String FACEBOOK_OAUTH_URI = "https://www.facebook.com/dialog/oauth";
	public static final String FACEBOOK_ACCESS_TOKEN = "access_token";
	public static final String FACEBOOK_TOKEN_EXPIRES = "expires";
	public static final String FACEBOOK_CLIENT_ID_PARAMETER = "client_id";
	public static final String FACEBOOK_CLIENT_SECRET_PARAMETER = "client_secret";
	public static final String FACEBOOK_CODE_PARAMETER = "code";
	public static final String FACEBOOK_REDIRECT_URI_PARAMETER = "redirect_uri";
	public static final String FACEBOOK_SCOPE_PARAMETER = "scope";
	 
	// Request URIs
	public static final String FACEBOOK_TOKEN_REQUEST_URI = "https://graph.facebook.com/oauth/access_token";
	public static final String FACEBOOK_GRAPH_API_URI = "https://graph.facebook.com/";
	public static final String FACEBOOK_FRIENDLIST_REQUEST_FRAGMENT = "/friends?access_token=";
	
	// Graph API User fields
	public static final String FACEBOOK_GRAPH_API_ID = "id";
	public static final String FACEBOOK_GRAPH_API_NAME = "name";
	public static final String FACEBOOK_GRAPH_API_PIC = "picture";
	public static final String FACEBOOK_GRAPH_API_EMAIL = "email";
	public static final String FACEBOOK_GRAPH_API_LOCATION = "hometown";
	
	// Legacy REST API
	public static final String FACEBOOK_REST_API_URI_METHOD = "https://api.facebook.com/method/";
	public static final String FACEBOOK_REST_API_MUTUAL_FRIENDS = "friends.getMutualFriends?";
	public static final String FACEBOOK_REST_API_TARGET_UID = "target_uid";
	public static final String FACEBOOK_REST_API_SOURCE_UID = "source_uid";
	public static final String FACEBOOK_REST_API_REQUEST_JSON = "&format=json";
	
	// Error
	public static final String FACEBOOK_ERROR = "error";
	public static final String FACEBOOK_ERROR_TYPE = "type";
	public static final String FACEBOOK_ERROR_MESSAGE = "message";
}
