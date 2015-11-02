package com.homefellas.model.core;


public class ListUserLevelsTO extends AbstractListTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2257528305257818863L;

	public ListUserLevelsTO()
	{
		
		
	}
	
	public ListUserLevelsTO(long id)
	{
		setId(id);
	}
	
	public ListUserLevelsTO(ListUserLevelEnum listUserLevelEnum)
	{
		setId(new Long(listUserLevelEnum.ordinal()));
	}
	
	public enum ListUserLevelEnum { NONE, ADMIN, USER}
}
