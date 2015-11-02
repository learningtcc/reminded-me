package com.homefellas.model.core;


public class ListUserTypesTO extends AbstractListTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public ListUserTypesTO()
	{
		
	}
	
	public ListUserTypesTO(long id)
	{
		setId(id);
	}
	
	public ListUserTypesTO(ListUserTypeEnum listUserTypeEnum)
	{
		setId(new Long(listUserTypeEnum.ordinal()));
	}
	
	public enum ListUserTypeEnum { NONE, BASIC, BLOG_WRITER, PARTNER }

}
