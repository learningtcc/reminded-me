package com.homefellas.rm.share;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.security.access.prepost.PreAuthorize;

import com.homefellas.batch.PushTypeEnum;
import com.homefellas.exception.ValidationException;
import com.homefellas.rm.calendar.Calendar;
import com.homefellas.rm.task.Task;
import com.homefellas.user.Member;
import com.homefellas.ws.core.AbstractRMWebService;

@Path("/share")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class ShareWebService extends AbstractRMWebService
{
	
	@POST
	@Path("/publish/calendar/store/apple")
	@PreAuthorize("hasRole('ROLE_HF_USER')")
	public Response publishCalendarToAppleStore(Calendar calendar)
	{
		try
		{
			String loggedInEmail = getEmailFromSecurityContext();
			return buildSuccessResponse(shareService.publishCalendarToStore(calendar, PushTypeEnum.APPLE, loggedInEmail));
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
	@Path("/list/accept/sharecaledarid/{id}")
	@PreAuthorize("hasRole('ROLE_HF_USER')")
	public Response acceptListShare(@PathParam("id")String shareCalendarId)
	{
		try
		{
			String loggedInEmail = getEmailFromSecurityContext();
			return buildSuccessResponse(shareService.acceptDeclineShareCaledarTX(shareCalendarId, ShareApprovedStatus.APPROVED, loggedInEmail));
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
	@Path("/list/decline/sharecaledarid/{id}")
	@PreAuthorize("hasRole('ROLE_HF_USER')")
	public Response declineListShare(@PathParam("id")String shareCalendarId)
	{
		try
		{
			String loggedInEmail = getEmailFromSecurityContext();
			return buildSuccessResponse(shareService.acceptDeclineShareCaledarTX(shareCalendarId, ShareApprovedStatus.DECLINED, loggedInEmail));
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
	@Path("/message")
	public Response sendNotificationsToShares(Invite invite) throws ValidationException
	{
		try
		{
			shareService.messageSharesTX(invite);
			return buildSuccessResponse(invite);
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
	@Path("/invite/update")
	public Response updateInvite(Invite invite)
	{
		try
		{
			invite = shareService.updateInviteTX(invite);
			return buildSuccessResponse(invite);
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
	public Response updateShare(Share share)
	{
		try
		{
			share = shareService.updateShareTX(share);
			return buildSuccessResponse(share);
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
	@Path("/bulk/{ids}")
	public Response getBulkShares(@PathParam("ids")String ids)
	{
		try
		{
			return buildSuccessResponse(shareService.getBulkSharesTX(ids));
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	@GET
	@Path("/calendars/bulk/{ids}")
	public Response getBulkShareCalendar(@PathParam("ids")String ids)
	{
		try
		{
			return buildSuccessResponse(shareService.getBulkShareCalendarsTX(ids));
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	@GET
	@Path("/invite/bulk/{ids}")
	public Response getBulkInvites(@PathParam("ids")String ids)
	{
		try
		{
			return buildSuccessResponse(shareService.getBulkInvitesTX(ids));
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	/**
	 * This web service to mark a share as being accepted.  You must pass the taskid and the member id of the share you want to create.
	 * This will look up the task and member.  Both need to be found or it will error out.  If there is already a share created for this
	 * member and task, it will update the share as accepted.  If not, and the task is marked as public, it will create a new share and 
	 * mark the new share as acceptd.  If the task is private it will error
	 * @return share
	 */
	@GET
	@Path("/accept/taskid/{taskid}/memberid/{memberid}")
	public Response acceptShare(@PathParam("taskid")String taskid, @PathParam("memberid")String memberid)
	{
		try
		{
			return buildSuccessResponse(shareService.acceptPublicShareTX(taskid, memberid));
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	/**
	 * This will return a share based on a task id and a member id.  There is no validation check to see if they are valid.
	 * @param a valid taskid
	 * @param a valid memberid
	 * @return a share
	 */
	@GET
	@Path("/get/taskid/{taskid}/memberid/{memberid}")
	public Response getShareForUserAndTask(@PathParam("taskid")String taskid, @PathParam("memberid")String memberid)
	{
		try
		{
			Task task = new Task();
			task.setId(taskid);
			Member user = new Member(memberid);
			return buildSuccessResponse(shareService.getShareForUserAndTaskTX(task, user));
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	/**
	 * This method will return a list of invites that have been recieved by the valid user id.
	 * @param a valid member id memberId
	 * @return a list of invites
	 */
	@GET
	@Path("/invites/received/memberid/{memberid}")
	public Response getRecievedInvitesForMember(@PathParam("memberid")String memberid)
	{
		try
		{
			return buildSuccessResponse(shareService.getInvitesSharedWithEmailTX(memberid));
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	/**
	 * This will get the invites for a member.  IT will return a list of invites.  The invites can then be 
	 * parsed to get the task id and deep linked too.
	 * @param a valid member id memberId
	 * @return a list of invites
	 */
	@GET
	@Path("/invites/sent/memberid/{memberid}")
	public Response getSentInvitesForMember(@PathParam("memberid")String memberId)
	{
		try
		{
			return buildSuccessResponse(shareService.getSentInvitesForMemberTX(memberId));
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	/**
	 * This method will delete a share.
	 * @param a valid shareId is return
	 * @return true if successful
	 */
	@GET
	@Path("/delete/id/{shareid}")
	public Response deleteShare(@PathParam("shareid")String shareId)
	{
		try
		{
			Share share = new Share();
			share.setId(shareId);
			shareService.deleteShareTX(share);
			return buildSuccessResponse(true);
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
			
	/**
	 * This method will return the shares for a user.  It can be used for a logged in user to determine which shares have been shared
	 * with the user
	 * @param a valid userId is required
	 * @return a list of shares
	 */
	@GET
	@Path("/get/memberid/{userid}")
	public Response getSharesForUser(@PathParam("userid")String userId)
	{
		try
		{
			Member member = new Member();
			member.setId(userId);
			return buildSuccessResponse(shareService.getSharesForUserTX(member));
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	/**
	 * This ws will mark a share as being viewed.  This should be called when a private task has been viewed by a non-logged 
	 * in user.  If the share.viewed flag is marked as true, then the client should blur the task.
	 * @return Share
	 */
	@GET
	@Path("/viewed/id/{shareid}")
	public Response shareViewed(@PathParam("shareid")String shareId)
	{
		try
		{
			return buildSuccessResponse(shareService.shareViewedTX(shareId));
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}

	/**
	 * This web service will return all the shares for a task.  You loop through the shares and see if they have been
	 * shared or not.   You must pass a valid shareId;
	 * @return List<Share>
	 */
	@GET
	@Path("/get/taskid/{taskid}")
	public Response getSharesForTasks(@PathParam("taskid")String taskid)
	{
		try
		{
			Task task = new Task();
			task.setId(taskid);
			return buildSuccessResponse(shareService.getSharesForTaskTX(task));
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	/**
	 * This web service to mark a share as being accepted.  You must pass the share id into the web service.
	 * @return share
	 */
	@GET
	@Path("/accept/shareid/{shareid}")
	public Response acceptShareOld(@PathParam("shareid")String shareid)
	{
		try
		{
			return buildSuccessResponse(shareService.acceptShareTX(shareid));
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
	 * This web service to mark a share as being decline.  You must pass the share id into the web service.
	 * @return share
	 */
	@GET
	@Path("/decline/shareid/{shareid}")
	public Response declineShare(@PathParam("shareid")String shareid)
	{
		try
		{
			return buildSuccessResponse(shareService.declineShareTX(shareid));
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	/**
	 * This method will share a task.  An email will be sent out to all the users that are listed and it will
	 * contain a link to the task.  subject, message, shareId (which should be the taskId), and an array of email
	 * addresses is required to be passed in the invite model.  
	 * @param invite subject, message, shareId (which should be the taskId), and an array of email addresses is required
	 * @return invite
	 * @see Invite
	 */
	@POST
	@Path("/task")
	public Response shareTask(Invite invite)
	{
		try
		{
			shareService.shareTaskTX(invite);
			return buildSuccessResponse(invite);
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
	@Path("/list")
	public Response shareCalendar(Invite invite)
	{
		try
		{
			return buildSuccessResponse(shareService.shareCalenderTX(invite));
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
	@Path("/get/direct/calendar/id/{calendarid}")
	public Response getPublicCalendarShareDirectLink(@Context HttpServletRequest request, @PathParam("calendarid")String calendarid)
	{ 
		String email = getEmailFromSecurityContext();
		try
		{
			return buildSuccessResponse(shareService.getTasksByCalendarDirectLinkTX(calendarid, email));
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
	@Path("/get/direct/calendar/id/{calendarid}/email/{email}")
	public Response getPrivateCalendarShareDirectLink(@Context HttpServletRequest request, @PathParam("calendarid")String calendarid, @PathParam("email")String email)
	{
		try
		{
			return buildSuccessResponse(shareService.getTasksByCalendarDirectLinkTX(calendarid, email));
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
	 * This will return a single task based on the task id.  This method will check to see if the requester is allowed to
	 * access the task by running through the rules.  If a user is logged in it will grab their profile from session and send
	 * it to the service.  If the member is not logged in, then null will be passed to the service.
	 * @param taskId A valid taskId is required
	 * @return Share
	 */
	@GET
	@Path("/get/direct/id/{taskId}")
	public Response getPublicShareDirectLink(@Context HttpServletRequest request, @PathParam("taskId")String taskId)
	{
//		String email = getMemberEmailFromSession(request);
		String email = getEmailFromSecurityContext();
		try
		{
			return buildSuccessResponse(shareService.getShareDirectLinkTX(taskId, email));
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
	 * This will return a single task based on the task id.  This method will check to see if the requester is allowed to
	 * access the task.  This can be called without being logged in.  The users email must be passed.
	 * @param shareId A valid taskId is required
	 * @return Share
	 */
	@GET
	@Path("/get/direct/id/{taskId}/email/{email}")
	public Response getPrivateShareDirectLink(@Context HttpServletRequest request, @PathParam("taskId")String taskId, @PathParam("email")String email)
	{
//		String profileId = getMemberEmailFromSession(request);
//		String profileId = getEmailFromSecurityContext();
//		if (profileId == null || "".equals(profileId) || "annymous".equals(profileId))
//			profileId = email;
		try
		{
			return buildSuccessResponse(shareService.getShareDirectLinkTX(taskId, email));
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
}
