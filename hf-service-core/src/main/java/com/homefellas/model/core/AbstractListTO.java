package com.homefellas.model.core;


public abstract class AbstractListTO extends AbstractBaseTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2130360477616449348L;

	private String code;
	private String name;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
