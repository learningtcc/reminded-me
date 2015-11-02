package com.homefellas.rm.share;

import com.homefellas.user.Member;

public interface IShare
{
	public Member getUser();
	public void setUser(Member member);
	public void setIShareable(IShareable iShareable);
	public void setInvite(Invite invite);
	public boolean isPublic();

}
