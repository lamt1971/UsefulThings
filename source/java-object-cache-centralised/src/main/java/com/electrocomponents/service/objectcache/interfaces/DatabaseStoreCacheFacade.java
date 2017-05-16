package com.electrocomponents.service.objectcache.interfaces;

import com.electrocomponents.commons.cache.CacheName;
import com.electrocomponents.service.objectcache.impl.ServicesCacheCallableWrapper;

/**
 * Persists/Saves the Cache entries to a centralised data source (e.g. database).
 * When an application asks the cache for an entry, for example the key X, and X is not in the in memory cache, this facade finds it from the CacheStore (underlying data source).
 */
public interface DatabaseStoreCacheFacade {
	
	/**
	 * Returns an existing value from in memory cache, if one is already existed.
	 * If its not in memory cache, return from the underlying CacheStore/data source if one is already existed.
	 * If not in CacheStore, then fetch the result by executing the callable and put in to in memory cache and also asynchronously store it in CacheStore (write-behind).
	 * Calling code need to make sure the {@link}CacheName, key and callable mapping is correct.
	 * 		
	 * @param cacheName - the name of the cache
	 * @param key - the key whose associated value is to be returned	 * 
	 * @param callable
	 * @return result - result of the callable.
	 */
	Object putIfAbsentAndGet(CacheName cacheName, String key, ServicesCacheCallableWrapper callableWrapper);

}
