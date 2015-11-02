package com.homefellas.service.location;

import java.util.List;

import com.homefellas.model.location.County;
import com.homefellas.model.location.LocationAlias;
import com.homefellas.model.location.LocationSearchResult;
import com.homefellas.model.location.State;
import com.homefellas.model.location.Zip;

public interface ILocationService {

	public List<Zip> getZips();
	public List<State> getStates();
	public List<County> getCounties();
	public List<LocationAlias> getCityAliases();
	public List<LocationAlias> getStateAliases();
	public List<LocationAlias> getCountyAliases();
//	void loadLocationCache();
	public List<LocationSearchResult> findLocation(String searchedData, int numberOfResults);
	public List<LocationSearchResult> findLocation(String searchedData);
}
	
