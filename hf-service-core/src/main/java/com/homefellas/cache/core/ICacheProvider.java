package com.homefellas.cache.core;

import java.io.Serializable;

public interface ICacheProvider
{
	public Object getOrPut(String cacheName, Serializable key, ICacheCreateAction cacheCreateObjectAction);
	public void update(String cacheName, Serializable key, ICacheUpdateAction cacheUpdateObjectAction);
	public void put(String cacheCategory, Serializable key, Object object); 
	public Object get(String cacheCategory, Serializable key);
	public void remove(String cacheCategory, Serializable key);
	public void remove(String cacheCategory);
//	public void clear();
	public void clearCategory(String cacheCategory);
}
