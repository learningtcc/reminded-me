package com.hiweb.facebook.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.HashCodeBuilder;

public class FacebookUserDetail implements Serializable {

	private static final long serialVersionUID = -8426090962402555866L;
	
	private String fullName;
	private String picture;
	private long uid;
	private String email;
	private String cityState;
	 
	public FacebookUserDetail() {}
	public FacebookUserDetail(String fullName, String picture, long uid, String email) {
		this.fullName = fullName;
		this.picture = picture;
		this.uid = uid;
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "Name: " + this.fullName + ", Pic: " + this.picture + ", UID: " + this.uid;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) { return true; }
		if (!(o instanceof FacebookUserDetail)) { return false; }
		
		FacebookUserDetail otherUser = (FacebookUserDetail) o;
		
		return (this.uid == otherUser.uid);
	}
	
	@Override
	public int hashCode() {
	    return new HashCodeBuilder(97, 109)
	       .append(this.uid)
	       .toHashCode();
	}
	
	public String getFullName() {
		return this.fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPicture() {
		return this.picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public long getUid() {
		return this.uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCityState() {
		return cityState;
	}
	public void setCityState(String cityState) {
		this.cityState = cityState;
	}
	
	
}
