package com.homefellas.rm.user;

import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import com.homefellas.exception.ValidationException;
import com.homefellas.user.ExtendedProfile;
import com.homefellas.user.IUserServiceTX;
import com.homefellas.user.Member;
import com.homefellas.user.OAuthModel;
import com.homefellas.user.Profile;
import com.homefellas.ws.core.AbstractRMWebService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiError;
import com.wordnik.swagger.annotations.ApiErrors;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Path("/users")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Api(value="/users", description="These are web service that are assiocated with user actions.  This includes pps and contacts as well")
public class UserWebService extends AbstractRMWebService {

	@Autowired
	protected IUserServiceTX userService;
	
	@Autowired
	protected IContactService contactService;
	
	@Autowired 
	protected IPersonalPointScoreService personalPointScoreService;
	
	protected String appBaseUrl;
	
	
	/***************************************************************************************
	 * 
	 * START METHODS
	 * 
	 ****************************************************************************************/

	@POST
	@Path("/import")
	@PreAuthorize("hasRole('ROLE_HF_USER')")
	@ApiOperation(value="This will allow you to import contacts from other sources.", notes="This method accepts a list of Contacts.  The contact object in the list nneds to be populate" +
			"based on the contact attrbite.  So for instance, if you are setting email it will be contact.member.email.  The origin of the import should also be set as a string.")
	public Response importContacts(List<Contact> contacts)
	{
		String loggedInProfileEmail = getEmailFromSecurityContext();
		try
		{
			contactService.importContacts(contacts, loggedInProfileEmail);
			return buildSuccessResponse(true);
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	
	@GET
	@Path("/pps/latest")
	@PreAuthorize("hasRole('ROLE_HF_USER')")
	@ApiOperation(value="This will get the latest pps for today.", notes="The PPS object contains a score from today as well as yesterday.  " +
			"But of these values are not touched by the server and it is responsible for the client to maintain.", responseClass="com.homefellas.rm.user.PersonalPointScore")
	@ApiErrors(value={@ApiError(code=403, reason="Not logged in")})		
	public Response lastestPPS()
	{
		String loggedInProfileEmail = getEmailFromSecurityContext();
		try
		{
			return buildSuccessResponse(personalPointScoreService.getLatestPersonalPointScoresTX(loggedInProfileEmail));
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	

	@GET
	@Path("/pps/range/{start}/{end}")
	@PreAuthorize("hasRole('ROLE_HF_USER')")
	@ApiOperation(value="This will get a range of pps for two dates.", notes="The PPS object contains a score from today as well as yesterday.  " +
			"But of these values are not touched by the server and it is responsible for the client to maintain.", responseClass="com.homefellas.rm.user.PersonalPointScore")
	@ApiErrors(value={@ApiError(code=403, reason="Not logged in")})
	public Response ppsRange(
			@ApiParam(value = "The date you want to start the range on.", allowableValues = "date", required = true)
			@PathParam("start")String start, 
			@ApiParam(value = "The date you want to end the range on.", allowableValues = "date", required = true)
			@PathParam("end")String end)
	{
		String loggedInProfileEmail = getEmailFromSecurityContext();
		try
		{
			return buildSuccessResponse(personalPointScoreService.getPersonalPointScoreByDateRangeTX(loggedInProfileEmail, start, end));
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	@GET
	@Path("/pps/single/{start}")
	@PreAuthorize("hasRole('ROLE_HF_USER')")
	public Response ppsRange(@PathParam("start")String start)
	{
		String loggedInProfileEmail = getEmailFromSecurityContext();
		try
		{
			return buildSuccessResponse(personalPointScoreService.getPersonalPointScoreByDateTX(loggedInProfileEmail, start));
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	
	@GET
	@Path("/search/{term}")
	@PreAuthorize("hasRole('ROLE_HF_USER')")
	public Response searchForContacts(@PathParam("term")String searchTerm)
	{
		String loggedInProfileEmail = getEmailFromSecurityContext();
		try
		{
			return buildSuccessResponse(contactService.searchForContactsTX(loggedInProfileEmail, searchTerm));
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	@GET
	@Path("/group/{max_results}")
	@PreAuthorize("hasRole('ROLE_HF_USER')")
	public Response getGroupContactsForUser(@PathParam("max_results")@DefaultValue("0")int maxResults)
	{
		String loggedInProfileEmail = getEmailFromSecurityContext();
		try
		{
			return buildSuccessResponse(contactService.getGroupContactsForUserTX(loggedInProfileEmail, maxResults));
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	@GET
	@Path("/popular/{max_results}")
	@PreAuthorize("hasRole('ROLE_HF_USER')")
	public Response getPopularContactsForUser(@PathParam("max_results")@DefaultValue("0")int maxResults)
	{
		String loggedInProfileEmail = getEmailFromSecurityContext();
		try
		{
			return buildSuccessResponse(contactService.getPopularContactsForUserTX(loggedInProfileEmail, maxResults));
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	@GET
	@Path("/popular/{email}/{max_results}")
	public Response getPopularContactsForAnotherUser(@PathParam("email")String email, @PathParam("max_results")@DefaultValue("0")int maxResults)
	{
		String loggedInProfileEmail = getEmailFromSecurityContext();
		try
		{
			return buildSuccessResponse(contactService.getPopularContactsForUserTX(email, maxResults));
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	@POST
	@Path("/facebook")
	public Response getProfileOrRegisterFaceBookUser(OAuthModel authModel)
	{
		try
		{
			Profile profile = userService.getProfileOrRegisterFaceBookUserTX(authModel);
			profile = userService.createTGT(profile);
			return buildSuccessResponse(profile);
		}
		catch (ValidationException validationException)
		{
			return handleValidationException(validationException);
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	@POST
	@Path("/update")
	public Response updateProfile(Profile profile)
	{
		try
		{
			return buildSuccessResponse(userService.updateProfileTX(profile));
		}
		catch (ValidationException validationException)
		{
			return handleValidationException(validationException);
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	/**
	 * This will register a member.  The server will create the memberId.  
	 * @param name is required.  member.password is required. member.email is required. member.roles will be set by server.
	 * @return Returns the created profile. profile.id is the same as member.id    
	 */
	@POST
	@Path("/register")
	public Response registerMember(@Context HttpServletRequest req, Profile member) {
		try
		{
			String ipAddress = req.getRemoteAddr();
			member.setJoinIpAddress(ipAddress);
			Profile returnedProfile = userService.registerBasicMemberTX(member);
			returnedProfile = userService.createTGT(returnedProfile);
			return buildSuccessResponse(returnedProfile);
		}
		catch (ValidationException validationException)
		{
			return handleValidationException(validationException);
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	/**
	 * This will either create or update an extended profile.  Note that if you are updated an extend profile, what ever you pass
	 * will trump whatever is in the database.  So if this is a true update, make sure you pass everything that needs to be persisted.
	 * The system will generate a pk for this object.  Also, a profile.id is required to be set.  If it is not then a validation error
	 * will be thrown.  The profile.id must be a valid user or a validation error will be thrown.  This is meant to be called during 
	 * step 2 of the join process
	 * @param extendedProfile All attributes are optional except for the profile.id.  This must be a valid profile id.
	 * @return This will return the extendedProfile that was create.  The id will be populate with the pk to the object
	 */
	@POST
	@Path("/extendedprofile/createupdate")
	public Response createUpdateExtendProfile(ExtendedProfile extendedProfile)
	{
		try
		{
			extendedProfile = userService.createUpdateExtendedProfile(extendedProfile);
			return buildSuccessResponse(extendedProfile);
		}
		catch (ValidationException validationException)
		{
			return handleValidationException(validationException);
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}

	/**
	 * This will add friends to an existing extendprofile.  It will basiclly merge the set of friends passed in with the extended
	 * profile from the database.
	 * @param extendedProfile Id is required.  This must be a valid extendProfile.id.  friendsEmails isn't required, but if it not passed then this method will not do anything
	 * @return
	 */
	@POST
	@Path("/extendedprofile/addfriends")
	public Response addFriendsEmailToExtendProfile(ExtendedProfile extendedProfile)
	{
		try
		{
			extendedProfile = userService.addFriendsToExtendedProfile(extendedProfile);
			return buildSuccessResponse(extendedProfile);
		}
		catch (ValidationException validationException)
		{
			return handleValidationException(validationException);
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	/**
	 * This will add user comments to an existing extendprofile.  It will basiclly merge the user comments passed in with the extended
	 * profile from the database.
	 * @param extendedProfile Id is required.  This must be a valid extendProfile.id.  userComments isn't required, but if it not passed then this method will not do anything
	 * @return
	 */
	@POST
	@Path("/extendedprofile/addcomments")
	public Response addUserCommentsToExtendProfile(ExtendedProfile extendedProfile)
	{
		try
		{
			extendedProfile = userService.addUserCommentsToExtendedProfileTX(extendedProfile);
			return buildSuccessResponse(extendedProfile);
		}
		catch (ValidationException validationException)
		{
			return handleValidationException(validationException);
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	/**
	 * This will get the user's profile based on the profile id.  This is the pk to the user and the member tables.
	 * @param profileid is required.  The id must be valid.
	 * @param id
	 * @return
	 */
	@GET
	@Path("/profile/get/{profileid}")
	public Response getProfile(@PathParam("profileid")String id)
	{
		try
		{
			Profile profile = userService.getProfile(id);
			return buildSuccessResponse(profile);
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	/**
	 * This is the url that is called when a user wants to reset their password.  You need to pass the email address of the user along
	 * with the base url of where the user will go to.  So what will happen when you call this method is that a ticket will be created
	 * in the database for the id that was passed and an email will be sent to the user with a reset url link in it.  This reset url
	 * will be the one that is passed with the email and ticket appended to the end.  So for instance if you pass http://reminded.me/passwordreset
	 * A email will be sent to the user with a link for htpp://reminded.me/passwordreset?email=test@reminded.me&ticket=asdjhfsdkjhfsd
	 * It is up to the client to program for this and parse the email ahnd ticket out of the url.  These values must be passed to the 
	 * reset password ws.
	 * @param email A valid email is required
	 * @param resetBaseUrl base url for where hte user will be sent via the reset password link
	 * @return
	 */
	@GET
	@Path("/forgot/password")
	public Response forgetPassword(@QueryParam("email")String email, @QueryParam("resetBaseUrl")String resetBaseUrl)
	{
		try
		{
			URL resetLink = new URL(resetBaseUrl);			
			return buildSuccessResponse(userService.forgotPassword(email, resetLink), MediaType.TEXT_HTML);
		}
		catch (ValidationException validationException)
		{
			return handleValidationException(validationException);
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	@GET
	@Path("/resetpassword")
	public Response resetPassword(@QueryParam("email")String email, @QueryParam("ticket")String ticket,@QueryParam("password")String password)
	{
		Member member = new Member();
		member.setTicket(ticket);
		member.setEmail(email);
		member.setPassword(password);
		
		try
		{
			userService.resetPassword(member);
			return buildSuccessResponse(true);
		}
		catch (ValidationException validationException)
		{
			return handleValidationException(validationException);
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	/**
	 * This method will reset the users password.  You are required to pass member.email, member.password, and member.ticket.  The email
	 * must be the validate email address in our system.  The password is the users's new password.  The ticket is the value that is 
	 * passed from the forget password email.  See the forget password method for more details about that.
	 * @param member email, password, and ticket must be set in this object otherwise an error will be thrown
	 * @return
	 */
	@POST
	@Path("/reset/password")
	public Response resetPassword(Member member)
	{
		try
		{
			userService.resetPassword(member);
			return buildSuccessResponse(true);
		}
		catch (ValidationException validationException)
		{
			return handleValidationException(validationException);
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	@GET
	@Path("/confirmation/email/{email}/ticket/{ticket}")
	public Response confirmProfile(@PathParam("email")String email, @PathParam("ticket")String ticket)
	{
		try
		{
			userService.confirmUser(email);
			return buildSuccessResponse(true);
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
}
