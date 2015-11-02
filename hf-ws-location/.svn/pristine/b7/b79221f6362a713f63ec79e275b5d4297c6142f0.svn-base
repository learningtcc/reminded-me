package com.homefellas.service.location;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.homefellas.model.location.ICacheableZip;
import com.homefellas.model.location.LocationSearchResult;
import com.homefellas.ws.core.AbstractWebService;

@Path("/locations")
public class LocationWebService extends AbstractWebService {

	@Autowired
	private ILocationService locationService;
	
	
	@GET
	@Path("/search")
	public Response findLocation(@QueryParam("s")String searchedData) {
		
		List<LocationSearchResult> locations=null;
		
		
//		Response response = new Response();
		try
		{
			locations = locationService.findLocation(searchedData);
//			List<LocationSearchResult> results = new ArrayList<LocationSearchResult>(locations.size());
//			for (LocationSearchResult cacheable:locations)
//			{
//				LocationSearchResult result = new LocationSearchResult();
//				ICacheableZip cacheableZip = (ICacheableZip)cacheable;
//				result.setDefaultZip(cacheableZip.getDefaultZip());
//				result.setDisplay(cacheableZip.getDisplay());
//				results.add(result);
//			}
			return buildSuccessResponse(locations);
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
		
	}
//
//	@GET
//	@Path("/prime")
//	public Response primeCache() {
//		
//		
//		try
//		{
//			locationService.loadLocationCache();
//			return buildSuccessResponse(true);
//		}
//		catch (Exception exception)
//		{
//			return handleException(exception);
//		}
//		
//	}

	
	
}
