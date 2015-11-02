package com.homefellas.user;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.codehaus.jackson.map.ObjectMapper;
import org.jasig.cas.client.util.CommonUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.homefellas.batch.Notification;
import com.homefellas.batch.NotificationService;
import com.homefellas.batch.NotificationTypeEnum;
import com.homefellas.batch.PushTypeEnum;
import com.homefellas.email.core.EmailTemplateEnum;
import com.homefellas.exception.DatabaseNotInitializedException;
import com.homefellas.exception.IValidationCode;
import com.homefellas.exception.ValidationException;
import com.homefellas.metrics.CollectTimeMetrics;
import com.homefellas.service.core.AbstractService;

public class UserService extends AbstractService 
{

	protected PasswordEncoder passwordEncoder;
	private SaltSource saltSource;
	protected IUserValidator userValidator;
	protected IUserDao userDao;
	
	@Autowired
	protected NotificationService notificationService;
	protected Ehcache roleCache;
	private String host = "http://reminded.me";
	private String oauthProfileUrl;
	private boolean generateTGT;

	
	
	public void setOauthProfileUrl(String oauthProfileUrl)
	{
		this.oauthProfileUrl = oauthProfileUrl;
	}



	public void setGenerateTGT(boolean generateTGT)
	{
		this.generateTGT = generateTGT;
	}

	

	public void setHost(String host)
	{
		this.host = host;
	}

	public void setSaltSource(SaltSource saltSource)
	{
		this.saltSource = saltSource;
	}

	public void setRoleCache(Ehcache roleCache)
	{
		this.roleCache = roleCache;
	}

	public void setUserDao(IUserDao userDao)
	{
		this.userDao = userDao;
	}


	public void setPasswordEncoder(PasswordEncoder passwordEncoder)
	{
		this.passwordEncoder = passwordEncoder;
	}

	public void setUserValidator(IUserValidator userValidator)
	{
		this.userValidator = userValidator;
	}

	// @Cacheable(cacheName="roleCache")
	public Role getRole(RoleEnum authorizationEnum)
	{
		Element element;
		if ((element = roleCache.get(authorizationEnum)) != null)
		{
			return (Role) element.getValue();
		}

		Role value = userDao.getRole(authorizationEnum);
		if (value != null)
		{
			roleCache.put(new Element(authorizationEnum, value));
		}
		return value;

	}

	protected Set<Role> getBasicMemberRoles() throws ValidationException
	{
		Set<Role> roles = new HashSet<Role>();
		roles.add(getRole(RoleEnum.HF_USER_ROLE));

		userValidator.validateRoles(roles);

		return roles;
	}

	protected Set<Role> getGuestRoles() throws ValidationException
	{
		Set<Role> roles = new HashSet<Role>();
		roles.add(getRole(RoleEnum.HF_USER_ROLE));
		roles.add(getRole(RoleEnum.GUEST));

		userValidator.validateRoles(roles);

		return roles;
	}

	// @Cacheable(cacheName="userCache")
	public UserDetails loadUserByUsername(String email)
			throws UsernameNotFoundException, DataAccessException
	{
		return userDao.getUserDetailsById(email);
	}

	@Transactional(readOnly = true)
	public Profile loadProfileById(String id)
	{
		return userDao.getProfileByID(id);
	}

	@Transactional(readOnly = true)
	public Member loadMemberById(String id)
	{
		return userDao.getMemberById(id);
	}
	
	@Transactional(readOnly = true)
	public Profile getProfileByEmail(String email)
	{
		return userDao.getProfileByEmail(email);
	}

