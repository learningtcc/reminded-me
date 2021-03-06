package com.homefellas.user.join;

import static com.homefellas.user.UserTestModelBuilder.buildBasicMember;
import static com.homefellas.user.UserTestModelBuilder.buildCasRegisterTO;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.homefellas.exception.DatabaseNotInitializedException;
import com.homefellas.exception.ValidationException;
import com.homefellas.user.AbstractUserTestDao;
import com.homefellas.user.ExtendedProfile;
import com.homefellas.user.ExtendedProfile.HowDidYouHearAboutUsEnum;
import com.homefellas.user.ExtendedProfile.PersonTypeEnum;
import com.homefellas.user.FriendEmail;
import com.homefellas.user.Interest;
import com.homefellas.user.Profile;
import com.homefellas.user.RegisterTicket;
import com.homefellas.user.UserService;
import com.homefellas.user.UserValidationCodeEnum;


public class JoinServiceTest extends AbstractUserTestDao
{
	@Autowired
	private UserService userService;
	
	@PostConstruct
	protected void disableTGT()
	{
		userService.setGenerateTGT(false);
	}
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	public void testRegisterBasicMember()
	{
		//Test 1 try and create a user that is non-sso
		Profile member = buildBasicMember(true,"user@homefellas.com",passwordEncoder);
		String oldEmail = member.getMember().getUsername();
		String rawpassword = member.getMember().getPassword();
		try
		{
			userService.registerBasicMember(member);
		}
		catch (ValidationException exception)
		{
			Assert.fail();
		}
		catch (DatabaseNotInitializedException databaseNotInitializedException)
		{
			Assert.fail();
		}
		
//		profileCounter++;
//		userCounter++;
		
//		dao.flush();
		
		assertEquals(0, super.countRowsInTable("u_registerTickets"));
//		assertEquals(profileCounter, super.countRowsInTable("u_profiles"));
//		assertEquals(userCounter, super.countRowsInTable("u_members"));
		Assert.assertTrue(member.isPrimaryKeySet());
		
		Profile classUnderTest = userService.getProfile(member.getId());
		assertEquals(member, classUnderTest);
		
		Assert.assertTrue(classUnderTest.getMember().getRoles().contains(roleUser));
		Assert.assertFalse(classUnderTest.getMember().getRoles().contains(roleAdmin));
		
		Assert.assertTrue(passwordEncoder.isPasswordValid(classUnderTest.getMember().getCreditials(), rawpassword, null));
		//Test 2 make sure that user fails on id
		member = buildBasicMember(true,"user@homefellas.com",passwordEncoder);
		member.getMember().setEmail(oldEmail);
		try
		{
			userService.registerBasicMember(member);
			
			Assert.fail();
		}
		catch (ValidationException exception)
		{
			Assert.assertTrue(true);
			Assert.assertTrue(exception.getValidationErrors().contains(UserValidationCodeEnum.MEMBER_ID_TAKEN));
			Assert.assertFalse(exception.getValidationErrors().contains(UserValidationCodeEnum.NO_TICKET_SET));
		}
		catch (DatabaseNotInitializedException databaseNotInitializedException)
		{
			Assert.fail();
		}
		
		//state of table should be the same
		assertEquals(0, super.countRowsInTable("u_registerTickets"));
//		assertEquals(profileCounter, super.countRowsInTable("u_profiles"));
//		assertEquals(userCounter, super.countRowsInTable("u_members"));
		Assert.assertTrue(member.isPrimaryKeySet());
		
		//Test 3 check for SSO and null tickets
		member = buildBasicMember(true,"user@homefellas.com",passwordEncoder);
		member.getMember().setTicket(null);
		try
		{
			userService.registerBasicMember(member);
			
			Assert.fail();
		}
		catch (ValidationException exception)
		{
			Assert.assertTrue(true);
			Assert.assertTrue(exception.getValidationErrors().contains(UserValidationCodeEnum.MEMBER_ID_TAKEN));
//			Assert.assertTrue(exception.getValidationErrors().contains(UserValidationCodeEnum.NO_TICKET_SET));
		}
		catch (DatabaseNotInitializedException databaseNotInitializedException)
		{
			Assert.fail();
		}
		
		//state of table should be the same
		assertEquals(0, super.countRowsInTable("u_registerTickets"));
//		assertEquals(profileCounter, super.countRowsInTable("u_profiles"));
//		assertEquals(userCounter, super.countRowsInTable("u_members"));
		Assert.assertTrue(member.isPrimaryKeySet());
		
		//Test 4 SSO join
		member = buildBasicMember(true,"usersdfsdf2@homefellas.com",passwordEncoder);
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
	}
	
	
	@Test
	public void testCreateSSOToken() {
		RegisterTicket casRegisterTO = buildCasRegisterTO();
		
		userService.createSSOTokenTX(casRegisterTO.getTicket(), casRegisterTO.getEmail());
		
		assertEquals(1, super.countRowsInTable("u_registerTickets"));
	}
	
