package com.homefellas.metrics;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.homefellas.model.core.TestModelBuilder;
import com.homefellas.ws.core.AbstractTestCoreWebService;
import com.sun.jersey.api.client.ClientResponse;

public class MetricWebServiceTest extends AbstractTestCoreWebService
{

//	@Test
//	public void testSaveBulk()
//	{
//		ClientMetric clientMetric1 = TestModelBuilder.buildClientMetric(true);
//		ClientMetric clientMetric2 = TestModelBuilder.buildClientMetric(true);
//		ClientMetric clientMetric3 = TestModelBuilder.buildClientMetric(true);
//		ClientMetric clientMetric4 = TestModelBuilder.buildClientMetric(true);
//		ClientMetric clientMetric5 = TestModelBuilder.buildClientMetric(true);
//		List<ClientMetric> clientMetrics = new ArrayList<ClientMetric>();
//		
////		ClientResponse response = postToWebservice("/bulk", ClientResponse.class, clientMetrics);
//		ClientResponse response = callWebService(MetricWebService.class, "saveBulkMetrics", ClientResponse.class, clientMetrics);
//		assertOKStatus(response);
//	}
//	
//	@Test
//	public void testSaveMetric()
//	{
//		ClientMetric clientMetric = TestModelBuilder.buildClientMetric(true);
//		
//		ClientResponse response = callWebService(MetricWebService.class, "saveMetric", ClientResponse.class, clientMetric);
//		assertOKStatus(response);
//	}
}
