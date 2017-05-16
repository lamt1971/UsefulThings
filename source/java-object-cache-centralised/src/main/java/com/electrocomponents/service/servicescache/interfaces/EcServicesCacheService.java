package com.electrocomponents.service.servicescache.interfaces;

import com.electrocomponents.model.domain.servicescache.EcServicesCache;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Rakesh Kumar
 * Created : 1 May 2012 at 10:47:57
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *          *          *            *
 * ************************************************************************************************
 * </pre>
 */

/**
 * A interface class for the EcServicesCacheServiceBean.
 * @author Rakesh Kumar
 */
public interface EcServicesCacheService {
    
    String EJB_MODULE_NAME = "java-object-cache-centralised";
	
	/**
	 * SLSB service bean name for EcServicesCacheService. 
	 */
	String SERVICE_NAME = "PersistedCacheService";

    /**
     * This method carries out a persist of the EcServicesCacheEntity.
     * @param ecServicesCache the ecCacheServiceEntity object
     * @return EcCacheService object.
     */
    EcServicesCache saveServicesCache(final EcServicesCache ecServicesCache);

    /**
     * This method carries out a search for the Service Cached Data based on the Cache Access Path and Key.
     * @param cacheAccessPath as Cache Access Path.
     * @param cacheKey as Cache Key
     * @return an object of EcServicesCache Record.
     */
    EcServicesCache getServicesCache(final String cacheAccessPath, final String cacheKey);

    /**
     * This method carries out a deletion for the Service Cached Data based on the Cache Access Path and Key.
     * @param ecServicesCache as EcCacheServiceEntity.
     * @return a Boolean if EcCacheService Record deleted successfully.
     */
    Boolean deleteServicesCache(final EcServicesCache ecServicesCache);

    /**
     * Method to get entity instance from DAO using the entity interface class.
     * @param entityInterfaceClass Entity interface.
     * @param <T> return type of entity.
     * @return <T> T Entity instance retrieved using Daomk2
     */
    <T> T getInstance(final Class entityInterfaceClass);

}
