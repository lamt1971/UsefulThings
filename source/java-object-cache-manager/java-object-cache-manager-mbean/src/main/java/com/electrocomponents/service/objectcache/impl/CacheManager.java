package com.electrocomponents.service.objectcache.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;

import com.electrocomponents.service.objectcache.interfaces.CacheManagerMXBean;

public class CacheManager implements CacheManagerMXBean {

    private static final Log LOG = LogFactory.getLog(CacheManager.class);
            
    @Resource(lookup="java:jboss/infinispan/container/services") 
    private EmbeddedCacheManager container;
        
    @Override
    public int clearRegion(String region) {
        Cache<String, Object> cache = container.getCache(region);
        if (cache != null) {
            int startSize = cache.size();
            long startTime = System.currentTimeMillis();
            Iterator<String> it = getCacheKeys(cache).iterator();            
            while (it.hasNext()) {
                cache.evict(it.next());
            }
            LOG.info("Took " + (System.currentTimeMillis() - startTime) + " milliseconds to evict " + 
                    (startSize - cache.size()) + " cache entries from region " + region);
            return startSize;
        } else {            
            LOG.warn("Cache " + region + " not found");            
            return -1;
        }
    }
    
    @Override
    public String getKeysAndValues(String region) {
        return getKeysAndValues(region, null);
    }
    
    @Override
    public String getKeysAndValues(String region, String keyRegEx) {
        Pattern p = null;
        if (keyRegEx != null && keyRegEx.length() > 0) {
            p = Pattern.compile(keyRegEx);
        }
        Cache<String, Object> cache = container.getCache(region);
        StringBuilder sb = new StringBuilder();
        if (cache != null) {
            for (String key : getCacheKeys(cache)) {
                if (p == null || p.matcher(key).matches()) {
                    sb.append(key).append(" = ").append(cache.get(key).toString()).append("\n");
                }
            }
        } 
        return sb.toString();
    }
    
    @Override
    public void evict(String applicationName, String region, String key) {
        // TODO: move the creation of this app specific key to a class that can be packaged in the java-object-cache client
        evict(region, getAppSpecificKey(applicationName, key));
    }
    
    @Override
    public void evict(String region, String key) {
        Cache<String, Object> cache = container.getCache(region);
        if (cache != null) {
            cache.evict(key);
        }
    }

    @Override
    public int getCacheRegionSize(String region) {
        Cache<String, Object> cache = container.getCache(region);
        if (cache != null) {
            return cache.size();
        } else {
            LOG.warn("Cache region " + region + " not accessible");
            return -1;
        }
    }
    
    @Override
    public boolean keyExists(String applicationName, String region, String key) {
        return keyExists(region, getAppSpecificKey(applicationName, key));        
    }
    
    @Override
    public boolean keyExists(String region, String key) {
        Cache<String, Object> cache = container.getCache(region);
        if (cache != null) {
            return cache.containsKey(key);
        } else {
            return false;
        }
    }
    
    @Override
    public String getValueInfo(String applicationName, String region, String key) {
        return getValueInfo(region, getAppSpecificKey(applicationName, key));
    }
    
    @Override
    public String getValueInfo(String region, String key) {
        Cache<String, Object> cache = container.getCache(region);
        StringBuilder cachedObjectDetails = new StringBuilder();
        if (cache != null) {
            try {
                Object cachedObject = cache.get(key);
                if (cachedObject != null) {
                    cachedObjectDetails.append("Type: ").append(cachedObject.getClass().getName());
                    cachedObjectDetails.append("Details: ").append(cachedObject.toString());
                } else {
                    cachedObjectDetails.append("Object not found in cache");
                }
            } catch (Exception e) {
                cachedObjectDetails.append("Error retrieving cache object information");
            }
        } else {
            cachedObjectDetails.append("Cache region: ").append(region).append(" not available");
        }
        return cachedObjectDetails.toString();
    }
    
    /**
     * Seriously grotty hack to expose the underlying cache implementation's keyset, which is hidden by default.
     * @param cache
     * @return
     */
    private Set<String> getCacheKeys(Cache<String, Object> cache) {
        try {
            return (Set<String>) cache.getClass().getSuperclass().getMethod("keySet").invoke(cache);
        } catch (Exception e) {
            LOG.error("Unable to get cache keys", e);
            return new HashSet<String>();
        } 
    }
    
    private String getAppSpecificKey(String applicationName, String key) {
        return applicationName + "-" + key;
    }
}
