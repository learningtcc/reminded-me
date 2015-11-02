package com.homefellas.metrics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.homefellas.model.core.AbstractGUIDModel;
import com.homefellas.model.core.AbstractSequenceModel;

@Entity
@Table(name="metric_webrequest")
@XmlRootElement
public class WebRequestMetric extends AbstractGUIDModel {

	private String ipAddress; 
	
	@Column(length=500)
	private String requestURI;
	
	@Column(length=5000)
	private String requestPayload;
	
	@Column(length=5000)
	private String responsePayload;
	private int status;
	
	@Column(length=200)
	private String tgt;
	private String operation;
	private long systemTime;
	
	private String email;
	
	public WebRequestMetric()
	{
		generateGUIDKey();
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getRequestURI() {
		return requestURI;
	}
	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}
	public String getRequestPayload() {
		return requestPayload;
	}
	public void setRequestPayload(String requestPayload) {
		this.requestPayload = requestPayload;
	}
	public String getResponsePayload() {
		return responsePayload;
	}
	public void setResponsePayload(String responsePayload) {
		this.responsePayload = responsePayload;
	}
	public String getTgt() {
		return tgt;
	}
	public void setTgt(String tgt) {
		this.tgt = tgt;
	}
	public long getSystemTime() {
		return systemTime;
	}
	public void setSystemTime(long systemTime) {
		this.systemTime = systemTime;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	 
	
	
}
