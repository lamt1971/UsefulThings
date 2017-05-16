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
 * The class provides UI specific cache implementation.
 * @author Raja Govindharaj
 */
public class UiCachePathImpl extends AbstractCachePath {

    /**
     * Service Cache Path Impl instance.
     */
    private static final UiCachePathImpl UI_CACHE_PATH_IMPL = new UiCachePathImpl();

    /**
     * Root Service Cache Path.
     */
    private static final String UI_CACHE_PATH_ROOT = "ui-specific";

    /**
     * Singleton object return.
     * @return UiCachePathImpl.
     */
    public static UiCachePathImpl getInstance() {
        return UI_CACHE_PATH_IMPL;
    }

    /** {@inheritDoc} **/
    @Override
    public String getRootPath() {
        return UI_CACHE_PATH_ROOT;
    }

}
