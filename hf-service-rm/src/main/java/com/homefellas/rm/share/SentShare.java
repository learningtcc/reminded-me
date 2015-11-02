package com.homefellas.rm.share;

import org.codehaus.jackson.annotate.JsonIgnore;

public class SentShare extends Share
{

	@JsonIgnore
	public String getSyncId()
	{
		return "id";
	}
	
	@Override
	@JsonIgnore
	public String getModelName()
	{
		return Share.class.getSimpleName();
	}
	
	@Override
	@JsonIgnore
	public String getMemberAttributeName()
	{
		return "invite.inviter";
	}
}
