package com.electrocomponents.model.domain.order;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Stuart Sim
 * Created : 3 Aug 2007 at 14:12:07
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *          *          *            *
 * ************************************************************************************************
 */

/**
 * Represents the type of an order O=Standard order, Q=Quote (unpriced).
 * @author Stuart Sim
 */
public enum OrderType {

    /**  */
    ORDER("O"),
    /**  */
    QUOTE("Q"),
    /**  */
    FORWARD("F"),
    /**  */
    RETURN("R"),
    /**  */
    SCHEDULED("S");

    /** The database value used to represent the enum. */
    private final String type;

    /**
     * @param orderType {@link #orderType}
     */
    private OrderType(final String orderType) {
        this.type = orderType;
    }

    /**
     * @return {@link #value}
     */
    public String getOrderType() {
        return this.type;
    }

    /**
     * @param type the value representing the enum in the database.
     * @return the enum associated with the supplied database value
     */
    public static OrderType value(final String type) {
        OrderType ot = null;

        final OrderType[] values = values();
        for (final OrderType orderType : values) {
            if (type.equals(orderType.type)) {
                ot = orderType;
                break;
            }
        }
        return ot;
    }
}
