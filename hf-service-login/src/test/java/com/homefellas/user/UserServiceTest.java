package com.homefellas.user;

import static com.homefellas.user.UserTestModelBuilder.buildBasicMember;
import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.batch.Notification;
import com.homefellas.batch.NotificationService;
import com.homefellas.exception.DatabaseNotInitializedException;
import com.homefellas.exception.ValidationException;

public class UserServiceTest extends AbstractUserServiceTest
{
	@Resource(name="userService")
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private IUserDao userDao;

	@Resource(name="notificationService")
	private NotificationService notificationService;
	
	@PostConstruct
	protected void disableTGT()
	{
		userService.setGenerateTGT(false);
	}
	
	@Test
	public void updateFacebookLoginAttempts()
	{
		Profile owner = UserTestModelBuilder.profile();
		owner.setFacebookId("123456789");
		owner = createProfile(owner);
		
		OAuthModel authModel =  new OAuthModel();
		authModel.setEmail(owner.getEmail());
		authModel.setId(owner.getFacebookId());
		try
		{
			Profile profile = userService.getProfileOrRegisterFaceBookUserTX(authModel);
			Assert.assertEquals(1, profile.getFacebookLogins());
			
			profile = userService.getProfileOrRegisterFaceBookUserTX(authModel);
			Assert.assertEquals(2, profile.getFacebookLogins());
			
			authModel =  new OAuthModel();
			authModel.setEmail(owner.getEmail());
			authModel.setId("987654321");
			
			profile = userService.getProfileOrRegisterFaceBookUserTX(authModel);
			Assert.assertEquals(3, profile.getFacebookLogins());
			Assert.assertEquals("987654321", profile.getFacebookId());
			
			authModel =  new OAuthModel();
			authModel.setEmail("email@reminded.me");
			authModel.setId("124598345768674553");
			
			profile = userService.getProfileOrRegisterFaceBookUserTX(authModel);
			Assert.assertEquals(1, profile.getFacebookLogins());
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
		}
		catch (DatabaseNotInitializedException e)
		{
			Assert.fail(e.getMessage());
		}
		
		
		
		
	}
	
	
	@Test
	public void getProfileByFaceBookId()
	{
		Profile profile1 = createProfile();
		
		String facebookId="4354235345345";
		profile1.setFacebookId(facebookId);
		try {
			userService.updateProfileTX(profile1);
		} catch (ValidationException e) {
			Assert.fail(e.getMessage());
		}
		
		Profile profile = userService.getProfileByFaceBookId(facebookId);
		
		Assert.assertEquals(userService.getProfile(profile1.getId()), profile);
		
	}
	
