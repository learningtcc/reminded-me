package com.homefellas.service.location;

import static com.homefellas.model.core.TestModelBuilder.buildCountry;
import static com.homefellas.model.core.TestModelBuilder.buildCounty;
import static com.homefellas.model.core.TestModelBuilder.buildLocationAlias;
import static com.homefellas.model.core.TestModelBuilder.buildState;
import static com.homefellas.model.core.TestModelBuilder.buildZip;
import static org.junit.Assert.assertTrue;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.dao.core.AbstractCoreTestDao;
import com.homefellas.model.location.Country;
import com.homefellas.model.location.County;
import com.homefellas.model.location.LocationAlias;
import com.homefellas.model.location.LocationAlias.LocationAliasEnum;
import com.homefellas.model.location.State;
import com.homefellas.model.location.Zip;

public class LocationDaoTest 
{//extends AbstractCoreTestDao {

//	@Autowired
//	private ILocationDao locationDao;
//	
//	protected Zip denvilleZip;
//	protected Zip wilmingtonZip1;
//	protected Zip wilmingtonZip2;
//	protected Zip parkRidgeZip;
//	protected Zip whippanyZip;
//	protected Zip newarkNJZip;
//	protected Zip newarkDEZip;
//	
//	protected State delware;
//	protected State newJersey;
//	
//	protected County newCastle;
//	protected County burgen;
//	protected County morris;
//	protected County essex;
//	
//	protected Country untitedStates;
//	
//	private LocationAlias cityAlias;
//	private LocationAlias countyAlias;
//	private LocationAlias stateAlias;
//	
////	@BeforeTransaction
//	@Before
//	public void buildTestData()
//	{
//		untitedStates = buildCountry(true, "US", "United States");
//		dao.save(untitedStates);
//		
//		delware = buildState(true, "DE", "Delaware", untitedStates, "19805", true);
//		dao.save(delware);
//		
//		newJersey = buildState(true, "NJ", "New Jersey", untitedStates, "07834", true);
//		dao.save(newJersey);
//		
//		newCastle = buildCounty(true, "19805", delware, "New Castle");
//		dao.save(newCastle);
//		
//		essex = buildCounty(true, "07777", newJersey, "Essex");
//		dao.save(essex);
//		
//		burgen = buildCounty(true, "07656", newJersey, "Burgen");
//		dao.save(burgen);
//		
//		morris = buildCounty(true, "07834", newJersey, "Morris");
//		dao.save(morris);
//		
//		wilmingtonZip1 = buildZip(true, "Wilmington", "19805", delware, newCastle);
//		dao.save(wilmingtonZip1);
//		
//		wilmingtonZip2 = buildZip(true, "Wlimington", "19803", delware, newCastle);
//		dao.save(wilmingtonZip2);
//		
//		newarkDEZip = buildZip(true, "Newark", "19711", delware, newCastle);
//		dao.save(newarkDEZip);
//		
//		denvilleZip = buildZip(true, "Denville", "07834", newJersey, morris);
//		dao.save(denvilleZip);
//		
//		newarkNJZip = buildZip(true, "Newark", "07777", newJersey, essex);
//		dao.save(newarkNJZip);
//		
//		parkRidgeZip = buildZip(true, "Park Ridge", "07656", newJersey, burgen);
//		dao.save(parkRidgeZip);
//		
//		whippanyZip = buildZip(true, "Whippany", "07981", newJersey, morris);
//		dao.save(whippanyZip);
//		
//		cityAlias = buildLocationAlias(true, LocationAliasEnum.CITY, "University of Delaware", "19711");
//		dao.save(cityAlias);
//		
//		countyAlias = buildLocationAlias(true, LocationAliasEnum.COUNTY, "Home County", "07834");
//		dao.save(countyAlias);
//		
//		stateAlias = buildLocationAlias(true, LocationAliasEnum.STATE, "Home State", "19805");
//		dao.save(stateAlias);
//	}
//	
//	@Test
//	@Transactional
//	public void testGetCityAliases() {
//		List<LocationAlias> list = locationDao.getCityAliases();
//		
//		Assert.assertTrue(list.contains(cityAlias));
//		Assert.assertFalse(list.contains(countyAlias));
//		Assert.assertFalse(list.contains(stateAlias));
//		
//		
//	}
//
//	@Test
//	@Transactional
//	public void testGetStateAliases() {
//		List<LocationAlias> list = locationDao.getStateAliases();
//		
//		Assert.assertFalse(list.contains(cityAlias));
//		Assert.assertFalse(list.contains(countyAlias));
//		Assert.assertTrue(list.contains(stateAlias));
//	}
//
//	@Test
//	@Transactional
//	public void testGetCountyAliases() {
//		List<LocationAlias> list = locationDao.getCountyAliases();
//		
//		Assert.assertFalse(list.contains(cityAlias));
//		Assert.assertTrue(list.contains(countyAlias));
//		Assert.assertFalse(list.contains(stateAlias));
//	}
//	
//	@Test
//	@Transactional
//	public void testGetStates()
//	{
//		List<State> states = locationDao.getStates();
//		
//		assertTrue(states.contains(newJersey));
//		assertTrue(states.contains(delware));
//	}
//	
//	@Test
//	@Transactional
//	public void testGetZips()
//	{
//		List<Zip> zips = locationDao.getZips();
//		
//		assertTrue(zips.contains(denvilleZip));
//		assertTrue(zips.contains(wilmingtonZip1));
//		assertTrue(zips.contains(wilmingtonZip2));
//		assertTrue(zips.contains(parkRidgeZip));
//		assertTrue(zips.contains(whippanyZip));
//		assertTrue(zips.contains(newarkNJZip));
//		assertTrue(zips.contains(newarkDEZip));
//		
//	}
//	
//	@Test
//	@Transactional
//	public void testGetCountries()
//	{
//		List<County> coutries = locationDao.getCounties();
//		
//		assertTrue(coutries.contains(newCastle));
//		assertTrue(coutries.contains(burgen));
//		assertTrue(coutries.contains(morris));
//		assertTrue(coutries.contains(essex));
//	}

}
