package com.homefellas.dao.core;


public interface IFilterablePersistantObject
{
	public QueryParameter getQueryParameter();
	public void setQueryParameter(QueryParameter queryParameter);
	public boolean isCount();
	public void setCount(boolean count);
}