	@Test
	public void getProfileOrRegisterFaceBookUser()
	{
		OAuthModel authModel = new OAuthModel();
		authModel.setEmail("facebookUser@homefellas.com");
		authModel.setFirstName("first");
		authModel.setLastName("lstname");
		authModel.setId("12345");
		try
		{
			Profile profile = userService.getProfileOrRegisterFaceBookUserTX(authModel);
			
			String profileid = profile.getId(); 
			
			Assert.assertTrue(profile.isPrimaryKeySet());
			
			profile = userService.getProfileOrRegisterFaceBookUserTX(authModel);
			Assert.assertEquals(profileid, profile.getId());
			
			
			
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
		}
		catch (DatabaseNotInitializedException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	
	@Test
	public void addFriendsToExtendedProfile()
	{
		Profile profile1 = createProfile();
		
		ExtendedProfile extendedProfile = new ExtendedProfile();
		extendedProfile.setProfile(profile1);
		
		try
		{
			extendedProfile = userService.createUpdateExtendedProfile(extendedProfile);
			
			Assert.assertTrue(extendedProfile.isPrimaryKeySet());
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
		}
		
//		Set<FriendEmail> friendEmails = new java.util.HashSet<FriendEmail>();
//		FriendEmail friendEmail1 = new FriendEmail();
//		friendEmail1.setExtendedProfile(extendedProfile);
		extendedProfile.addFriendEmail("friendemail1@reminded.me");
//		friendEmails.add(friendEmail1);
		
//		FriendEmail friendEmail2 = new FriendEmail();
//		friendEmail2.setExtendedProfile(extendedProfile);
		extendedProfile.addFriendEmail("friendemail2@reminded.me");
//		friendEmails.add(friendEmail2);
		
		try
		{
			userService.addFriendsToExtendedProfile(extendedProfile);
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
		}
		
		for (FriendEmail friendEmail: extendedProfile.getFriendEmails())
		{
			Notification notification = notificationService.getNotificationsForINotificationTX(friendEmail).get(0);
			Assert.assertEquals(String.valueOf(friendEmail.getId()), notification.getiNotificationId());
		}
		
	}
	
	@Test
	public void regsiterGuest()
	{
		try
		{
			Profile profile = userService.reigsterGuest("newemail@homefellas.com", "Test");
			
			Assert.assertEquals(profile, userService.getProfile(profile.getId()));
			Assert.assertEquals(profile.getMember(), getMember(profile.getMember().getId()));
			
//			profileCounter++;
//			userCounter++;
//			
//			assertTableRowChecks();
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
		}
		catch (DatabaseNotInitializedException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	
	@Test 
	public void loginMember()	
	{
		Profile profile1 = createProfile();
		Profile profile3Guest1 = createGuest();
		
		Assert.assertTrue(userService.loginMember(profile1.getMember().getUsername(), profile1.getMember().getPassword()));
		profile3Guest1.getMember().setPassword("password");
		try {
			userService.updateProfileTX(profile3Guest1);
		} catch (ValidationException e) {
			Assert.fail(e.getMessage());
		}
		
		boolean loggedin = userService.loginMember(profile3Guest1.getMember().getUsername(), profile3Guest1.getMember().getCreditials());
		Assert.assertTrue(loggedin);
		
		Member classUnderTest = getMember(profile3Guest1.getMember().getId());
		Assert.assertFalse(classUnderTest.isGuest());
	}
	
	@Test
	public void handleGuestLogin()
	{
		Profile profile3Guest1 = createGuest();
		
		Assert.assertTrue(profile3Guest1.getMember().isGuest());
		
		userService.handleGuestLogin(profile3Guest1.getMember());
		
		Member classUnderTest = getMember(profile3Guest1.getMember().getId());
		Assert.assertFalse(classUnderTest.isGuest());
	}
	
	@Test 
	public void testUpgradeFromGuest()
	{

		Profile profile3Guest1 = createGuest();
		
		String newPassword = "newpasswordthatrocks";
		profile3Guest1.getMember().setPassword(newPassword);
		
		try
		{
			userService.registerBasicMember(profile3Guest1);
			
			Profile classUnderTest = userService.getProfile(profile3Guest1.getId());
			Assert.assertTrue(passwordEncoder.isPasswordValid(classUnderTest.getMember().getCreditials(), newPassword, null));
			Assert.assertFalse(classUnderTest.getMember().isGuest());
			
//			assertTableRowChecks();
		}
		catch (ValidationException e)
		{
			Assert.fail(e.getMessage());
		}
		catch (DatabaseNotInitializedException e)
		{
			Assert.fail(e.getMessage());
		}

	}
	
	@Test
	public void registerUserWithJoinConfirmation()
	{
		Profile member = buildBasicMember(true,"user@homefellas.com",passwordEncoder);
		String oldEmail = member.getMember().getUsername();
		String rawpassword = member.getMember().getPassword();
		Profile classUnderTest;
		
		member.setBaseURL("http://reminded.me");
		
		String ticket = UUID.randomUUID().toString();
		member.getMember().setTicket(ticket);
		rawpassword = member.getMember().getPassword();
		try
		{
			userService.registerBasicMember(member);
		}
		catch (ValidationException exception)
		{
			Assert.fail(exception.getMessage());
		}
		catch (DatabaseNotInitializedException databaseNotInitializedException)
		{
			Assert.fail();
		}
		
//		profileCounter++;
//		userCounter++;
//		dao.flush();
		
		//should have a ticket
		assertEquals(0, super.countRowsInTable("u_registerTickets"));
		//shopuld have 2 members now
//		assertEquals(profileCounter, super.countRowsInTable("u_profiles"));
//		assertEquals(userCounter, super.countRowsInTable("u_members"));
		Assert.assertTrue(member.isPrimaryKeySet());
		
		classUnderTest = userService.getProfile(member.getId());
		assertEquals(member, classUnderTest);
		
		Assert.assertTrue(classUnderTest.getMember().getRoles().contains(roleUser));
		Assert.assertFalse(classUnderTest.getMember().getRoles().contains(roleAdmin));
		
		Assert.assertTrue(passwordEncoder.isPasswordValid(classUnderTest.getMember().getCreditials(), rawpassword, null));
		
		List<Notification> notifications = notificationService.getNotificationsForINotificationTX(member.getMember());
		Notification notification = notifications.get(0);
		Assert.assertEquals(member.getMember().getEmail(), notification.getSendTo());
		
		//test to make sure dup email address works
		try
		{
			userService.registerBasicMember(member);
			Assert.fail();
		}
		catch (ValidationException exception)
		{
			Assert.assertTrue(exception.getValidationErrors().contains(UserValidationCodeEnum.MEMBER_ID_TAKEN));
		}
		catch (DatabaseNotInitializedException databaseNotInitializedException)
		{
			Assert.fail();
		}
		
	}
	@Test
	public void sendLostPasswordEmail()
	{
		Profile profile1 = createProfile();
		
		URL resetURL=null;
		try
		{
			resetURL = new URL("http://homefellas.com/resetpassword/");
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		userService.sendLostPasswordEmail(profile1.getMember(), profile1.getMember().generateUnquieId(), resetURL, Locale.ENGLISH);		
		List<Notification> notifications = notificationService.getNotificationsForINotificationTX(profile1.getMember());

		Assert.assertTrue(notifications.size()>1);
		
		Notification notification = notifications.get(0);
		Assert.assertEquals(profile1.getMember().getEmail(), notification.getSendTo());
		
//		String resetLink = notification.getBody().substring(notification.getBody().indexOf("http://homefellas.com/resetpassword"));
//		System.out.println(resetLink);
	}
	
	@Test 
	public void forgotPassword()
	{
		Profile profile1 = createProfile();
		
		URL resetURL=null;
		try
		{
			resetURL = new URL("http://homefellas.com/resetpassword");
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try
		{
			userService.forgotPassword("fakeEamil@reminded.me", resetURL);
			
			Assert.fail("Fake email test failed");
		}
		catch (ValidationException e)
		{
			Assert.assertEquals(e.getValidationErrors().get(0), UserValidationCodeEnum.MEMBER_NOT_FOUND);
		}
		
		try
		{
			userService.forgotPassword(null, resetURL);
			
			Assert.fail("null email test failed");
		}
		catch (ValidationException e)
		{
			Assert.assertEquals(e.getValidationErrors().get(0), UserValidationCodeEnum.EMAIL_NULL);
		}
		
		try
		{
			String ticket = userService.forgotPassword(profile1.getMember().getEmail(), resetURL);
			
			Assert.assertTrue(ticket!=null);
		}
		catch (ValidationException e)
		{
			Assert.fail("Validation exception should not happen");
		}
		
//		List<RegisterTicket> tickets = getAllRegisterTickets();
		assertRowCount(1, new RegisterTicket());
//		Assert.assertTrue(tickets.size()>0);
		
		List<Notification> notifications = notificationService.getNotificationsForINotificationTX(profile1.getMember());

		Assert.assertTrue(notifications.size()>1);
		
		Notification notification = notifications.get(0);
		Assert.assertEquals(profile1.getMember().getEmail(), notification.getSendTo());
	}
	
	@Test
	public void resetPassword()
	{
		Profile profile1 = createProfile();

	
		try
		{
			userService.resetPassword(new Member());
			Assert.fail();
		}
		catch (ValidationException e)
		{
			Assert.assertTrue(e.getValidationErrors().contains(UserValidationCodeEnum.EMAIL_NULL));
			Assert.assertTrue(e.getValidationErrors().contains(UserValidationCodeEnum.PASSWORD_NULL));
			Assert.assertTrue(e.getValidationErrors().contains(UserValidationCodeEnum.NO_TICKET_SET));
		}
		
		try
		{
			profile1.getMember().setTicket(null);
			userService.resetPassword(profile1.getMember());
			Assert.fail();
		}
		catch (ValidationException e)
		{
			Assert.assertTrue(e.getValidationErrors().contains(UserValidationCodeEnum.NO_TICKET_SET));
		}
		
		try
		{
			profile1.getMember().setTicket(profile1.getMember().generateUnquieId());
			userService.resetPassword(profile1.getMember());
			Assert.fail();
		}
		catch (ValidationException e)
		{
			Assert.assertTrue(e.getValidationErrors().contains(UserValidationCodeEnum.PASSWORD_RESET_MISMATCH));
		}
		
		URL resetURL=null;
		try
		{
			resetURL = new URL("http://homefellas.com/resetpassword");
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String newPassword = "newPassword";
		try
		{
			String ticket = userService.forgotPassword(profile1.getMember().getEmail(), resetURL);
			
			Assert.assertTrue(ticket!=null);
			
			profile1.getMember().setTicket(ticket);
			
			profile1.getMember().setPassword(newPassword);
			userService.resetPassword(profile1.getMember());
		}
		catch (ValidationException e)
		{
			Assert.fail("Validation exception should not happen");
		}
		
		Member member = getMember(profile1.getMember().getId());
		Assert.assertTrue(passwordEncoder.isPasswordValid(member.getCreditials(), newPassword, null));
	}
	
	@Transactional
	private Member getMember(String id)
	{
		return userDao.getMemberById(id);
	}
	
}
