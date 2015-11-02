package com.homefellas.rm.share;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.exception.ValidationException;
import com.homefellas.rm.AbstractRMTestDao;
import com.homefellas.rm.calendar.Calendar;
import com.homefellas.rm.calendar.ICalendarDao;
import com.homefellas.rm.reminder.IReminderDao;
import com.homefellas.rm.task.ITaskDao;
import com.homefellas.rm.task.Task;
import com.homefellas.user.Member;
import com.homefellas.user.Profile;

public class ShareDaoTest extends AbstractRMTestDao 
{

	@Resource(name="shareDao")
	private IShareDao shareDao;
	
	@Resource(name="calendarDao")
	private ICalendarDao calendarDao;
	
	@Resource(name="taskDao")
	private ITaskDao taskDao;
	
	@Resource(name="shareService")
	private ShareService shareService;
	
	
	@Test
	@Transactional
	public void getShareCalendarByCalendarAndMember()
	{
		Profile profile1 = createProfile();
		Profile profile2 = createProfile();
		Profile profile4Guest2 = createGuest();
		
		Task task2 = createTask(profile1);
		Task task3 = createTask(profile1);
		
		Calendar calendar = new Calendar();
		calendar.setCalendarName("Stuff to do");
		calendar.setMember(profile2);
		try {
			calendarDao.createCalendar(calendar);
		

		task2.addCalendar(calendar);
		taskDao.updateTask(task2);
		
		task3.addCalendar(calendar);
		taskDao.updateTask(task3);
		
		Invite invite = new Invite();
		Map<String, String>emailAddresses = new HashMap<String, String>();
		emailAddresses.put(profile4Guest2.getEmail(), null);
		invite.setEmailAddresses(emailAddresses);
		invite.setInviter(profile2.getMember());
		invite.setMessage("some message");
		invite.setSubject("some subject");
		invite.setDirectLink("http://reminded.me");
		invite.setShareId(calendar.getId());
		
	
			shareService.shareCalender(invite);
			
			ShareCalendar shareCalendar = shareDao.getShareCalendarByCalendarAndMember(calendar, profile4Guest2.getMember());
			Assert.assertNotNull(shareCalendar);
			
			Assert.assertEquals(calendar, shareCalendar.getCalendar());
			Assert.assertEquals(profile4Guest2.getMember(), shareCalendar.getUser());
		}
		catch (ValidationException exception)
		{
			Assert.fail(exception.getMessage());
		}
	}
	
//	@Test
//	@Transactional
//	public void updateShareStatusForTasks()
//	{
//		Calendar calendar = new Calendar();
//		calendar.setCalendarName("Stuff to do");
//		calendar.setMember(profile2.getMember());
//		dao.save(calendar);
//		
//		task2.addCalendar(calendar);
//		dao.updateObject(task2);
//		
//		task3.addCalendar(calendar);
//		dao.updateObject(task3);
//		
//		Invite invite = new Invite();
//		Map<String, String>emailAddresses = new HashMap<String, String>();
//		emailAddresses.put(profile4Guest2.getEmail(), null);
//		invite.setEmailAddresses(emailAddresses);
//		invite.setInviter(profile2.getMember());
//		invite.setMessage("some message");
//		invite.setSubject("some subject");
//		invite.setDirectLink("http://reminded.me");
//		invite.setShareId(calendar.getId());
//		
//		try
//		{
//			shareService.shareCalenderTX(invite);
//			
//			Share share = shareService.getShareForTaskAndEmail(task2, profile4Guest2.getEmail());
//			Assert.assertEquals(ShareApprovedStatus.NO_ACTION.ordinal(), share.getShareApprovedStatusOrdinal());
//			
//			share = shareService.getShareForTaskAndEmail(task3, profile4Guest2.getEmail());
//			Assert.assertEquals(ShareApprovedStatus.NO_ACTION.ordinal(), share.getShareApprovedStatusOrdinal());
//			
//			List<String>taskIds = new ArrayList<String>();
//			taskIds.add(task2.getId());
//			taskIds.add(task3.getId());
//			int recordsUpated = shareDao.updateShareStatusForTasks(taskIds, profile4Guest2.getMember(), ShareApprovedStatus.APPROVED);
//			Assert.assertEquals(2, recordsUpated);
//			
//			dao.flush();
//			
//			share = shareService.getShareForTaskAndEmail(task2, profile4Guest2.getEmail());
//			Assert.assertEquals(ShareApprovedStatus.APPROVED.ordinal(), share.getShareApprovedStatusOrdinal());
//			
//			share = shareService.getShareForTaskAndEmail(task3, profile4Guest2.getEmail());
//			Assert.assertEquals(ShareApprovedStatus.APPROVED.ordinal(), share.getShareApprovedStatusOrdinal());
//		}
//		catch (ValidationException e)
//		{
//			Assert.fail(e.getMessage());
//		}
//	}
	
