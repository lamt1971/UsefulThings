package com.electrocomponents.commons.cache;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Raja Govindharaj
 * Created : 21 Jul 2011 at 13:07:36
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
 * The class provides service specific cache implementation.
 * @author Raja Govindharaj
 */
public class ServiceCachePathImpl extends AbstractCachePath {

    /**
     * Service Cache Path Impl instance.
     */
    private static final ServiceCachePathImpl SERVICE_CACHE_PATH_IMPL = new ServiceCachePathImpl();

    /**
     * Root Service Cache Path.
     */
    private static final String SERVICE_CACHE_PATH_ROOT = "service-specific";

    /**
     * Singleton object return.
     * @return ServiceCachePathImpl.
     */
    public static ServiceCachePathImpl getInstance() {
        return SERVICE_CACHE_PATH_IMPL;
    }

    /** {@inheritDoc} **/
    @Override
    public String getRootPath() {
        return SERVICE_CACHE_PATH_ROOT;
    }

}
