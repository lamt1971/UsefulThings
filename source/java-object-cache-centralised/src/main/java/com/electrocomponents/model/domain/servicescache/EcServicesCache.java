package com.electrocomponents.model.domain.servicescache;

import com.electrocomponents.model.domain.DateTime;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Rakesh Kumar
 * Created : 30 Apr 2012 at 14:21:32
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
 *  An interface for EcServicesCacheEntity.
 * @author Rakesh Kumar
 */
public interface EcServicesCache {

    /**
     * The getServicesCacheId is getter method.
     * @return servicesCacheId
     */
    Long getServicesCacheId();

    /**
    * The setServicesCacheId is Setter method.
     * @param servicesCacheId as services Cache Id
     */
    void setServicesCacheId(final Long servicesCacheId);

    /**
     * The setCacheAccessPath is Setter method.
     * @param cacheAccessPath Cache Access Path parameter
     */

    void setCacheAccessPath(final String cacheAccessPath);

    /**
     * The getCacheAccessPath is getter method.
     * @return cacheAccessPath Cache Access Path
     */
    String getCacheAccessPath();

    /**
     * The getCacheKey is getter method.
     * @return cacheKey
     */
    String getCacheKey();

    /**
     * The setCacheKey is Setter method.
     * @param cacheKey Cache Key parameter
     */
    void setCacheKey(final String cacheKey);

    /**
     * The getLastModifiedTime is getter method.
     * @return the lastModifiedTime
     */
    DateTime getLastModifiedTime();

    /**
     * The setLastModifiedTime is Setter method.
     * @param lastModifiedTime last update DateTime parameter
     */
    void setLastModifiedTime(final DateTime lastModifiedTime);

    /**
     * The getValue is getter method.
     * @return value
     */
    byte[] getValue();

    /**
     * The setValue is Setter method.
     * @param value Value parameter
     */
    void setValue(final byte[] value);
}
