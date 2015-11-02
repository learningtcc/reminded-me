package com.homefellas.integration.restlet;

import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.ticket.TicketException;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Status;
import org.restlet.resource.StringRepresentation;

import com.homefellas.authentication.principal.FacebookCredentials;
import com.homefellas.exception.ValidationException;
import com.homefellas.user.Profile;
import com.homefellas.user.UserValidationCodeEnum;

public class FacebookResource extends AbstractTGTResource
{    
	private String facebookId;
	
	@Override
	void initRequestParmaters(Request request)
	{
		this.facebookId = (String) request.getAttributes().get("facebookId");
	}

	@Override
	Credentials createCredentials()
	{
		FacebookCredentials c = new FacebookCredentials();
		c.setId(facebookId);
		
		return c;
	}

	@Override
	String getJSONResponse(String ticketGrantingTicketId) throws ValidationException
	{
		Profile profile = userService.getProfileByFaceBookIdTX(facebookId);
		if (profile == null)
        {
			throw new ValidationException(UserValidationCodeEnum.FACEBOOK_ID_NOT_LINKED);
        }
        String jsonResponse = "{\"tgt\":\""+ticketGrantingTicketId+"\",\"id\":\""+profile.getId()+"\",\"email\":\""+profile.getMember().getEmail()+"\",\"name\":\""+profile.getName()+"\"}";
        return jsonResponse;
	}
		
			
					
}
