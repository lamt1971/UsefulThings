package com.electrocomponents.commons.cache;

/**
 * @author C0950079
 *
 */
public class CacheKeyGenerator {
	
	/**
	 * cache key separator.
	 */
	private static String CACHE_KEY_SEPARATOR="/"; 

	/**
	 * generates a cache key by appending all different strings to make it a unique key (locale, property-name or label-name)
	 * @param mandatoryKey - this part is mandatory.
	 * @param cacheKeyParts - this is an optional list.
	 * @return cache key value- combined list of string passed in with a separator '/'
	 */
	public static String generateCacheKey(String... cacheKeyParts) {
		StringBuilder keyBuilder = new StringBuilder();	
		
		if(cacheKeyParts != null) {
			for(String cacheKeyPart : cacheKeyParts) {
				keyBuilder.append(CACHE_KEY_SEPARATOR).append(cacheKeyPart);
			}
		}
		
	    return keyBuilder.toString();
	}

}
