package com.electrocomponents.model.domain.jop;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Zende
 * Created : 22nd September 2009 at 14:50:00
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *  GEN-425 * E0161085 * 21/12/2009 * Added Eprocurement as an Order Type.
 *          *          * 09/02/2010 * Now removed. Eproc will use STANDARD.
 * ************************************************************************************************
 * </pre>
 */

/**
 * Represents the type of an order type used in order pipeline.
 * @author s.zende
 */
public enum JopOrderType {

    /**  */
    STANDARD("STANDARD"),
    /**  */
    PRE_PAID("PRE_PAID"),
    /**  */
    EXPORT("EXPORT"),
    /**  */
    QUOTATION("QUOTATION");

    /** The database value used to represent the enum. */
    private final String type;

    /**
     * @param orderType {@link #orderType}
     */
    private JopOrderType(final String orderType) {
        this.type = orderType;
    }

    /**
     * @return {@link #value}
     */
    public String getJopOrderType() {
        return this.type;
    }

    /**
     * @param type the value representing the enum in the database.
     * @return the enum associated with the supplied database value
     */
    public static JopOrderType value(final String type) {
        JopOrderType ot = null;

        final JopOrderType[] values = values();
        for (final JopOrderType orderType : values) {
            if (type.equals(orderType.type)) {
                ot = orderType;
                break;
            }
        }
        return ot;
    }
}
