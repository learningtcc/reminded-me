package com.homefellas.ws.core;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.homefellas.core.SpringBean;
import com.homefellas.exception.IValidationCode;
import com.homefellas.exception.ValidationException;
import com.homefellas.model.core.AbstractModel;
import com.homefellas.rm.reminder.Alarm;
import com.homefellas.rm.reminder.IReminderServiceTX;
import com.homefellas.rm.repeatsetup.TaskRepeat;
import com.homefellas.rm.share.IShareServiceTX;
import com.homefellas.rm.share.Share;
import com.homefellas.rm.task.ITaskServiceTX;
import com.homefellas.rm.task.Task;
import com.homefellas.service.location.ILocationService;
import com.homefellas.user.UserValidationCodeEnum;
import com.homefellas.ws.model.TaskUI;

public class AbstractWebService extends SpringBean {

	
	
	
	
	public static String stackTraceToString(Throwable e) {
	    StringBuilder sb = new StringBuilder(e.getMessage());
	    sb.append("\n");
	    for (StackTraceElement element : e.getStackTrace()) {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    return sb.toString();
	}


	
	
	protected Response handleException(Exception exception, String customMessage)
	{
		WebServiceSystemException webServiceSystemException = new WebServiceSystemException(customMessage, exception);
		return handleException(webServiceSystemException);
	}
	
	protected Response handleException(Exception exception)
	{
		WebServiceSystemException webServiceSystemException = new WebServiceSystemException(exception);
		return handleException(webServiceSystemException);
	}
	
	private Response handleException(WebServiceSystemException webServiceSystemException)
	{	
		ObjectMapper objectMapper = new ObjectMapper();
		try
		{
			System.out.println(objectMapper.writeValueAsString(webServiceSystemException));
		}
		catch (Exception e)
		{
			System.out.println("WebServiceSystemException to JSON has failed so here is the occurance number at: "+webServiceSystemException.getOccurance());
		}
		webServiceSystemException.getException().printStackTrace();
		return Response.status(500).entity(webServiceSystemException).type(MediaType.APPLICATION_JSON).build();
	}
	
	protected Response handleValidationException(ValidationException validationException)
	{		
		boolean authenicationError=false;
		List<ValidationError> errors = new ArrayList<ValidationError>();
		for (IValidationCode code : validationException.getValidationErrors())
		{
			if (code.equals(UserValidationCodeEnum.USER_MUST_BE_LOGGED_IN))
				authenicationError=true;
			errors.add(new ValidationError(code));
		}
		
		return handleValidationException(errors, validationException.getModel(), authenicationError);
	}
	
	protected Response handleValidationException(IValidationCode validationCode)
	{
		boolean authenicationError=false;
		if (validationCode.equals(UserValidationCodeEnum.USER_MUST_BE_LOGGED_IN))
			authenicationError=true;
		
		List<ValidationError> errors = new ArrayList<ValidationError>();
		errors.add(new ValidationError(validationCode));
		
		return handleValidationException(errors, null, authenicationError);
	}
	
//	protected Response handleValidationException(List<ValidationError> errors)
//	{
//		return handleValidationException(errors, null);
//	}
	
	protected Response handleValidationException(List<ValidationError> errors, AbstractModel model,boolean authenicationError)
	{
		System.out.println(errors.toString() +(model!=null ? " occured on class"+model.getClass().getName()+" with id = "+model.getPrimaryKey() : ""));
		
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			String json = mapper.writeValueAsString(errors);
			
			return Response.status(authenicationError?401:400).entity(json).type(MediaType.APPLICATION_JSON).build();
		}
		catch (Exception e)
		{
			return Response.status(400).entity(errors).type(MediaType.APPLICATION_JSON).build();
		}
		
		
	}
	
	protected Response buildSuccessResponse(Object entity)
	{
		return buildSuccessResponse(entity, MediaType.APPLICATION_JSON);
	}
	
	protected Response buildSuccessResponse(Object entity, String mediaType)
	{
		if (entity==null)
			return handleValidationException(new IValidationCode() {
				
				@Override
				public int ordinal()
				{
					return 999;
				}
				
				public String toString()
				{
					return "RETURN_OBJECT_IS_NULL";
				}
			});
		
		return Response.ok()
//		.header("Access-Control-Allow-Origin", "*").header("Allow-Control-Allow-Methods", "POST,GET,OPTIONS").header("Access-Control-Max-Age", "3600").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Headers", "Content-Type,X-Requested-With")
		.entity(entity).type(mediaType).build();
	}
	
	protected Authentication getAuthenicationFromSecurityContext()
	{
		 return SecurityContextHolder.getContext().getAuthentication();
	}
	
	
	
//	protected String getMemberEmailFromSession(HttpSession session)
//	{
//		Assertion assertion = (Assertion)session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
//		if (assertion==null)
//		{
//			return sessionEmail;
//		}
//		else
//		{
//			
//			return assertion.getPrincipal().getName();
//		}
//	}
//	
//	protected String getMemberEmailFromSession(HttpServletRequest request)
//	{
//		HttpSession httpSession = request.getSession();
//		
//		return getMemberEmailFromSession(httpSession);
//	}
	
	
	
}
