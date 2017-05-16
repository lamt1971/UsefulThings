/**
 *
 */
package com.electrocomponents.model.domain.searchzeroresult;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Neeraj Singh
 * Created : 3 Nov 2009 at 09:10:34
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
 * Enum to specify Link Types for Zero result.
 * @author Neeraj Singh
 * @to-do: This enum is need to be renamed to make it as generic as used in zero search result and technical note.
 */
public enum ZeroResultLinkType {

    /** Null value which is required for adding link types in technical note functionality. */
    NULL(""),
    /** Link to a "General Display" campaign by campaign Id. */
    CAMPAIGN("CAMPAIGN"),
    /** Search on the brand name using the brand search interface. */
    BRAND("BRAND"),
    /** Link to a single product. */
    PRODUCT("PRODUCT"),
    /** Run an alternative standard keyword search. */
    SEARCH("SEARCH");

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(ZeroResultLinkType.class);

    /** The value representing the enum in the database. */
    private String value;

    /**
     * @param value {@link #value}
     */
    private ZeroResultLinkType(final String value) {
        this.value = value;
    }

    /**
     * @return {@link #value}
     */
    public String getLinkType() {
        return this.value;
    }

    /**
     * @param value config Type.
     * @return the enum associated with the supplied value
     */
    public static ZeroResultLinkType value(final String value) {
        ZeroResultLinkType linkType = null;
        if (StringUtils.isBlank(value)) {
            return ZeroResultLinkType.NULL;
        }
        for (final ZeroResultLinkType type : values()) {
            if (value.equals(type.value)) {
                linkType = type;
                break;
            }
        }
        if (linkType == null) {
            LOG.warn("link type is null");
        }
        return linkType;
    }

}
