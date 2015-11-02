package com.homefellas.user;

import java.util.List;

import org.hibernate.Query;
import org.springframework.security.core.userdetails.UserDetails;

import com.homefellas.dao.hibernate.core.HibernateCRUDDao;

public class UserDao extends HibernateCRUDDao implements IUserDao
{
	
	public UserDetails getUserDetailsById(String email)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("from Member authenication ");
		buffer.append("left join fetch authenication.roles ");
		buffer.append("where email=?");

		Query query = getQuery(buffer.toString());
		query.setString(0, email);
		return (UserDetails)extractSingleElement(query.list());
	}
	
	public Profile getProfileByEmail(String email)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("from Profile p ");
		buffer.append("where p.member.email=?");

		Query query = getQuery(buffer.toString());
		query.setString(0, email);
		return (Profile)extractSingleElement(query.list());
	}
	
	public Member getMemberByEmail(String email)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("from Member m ");
		buffer.append("where m.email=?");

		Query query = getQuery(buffer.toString());
		query.setString(0, email);
		return (Member)extractSingleElement(query.list());
	}
	
//	public User getUserByEmail(String email)
//	{
//		StringBuffer buffer = new StringBuffer();
//		buffer.append("from User u ");
//		buffer.append("where u.email=?");
//
//		Query query = getQuery(buffer.toString());
//		query.setString(0, email);
//		return (User)query.uniqueResult();
//	}
	
	public RegisterTicket getCasRegisterTO(String ticket, String email)
	{
		Query query = getQuery("from RegisterTicket r where r.ticket=? and r.email=?");
		query.setString(0, ticket);
		query.setString(1, email);
		return (RegisterTicket)extractSingleElement(query.list());
	}
	
	public Role getRole(RoleEnum authorizationEnum)
	{
		Query query = getQuery("from Role r where r.roleName=?");
		query.setString(0, authorizationEnum.getRole());
		
		return (Role)extractSingleElement(query.list());
	}
	
	public Profile getProfileByFaceBookId(String facebookId)
	{
		Query query = getQuery("from Profile p where p.facebookId=?");
		query.setParameter(0, facebookId);
		
		return (Profile)query.uniqueResult();
	}

	@Override
	public List<Profile> getAllProfiles()
	{
		return loadAllObjects(Profile.class);
	}

	@Override
	public Profile createProfile(Profile profile) {
		save(profile);
		
		return profile;
	}

	@Override
	public Profile updateProfile(Profile profile) {
		updateObject(profile);
		
		return profile;
	}

	@Override
	public Profile getProfileByID(String id) {
		return loadByPrimaryKey(Profile.class, id);
	}

	@Override
	public Member updateMember(Member member) {
		updateObject(member);
		
		return member;
	}

	@Override
	public ExtendedProfile createExtendedProfile(ExtendedProfile extendedProfile) {
		save(extendedProfile);
		
		return extendedProfile;
	}

	@Override
	public RegisterTicket createRegisterTicket(RegisterTicket registerTicket) {
		save(registerTicket);
		
		return registerTicket;
	}

	@Override
	public ExtendedProfile getExtendedProfileById(long id) {
		return loadByPrimaryKey(ExtendedProfile.class, id);
	}

	@Override
	public Member getMemberById(String id) {
		return loadByPrimaryKey(Member.class, id);
	}

	@Override
	public ExtendedProfile updateExtendProfile(ExtendedProfile extendedProfile) {
		updateObject(extendedProfile);
		
		return extendedProfile;
	}

	@Override
	public void removeRegisterTicket(RegisterTicket registerTicket) {
		deleteObject(registerTicket);
	}

	@Override
	public Role createRole(Role role) {
		save(role);
		
		return role;
	}

	@Override
	public Role getRoleById(long id) {
		return loadByPrimaryKey(Role.class, id);
	}
	
	
	
	
//	@Deprecated
//	public void updatePassword(final MemberTO memberTO, final String password)
//	{
////		getHibernateTemplate().execute(new HibernateCallback() {
////			   public Object doInHibernate(Session session) throws HibernateException, SQLException {
//		String hql = "update MemberTO set password = ? where id = ?";
//		Query query = getQuery(hql);
//		query.setString(0, password);
//		query.setLong(1, memberTO.getId());
//
//		query.executeUpdate();
////			   }});
//	}
}
