package com.homefellas.cache.ehcache;

import java.io.Serializable;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import com.homefellas.cache.core.AbstractCacheProvider;

public class EhCacheProvider extends AbstractCacheProvider
{
	protected CacheManager cacheManager;

//	@Override
//	public void clear()
//	{
//		cacheManager.removalAll();		
//	}

	@Override
	public Object get(String cacheName, Serializable key)
	{
		Element element = cacheManager.getCache(cacheName).get(key);
		return element == null ? null : element.getObjectValue();	
	}

	@Override
	public void put(String cacheName, Serializable key, Object object)
	{
		cacheManager.getCache(cacheName).put(new Element(key, object));		
	}

	@Override
	public void remove(String cacheName, Serializable key)
	{
		cacheManager.getCache(cacheName).remove(key);
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public void remove(String cacheCategory)
	{
		cacheManager.removeCache(cacheCategory);
	}

	public void clearCategory(String cacheCategory)
	{
		cacheManager.getCache(cacheCategory).removeAll();
	}
	

	
	
	
}
