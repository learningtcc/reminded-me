package com.homefellas.model.core;

import org.joda.time.DateTime;

import com.homefellas.exception.ValidationException;

public interface IGenericSynchroinzedLifeCycle
{

	public void validate() throws ValidationException;
	public String getEmailForAuthorization();
	public void setModifiedDateZone(DateTime dateTime);
	public String getId();
	public long getModifiedDate();
	public DateTime getClientUpdateTimeStamp();
	public void markForDeletion();
}
