package com.electrocomponents.model.domain.order;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Stuart Sim
 * Created : 5th June 2009 at 14:40:00
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
 * Indicates the transport method used to transfer the order to the OPCO.
 * @author Stuart Sim
 */
public enum OrderTransportMethod {

    /** Value for IBM EAI Middleware Queue. */
    EAI("EAI"),

    /** Value for File Transfer Protocol Transfer. */
    FTP("FTP"),

    /** Value SMTP Email Transfer. */
    EMAIL("EMAIL"),

    /** Value for SalesOrderService Submission. */
    JOP("JOP");

    /** The database value used to represent the enum. */
    private final String orderTransportMethod;

    /**
     * @param orderTransportMethod {@link #orderTransportMethod}
     */
    private OrderTransportMethod(final String orderTransportMethod) {
        this.orderTransportMethod = orderTransportMethod;
    }

    /**
     * @return {@link #value}
     */
    public String getOrderTransportMethod() {
        return this.orderTransportMethod;
    }

    /**
     * @param value the value representing the enum in the database
     * @return the enum associated with the supplied database value
     */
    public static OrderTransportMethod value(final String value) {
        OrderTransportMethod otm = null;
        final OrderTransportMethod[] values = values();
        for (final OrderTransportMethod orderTransportMethod : values) {
            if (value.equals(orderTransportMethod.orderTransportMethod)) {
                otm = orderTransportMethod;
                break;
            }
        }
        return otm;
    }
}
