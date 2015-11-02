package com.hiweb.facebook.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hiweb.facebook.model.FacebookConstants;

/**
 * This class is home to several utility methods needed
 * by the Facebook services.
 * 
 */
public class FacebookHelper {


	private final static String UTF8 = "UTF-8";
	public static final Pattern BASE_64_URL_PATTERN = Pattern.compile("-_");
	
	/**
	 * Builds a Graph API based link to the user's photo.
	 * 
	 * @param uid
	 * @param type
	 * @return
	 */
	public static String buildProfilePhotoLink(String uid, String type) {
		StringBuilder profilePhotoBuilder = new StringBuilder("graph.facebook.com/");
		profilePhotoBuilder.append(uid);
		profilePhotoBuilder.append("/picture");
		
		if (type != null) {
			profilePhotoBuilder.append("?type=");
			profilePhotoBuilder.append(type);
		}
		
		return profilePhotoBuilder.toString();
	}
	
	public static boolean isFacebookApp(HttpServletRequest request)
	{
		String facebookApp = request.getParameter("facebook");
		if (facebookApp!=null&&facebookApp.equals("true"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Takes a URL query string and parses the values into a Map<String, String>
	 * Additionally, this handles UTF-8 decoding.
	 * 
	 * i.e.: param1=test1&param2=test2&param3=test3. 
	 * becomes { "param1" : "test1", "param2" : "test2", "param3" : "test3",}
	 */
	public static Map<String, String> parseQueryString(String s) {
		Map<String, String> result = new HashMap<String, String>();
		if (s != null) {
			String[] params = s.split("&");
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					String[] nameval = params[i].split("=");
					if (nameval != null && nameval.length == 2) {
						try {
							result.put(nameval[0], URLDecoder.decode(
									nameval[1], UTF8));
						} catch (UnsupportedEncodingException uee) {
							result.put(nameval[0], nameval[1]);
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * @param rawString
	 * @return
	 */
	public static List<Long> parseMutualFriendsString(String rawString) {
		if (rawString == null) {
			return null;
		}
		
		List<Long> mutualFriendIds = null;
		
		String rawMutualFriendstring = rawString.substring(1,(rawString.length() - 1));
		String[] idStrings = rawMutualFriendstring.split(",");
		
		if (idStrings != null) {
			mutualFriendIds = new ArrayList<Long>();
			for ( int i = 0 ; i < idStrings.length ; i++ ) {
				String idString = idStrings[i];
				Long uid = null;
				try {
					uid = Long.valueOf(idString);
				} catch (NumberFormatException nfe) {
					
				}
				
				if (uid != null) {
					mutualFriendIds.add(uid);
				}
			}
			
			if (mutualFriendIds.isEmpty()) {
				mutualFriendIds = null;
			}
		}
		
		return mutualFriendIds;
	}
	
	/**
	 * Sends a get request to the passed in url parameter.
	 * @param url
	 * @return A String containing the server's response.
	 * @throws IOException
	 */
	public static String sendGetRequest(String url, boolean debug) throws IOException {
		StringBuffer result = new StringBuffer();
		Log log = LogFactory.getLog("com.hiweb.facebook.util.FaceBookHelper");
		if (debug)
		{
			log.info("Using the SSL link to facebook");
			X509TrustManager tm = new X509TrustManager() 
			{
	
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] arg0, String arg1)
						throws java.security.cert.CertificateException {
					
				}
	
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] arg0, String arg1)
						throws java.security.cert.CertificateException {
					
					
				}
	
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					
					return null;
				}
				
				
			};
			try
			{
				SSLContext ctx = SSLContext.getInstance("TLS");
			
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLContext.setDefault(ctx); 
			HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
			conn.setHostnameVerifier(new HostnameVerifier() 
				{
	
					public boolean verify(String arg0, SSLSession arg1) {
						return true;
					}
					
				});
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				result.append(inputLine);
			}
			log.info("result="+result);
			return result.toString();
				 
		
			}
			catch (NoSuchAlgorithmException exception)
			{
				exception.printStackTrace();
				return null;
			}
			catch (KeyManagementException exception)
			{
				exception.printStackTrace();
				return null;
			}
		}
		else
		{
		
			URL u = new URL(url);
			URLConnection con = u.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				result.append(inputLine);
			}
			log.info("result="+result);
			return result.toString();
		}
	}
	
	public static String base64UrlStringSanitize(String rawString) {
		return FacebookHelper.BASE_64_URL_PATTERN.matcher(rawString).replaceAll("+/");
		
	}
	
	public static void staggeredListShuffle(List<?> listToShuffle, int staggerAmount) {
		if ((listToShuffle == null) || (listToShuffle.size() < 2)) {
			return;
		}
		
		int listSize = listToShuffle.size();
		
		if (listSize < staggerAmount) {
			Collections.shuffle(listToShuffle);
			return;
		}
		
		int sizeLeft = listSize;
		int startPosition = 0;
		int endPosition = staggerAmount;
		
		while (sizeLeft > 0) {
			if (endPosition > listSize) {
				endPosition = listSize;
				sizeLeft = -1;
				if ((endPosition - startPosition) < 2) {
					return;
				}
			}
			Collections.shuffle(listToShuffle.subList(startPosition, endPosition));
			sizeLeft -= staggerAmount;
			startPosition += staggerAmount;
			endPosition += staggerAmount;
		}
	}
	
	public static String constructOauthRedirectUri(String clientId, String redirectUri, String scope) {
		StringBuilder oauthUriBuilder = new StringBuilder(FacebookConstants.FACEBOOK_OAUTH_URI);
		oauthUriBuilder.append("?");
		
		oauthUriBuilder.append(FacebookConstants.FACEBOOK_CLIENT_ID_PARAMETER);
		oauthUriBuilder.append("=");
		oauthUriBuilder.append(clientId);
		
		oauthUriBuilder.append("&");
		oauthUriBuilder.append(FacebookConstants.FACEBOOK_REDIRECT_URI_PARAMETER);
		oauthUriBuilder.append("=");
		oauthUriBuilder.append(redirectUri);
		
		oauthUriBuilder.append("&");
		oauthUriBuilder.append(FacebookConstants.FACEBOOK_SCOPE_PARAMETER);
		oauthUriBuilder.append("=");
		oauthUriBuilder.append(scope);
		
		
		return oauthUriBuilder.toString();
	}
	
	public static String cookieTrim(String input) {
		if (input == null) {
			return null;
		}
		
		StringBuilder outputBuilder = new StringBuilder(input);
		while((outputBuilder.length() > 0) 
				&& ( (outputBuilder.charAt(0) == '\"') || (outputBuilder.charAt(0) == '\\') )) {
			outputBuilder.deleteCharAt(0);
		}
		
		while( (outputBuilder.charAt(outputBuilder.length() - 1) == '\"') 
				|| (outputBuilder.charAt(outputBuilder.length() - 1) == '\\') ) {
			outputBuilder.deleteCharAt(outputBuilder.length() - 1);
		}
		
		return outputBuilder.toString();
	}
	
	public static <K> LinkedHashMap<String, K> phpKsort(Map<String, K> inputMap) {
		if (inputMap == null) {
			return null;
		}
		
		List<String> keyList = new ArrayList<String>(inputMap.size());
		for (String key : inputMap.keySet()) {
			keyList.add(key);
		}
		
		Collections.sort(keyList);
		
		LinkedHashMap<String, K> outputMap = new LinkedHashMap<String, K>();
		for (String key : keyList) {
			outputMap.put(key, inputMap.get(key));
		}
		
		return outputMap;
	}
}
