package com.homefellas.metrics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.homefellas.model.core.AbstractSequenceModel;

@Entity
@Table(name="metric_server")
public class Metric extends AbstractSequenceModel {

	@Column(name="fqmn",nullable=false)
	private String key;
	
	@Column(name="maxTime",nullable=false,length=5)
	private int max;
	
	@Column(name="minTime",nullable=false,length=5)
	private int min;
	
	@Column(name="averageTime",nullable=false,length=5)
	private double average;
	
	@Column(name="whenAddedToDb",nullable=false)
	private long timestamp;
	
	@Column(nullable=false)
	private int occurances;
	
	@Column(name="systemOfOccurance",nullable=false)
	private int system = MetricSystemEnum.Service.ordinal();
	
	public Metric()
	{
		
	}
	
	public Metric(String fullyQualiedMethodName, long executionTime)
	{
		this.key = fullyQualiedMethodName;
		this.max = (int)executionTime;
		this.min = (int)executionTime;
		this.average = executionTime;
		this.occurances = 1;
		
	}
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getSystem() {
		return system;
	}

	public void setSystem(int system) {
		this.system = system;
	}

	public int getOccurances() {
		return occurances;
	}

	public void setOccurances(int occurances) {
		this.occurances = occurances;
	}
	
	
	
	
}
