package com.homefellas.rm;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * This interface defines a class as being able to be filter by status.  For instance if you delete a model, you can use this 
 * interface to define which records you want to exclude on the sync based on the status.
 * @author prc9041
 *
 */
public interface ISynchronizeableFiltered extends ISynchronizeable
{

//	@JsonIgnore
//	public Object[] getSynchronizedFilterValues(); 
	
	public String getDeleteStatusField();
	public int getDeletedTrueValue();
	
	/*
	 * If you want every status but (!=) the true value, then set exclusive to true.  If you want every status that equals (inclusive) set this to false 
	 */
	public boolean isStatusExclusive();
}
