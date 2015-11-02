package com.homefellas.service.location;

import static com.homefellas.model.core.TestModelBuilder.buildCountry;
import static com.homefellas.model.core.TestModelBuilder.buildCounty;
import static com.homefellas.model.core.TestModelBuilder.buildLocationAlias;
import static com.homefellas.model.core.TestModelBuilder.buildState;
import static com.homefellas.model.core.TestModelBuilder.buildZip;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.homefellas.dao.core.IDao;
import com.homefellas.model.location.Country;
import com.homefellas.model.location.County;
import com.homefellas.model.location.LocationAlias;
import com.homefellas.model.location.LocationAlias.LocationAliasEnum;
import com.homefellas.model.location.LocationSearchResult;
import com.homefellas.model.location.State;
import com.homefellas.model.location.Zip;
import com.homefellas.ws.core.AbstractLocationWebServiceTest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;


public class LocationWebServiceTest extends AbstractLocationWebServiceTest {

	// TODO Need to refactor this test into a new project for locations ws
	protected Zip denvilleZip;
	protected Zip wilmingtonZip1;
	protected Zip wilmingtonZip2;
	protected Zip parkRidgeZip;
	protected Zip whippanyZip;
	protected Zip newarkNJZip;
	protected Zip newarkDEZip;
	
	protected State delware;
	protected State newJersey;
	
	protected County newCastle;
	protected County burgen;
	protected County morris;
	protected County essex;
	
	protected Country untitedStates;
	
	private LocationAlias cityAlias;
	private LocationAlias countyAlias;
	private LocationAlias stateAlias;
	
	@Autowired
	protected ILocationService locationService;
	
	@Before
	public void setupDataBase()
	{
//		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:com/homefellas/context/hf-ws-location-test-context.xml");
		dao = (IDao)getServer().getSpringBean("dao");
		transactionManager = (PlatformTransactionManager)getServer().getSpringBean("transactionManager");
		
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
		   @Override
		   protected void doInTransactionWithoutResult(TransactionStatus status) {
		        // DAO has access to the session through sessionFactory.getCurrentSession()
		   
		
		untitedStates = buildCountry(true, "US", "United States");
		dao.save(untitedStates);
		
		delware = buildState(true, "DE", "Delaware", untitedStates, "19805", true);
		dao.save(delware);
		
		newJersey = buildState(true, "NJ", "New Jersey", untitedStates, "07834", true);
		dao.save(newJersey);
		
		newCastle = buildCounty(true, "19805", delware, "New Castle");
		dao.save(newCastle);
		
		essex = buildCounty(true, "07777", newJersey, "Essex");
		dao.save(essex);
		
		burgen = buildCounty(true, "07656", newJersey, "Burgen");
		dao.save(burgen);
		
		morris = buildCounty(true, "07834", newJersey, "Morris");
		dao.save(morris);
		
		wilmingtonZip1 = buildZip(true, "Wilmington", "19805", delware, newCastle);
		dao.save(wilmingtonZip1);
		
		wilmingtonZip2 = buildZip(true, "Wlimington", "19803", delware, newCastle);
		dao.save(wilmingtonZip2);
		
		newarkDEZip = buildZip(true, "Newark", "19711", delware, newCastle);
		dao.save(newarkDEZip);
		
		denvilleZip = buildZip(true, "Denville", "07834", newJersey, morris);
		dao.save(denvilleZip);
		
		newarkNJZip = buildZip(true, "Newark", "07777", newJersey, essex);
		dao.save(newarkNJZip);
		
		parkRidgeZip = buildZip(true, "Park Ridge", "07656", newJersey, burgen);
		dao.save(parkRidgeZip);
		
		whippanyZip = buildZip(true, "Whippany", "07981", newJersey, morris);
		dao.save(whippanyZip);
		
		cityAlias = buildLocationAlias(true, LocationAliasEnum.CITY, "University of Delaware", "19711");
		dao.save(cityAlias);
		
		countyAlias = buildLocationAlias(true, LocationAliasEnum.COUNTY, "Home County", "07834");
		dao.save(countyAlias);
		
		stateAlias = buildLocationAlias(true, LocationAliasEnum.STATE, "Home State", "19805");
		dao.save(stateAlias);
		
		   }});
	}
	
	@Test
	public void testFindLocation()
	{
//		ClientResponse response = callWebService(LocationWebService.class, "primeCache", ClientResponse.class, null);
//		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		
		String searchTerm = "new";
		String wsPath = "/locations/search";
		URI uri = UriBuilder.fromPath(getContextRoot()+getJerseyMapping()+wsPath).build();
		ClientResponse response = createClient().resource(getServer().uri()).path(uri.getPath()).queryParam("s", searchTerm).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		List<LocationSearchResult> locations = response.getEntity(new GenericType<List<LocationSearchResult>>(){});
		
		Assert.assertTrue(locations.size()>1);
		
		
		
	}
}
