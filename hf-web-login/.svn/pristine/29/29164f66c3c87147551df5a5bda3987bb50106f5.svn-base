package com.homefellas.integration.restlet;

import org.jasig.cas.authentication.principal.Credentials;
import org.restlet.data.Request;

import com.homefellas.exception.ValidationException;
import com.homefellas.register.RegisterCredentials;
import com.homefellas.user.Profile;
import com.homefellas.user.UserValidationCodeEnum;

public class RegisterResource extends AbstractTGTResource
{

	private String token;
	private String email;

	@Override
	void initRequestParmaters(Request request)
	{
		this.token = (String) request.getAttributes().get("token");
        this.email = (String) request.getAttributes().get("email");
	}
	
	@Override
	Credentials createCredentials()
	{
		RegisterCredentials registerCredentials = new RegisterCredentials(token, email);
		return registerCredentials;
	}
	@Override
	String getJSONResponse(String ticketGrantingTicketId) throws ValidationException
	{
		String jsonResponse = "{\"tgt\":\""+ticketGrantingTicketId+"\"}";
        return jsonResponse;
	}
	
	      
}