	@Test
	@Transactional 
	public void getSentInvitesForMember()
	{
		Profile profile1 = createProfile();
		Profile profile2 = createProfile();
		
		
		Task task6 = createTask(profile1);
		Task task9 = createTask(profile2);
		
		Share share1 = createShare(task6, profile2);
		Share share2 = createShare(task9, profile1);
		
		List<Invite> invites = shareDao.getSentInvitesForMember(profile1.getId());
		Assert.assertTrue(invites.get(0).getShareId().equals(task6.getId()));
//		Assert.assertFalse(invites.contains(invite2Task9));
	}
	
	@Test
	@Transactional
	public void getInvitedContactsByMemberId()
	{
		Profile profile1 = createProfile();
		Profile profile2 = createProfile();
		Profile profile3Guest1 = createGuest();
		Profile profile4Guest2 = createGuest();
		
		Task task6 = createTask(profile1);
		Map<String, String> emailaddress = new HashMap<String, String>();
		emailaddress.put(profile2.getEmail(), profile2.getName());
		emailaddress.put(profile3Guest1.getEmail(), "");
		emailaddress.put(profile4Guest2.getEmail(), null);
		
		createShare(task6, emailaddress);
		
		//get the friends of profile 1
		List<Profile> profiles = shareDao.getInvitedContactsByMemberId(profile1.getId());
		
		Assert.assertTrue(profiles.contains(profile3Guest1));
		Assert.assertTrue(profiles.contains(profile4Guest2));
		Assert.assertTrue(profiles.contains(profile2));
		Assert.assertFalse(profiles.contains(profile1));
	}
	
	@Test
	@Transactional
	public void getAcceptedSharedTasksForUser()
	{
		Profile profile1 = createProfile();
		Profile profile2 = createProfile();
		Profile profile3Guest1 = createGuest();
		Profile profile4Guest2 = createGuest();
		
		Task task6 = createTask(profile1);
		Task task9 = createTask(profile2);
		
		Share share1Profile3Task6 = createShare(task6, profile3Guest1);
		Share share2Profile3Task9 = createShare(task9, profile3Guest1);
		Share share3Profile4Task6 = createShare(task6, profile4Guest2);
		
		List<Task> tasks = shareDao.getAcceptedSharedTasksForUser(profile3Guest1.getMember());
		
		Assert.assertTrue(tasks.isEmpty());
		
		share1Profile3Task6.setShareApprovedStatusOrdinal(ShareApprovedStatus.APPROVED.ordinal());
		shareDao.createShare(share1Profile3Task6);
		
		tasks = shareDao.getAcceptedSharedTasksForUser(profile3Guest1.getMember());
		
		Assert.assertTrue(tasks.contains(task6));
		Assert.assertFalse(tasks.contains(task9));
		
		share3Profile4Task6.setShareApprovedStatusOrdinal(ShareApprovedStatus.DECLINED.ordinal());
		shareDao.createShare(share3Profile4Task6);
		
		tasks = shareDao.getAcceptedSharedTasksForUser(profile3Guest1.getMember());
		
		Assert.assertTrue(tasks.contains(task6));
		Assert.assertFalse(tasks.contains(task9));
		
		share2Profile3Task9.setShareApprovedStatusOrdinal(ShareApprovedStatus.APPROVED.ordinal());
		shareDao.createShare(share2Profile3Task9);
		
		tasks = shareDao.getAcceptedSharedTasksForUser(profile3Guest1.getMember());
		
		Assert.assertTrue(tasks.contains(task6));
		Assert.assertTrue(tasks.contains(task9));
		
		share3Profile4Task6.setShareApprovedStatusOrdinal(ShareApprovedStatus.APPROVED.ordinal());
		shareDao.createShare(share3Profile4Task6);
		
		tasks = shareDao.getAcceptedSharedTasksForUser(profile4Guest2.getMember());
		Assert.assertTrue(tasks.contains(task6));
		
	}


