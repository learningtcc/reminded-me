package com.homefellas.test.core;

import java.io.Serializable;

import javax.persistence.Transient;

public class CassandraTestModel implements Serializable{

	private String id;
	private String firstName;
	private String lastName;
	private String email;
	
	public CassandraTestModel()
	{
		this.id = java.util.UUID.randomUUID().toString();
	}
	@Transient
	private String ssn;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
