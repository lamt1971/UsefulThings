package com.electrocomponents.model.domain.jop;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Zende
 * Created : 22 Sep 2009 at 14:45:28
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *  GEN-425 * E0161085 * 21/12/2009 * Added Eprocurement as an Order Type.
 *          *          * 09/02/2010 * Changed this to just MANUAL and removed AUTO option.
 * ************************************************************************************************
 *          *          *            *
 * ************************************************************************************************
 * </pre>
 */

/**
 * TODO add class-level javadoc.
 * @author s.zende
 */
public enum JopOrderVariant {

    /**  */
    STANDARD("STANDARD"),
    /**  */
    QUOTE("QUOTE"),
    /**  */
    FORWARD("FORWARD"),
    /**  */
    RETURN("RETURN"),
    /**  */
    SCHEDULE("SCHEDULE"),
    /**
     * Manual Eprocurement Order.
     */
    STANDARD_MANUAL("MANUAL");

    /** The database value used to represent the enum. */
    private final String orderVariant;

    /**
     * @param orderVariant orderVariant
     */
    private JopOrderVariant(final String orderVariant) {
        this.orderVariant = orderVariant;
    }

    /**
     * @return {@link #value}
     */
    public String getJopOrderVariant() {
        return this.orderVariant;
    }

    /**
     * @param value the value representing the enum in the database
     * @return the enum associated with the supplied database value
     */
    public static JopOrderVariant value(final String value) {
        JopOrderVariant jov = null;
        final JopOrderVariant[] values = values();
        for (final JopOrderVariant jopOrderVariant : values) {
            if (value.equals(jopOrderVariant.orderVariant)) {
                jov = jopOrderVariant;
                break;
            }
        }
        return jov;
    }
}
