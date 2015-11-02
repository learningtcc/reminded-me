package com.homefellas.rm.dyk;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.homefellas.ws.core.AbstractWebService;

@Path("/dyk")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class DidYouKnowWebService extends AbstractWebService
{

	private IDidYouKnowService didYouKnowService;

	public void setDidYouKnowService(IDidYouKnowService didYouKnowService)
	{
		this.didYouKnowService = didYouKnowService;
	}
	
	/**
	 * This will return all the valid messages    
	 * @return List<DidYouKnow>
	 */
	@GET
	@Path("/get/messages")
	public Response getMessages()
	{
		try
		{
			return buildSuccessResponse(didYouKnowService.getValidDidYouKnowMessages());
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	/**
	 * This will return all the valid messages.  You must pass a date that is MMddyyyy so 01022013.
	 * @return List<DidYouKnow>
	 */
	@GET
	@Path("/get/messages/date/{date}")
	public Response getMessagesByDate(@PathParam("date")String date)
	{
		try
		{
			DateFormat formatter = new SimpleDateFormat("MMddyyyy");
			return buildSuccessResponse(didYouKnowService.getValidDidYouKnowMessages(new Date(formatter.parse(date).getTime())));
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
}