	@Test
	public void testCreateUpdateExtendedProfile()
	{
		Profile profile1 = createProfile();
		
		ExtendedProfile extendedProfile = new ExtendedProfile();
		String affilication = "University of Delaware";
		extendedProfile.setAffiliation(affilication);
		
		//step 2 in join process, create the extended profile and add one friend
		Set<Interest> interests = new HashSet<Interest>();
		Interest skiing = new Interest();
		skiing.setInterestName("Skiing");
		Interest softball = new Interest();
		softball.setInterestName("Softball");
		interests.add(skiing);
		interests.add(softball);
		extendedProfile.addFriendEmail("test2222@homefellas.com");
		extendedProfile.setHowDidYouHearAboutUs(HowDidYouHearAboutUsEnum.FRIEND_TOLD_ME.ordinal());
		extendedProfile.setInterests(interests);
		extendedProfile.setPersonType(PersonTypeEnum.OTHER.ordinal());
		extendedProfile.setProfile(profile1);
		
		int friendCounter=1;
		try
		{
			extendedProfile = userService.createUpdateExtendedProfile(extendedProfile);
		}
		catch (ValidationException exception)
		{
			Assert.fail(exception.getMessage());
		}
		long id = extendedProfile.getId();
		Assert.assertEquals(1, countRowsInTable(extendedProfile.getTableName()));
		Assert.assertEquals(2, countRowsInTable(new Interest().getTableName()));
		Assert.assertEquals(friendCounter, countRowsInTable(new FriendEmail().getTableName()));
		Assert.assertTrue(id>0);
		
		//step 3, add more friends.  
		extendedProfile = new ExtendedProfile();
		extendedProfile.setId(id);
		extendedProfile.setProfile(profile1);
		extendedProfile.addFriendEmail("test123@homefellas.com");
		extendedProfile.addFriendEmail("test345@homefellas.com");
		extendedProfile.addFriendEmail("test678@homefellas.com");
		friendCounter+=3;
		try
		{
			extendedProfile = userService.addFriendsToExtendedProfile(extendedProfile);
		}
		catch (ValidationException exception)
		{
			Assert.fail(exception.getMessage());
		}
		
		assertRowCount(friendCounter, new FriendEmail());
		assertEquals(affilication, extendedProfile.getAffiliation());
		assertEquals(id, extendedProfile.getId());
		
		//step 4 add comments
		extendedProfile = new ExtendedProfile();
		extendedProfile.setId(id);
		extendedProfile.setProfile(profile1);
		String userComments = "This is the best product I've ever seen.  When will this be live?";
		extendedProfile.setUserComments(userComments);
		try
		{
			extendedProfile = userService.addUserCommentsToExtendedProfileTX(extendedProfile);
		}
		catch (ValidationException exception)
		{
			Assert.fail(exception.getMessage());
		}
		
		Assert.assertTrue(extendedProfile.getInterests().contains(softball));
		Assert.assertTrue(extendedProfile.getInterests().contains(skiing));
		assertEquals(friendCounter, extendedProfile.getFriendEmails().size());
		assertEquals(userComments, extendedProfile.getUserComments());
		assertEquals(id, extendedProfile.getId());
	}
}
