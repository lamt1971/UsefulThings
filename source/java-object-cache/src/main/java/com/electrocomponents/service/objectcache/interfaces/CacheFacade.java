package com.electrocomponents.service.objectcache.interfaces;

import java.util.concurrent.Callable;

import com.electrocomponents.commons.cache.CacheName;

/**
 * Manages multiple caches (buckets), abstracts all internal cache implementation details, provides a simple interface to access cache.
 */
public interface CacheFacade {
	
    String EJB_MODULE_NAME = "java-object-cache";
    
	/**
	 * Implementation Service Bean Name. 
	 */
	String SERVICE_NAME = "JavaHeapCache";
	/**
	 * Gets an entry from the cache.
	 * @param key - the key whose associated value is to be returned
	 * @param cacheName - the name of the cache
	 * @return the value, or null, if it does not exist.
	 */
	Object get(CacheName cacheName, String key);
	
	/**
	 * Returns an existing value if one is already existed.
	 * Associates the specified callable with the specified key in this cache (If the cache doesn't contain a mapping for the key already);
	 * Calling code need to make sure the key and callable mapping is correct.
	 * 		
	 * @param key - the key whose associated value is to be returned
	 * @param cacheName - the name of the cache
	 * @param callable
	 */
	<T1> T1 putIfAbsentAndGet(CacheName cacheName, String key, Callable<T1> callable);

	
	/**
	 * Determines if the Cache contains an entry for the specified key.
	 * More formally, returns true if and only if this cache contains a mapping for a key k such that key.equals(k). 
	 * @param key - key whose presence in this cache is to be tested.
	 * @return true if this map contains a mapping for the specified key
	 */	
	boolean containsKey(CacheName cacheName, String key);
	
	/**
	 * Associates the specified value with the specified key in the cache. 
	 * If the Cache previously contained a mapping for the key, the old value is replaced by the specified value.
	 * @param     key - key with which the specified value is to be associated
	 * @param cacheName - the name of the cache
     * @param value - - value to be associated with the specified key
     * @return value - the cache value associated with the key.
	 */
	Object put(CacheName cacheName, String key, Object value);
	
	/**
	 * Removes the mapping for a key from this cache if it is present.
	 * Returns true if this cache previously associated the key, or false if the cache contained no mapping for the key.
	 * The cache will not contain a mapping for the specified key once the call returns.
	 * @param key - key whose mapping is to be removed from the cache
	 * @param cacheName - the name of the cache
	 * @return value - the cache value associated the key, or null if the cache contained no mapping for the key.
	 */	
	Object remove(CacheName cacheName, String key);
}
