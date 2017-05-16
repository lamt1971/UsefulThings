package com.electrocomponents.model.domain.order;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Stuart Sim
 * Created : 3 Aug 2007 at 13:41:59
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
 * Represents the stock type of an Order or OrderLineItem, S=Stocked, N=Non-Stocked, M=Mixed (Orders only).
 * @author Stuart Sim
 */
public enum OrderStockType {

    /** Used on both Order and OrderLineItem. */
    STOCKED("S"),
    /** Used on both Order and OrderLineItem. */
    NON_STOCKED("N"),
    /** Used ONLY on Order. */
    MIXED_STOCK_ORDER("M");

    /** The database value used to represent the enum. */
    private final String stockType;

    /**
     * @param stockType {@link #stockType}
     */
    private OrderStockType(final String stockType) {
        this.stockType = stockType;
    }

    /**
     * @return {@link #value}
     */
    public String getStockType() {
        return this.stockType;
    }

    /**
     * @param stockType the value representing the enum in the database.
     * @return the enum associated with the supplied database value
     */
    public static OrderStockType value(final String stockType) {
        OrderStockType ost = null;

        final OrderStockType[] values = values();
        for (final OrderStockType orderStockType : values) {
            if (stockType.equals(orderStockType.stockType)) {
                ost = orderStockType;
                break;
            }
        }
        return ost;
    }
}
