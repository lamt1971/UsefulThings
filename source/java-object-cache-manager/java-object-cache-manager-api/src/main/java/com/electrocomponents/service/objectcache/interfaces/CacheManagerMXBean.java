package com.electrocomponents.service.objectcache.interfaces;

public interface CacheManagerMXBean {

    /**
     * Remove a key from the cache using the applicationName suffix.
     * @param applicationName the JNDI name of the application which added this item (e.g. "predictive-search", "ecommerce-desktop-web-app", etc)
     * @param region the cache region, typically taking from the cacheName property of the CacheName enum
     * @param key the key
     */
    void evict(String applicationName, String region, String key);
    
    /**
     * Remove a key from the cache
     * @param applicationName the JNDI name of the application which added this item (e.g. "predictive-search", "ecommerce-desktop-web-app", etc)
     * @param region the cache region, typically taking from the cacheName property of the CacheName enum
     * @param key the key
     */
    void evict(String region, String key);

    /**
     * Clears the requested cache region. Note that this iterates over the cache region's keys and evicts them one by one.
     * @param region
     * @return the number of elements in the cache region at the start of the clear operation, or -1 is this region doesn't exist.
     */
    int clearRegion(String region);
    
    /**
     * Get all the keys and their values for the specified region.
     * @param region
     * @return
     */
    String getKeysAndValues(String region);
    
    /**
     * Get all the keys and their values for the specified region.
     * @param region
     * @param regEx a regular expression - only entries with keys matching this will be shown
     * @return
     */
    String getKeysAndValues(String region, String regEx);

    /**
     * Gets the size of the specified region.
     * @param region
     * @return the number of elements in the cache region, or -1 is this region doens't exist.
     */
    int getCacheRegionSize(String region);
    
    /**
     * Checks if a key exists in the specified cache using the applicationName suffix.
     * @param applicationName
     * @param region
     * @param key
     * @return
     */
    boolean keyExists(String applicationName, String region, String key);
    
    /**
     * Checks if a key exists in the specified cache.
     * @param region
     * @param key
     * @return
     */
    boolean keyExists(String region, String key);
    
    /**
     * Get the value of the specified key using the applicationName suffix.
     * @param applicationName
     * @param region
     * @param key
     * @return
     */
    
    String getValueInfo(String applicationName, String region, String key);
    /**
     * Get the value of the specified key using the applicationName suffix.
     * @param region
     * @param key
     * @return
     */
    String getValueInfo(String region, String key);

}
