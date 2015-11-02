package com.homefellas.rm.reminder;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.security.access.prepost.PreAuthorize;

import com.homefellas.exception.ValidationException;
import com.homefellas.ws.core.AbstractRMWebService;

@Path("/reminder")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class ReminderWebService extends AbstractRMWebService
{
	
	@GET
	@Path("/alarm/bulk/{ids}")
	@PreAuthorize("hasRole('ROLE_HF_USER')")
	public Response getBulkAlarms(@PathParam("ids")String ids)
	{
		try
		{
			return buildSuccessResponse(reminderService.getBulkAlarmsTX(ids, getEmailFromSecurityContext()));
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
	@Path("/alarm/save")
	public Response createAlarm(Alarm alarm)
	{
		try
		{
			return buildSuccessResponse(reminderService.createAlarmTX(alarm));
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
	

	@Path("/alarm/update")
	@POST
	public Response updateAlarm(Alarm alarm)
	{
		try
		{
			return buildSuccessResponse(reminderService.updateAlarmTX(alarm));
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

	
	@Path("/alarm/delete/{id}")
	@GET
	public Response deleteAlarm(@PathParam("id")String reminderId)
	{
		try
		{
			Alarm alarm = new Alarm();
			alarm.setId(reminderId);
			reminderService.deleteAlarmTX(alarm.getId());
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
	
	
	

}
