package com.homefellas.model.location;

import com.homefellas.model.core.AbstractBaseTO;

public class LocationAlias extends AbstractBaseTO implements ICacheableZip{

	private String context;
	private String value;
	private String alias;
	private int importance;
	
	public enum LocationAliasEnum 
	{
		STATE("state"), COUNTY("county"), CITY("city");
		
		private String context;
		
		private LocationAliasEnum(String value)
		{
			this.context = value;
		}
		
		public String getContext()
		{
			return context;
		}
		
	}
	
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public int getImportance() {
		return importance;
	}
	public void setImportance(int importance) {
		this.importance = importance;
	}
	public String getDefaultZip() {
		return value;
	}
	@Override
	public String getDisplay() {
		return alias;
	}
	
	
}
