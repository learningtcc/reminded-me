package com.homefellas.model.core;

import java.util.UUID;

public class CassandraModel {

	protected String id;
	
	public CassandraModel()
	{
		this.id = UUID.randomUUID().toString();
	}
	
	public CassandraModel(String id)
	{
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
