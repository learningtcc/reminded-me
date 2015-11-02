package com.homefellas.ws.model;

import com.homefellas.rm.share.Share;
import com.homefellas.rm.share.Share.ShareStatus;
import com.homefellas.rm.share.ShareApprovedStatus;
import com.homefellas.user.Member;

public class ShareUI extends AbstractUI
{	
	private int shareApprovedStatusOrdinal = ShareApprovedStatus.NO_ACTION.ordinal();
	private int status=ShareStatus.UNACTIONED.ordinal();
	
	private boolean viewed=false;
	private boolean blurred=false;
	
	private String lastModifiedDeviceId;
	
	private Member user;
	
	ShareUI() {}
	
	public ShareUI(Share share)
	{
		super(share.getId(), share.getLastModifiedDeviceId(), share.getCreatedDate(), share.getModifiedDate(), share.getCreatedDateZone(), share.getModifiedDateZone(), share.getClientUpdateTimeStamp());
		
		this.shareApprovedStatusOrdinal=share.getShareApprovedStatusOrdinal();
		this.status=share.getStatus();
		
		this.viewed=share.isViewed();
		this.blurred=share.isBlurred();
		
		this.user = new Member(share.getUser().getId());
	}

	public int getShareApprovedStatusOrdinal()
	{
		return shareApprovedStatusOrdinal;
	}

	public int getStatus()
	{
		return status;
	}

	public boolean isViewed()
	{
		return viewed;
	}

	public boolean isBlurred()
	{
		return blurred;
	}

	public String getLastModifiedDeviceId()
	{
		return lastModifiedDeviceId;
	}

	public Member getUser()
	{
		return user;
	}

	void setShareApprovedStatusOrdinal(int shareApprovedStatusOrdinal)
	{
		this.shareApprovedStatusOrdinal = shareApprovedStatusOrdinal;
	}

	void setStatus(int status)
	{
		this.status = status;
	}

	void setViewed(boolean viewed)
	{
		this.viewed = viewed;
	}

	void setBlurred(boolean blurred)
	{
		this.blurred = blurred;
	}

	void setLastModifiedDeviceId(String lastModifiedDeviceId)
	{
		this.lastModifiedDeviceId = lastModifiedDeviceId;
	}

	void setUser(Member user)
	{
		this.user = user;
	}
	
	
}
