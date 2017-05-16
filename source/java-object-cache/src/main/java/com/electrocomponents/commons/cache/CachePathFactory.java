package com.electrocomponents.commons.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Raja Govindharaj
 * Created : 21 Jul 2011 at 11:25:16
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
 * The factory class provide factory pattern implementation for cache path generation.
 * @author Raja Govindharaj
 */
public final class CachePathFactory {

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(CachePathFactory.class);

    /**
     * Private Constructor.
     */
    private CachePathFactory() {
        /*
         * Empty Constructor.
         */
    }

    /**
     * The method returns Cache Path instance of implementation.
     * @param cachePathEnum is Cache Path Enum.
     * @return CachePath
     */
    public static CachePath getCachePath(final CachePathEnum cachePathEnum) {
        CachePath cachePath = null;
        if (CachePathEnum.SERVICE_CACHE.equals(cachePathEnum)) {
            cachePath = ServiceCachePathImpl.getInstance();
        } else if (CachePathEnum.UI_CACHE.equals(cachePathEnum)) {
            cachePath = UiCachePathImpl.getInstance();
        } else {
            if (LOG.isWarnEnabled()) {
                LOG.warn("The Cache Path : " + cachePathEnum + " can't have any implmentation. Please provide the implemenatation.");
            }
        }

        return cachePath;

    }

}
