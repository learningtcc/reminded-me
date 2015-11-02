package com.homefellas.model.location;

import javax.xml.bind.annotation.XmlRootElement;

import com.homefellas.model.core.AbstractBaseTO;

public class Zip extends AbstractBaseTO implements ICacheableZip {

	private String zipCode;
	private String city;
	private double latitude;
	private double longitude;
	private String zipClass;
	private State state;
	private County county;
	
	//non-persistent fields
	private double distance;

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getZipClass() {
		return zipClass;
	}

	public void setZipClass(String zipClass) {
		this.zipClass = zipClass;
	}
	
	public County getCounty() {
		return county;
	}

	public void setCounty(County county) {
		this.county = county;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getDefaultZip() {
		return zipCode;
	}

	@Override
	public String getDisplay() {
		return city + ", "+state.getDisplay();
	}
	
	
}
