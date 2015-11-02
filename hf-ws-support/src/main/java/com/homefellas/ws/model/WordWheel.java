package com.homefellas.ws.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WordWheel {

	private String display;
	private String zip;
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	
}
