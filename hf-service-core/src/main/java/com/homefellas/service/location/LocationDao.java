package com.homefellas.service.location;

import java.util.List;

import org.hibernate.Query;

import com.homefellas.dao.hibernate.core.HibernateCRUDDao;
import com.homefellas.model.location.Country;
import com.homefellas.model.location.County;
import com.homefellas.model.location.LocationAlias;
import com.homefellas.model.location.LocationAlias.LocationAliasEnum;
import com.homefellas.model.location.State;
import com.homefellas.model.location.Zip;

public class LocationDao extends HibernateCRUDDao implements ILocationDao {

	public List<LocationAlias> getCityAliases() {
//		return getSession().createQuery("").list()
		
		Query query = getSession().createQuery("from LocationAlias l where l.context = ?");
		query.setString(0, LocationAliasEnum.CITY.getContext());
		
		return query.list();
		
//		find("from LocationAlias l where l.context = ?", LocationAliasEnum.CITY.getContext());
	}

	public List<LocationAlias> getStateAliases() {
//		return getSession().find("from LocationAlias l where l.context = ?", LocationAliasEnum.STATE.getContext());
		Query query = getSession().createQuery("from LocationAlias l where l.context = ?");
		query.setString(0, LocationAliasEnum.STATE.getContext());
		
		return query.list();
	}

	public List<LocationAlias> getCountyAliases() {
//		return getSession().find("from LocationAlias l where l.context = ?", LocationAliasEnum.COUNTY.getContext());
		Query query = getSession().createQuery("from LocationAlias l where l.context = ?");
		query.setString(0, LocationAliasEnum.COUNTY.getContext());
		
		return query.list();
	}

	
	public List<Zip> getZips()
	{
		Query query = getSession().createQuery("from Zip");		
		return query.list();
	}
	
	public List<State> getStates()
	{
		Query query = getSession().createQuery("from State");		
		return query.list();
	}
	
	public List<County> getCounties()
	{
		Query query = getSession().createQuery("from County");		
		return query.list();
	}

	@Override
	public County createCounty(County county) {
		save(county);
		
		return county;
	}

	@Override
	public State createState(State state) {
		save(state);
		
		return state;
	}

	@Override
	public Zip createZip(Zip zip) {
		save(zip);
		
		return zip;
	}

	@Override
	public LocationAlias createLocationAlias(LocationAlias locationAlias) {
		save(locationAlias);
		
		return locationAlias;
	}

	@Override
	public Country createCountry(Country country) {
		save(country);
		
		return country;
	}
	
	
}
