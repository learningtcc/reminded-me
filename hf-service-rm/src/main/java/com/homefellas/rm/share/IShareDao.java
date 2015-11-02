package com.homefellas.rm.share;

import java.util.List;

import com.homefellas.rm.calendar.Calendar;
import com.homefellas.rm.task.Task;
import com.homefellas.rm.user.Contact;
import com.homefellas.user.Member;
import com.homefellas.user.Profile;

public interface IShareDao
{

	//create
	public IShare createShare(IShare share);
	public Invite createInvite(Invite invite);
	public ShareCalendar createShareCalendar(ShareCalendar shareCalendar);
	
	//read
	public List<Task> getAcceptedSharedTasksForUser(Member user);
	public List<Share> getSharesForUser(Member user);
	public Share getShareForUserAndTask(Task task, Member user);
	public List<Share> getSharesForTask(Task task);
	public List<String> getAcceptedSharedTaskIdsForUser(String userId);
	public Share getShareForTaskAndEmail(Task task, String email);
	public List<Profile> getInvitedContactsByMemberId(String memberId);
	public List<Invite> getSentInvitesForMember(String memberId);
	public List<Invite> getInvitesSharedWithEmail(String memberId);
	public List<Share> getSharesByIds(List<String>ids);
	public List<Invite> getInvitesByIds(List<String>ids);
	public Share getShareById(String id);
	public ShareCalendar getShareForUserAndCalendar(Calendar calendar, Member user);
	public List<ShareCalendar> getCalenderShares(Calendar calendar);
	public List<Member> getMembersFromCalendarShares(List<String> calendarKeys);
	public ShareCalendar getShareCalendarByCalendarAndMember(Calendar calendar, Member member);
	public List<Share> getAcceptedSharesForTask(Task task);
	public Contact getContactByProfileId(String contactId, String ownerId);
	public List<ShareCalendar> getShareCalendarsByIds(List<String>ids);
	public Invite getInviteById(String id);
	public ShareCalendar getShareCalendarById(String id);
	
	//update
	public Share updateShare(Share share);
	public Invite updateInvite(Invite invite);
	public ShareCalendar updateShareCalendar(ShareCalendar shareCalendar); 
}
