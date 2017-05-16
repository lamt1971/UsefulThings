/**
 * Package defining order domain objects and enums.
 */
package com.electrocomponents.model.domain.order;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Enum class for System ID information.
 *
 * Author  : Himanshu Goyal.
 * Created : 23 Oct 2008 at 15:54:12
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
 * Enum to specify source system: Mobile/Web/Eproc/PmUser.
 * @author Himanshu Goyal.
 */
public enum EcSystemId {

    /** MOBILE System. */
    MOBILE("MOBILE"),

    /** RS Website. */
    WEB("WEB"),

    /** EPROC. */
    EPROC("EPROC"),

    /** DSPCB. */
    DSPCB("DSPCB"),

    /** DSMECH. */
    DSMECH("DSMECH"),

    /** Quote Manager. */
    QM("QM"),

    /** Purchasing Manager. */
    PM("PM"),

    /** Java order pipeline. */
    JOP("JOP");

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(EcSystemId.class);

    /** The value representing the enum in the database. */
    private String value;

    /**
     * @param value {@link #value}
     */
    private EcSystemId(final String value) {
        this.value = value;
    }

    /**
     * @return {@link #value}
     */
    public String getSystemId() {
        return this.value;
    }

    /**
     * @param value Source of order placement: Mobile/Web
     * @return the enum associated with the supplied value
     */
    public static EcSystemId value(final String value) {
        EcSystemId ecSystemId = null;
        for (final EcSystemId orderPlacementSource : values()) {
            if (value.equals(orderPlacementSource.value)) {
                ecSystemId = orderPlacementSource;
                break;
            }
        }
        if (ecSystemId == null) {
            LOG.warn("Creation System Id is null");
        }
        return ecSystemId;
    }
}
