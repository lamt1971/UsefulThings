package com.electrocomponents.commons.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Raja Govindharaj
 * Created : 21 Jul 2011 at 11:25:42
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
 * The enum class provides constant for specific cache path generation.
 * @author Raja Govindharaj
 */
public enum CachePathEnum {
    /** Large Image type. */
    SERVICE_CACHE("SERVICE"),

    /** Simple Image type. */
    UI_CACHE("UI");

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(CachePathEnum.class);

    /** Cache Path enum value. */
    private final String cachePathEnumValue;

    /**
     * Private constructor, used to set the cache path.
     * @param cachePathEnumValue value of the type of image
     */
    private CachePathEnum(final String cachePathEnumValue) {
        this.cachePathEnumValue = cachePathEnumValue;
    }

    /**
     * Returns a string representation of the value of the image.
     * @return imageTypeVal String value pointing to the image
     */
    public String getCachePathVal() {
        return cachePathEnumValue;
    }

    /**
     * Returns the enum associated with the supplied value.
     * @param value given symbol value
     * @return ImageTypeEnum that match the given value parameter
     */
    public static CachePathEnum value(final String value) {
        CachePathEnum it = null;
        final CachePathEnum[] values = values();
        for (final CachePathEnum cachePathEnum : values) {
            if (cachePathEnum.cachePathEnumValue.equals(value)) {
                it = cachePathEnum;
                break;
            }
        }
        if (it == null) {
            LOG.warn("Cache Path e Enum is null");
        }
        return it;
    }
}
