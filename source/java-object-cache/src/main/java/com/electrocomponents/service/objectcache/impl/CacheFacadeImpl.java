package com.electrocomponents.service.objectcache.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;

import com.electrocomponents.commons.cache.CacheName;
import com.electrocomponents.service.core.client.ApplicationNameResolver;
import com.electrocomponents.service.core.concurrent.ViewableFutureTask;
import com.electrocomponents.service.objectcache.interfaces.CacheFacade;
import com.electrocomponents.service.objectcache.interfaces.CacheFacadeLocal;


/**
 * A singleton EJB, abstracts all internal cache implementation details, provides a simple interface to access underlying infinispan cache.
 */
@Singleton(name=CacheFacade.SERVICE_NAME)
@Startup
@Local({CacheFacadeLocal.class, CacheFacade.class})
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class CacheFacadeImpl implements CacheFacade {
	
    /** Logger. */
    private static final Log LOG = LogFactory.getLog(CacheFacadeImpl.class);
    
    /**
     * cache where all in-progress callable objects will be stored. 
     */
    private static final String CALLABLES_CACHE_NAME="callables-cache";
	
	@Resource(lookup="java:jboss/infinispan/container/services") 
	private EmbeddedCacheManager container;
	
	/**
	 * CachableExecutor instance, deals with executing the callables / future tasks and returns the result. 
	 * Internally uses a cache and try to avoid multiple executions of the same callables submitted by multiple requestors.
	 */
	CachableExecutor executorsHandler = null;

	/**
	 * prefix all keys with this to ensure insertions are class-loader specific.
	 */
	private String appID;
	
	@PostConstruct
	public void initialise() {
		Cache<String, FutureTask<Object>> cache = container.getCache(CALLABLES_CACHE_NAME);	
		executorsHandler = new CachableExecutor(cache);
		appID = ApplicationNameResolver.getInstance().getApplicationName();
		LOG.info("Set cache application ID to [" + appID + "].");
	}

	/**
	 * default constructor.
	 */
	public CacheFacadeImpl() {		
	}

	/**
	 * Gets an entry from the cache.
	 * @param key - the key whose associated value is to be returned
	 * @param cacheName - the name of the cache
	 * @return the value, or null, if it does not exist.
	 */
	@Override
	public Object get(CacheName cacheName, String key) {
		
		if(LOG.isDebugEnabled()) {
			LOG.debug("Start get - cacheName: " + cacheName.getCacheName() + ", Key: " + key );	
		}		

		Cache<String, Object> cache = container.getCache(cacheName.getCacheName());
		Object val = cache.get(getAppSpecificKey(key));
		if(LOG.isDebugEnabled()) {
			LOG.debug("Finish get - cacheName: " + cacheName.getCacheName() + ", Key: " + key );
		}
		return val;
	}

	/**
	 * Returns an existing value if one is already existed.
	 * Associates the specified callable with the specified key in this cache (If the cache doesn't contain a mapping for the key already);
	 * Calling code need to make sure the key and callable mapping is correct.
	 * 		
	 * @param key - the key whose associated value is to be returned
	 * @param cacheName - the name of the cache
	 * @param callable
	 */
	@Override
	public <T1> T1 putIfAbsentAndGet(CacheName cacheName, String key, Callable<T1> callable) {
		if(LOG.isDebugEnabled()) {
			LOG.debug("Start putIfAbsentAndGet - cacheName: " + cacheName.getCacheName() + ", Key: " + key );
		}
		//STEP 1. Check if the key is already in cache, if yes return the value if not go-to STEP2
		T1 callableResult = null;
		Cache<String, Object> cache = container.getCache(cacheName.getCacheName());
		callableResult = (T1) cache.get(getAppSpecificKey(key));
		//there's already an object in the cache with this address so don't execute the Callable
		if(callableResult != null ) {			 
			if(LOG.isDebugEnabled()) {
				LOG.debug("Finish putIfAbsentAndGet (cache hit) - cacheName: " + cacheName.getCacheName() + ", Key: " + key );
			}
			return  callableResult;
		}
		
		//STEP 2. Submit this callable for execution and take result		
		
		String jobKey = new StringBuilder(cacheName.getCacheName()).append(":").append(key).toString();
		callableResult = executorsHandler.execute(jobKey, callable);
		
		
		//STEP 3. put it in to cache and clear the callable from executor.
		//TO-DO: make this async.
		if(callableResult != null) {
			cache.put(getAppSpecificKey(key), callableResult);	
		}		
		//remove from temp cache.
		executorsHandler.clear(jobKey);
		if(LOG.isDebugEnabled()) {
			LOG.debug("Finish putIfAbsentAndGet (cache miss) - cacheName: " + cacheName.getCacheName() + ", Key: " + key );
		}
		//STEP 4. return the reault.
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
		if(LOG.isDebugEnabled()) {
			LOG.debug("Start containsKey - cacheName: " + cacheName.getCacheName() + ", Key: " + key );
		}
		Cache<String, Object> cache = container.getCache(cacheName.getCacheName());
		boolean isKeyPresentInCache = cache.containsKey(getAppSpecificKey(key));
		if(LOG.isDebugEnabled()) {
			LOG.debug("Finish containsKey - cacheName: " + cacheName.getCacheName() + ", Key: " + key );
		}
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
		if(LOG.isDebugEnabled()) {
			LOG.debug("Start put - cacheName: " + cacheName.getCacheName() + ", Key: " + key );
		}
		Cache<String, Object> cache = container.getCache(cacheName.getCacheName());
		Object val = cache.put(getAppSpecificKey(key), value);
		if(LOG.isDebugEnabled()) {
			LOG.debug("Finish put - cacheName: " + cacheName.getCacheName() + ", Key: " + key );
		}
		return val;
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
	public Object remove(CacheName cacheName, String key) {
		if(LOG.isDebugEnabled()) {
			LOG.debug("Start remove - cacheName: " + cacheName.getCacheName() + ", Key: " + key );
		}
		Cache<String, Object> cache = container.getCache(cacheName.getCacheName());
		
		if(LOG.isDebugEnabled()) {
			LOG.debug("Finish get - remove: " + cacheName.getCacheName() + ", Key: " + key );
		}
		return cache.remove(getAppSpecificKey(key));
	}
	
	private String getAppSpecificKey(String key) {
	    return appID + "-" + key;
	}
	
	/**
	 * deals with executing the callables / future tasks and returns the result. 
	 * Internally uses a cache and try to avoid multiple executions of the same callables submitted by multiple requestors.
	 */	
	private class CachableExecutor {
	    
		/**
		 * internal cache.
		 */
		private Cache<String, FutureTask<Object>> callableCache = null;
		
		private CachableExecutor(Cache<String, FutureTask<Object>> cache) {		
			this.callableCache = cache;
		}
		/**
		 * checks if a job is already in progress with the same job-id, and executes it if it is not running already. 
		 * @param callbleJobId - identifier for the calllble.
		 * @param callable - actual callable instance which will be executed.
		 * @return the result of callble.
		 */
		<T1> T1 execute(String callbleJobId, Callable<T1> callable) {
			
			T1 finalResult = null;
			FutureTask<Object> futureTask = this.callableCache.get(callbleJobId);
			
			if(futureTask == null) {
				futureTask = new ViewableFutureTask(callable);
				this.callableCache.put(callbleJobId, futureTask);
				futureTask.run();	
				
			}		
			
		try {			
			 finalResult = (T1) futureTask.get();
		} catch (InterruptedException e) {
			LOG.warn("InterruptedException while executing the callable for job id : " + callbleJobId + "exception details : " + e.getMessage());
		} catch (ExecutionException e) {
			LOG.warn("ExecutionException while executing the callable for job id : " + callbleJobId + "exception details : " + e.getMessage());		
		} catch (Throwable vUnexpected) {
            LOG.error("Strange error while executing the callable for job id : " + callbleJobId + "exception details : " + vUnexpected.getMessage(), vUnexpected);
        }
			return finalResult;
		}
		
		/**
		 * Stop all tasks if they are in progress and clear the map / cache.
		 * useful abrupt shutdown of server/vm.
		 */
		@PreDestroy
		void cleardowncache() {
			for(String key : callableCache.keySet()) {
				FutureTask<Object> futureTask = callableCache.get(key);
				if(!futureTask.isDone()) {
					futureTask.cancel(Boolean.TRUE);	
				}	
			}
			callableCache.clear();
		}
		
		/**
		 * removes the callable from the the map / cache. 
		 * This method doesn't abort the task if it is already started and in-progress.
		 * @param callbleJobId - key assigned to the callable.
		 */
		
		public void clear(String callbleJobId ) {
			callableCache.remove(callbleJobId);
		}
		
		/**
		 * removes the callable from the the map / cache. 
		 * This method interrupts the task if it is in-progress.
		 * @param callbleJobId - key assigned to the callable.
		 */

		public void interruptAndclear(String callbleJobId ) {
			FutureTask<Object> futureTask = callableCache.get(callbleJobId);
			if(futureTask != null && !futureTask.isDone()) {
				futureTask.cancel(Boolean.TRUE);
			}
			callableCache.remove(callbleJobId);
		}
	}
}
