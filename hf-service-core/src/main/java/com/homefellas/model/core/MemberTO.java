package com.homefellas.model.core;

import java.util.Date;


public class MemberTO extends AbstractBaseTO{

	private static final long serialVersionUID = 6376568551034910445L;
	private String accountId;
	private String password;
	private String referredBy;
	private boolean acceptTermsAndConditions=true;
	private boolean newsLetter=true;
	private Date lastModified = new Date(System.currentTimeMillis());
	private long facebookId;
	private boolean aborted=false;
	
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	public boolean isAcceptTermsAndConditions() {
		return acceptTermsAndConditions;
	}
	public void setAcceptTermsAndConditions(boolean acceptTermsAndConditions) {
		this.acceptTermsAndConditions = acceptTermsAndConditions;
	}
	public String getReferredBy() {
		return referredBy;
	}
	public void setReferredBy(String referredBy) {
		this.referredBy = referredBy;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAccountIdPrefixOnly(){
		int atLocation = getAccountId().indexOf("@");
		if(atLocation == -1){
			return getAccountId();
		}
		else{
			return getAccountId().substring(0,atLocation);
		}
	}
	public boolean isNewsLetter() {
		return newsLetter;
	}
	public void setNewsLetter(boolean newsLetter) {
		this.newsLetter = newsLetter;
	}
	public long getFacebookId() {
		return facebookId;
	}
	public void setFacebookId(long facebookId) {
		this.facebookId = facebookId;
	}
	public boolean isAborted() {
		return aborted;
	}
	public void setAborted(boolean aborted) {
		this.aborted = aborted;
	}
	
	
}
