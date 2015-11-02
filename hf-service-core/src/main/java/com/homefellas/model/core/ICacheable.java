package com.homefellas.model.core;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.homefellas.model.location.LocationSearchResult;
	
@JsonSerialize(as=LocationSearchResult.class)
@JsonDeserialize(as=LocationSearchResult.class)
public interface ICacheable {

}
