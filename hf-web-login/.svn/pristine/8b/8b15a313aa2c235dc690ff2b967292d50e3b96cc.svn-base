package com.homefellas.integration.restlet;

import org.codehaus.jackson.map.ObjectMapper;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.integration.restlet.TicketGrantingTicketResource;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.restlet.Context;
import org.restlet.data.MediaType;
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

import com.homefellas.cas.client.authentication.ProfileJson;


public class ProfileResource extends Resource {
	    
	    private final static Logger log = LoggerFactory.getLogger(ProfileResource.class);

//	    @Autowired
//	    private CentralAuthenticationService centralAuthenticationService;
	    
	    @Autowired
	    private final TicketRegistry ticketRegistry=null;
	    
	    private String ticketGrantingTicketId;

//	    @Autowired
//	    @NotNull
//	    private HttpClient httpClient;

	    public void init(final Context context, final Request request, final Response response) {
	        super.init(context, request, response);
	        this.ticketGrantingTicketId = (String) request.getAttributes().get("ticketGrantingTicketId");
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
			// TODO Auto-generated method stub
			return true;
		}

		public void removeRepresentations() throws ResourceException {
//	        this.centralAuthenticationService.destroyTicketGrantingTicket(this.ticketGrantingTicketId);
//	        getResponse().setStatus(Status.SUCCESS_OK);
	    }

	    public void acceptRepresentation(final Representation entity)
	        throws ResourceException {
//	        final Form form = getRequest().getEntityAsForm();
//	        final String serviceUrl = form.getFirstValue("service");
	    	
	    	
	        try {
	        	TicketGrantingTicket ticketGrantingTicket = (TicketGrantingTicket)ticketRegistry.getTicket(ticketGrantingTicketId);
	        	
	        	Principal principal = ticketGrantingTicket.getAuthentication().getPrincipal();
	        	ProfileJson profileJson = new ProfileJson();
	        	profileJson.setId(principal.getId());
	        	profileJson.setAttributes(principal.getAttributes());
	            
	        	ObjectMapper objectMapper = new ObjectMapper();
	        	String json = objectMapper.writeValueAsString(profileJson);
	            
//	        	final String serviceTicketId = this.centralAuthenticationService.grantServiceTicket(this.ticketGrantingTicketId, new SimpleWebApplicationServiceImpl(serviceUrl, this.httpClient));
	            getResponse().setEntity(json, MediaType.APPLICATION_JSON);
	        } catch (final Exception e) {
	            log.error(e.getMessage(),e);
	            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
	        }
	    }

		@Override
		public Representation represent(Variant variant)
				throws ResourceException
		{
			try {
	        	TicketGrantingTicket ticketGrantingTicket = (TicketGrantingTicket)ticketRegistry.getTicket(ticketGrantingTicketId);
	        	
	        	if (ticketGrantingTicket==null||ticketGrantingTicket.getAuthentication()==null||ticketGrantingTicket.getAuthentication().getPrincipal()==null)
	        	{
//	        		getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "INVALID TGT");
	        		Representation representation = new StringRepresentation("{\"error\":\"INVALID TGT\"}", MediaType.APPLICATION_JSON);
	        		
		        	return representation;
	        	}
	        	Principal principal = ticketGrantingTicket.getAuthentication().getPrincipal();
	        	ProfileJson profileJson = new ProfileJson();
	        	profileJson.setId(principal.getId());
	        	profileJson.setAttributes(principal.getAttributes());
	            
	        	ObjectMapper objectMapper = new ObjectMapper();
	        	String json = objectMapper.writeValueAsString(profileJson);
	            
//	        	final String serviceTicketId = this.centralAuthenticationService.grantServiceTicket(this.ticketGrantingTicketId, new SimpleWebApplicationServiceImpl(serviceUrl, this.httpClient));
//	            getResponse().setEntity(json, MediaType.APPLICATION_JSON);
	        	Representation representation = new StringRepresentation(json, MediaType.APPLICATION_JSON);
	        	return representation;
	        	
	        } catch (final Exception e) {
	            log.error(e.getMessage(),e);
	            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
	            throw new RuntimeException();
//	            return super.represent();
	        }
		}
	    
	    
	    
	    
}
