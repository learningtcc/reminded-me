package com.homefellas.user;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.homefellas.model.core.TestModelBuilder;

public class UserTestModelBuilder extends TestModelBuilder
{

	public static Profile profile()
	{
		return profile("profile"+String.valueOf(new Random().nextInt(10000))+"@reminded.me");
	}
	
	public static String guestEmail()
	{
		return "guest"+String.valueOf(new Random().nextInt(10000))+"@reminded.me";
	}
	public static Profile profile(String email)
	{
		Profile profile = new Profile();
		profile.setName(factory.getStrategy().getStringValue());
		profile.generateUnquieId();
		Member member = new Member();
		member.setEmail(email);
		member.setPassword("password");
		profile.setMember(member);
		
		return profile;
		
	}
	
	public static Profile buildUser(boolean newUser, String email, PasswordEncoder passwordEncoder)
	{
		
		Role basicRole = buildRole(RoleEnum.HF_USER_ROLE);
		Role guestRole = buildRole(RoleEnum.GUEST);
		Set<Role> roles = new HashSet<Role>();
		roles.add(basicRole);
		roles.add(guestRole);
		
		return buildProfile(newUser, email, roles, passwordEncoder);
	}
	
	public static Profile buildBasicMember(boolean newUser, PasswordEncoder passwordEncoder)
	{
		return buildBasicMember(newUser,"user@homefellas.com", passwordEncoder);
	}
	public static Profile buildBasicMember(boolean newUser, String email, PasswordEncoder passwordEncoder)
	{
		Role basicRole = buildRole(RoleEnum.HF_USER_ROLE);
		Set<Role> roles = new HashSet<Role>();
		roles.add(basicRole);
		
		return buildProfile(newUser, email, roles, passwordEncoder);
	}
	
	public static Profile buildProfile(boolean newUser)
	{
		Profile profile = new Profile();
		profile.setName(factory.getStrategy().getStringValue());
		profile.generateUnquieId();
		newObjectCheck(profile, newUser);
		
		return profile;
	}

	public static Profile buildProfile(boolean newUser, String email, Set<Role> roles, PasswordEncoder passwordEncoder)
	{
		Profile member = new Profile();
		member.setName(factory.getStrategy().getStringValue());
		member.setMember(buildMember(newUser, email, roles, passwordEncoder));
		newObjectCheck(member, newUser);
		
		return member;
	}
	
	public static Role buildRole(RoleEnum authorizationEnum)
	{
		Role role = new Role();
		role.setRoleName(authorizationEnum.getRole());
		role.setId(authorizationEnum.getId());
		return role;
	}
	
	public static Member buildMember(boolean newUser, String email, Set<Role> roles, PasswordEncoder passwordEncoder)
	{
		Member authenication = new Member();
		authenication.setRoles(roles);
		authenication.setEmail(email);
		if (passwordEncoder!=null)
			authenication.setPassword(passwordEncoder.encodePassword("test12", null));
		else
			authenication.setPassword("test12");
		authenication.setTicket(factory.getStrategy().getStringValue());
		newObjectCheck(authenication, newUser);
		return authenication;
	}
	
	public static RegisterTicket buildCasRegisterTO()
	{
		RegisterTicket registerTicket = new RegisterTicket();
		registerTicket.setEmail(factory.getStrategy().getStringValue());
		registerTicket.setTicket(factory.getStrategy().getStringValue());
		return registerTicket;
	}
}
