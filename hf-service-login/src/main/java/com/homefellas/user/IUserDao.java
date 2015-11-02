package com.homefellas.user;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;


public interface IUserDao
{

	//create
	public Profile createProfile(Profile profile);
	public ExtendedProfile createExtendedProfile(ExtendedProfile extendedProfile);
	public RegisterTicket createRegisterTicket(RegisterTicket registerTicket);
	public Role createRole(Role role);
	
	//read
	public UserDetails getUserDetailsById(String email);
	public RegisterTicket getCasRegisterTO(String ticket, String email);
	public Role getRole(RoleEnum authorizationEnum);
	public Profile getProfileByEmail(String email);
	public Member getMemberByEmail(String email);
	public Profile getProfileByFaceBookId(String facebookId);
	public List<Profile> getAllProfiles();
	public Profile getProfileByID(String id);
	public ExtendedProfile getExtendedProfileById(long id);
	public Member getMemberById(String id);
	public Role getRoleById(long id);
	
	//update
	public Profile updateProfile(Profile profile);
	public Member updateMember(Member member);
	public ExtendedProfile updateExtendProfile(ExtendedProfile extendedProfile);
	
	//delete 
	public void removeRegisterTicket(RegisterTicket registerTicket);
}
