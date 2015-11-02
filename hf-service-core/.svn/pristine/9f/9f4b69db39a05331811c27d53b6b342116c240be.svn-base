package com.homefellas.metrics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.homefellas.model.core.AbstractSequenceModel;

@Entity
@Table(name="metric_outlier")
public class MetricOutlier extends AbstractSequenceModel
{

	@Column(name="executionTime",nullable=false)
	private long executionTime;
	
	@Column(name="occuranceTime",nullable=false)
	private long occuranceTime = System.currentTimeMillis();
	
	@Column(name="threshold",nullable=false)
	private int threshold;
	
	@ManyToOne(optional=false,fetch=FetchType.EAGER)
	private Metric metric;

	public long getOccuranceTime()
	{
		return occuranceTime;
	}

	public long getExecutionTime()
	{
		return executionTime;
	}

	public void setExecutionTime(long executionTime)
	{
		this.executionTime = executionTime;
	}

	public void setOccuranceTime(long occuranceTime)
	{
		this.occuranceTime = occuranceTime;
	}

	public int getThreshold()
	{
		return threshold;
	}

	public void setThreshold(int threshold)
	{
		this.threshold = threshold;
	}

	public Metric getMetric()
	{
		return metric;
	}

	public void setMetric(Metric metric)
	{
		this.metric = metric;
	}
	
	
}
