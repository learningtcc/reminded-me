package com.homefellas.model.core;


public class CacheContent<T extends ICacheable> {

	
	private static int interval =600000;
	private String key;
	private T value;
	private int count=1;
	private int previousCount;
	private long countResetTimeStamp;
	public CacheContent(){};
	public CacheContent(String key, T value){
		this.key = key;
		this.value = value;
	};
	public long getCountResetTimeStamp() {
		return countResetTimeStamp;
	}

	public void setCountResetTimeStamp(long countResetTimeStamp) {
		this.countResetTimeStamp = countResetTimeStamp;
	}

	public int getCount() {
		return count;
	}
	private boolean hasIntervalExpired(){
		long temp = System.currentTimeMillis() - getCountResetTimeStamp();
		if(temp>interval)
			return true;
		else
			return false;
	}
	public void increamentCount() {
		if(hasIntervalExpired()){
			previousCount = count;
			count =1;
			countResetTimeStamp = System.currentTimeMillis();
		}
		this.count++;
	}

	public int getFrequency() {
		long temp = System.currentTimeMillis() - getCountResetTimeStamp();
		if(temp <= 2*interval)
			previousCount = count;
		else if(temp > interval) {
			count =0;
		}
		return count+(previousCount/2); 
	}
	
	public int getPreviousCount() {
		return previousCount;
	}

	public void setPreviousCount(int previousCount) {
		this.previousCount = previousCount;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CacheContent<ICacheable> other = (CacheContent<ICacheable>) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
	
	
	
}