	@Transactional
	public String forgotPassword(String principalId, URL resetLink)
			throws ValidationException
	{

		List<IValidationCode> errors = new ArrayList<IValidationCode>();
		if (UserValidator.isNullOrBlank(principalId))
		{
			errors.add(UserValidationCodeEnum.EMAIL_NULL);
			throw new ValidationException(errors);
		}

		Member fromDb = userDao.getMemberByEmail(principalId);
		if (fromDb == null)
		{
			errors.add(UserValidationCodeEnum.MEMBER_NOT_FOUND);
			throw new ValidationException(errors);
		}

		RegisterTicket registerTicket = new RegisterTicket();
		registerTicket.setEmail(principalId);
		registerTicket.setTicket(registerTicket.generateUnquieId());
		userDao.createRegisterTicket(registerTicket);

		sendLostPasswordEmail(fromDb, registerTicket.getTicket(), resetLink,
				Locale.ENGLISH);

		return registerTicket.getTicket();
	}

	@Transactional
	public boolean resetPassword(Member authenication)
			throws ValidationException
	{
		userValidator.validatePasswordReset(authenication);

		RegisterTicket casRegisterTO = userDao.getCasRegisterTO(
				authenication.getTicket(), authenication.getUsername());
		if (casRegisterTO != null)
		{
			Member retrievedUserTOFromDB = (Member) loadUserByUsername(authenication
					.getUsername());

			String password = passwordEncoder.encodePassword(
					authenication.getPassword(), null);
			retrievedUserTOFromDB.setCreditials(password);
			userDao.updateMember(retrievedUserTOFromDB);

			deleteCasRegisterTO(casRegisterTO);
			return true;
		}
		else
		{
			throw new ValidationException(
					UserValidationCodeEnum.PASSWORD_RESET_MISMATCH);
		}
	}

	void sendLostPasswordEmail(Member authenication, String ticket,
			URL resetLink, Locale locale)
	{

		if (locale == null)
			locale = Locale.ENGLISH;

		StringBuffer url = new StringBuffer(resetLink.toString());
		if (url.toString().endsWith("/"))
			url.replace(url.length() - 1, url.length(), "?");
		else
			url.append("?");
		url.append("email=");
		try
		{
			url.append(URLEncoder.encode(authenication.getEmail(), "UTF-8"));
		}
		catch (UnsupportedEncodingException e1)
		{
			url.append(authenication.getEmail());
		}
		url.append("&ticket=");
		url.append(ticket);

		final Map<String, Object> model = new HashMap<String, Object>();
		model.put("userTO", authenication);
		// try
		// {
		// model.put("resetLink", URLEncoder.encode(url.toString(), "UTF-8"));
		// }
		// catch (UnsupportedEncodingException e)
		// {
		// e.printStackTrace();
		// model.put("resetLink", url.toString());
		// }
		model.put("resetLink", url.toString());

		String emailBody = VelocityEngineUtils.mergeTemplateIntoString(
				getVelocityEngine(),
				getEmailTemplate(EmailTemplateEnum.FORGET_PASSWORD, locale),
				model);

		Notification notification = buildImmediateEmailNotification(
				getEmailSubject(EmailTemplateEnum.FORGET_PASSWORD,
						Locale.ENGLISH),
				getEmailFromAddress(EmailTemplateEnum.FORGET_PASSWORD,
						Locale.ENGLISH), Locale.ENGLISH, authenication,
				emailBody, authenication.getEmail());
		notificationService.scheduleNotification(notification);
		return;
	}

	void sendAdminJoinEmail(Profile member, Locale locale)
	{
		final Map<String, Object> model = new HashMap<String, Object>();
		model.put("userTO", member);

		String emailBody = VelocityEngineUtils.mergeTemplateIntoString(
				getVelocityEngine(),
				getEmailTemplate(EmailTemplateEnum.JOIN_CONFIRMATION_ADMIN,
						locale), model);

		Notification notification = buildImmediateEmailNotification(
				getEmailSubject(EmailTemplateEnum.JOIN_CONFIRMATION_ADMIN,
						Locale.ENGLISH),
				getEmailFromAddress(EmailTemplateEnum.JOIN_CONFIRMATION_ADMIN,
						Locale.ENGLISH),
				locale,
				member.getMember(),
				emailBody,
				getEmailToAddress(EmailTemplateEnum.JOIN_CONFIRMATION_ADMIN,
						locale));

		notificationService.scheduleNotification(notification);
	}

