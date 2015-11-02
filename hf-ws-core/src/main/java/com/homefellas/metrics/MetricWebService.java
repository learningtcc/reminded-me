package com.homefellas.metrics;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.enunciate.jaxrs.TypeHint;

import com.homefellas.exception.ValidationException;
import com.homefellas.user.Member;
import com.homefellas.ws.core.AbstractWebService;

@Path("/metrics")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MetricWebService extends AbstractWebService
{

	private IMetricService metricService;
	
	
	
	public void setMetricService(IMetricService metricService)
	{
		this.metricService = metricService;
	}

	/**
	 * This is a bulk method. 
	 * @param metrics This accepts a list of metrics.
	 * @return Returns whether or not the update was successful
	 */
	@POST
	@Path("/bulk")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveBulkMetrics(List<ClientMetric> metrics)
	{
		try
		{
			metricService.saveBulkClientMetrics(metrics);
			return buildSuccessResponse(true);
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	/**
	 * This is a save method for saving client
	 * @param metric All attributes are optinal.
	 * @return Returns whether or not the create was successful
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveMetric(@TypeHint(ClientMetric.class)ClientMetric metric)
	{
		try
		{
			metricService.saveClientMetric(metric);
			return buildSuccessResponse(true);
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
}