	@Test
	@Transactional
	public void testGetSharesForUser()
	{
		Profile profile1 = createProfile();
		Profile profile2 = createProfile();
		Profile profile3Guest1 = createGuest();
		Profile profile4Guest2 = createGuest();
		
		Task task6 = createTask(profile1);
		Task task9 = createTask(profile2);
		
		Share share1Profile3Task6 = createShare(task6, profile3Guest1);
		Share share2Profile3Task9 = createShare(task9, profile3Guest1);
		Share share3Profile4Task6 = createShare(task6, profile4Guest2);
		
		
		List<Share> shares = shareDao.getSharesForUser(profile3Guest1.getMember());
		Assert.assertTrue(shares.contains(share1Profile3Task6));
		Assert.assertTrue(shares.contains(share2Profile3Task9));
		
		shares = shareDao.getSharesForUser(profile4Guest2.getMember());
		Assert.assertTrue(shares.contains(share3Profile4Task6));
		
	}

	@Test
	@Transactional
	public void testGetShareForUserAndTask()
	{
		Profile profile1 = createProfile();
		Profile profile2 = createProfile();
		Profile profile3Guest1 = createGuest();
		Profile profile4Guest2 = createGuest();
		
		Task task2 = createTask(profile1);
		Task task3 = createTask(profile1);
		Task task6 = createTask(profile1);
		Task task9 = createTask(profile2);
		
		Share share1Profile3Task6 = createShare(task6, profile3Guest1);
		Share share2Profile3Task9 = createShare(task9, profile3Guest1);
		Share share3Profile4Task6 = createShare(task6, profile4Guest2);
		
		
		Share share = shareDao.getShareForUserAndTask(task6, profile3Guest1.getMember());
		Assert.assertEquals(share1Profile3Task6, share);
		
		share = shareDao.getShareForUserAndTask(task9, profile3Guest1.getMember());
		Assert.assertEquals(share2Profile3Task9, share);
		
		share = shareDao.getShareForUserAndTask(task6, profile4Guest2.getMember());
		Assert.assertEquals(share3Profile4Task6, share);
		
		share = shareDao.getShareForUserAndTask(task3, profile3Guest1.getMember());
		Assert.assertNull(share);
		
		share = shareDao.getShareForUserAndTask(task2, profile4Guest2.getMember());
		Assert.assertNull(share);
	}
	
	@Test
	@Transactional
	public void getSharesForTask()
	{
		Profile profile1 = createProfile();
		Profile profile2 = createProfile();
		Profile profile3Guest1 = createGuest();
		Profile profile4Guest2 = createGuest();
		
		Task task6 = createTask(profile1);
		Task task9 = createTask(profile2);
		
		Share share1Profile3Task6 = createShare(task6, profile3Guest1);
		Share share2Profile3Task9 = createShare(task9, profile3Guest1);
		Share share3Profile4Task6 = createShare(task6, profile4Guest2);
		
		List<Share> shares = shareDao.getSharesForTask(task6);
		
		Assert.assertTrue(shares.contains(share1Profile3Task6));
		Assert.assertTrue(shares.contains(share3Profile4Task6));
		
		shares = shareDao.getSharesForTask(task9);
		
		Assert.assertTrue(shares.contains(share2Profile3Task9));
	}

}
