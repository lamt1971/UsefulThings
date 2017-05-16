package com.electrocomponents.model.domain.order;

/**
 *
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ian Collington
 * Created : 1 Aug 2008 at 15:49:29
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
 * Indicates the transfer method for an order.
 * @author Ian Collington
 */
public enum OrderTransferMethod {
    /** */
    NON_INTEGRATED("NON_INTEGRATED"),
    /** */
    INTEGRATED("INTEGRATED"),
    /** Default transfer method if the value is null. */
    UNKNOWN("UNKNOWN");

    /** The database value used to represent the enum. */
    private final String orderTransferMethod;

    /**
     * @param orderTransferMethod {@link #orderTransferMethod}
     */
    private OrderTransferMethod(final String orderTransferMethod) {
        this.orderTransferMethod = orderTransferMethod;
    }

    /**
     * @return {@link #value}
     */
    public String getOrderTransferMethod() {
        return this.orderTransferMethod;
    }

    /**
     * @param value the value representing the enum in the database
     * @return the enum associated with the supplied database value
     */
    public static OrderTransferMethod value(final String value) {
        OrderTransferMethod otm = null;
        final OrderTransferMethod[] values = values();
        for (final OrderTransferMethod orderTransferMethod : values) {
            if (value.equals(orderTransferMethod.orderTransferMethod)) {
                otm = orderTransferMethod;
                break;
            }
        }
        return otm;
    }
}
