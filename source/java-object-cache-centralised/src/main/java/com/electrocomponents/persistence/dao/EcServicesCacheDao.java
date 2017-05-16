package com.electrocomponents.persistence.dao;

import javax.ejb.Local;

import com.electrocomponents.model.domain.servicescache.EcServicesCache;
import com.electrocomponents.persistence.daomk2.GenericDaoMk2;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Rakesh Kumar
 * Created : 30 Apr 2012 at 17:32:12
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
 * A DAO class for CRUD operations of EcServicesCacheEntity.
 * @author Rakesh Kumar
 */
@Local
public interface EcServicesCacheDao extends GenericDaoMk2<EcServicesCache> {

    /**
     * @param ecServicesCache the EcServicesCache object
     * @return EcServicesCache object.
     */
    EcServicesCache saveEcServicesCache(final EcServicesCache ecServicesCache);

    /**
     * Find the EcServicesCacheEntities based on Key and Cache Access Path parameters.
     * @param cacheAccessPath the Cache Access Path.
     * @param cacheKey the cacheKey parameter.
     * @return a object of EcServicesCache
     */
    EcServicesCache getEcServicesCache(final String cacheAccessPath, final String cacheKey);

    /**
     * @param ecServicesCache an EcServicesCache object
     * @return true on successful deletion / false on failure
     */
    boolean deleteEcServicesCache(final EcServicesCache ecServicesCache);

}
