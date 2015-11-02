package com.homefellas.metrics;

import java.util.UUID;

import com.homefellas.model.core.AbstractBaseTO;

public class MetricDataPoint extends AbstractBaseTO {

	public static final String CACHE_KEY = "metricCachedDataPoints";
	
	private String methodName;
	private String className;
	private long executionTime;
	private long timestamp;
	private int system = MetricSystemEnum.Service.ordinal();
	private boolean collected=false;
	
	public long getExecutionTime() {
		return executionTime;
	}
	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getFQMN()
	{
		return className+"."+methodName;
	}
	
	public int getSystem() {
		return system;
	}
	public void setSystem(int system) {
		this.system = system;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result
				+ (int) (executionTime ^ (executionTime >>> 32));
		result = prime * result
				+ ((methodName == null) ? 0 : methodName.hashCode());
		result = prime * result + system;
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetricDataPoint other = (MetricDataPoint) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (executionTime != other.executionTime)
			return false;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		if (system != other.system)
			return false;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}
	
	public void collected()
	{
		this.collected=true;
	}

	public boolean isCollected() {
		return collected;
	}

	public void setCollected(boolean collected) {
		this.collected = collected;
	}
	
}