	public void sendJoinEmail(Profile member, Locale locale)
	{

		StringBuffer passwordResetURL = new StringBuffer(host);
		passwordResetURL.append("/reset-password?email=");
		passwordResetURL.append(member.getMember().getEmail());

		StringBuffer confirmationurl = new StringBuffer(host);
		if (confirmationurl.toString().endsWith("/"))
			confirmationurl.replace(confirmationurl.length() - 1,
					confirmationurl.length(), "");
		confirmationurl.append("/ticket/" + member.generateUnquieId());

		final Map<String, Object> model = new HashMap<String, Object>();
		model.put("userTO", member);
		model.put("confirmationLink", confirmationurl);
		model.put("passwordReset", passwordResetURL);
		String emailBody = VelocityEngineUtils.mergeTemplateIntoString(
				getVelocityEngine(),
				getEmailTemplate(EmailTemplateEnum.JOIN_CONFIRMATION, locale),
				model);

		Notification notification = buildImmediateEmailNotification(
				getEmailSubject(EmailTemplateEnum.JOIN_CONFIRMATION,
						Locale.ENGLISH),
				getEmailFromAddress(EmailTemplateEnum.JOIN_CONFIRMATION,
						Locale.ENGLISH), locale, member.getMember(), emailBody,
				member.getMember().getEmail());

		notificationService.scheduleNotification(notification);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public RegisterTicket createSSOTokenTX(String ticket, String accountId)
	{
		RegisterTicket casRegisterTO = new RegisterTicket(ticket, accountId);
		userDao.createRegisterTicket(casRegisterTO);
		
		return casRegisterTO;
	}

	@Transactional
	public RegisterTicket getCasRegisterTX(String ticket, String email)
	{
		return userDao.getCasRegisterTO(ticket, email);
	}

	@Transactional
	public void deleteCasRegisterTO(RegisterTicket casRegisterTO)
	{
		userDao.removeRegisterTicket(casRegisterTO);
	}

	@Transactional
	public Profile registerBasicMember(Profile profile)
			throws ValidationException, DatabaseNotInitializedException
	{
		userValidator.validateMember(profile);

		Profile profileFromDB = getProfileByEmail(profile.getMember()
				.getEmail());
		boolean convertUser = false;
		if (profileFromDB != null && profileFromDB.getMember() != null)
		{
			// check to see if the member is a guest
			if (!profileFromDB.getMember().isGuest())
			{
				// member is not a guest, so they need to choose a different
				// email address
				List<IValidationCode> errors = new ArrayList<IValidationCode>();
				errors.add(UserValidationCodeEnum.MEMBER_ID_TAKEN);
				throw new ValidationException(errors, profile);
			}
			else
			{
				// they are a guest, so mark to convert user
				convertUser = true;

			}
		}

		// encode password
		String unEncodedPassword = profile.getMember().getPassword();

		if (convertUser)
		{
			// set the new password
			profileFromDB.getMember().setCreditials(
					passwordEncoder.encodePassword(unEncodedPassword, null));

			// remove the guest role
			profileFromDB.getMember().removeGuestRole();

			//set the join date to be now
			profileFromDB.setJoinDate(new DateTime());
			
			// update the database with the change
			userDao.updateProfile(profileFromDB);

			postGuestUpgrade(profileFromDB);
			
			profile.setId(profileFromDB.getId());
		}
		else
		{
			profile.getMember().setCreditials(
					passwordEncoder.encodePassword(unEncodedPassword, null));

			Set<Role> roles = getBasicMemberRoles();
			if (roles == null || roles.isEmpty())
				throw new DatabaseNotInitializedException();

			// add roles
			profile.getMember().setRoles(roles);

			// create an id
			profile.generateGUIDKey();

			// copy the same id to member id
			profile.getMember().setId(profile.getId());

			//set the join date to be now
			profile.setJoinDate(new DateTime());
			
			// save member info
			userDao.createProfile(profile);

			postMemberRegister(profile);
		}

//		// create tgt
//		String ticket = createAndReturnTGTOnRegister(profile.getEmail());
//		profile.getMember().setTicket(ticket);

		// send join email
		sendJoinEmail(profile, Locale.ENGLISH);

		// send admin email
		sendAdminJoinEmail(profile, Locale.ENGLISH);

		return profile;
	}
	
	public Profile createTGT(Profile profile)
	{
		// create tgt
		String ticket = createAndReturnTGTOnRegister(profile.getEmail());
		profile.getMember().setTicket(ticket);
		
		return profile;
	}

	public Profile reigsterGuest(String email, String alias)
			throws ValidationException, DatabaseNotInitializedException
	{
		Profile profile = new Profile();

		// check to see if alias is passed
		if (alias == null || "".equals(alias))
			profile.setName(email);
		else
			profile.setName(alias);

		// create a member id
		profile.generateGUIDKey();

		// create member
		Member member = new Member();

		// set the email and profile id
		member.setEmail(email);
		member.setId(profile.getId());

		// create a password
		String password = profile.generateUnquieId();
		member.setCreditials(password);

		// grab the roles for a guest
		Set<Role> roles = getGuestRoles();
		if (roles == null || roles.isEmpty())
			throw new DatabaseNotInitializedException();
		member.setRoles(roles);

		// set the member
		profile.setMember(member);

		userDao.createProfile(profile);

		postGuestRegister(profile);

		return profile;
	}

	@Transactional
	@CollectTimeMetrics
	public ExtendedProfile addUserCommentsToExtendedProfileTX(
			ExtendedProfile extendedProfile) throws ValidationException
	{
		if (extendedProfile.getId() == 0)
			throw new ValidationException(
					UserValidationCodeEnum.CANNOT_UPDATE_WITHOUT_PK);

		ExtendedProfile extendProfileFromDB = userDao.getExtendedProfileById(extendedProfile.getId());
		extendProfileFromDB.setUserComments(extendedProfile.getUserComments());

		userDao.updateExtendProfile(extendProfileFromDB);

		return extendProfileFromDB;
	}

	@Transactional
	public ExtendedProfile addFriendsToExtendedProfile(
			ExtendedProfile extendedProfile) throws ValidationException
	{
		if (extendedProfile.getId() == 0)
			throw new ValidationException(
					UserValidationCodeEnum.CANNOT_UPDATE_WITHOUT_PK);

		ExtendedProfile extendProfileFromDB = userDao.getExtendedProfileById(extendedProfile.getId());
		if (extendProfileFromDB == null)
			throw new ValidationException(
					UserValidationCodeEnum.EXTENDED_PROFILE_DOES_NOT_EXIST);

		extendProfileFromDB.addAllFriends(extendedProfile.getFriendEmails());

		userDao.updateExtendProfile(extendProfileFromDB);

		for (FriendEmail friendEmail : extendedProfile.getFriendEmails())
			sendFriendInviteEmail(extendProfileFromDB.getProfile(),
					friendEmail, NotificationTypeEnum.EMAIL, Locale.ENGLISH);

		return extendProfileFromDB;
	}

	void sendFriendInviteEmail(Profile profile, FriendEmail friendEmails,
			NotificationTypeEnum notificationTypeEnum, Locale locale)
	{
		if (locale == null)
			locale = Locale.ENGLISH;

		final Map<String, Object> model = new HashMap<String, Object>();
		model.put("profile", profile);
		model.put("friend", friendEmails);

		String emailBody = VelocityEngineUtils.mergeTemplateIntoString(
				getVelocityEngine(),
				getEmailTemplate(EmailTemplateEnum.FRIEND_INVITE, locale),
				model);

		Notification notification = new Notification();
		notification.setBody(emailBody);
		notification.setSubject(getSubject(EmailTemplateEnum.FRIEND_INVITE,
				locale));
		notification.setSendFrom(getEmailFromAddress(
				EmailTemplateEnum.FRIEND_INVITE, locale));
		notification.setNotificationTypeOrdinal(notificationTypeEnum.ordinal());
		notification.setPushTypeOrdinal(PushTypeEnum.NONE.ordinal());

		notification.setSendTo(friendEmails.getFriendEmailAddress());
		notification.setToSendTime(System.currentTimeMillis());
		notification.setINotification(friendEmails);

		notificationService.scheduleNotification(notification);
		return;
	}

	@Transactional
	public ExtendedProfile createUpdateExtendedProfile(
			ExtendedProfile extendedProfile) throws ValidationException
	{
		userValidator.validateExtendedProfile(extendedProfile);

		String profileId = extendedProfile.getProfile().getId();
		Profile profile = userDao.getProfileByID(profileId);
		if (profile == null)
			throw new ValidationException(
					UserValidationCodeEnum.MEMBER_NOT_FOUND);

		if (extendedProfile.getInterests() != null)
		{
			for (Interest interest : extendedProfile.getInterests())
			{
				if (interest.getExtendedProfile() == null
						|| interest.getExtendedProfile().getId() == 0)
					interest.setExtendedProfile(extendedProfile);
			}
		}

		if (extendedProfile.getFriendEmails() != null)
		{
			for (FriendEmail friendEmail : extendedProfile.getFriendEmails())
			{
				if (friendEmail.getExtendedProfile() == null
						|| friendEmail.getExtendedProfile().getId() == 0)
					friendEmail.setExtendedProfile(extendedProfile);
			}
		}

		userDao.createExtendedProfile(extendedProfile);
		// if (extendedProfile.getId()==0)
		// dao.save(extendedProfile);
		// else
		// {
		// ExtendedProfile extendProfileFromDB =
		// dao.loadByPrimaryKey(ExtendedProfile.class, extendedProfile.getId());
		// dao.merge(extendedProfile);
		// }
		return extendedProfile;
	}

	@Transactional(readOnly = true)
	public Profile getProfile(String id)
	{
		return userDao.getProfileByID(id);
	}

	@Transactional
	public void confirmUser(String email)
	{
		Profile profile = userDao.getProfileByEmail(email);
		profile.setConfirmed(true);
		userDao.updateProfile(profile);
	}

	public boolean loginMember(String userName, String password)
	{
		Member userDetails = (Member)loadUserByUsername(userName);
		
		try
		{
			Object salt = null;
			if (this.saltSource != null)
			{
				salt = this.saltSource.getSalt(userDetails);
			}

			String presentedPassword = userDetails.getCreditials();
			if (presentedPassword.equals(password))
			{
				handleGuestLogin((Member) userDetails);
				return true;
			}
			else
			{
			
				if (passwordEncoder.isPasswordValid(presentedPassword, password,
						salt))
				{
					handleGuestLogin((Member) userDetails);
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			return false;
		}
	}

	void handleGuestLogin(Member member)
	{
		if (member.isGuest())
		{
			member.removeGuestRole();
			
		}
		member.setLastLoginDate(new DateTime());
		userDao.updateMember(member);
			
	}

	// @Override
	//
	//
	// public User getUserByEmail(String email)
	// {
	// return userDao.getUserByEmail(email);
	// }

	protected void postMemberRegister(Profile profile)
	{

	}

	protected void postGuestUpgrade(Profile profile)
	{

	}

	protected void postGuestRegister(Profile profile)
	{

	}

	private String createAndReturnTGTOnRegister(String email)
	{
		if (!generateTGT)
			return null;
		
		UUID uuid = UUID.randomUUID();
		String ticket = String.valueOf(uuid.getMostSignificantBits())+String.valueOf(uuid.getLeastSignificantBits());
		RegisterTicket registerTicket = createSSOTokenTX(ticket, email);
		
		StringBuffer registerURL = new StringBuffer(oauthProfileUrl);
		registerURL.append("/register/");
		registerURL.append(registerTicket.getTicket());
		registerURL.append("/");
		registerURL.append(registerTicket.getEmail());
		String serverResponse = CommonUtils.getResponseFromServer(
				registerURL.toString(), null);

		// need to check the response...if it has an error that probably means
		// the TGT is expired so redirect to login page
		if (!CommonUtils.isNotBlank(serverResponse) || serverResponse.contains("error"))
		{
			return null;
		}
		
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			CASResourceResponse casResourceResponse = mapper.readValue(serverResponse, CASResourceResponse.class);
			if (casResourceResponse==null)
				return null;
			
			return casResourceResponse.getTgt();
		}
		catch (Exception exception)
		{
			return null;
		}
		
	}

	@Transactional
	@CollectTimeMetrics
	public Profile updateProfileTX(Profile profile) throws ValidationException
	{
		userValidator.validateProfileUpdate(profile);


		Profile profileFromDB = userDao.getProfileByID(profile.getId());

		if (profile.getUserAttributes() != null)
		{
			profileFromDB.clearUserAttributes();
			for (UserAttribute userAttribute : profile.getUserAttributes())
			{
				if (!userAttribute.isPrimaryKeySet())
					userAttribute.generateGUIDKey();
				userAttribute.setProfile(profileFromDB);

				profileFromDB.addUserAttribute(userAttribute);
			}
		}

		if (profile.getName() != null && !profile.getName().equals(""))
			profileFromDB.setName(profile.getName());

		userDao.updateProfile(profileFromDB);

		return profile;
	}

	@Transactional
	@CollectTimeMetrics
	public Profile getProfileByFaceBookIdTX(String facebookId)
	{
		return getProfileByFaceBookId(facebookId);
	}

	public Profile getProfileByFaceBookId(String facebookId)
	{
		return userDao.getProfileByFaceBookId(facebookId);
	}

	@CollectTimeMetrics
	@Transactional
	public Profile getProfileOrRegisterFaceBookUserTX(OAuthModel authModel)
			throws ValidationException, DatabaseNotInitializedException
	{
		if (authModel == null || authModel.getId() == null
				|| authModel.getId().equals(""))
			throw new ValidationException(
					UserValidationCodeEnum.USER_NOT_LOGGED_INTO_FACEBOOK);

		// get my fb id
		Profile profile = getProfileByFaceBookId(authModel.getId());

		//check to see if they have logged in with facebook before
		if (profile == null)
		{
			// we need to see if the user is already in the system
			profile = getProfileByEmail(authModel.getEmail());
			if (profile != null)
			{
				//user was entered in the system and is now logging in with facebook id
				profile.increamentFacebookLogins();
				profile.setFacebookId(authModel.getId());
				userDao.updateProfile(profile);

				return profile;
			}
			profile = new Profile();
			profile.increamentFacebookLogins();
			profile.setFacebookId(authModel.getId());
			profile.setName(authModel.getFirstName() + " "
					+ authModel.getLastName());
			Member member = new Member();
			member.setEmail(authModel.getEmail());
			String fakePassword = member.generateUnquieId();
			member.setCreditials(fakePassword);
			member.setPassword(fakePassword);
			profile.setMember(member);
			registerBasicMember(profile);
		}
		else
		{
			//at least second login on facebook
			profile.increamentFacebookLogins();
			userDao.updateProfile(profile);
		}
		
		return profile;
	}

	//
	// public Profile updateProfile(Profile profile) throws ValidationException
	// {
	// Profile profileFromDB = getProfile(profile.getId());
	//
	//
	// }
	//
}
