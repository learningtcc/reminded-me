package com.hiweb.facebook.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UserDetails;

import com.hiweb.facebook.NotEnoughSharedException;
import com.hiweb.facebook.model.FacebookUserDetail;

public interface IFacebookService {
	
	public UserDetails getUserDetailsFromFacebookCookie(HttpServletRequest request) throws NotEnoughSharedException;
	public FacebookUserDetail  getFaceBookUserDetailsFromFacebookCookie(HttpServletRequest request) throws NotEnoughSharedException;
}
