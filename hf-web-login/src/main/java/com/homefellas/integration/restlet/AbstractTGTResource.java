package com.homefellas.integration.restlet;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.ticket.TicketException;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.homefellas.exception.ValidationException;
import com.homefellas.user.IUserServiceTX;
import com.homefellas.user.Profile;

public abstract class AbstractTGTResource extends Resource
{
		private final static Logger log = LoggerFactory.getLogger(FacebookResource.class);

		@Autowired
	    private CentralAuthenticationService centralAuthenticationService;
	    
	    @Autowired
	    protected IUserServiceTX userService;
	    
	    @Override
		public Representation represent(Variant variant)
				throws ResourceException
		{
		try
		{
			Credentials c = createCredentials();
			if (c==null)
			{
				String errorMessage = "credentials is null";
	        	getResponse().setStatus(Status.CLIENT_ERROR_FORBIDDEN, errorMessage);
	            return new StringRepresentation(errorMessage, MediaType.APPLICATION_JSON);
			}
			final String ticketGrantingTicketId = this.centralAuthenticationService.createTicketGrantingTicket(c);
	        getResponse().setStatus(Status.SUCCESS_CREATED);
	        final Reference ticket_ref = getRequest().getResourceRef().addSegment(ticketGrantingTicketId);
	        getResponse().setLocationRef(ticket_ref);
	        
	        String jsonResponse = getJSONResponse(ticketGrantingTicketId);
	        Representation representation = new StringRepresentation(jsonResponse, MediaType.APPLICATION_JSON);
	    	return representation;
		} catch (final TicketException e) {
        log.error(e.getMessage(),e);
        getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
        return new StringRepresentation(e.getMessage(), MediaType.APPLICATION_JSON);
		}
		catch (ValidationException exception)
		{
			getResponse().setStatus(Status.CLIENT_ERROR_FORBIDDEN, exception.getMessage());
            return new StringRepresentation(exception.getMessage(), MediaType.APPLICATION_JSON);
		}
	}
	
	abstract void initRequestParmaters(Request request);
	abstract Credentials createCredentials();
	abstract String getJSONResponse(String tgt) throws ValidationException;
	
	 public void init(final Context context, final Request request, final Response response) {
	        super.init(context, request, response);
	        initRequestParmaters(request);
	        this.getVariants().add(new Variant(MediaType.TEXT_HTML));
	    }

	    public boolean allowDelete() {
	        return false;
	    }

	    public boolean allowPost() {
	        return true;
	    }
	    
	

	    @Override
		public boolean allowGet()
		{
			return true;
		}

		public void removeRepresentations() throws ResourceException {
	    }
		
}
