package com.homefellas.cache.core;

import java.io.Serializable;


public abstract class AbstractCacheProvider implements ICacheProvider
{

	public abstract void put(String cacheName, Serializable key, Object object);
	public abstract Object get(String cacheName, Serializable key);
	public abstract void remove(String cacheName, Serializable key);
//	public abstract void clear();

	public Object getOrPut(String cacheName, Serializable key, ICacheCreateAction cacheCreateObjectAction) {
		Object object = get(cacheName, key);

		// if the object was not found in cache create it
		if (object == null) {
			object = cacheCreateObjectAction.createCacheObject();

			// store in cache
			put(cacheName, key, object);
		}

		return object;
	}

	public void update(String cacheName, Serializable key, ICacheUpdateAction cacheUpdateObjectAction) 
	{
		Object object = get(cacheName, key);

		if (object != null)
			cacheUpdateObjectAction.updateCacheObject(object);

	}
}
