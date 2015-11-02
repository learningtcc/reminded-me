package com.homefellas.user.login;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.homefellas.user.AbstractUserTestDao;
import com.homefellas.user.Member;
import com.homefellas.user.Profile;
import com.homefellas.user.UserService;


public class LoginServiceTest extends AbstractUserTestDao
{
	@Autowired
	private UserService userService;

	@PostConstruct
	protected void disableTGT()
	{
		userService.setGenerateTGT(false);
	}
	
	@Test
	public void testLoadUserByUsername() {
		
		Profile profile1 = createProfile();
		Member memeber1 = profile1.getMember();
		Profile profile3Guest1 = createGuest();
		Member member3Guest1 = profile3Guest1.getMember();
		
		Member authenication = (Member)userService.loadUserByUsername(memeber1.getUsername());
		assertEquals(memeber1.getUsername(),authenication.getEmail());

		assertTrue(authenication.getRoles().contains(roleUser));
		assertFalse(authenication.getRoles().contains(roleAdmin));
		
		authenication = (Member)userService.loadUserByUsername(member3Guest1.getUsername());
		assertEquals(member3Guest1.getUsername(),authenication.getEmail());
		assertTrue(authenication.getRoles().contains(roleUser));
		assertTrue(authenication.getRoles().contains(roleGuest));
		
	}
}
