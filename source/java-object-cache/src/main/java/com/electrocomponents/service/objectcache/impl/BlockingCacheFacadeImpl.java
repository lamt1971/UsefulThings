package com.electrocomponents.service.objectcache.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;

import com.electrocomponents.commons.cache.CacheName;
import com.electrocomponents.service.core.concurrent.ViewableFutureTask;
import com.electrocomponents.service.objectcache.interfaces.CacheFacade;


/**
 * A CacheFacade implementation that prevents multiple Threads inserting an object at the same cache
 * address. If 2 Threads attempt to insert a Callable at the same address the second Thread will be 
 * blocked until the Callable inserted by the first Thread is resolved. The second Thread will receive 
 * the object cached by the first Thread and its Callable will not be executed.
 * 
 * @author Jim Britton
 *
 */

// this is an alternative implementation to the CacheFacadeImpl. EJB injection of CacheFacadeImpl
// is done using only the interface (no beanName), so to ensure only 1 implementation of CacheFacade 
// exists and is available for injection this bean is not annotated as a Singleton, until we've tested
// the performance characteristics of both implementations and selected one.

//@Singleton(name="BlockingJavaHeapCache")
//@Startup
//@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
// allow unsynchronized access to all the business methods of this bean.
//@Lock(LockType.READ)
public class BlockingCacheFacadeImpl implements CacheFacade {
	
    /** Logger. */
    private static final Log LOG = LogFactory.getLog(BlockingCacheFacadeImpl.class);
    
    /**
     * A map of monitors for synchonized access to cache elements, indexed by cache key.
     * Items in this map are only removed when their cached objects are removed from the cache.
     */
    private Map<String, Object> cacheKeyMonitors = new HashMap<String, Object>();
    	
	@Resource(lookup="java:jboss/infinispan/container/services") 
	private EmbeddedCacheManager container;

	/**
	 * Gets an entry from the cache.
	 * @param key - the key whose associated value is to be returned
	 * @param cacheName - the name of the cache
	 * @return the value, or null, if it does not exist.
	 */
	@Override
	public Object get(CacheName cacheName, String key) {
		LOG.info("Start get - cacheName: " + cacheName + "Key: " + key );

		Cache<String, Object> cache = container.getCache(cacheName.getCacheName());
		Object val = cache.get(key);
		LOG.info("Finish get - cacheName: " + cacheName + "Key: " + key );
		return val;
	}

	/**
	 * Returns an existing value if one is already in the cache *and its resolution has been completed*.
	 * Associates the callable's result with the specified key in this cache (If the cache doesn't contain a mapping for the key already);
	 * 
	 * If an object already exists for the provided cache address it will be returned immediately and the provided
	 * callable will not be executed. If the object does not exist the callable will be executed and the result added
	 * to the cache *once it's resolved*. Concurrent calls with the same cache key are allowed to execute in parallel, with the first result
	 * to be resolved being cached. The remaining concurrent Threads will still return the object they resolved (e.g. read from the database) but this 
	 * won't be cached. Later calls with the same cache key will return the object cached by the first Thread that resolved it.
	 * 		
	 * @param key - the key whose associated value is to be returned
	 * @param cacheName - the name of the cache
	 * @param callable
	 */
	@Override
	public <T1> T1 putIfAbsentAndGet(CacheName cacheName, String key, Callable<T1> callable) {
	    T1 callableResult = null;
	    Cache<String, Object> cache = container.getCache(cacheName.getCacheName());
	    Object cacheKeyMonitor = getCacheKeyMonitor(cacheName, key);
	    callableResult = (T1) cache.get(key);
        if (callableResult != null) {
            // there's already an object in the cache with this address so don't execute the Callable
            LOG.info("Finish putIfAbsentAndGet (cache hit) - cacheName: " + cacheName + "Key: " + key );
        } else {
            FutureTask<T1> futureTask = new ViewableFutureTask(callable);
            futureTask.run();
            try {
                callableResult = futureTask.get();
                if (callableResult != null) {
                    synchronized (cacheKeyMonitor) {
                        if (cache.get(key) == null) {
                            cache.put(key, callableResult); 
                        }
                    }
                }
            } catch (InterruptedException e) {
                LOG.warn("InterruptedException while executing the callable for key : " + key + "exception details : " + e.getMessage());
            } catch (ExecutionException e) {
                LOG.warn("ExecutionException while executing the callable for key : " + key + "exception details : " + e.getMessage());     
            }
        }	    
	    return callableResult;
	}

	/**
	 * Determines if the Cache contains an entry for the specified key.
	 * More formally, returns true if and only if this cache contains a mapping for a key k such that key.equals(k). 
	 * @param key - key whose presence in this cache is to be tested.
	 * @return true if this map contains a mapping for the specified key
	 */	
	@Override
	public boolean containsKey(CacheName cacheName, String key) {
		LOG.info("Start containsKey - cacheName: " + cacheName + "Key: " + key );
		Cache<String, Object> cache = container.getCache(cacheName.getCacheName());
		boolean isKeyPresentInCache = cache.containsKey(key);
		LOG.info("Finish containsKey - cacheName: " + cacheName + "Key: " + key );
		return isKeyPresentInCache;
	}

	/**
	 * Associates the specified value with the specified key in the cache. 
	 * If the Cache previously contained a mapping for the key, the old value is replaced by the specified value.
	 * @param     key - key with which the specified value is to be associated
	 * @param cacheName - the name of the cache
     * @param value - - value to be associated with the specified key
     * @return value - the cache value associated with the key.
	 */
	@Override	
	public Object put(CacheName cacheName, String key, Object value) {
		LOG.info("Start put - cacheName: " + cacheName + "Key: " + key );
		Cache<String, Object> cache = container.getCache(cacheName.getCacheName());
		Object val = cache.put(key, value);
		LOG.info("Finish put - cacheName: " + cacheName + "Key: " + key );
		return val;
	}

	/**
	 * Removes the value with the specified key from the cache. 
	 * @param     key - key with which the specified value is to be associated
	 * @param cacheName - the name of the cache
	 * @return the value associated with this key (or null).
	 */
	@Override
	public Object remove(CacheName cacheName, String key) {
		LOG.info("Start remove - cacheName: " + cacheName + "Key: " + key );
		Cache<String, Object> cache = container.getCache(cacheName.getCacheName());		
		removeCachedKeyMonitor(cacheName, key);
		LOG.info("Finish get - remove: " + cacheName + "Key: " + key );
		return cache.remove(key);
	}	

    /**
     * Retrieve or create a synchronization monitor for a cache name / key combination.
     * @param cacheName
     * @param cacheKey
     * @return
     */
    private synchronized Object getCacheKeyMonitor(CacheName cacheName, String cacheKey) {
        String cacheKeyMonitorKey = cacheName.getCacheName() + ":" + cacheKey;
        Object cacheKeyMonitor = cacheKeyMonitors.get(cacheKeyMonitorKey);
        if (cacheKeyMonitor == null) {
            cacheKeyMonitor = new Object();
            cacheKeyMonitors.put(cacheKeyMonitorKey, cacheKeyMonitor);
        }
        return cacheKeyMonitor;
    }
    
    /**
     * Remove a cache key synchronization monitor.
     * @param cacheName
     * @param cacheKey
     * @return
     */
    private synchronized void removeCachedKeyMonitor(CacheName cacheName, String cacheKey) {
        String cacheKeyMonitorKey = cacheName.getCacheName() + ":" + cacheKey;
        cacheKeyMonitors.remove(cacheKeyMonitorKey);        
    }
}
