package com.electrocomponents.model.domain.order;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Stuart Sim
 * Created : 3 Aug 2007 at 11:39:14
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
 * Indicates the transfer method and status of an order from eCommerce to the appopriate OPCO / ERS.
 * @author Stuart Sim
 */
public enum OrderTransferStatus {
    /** */
    ORDER_AWAITING_EAI_TRANSFER("0"),
    /** */
    ORDER_TRANSFERRED_VIA_EAI("1"),
    /** */
    ORDER_AWAITING_FTP_TRANSFER("2"),
    /** */
    ORDER_TRANSFERRED_VIA_FTP("3"),
    /** */
    ORDER_AWAITING_EMAIL_TRANSFER("4"),
    /** */
    ORDER_TRANSFERRED_VIA_EMAIL("5"),
    /** */
    ORDER_TRANSFERRED_NOT_REQUIRED("x"),
    /** Default Status if the value is null. */
    ORDER_STATUS_UNKNOWN("999");

    /** The database value used to represent the enum. */
    private final String status;

    /**
     * @param status {@link #status}
     */
    private OrderTransferStatus(final String status) {
        this.status = status;
    }

    /**
     * @return {@link #value}
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Method which decides the status of the order before sending on statging table.
     * @param transferMethod transfer method
     * @return OrderTransferStatus OrderTransferStatus
     */
    public static OrderTransferStatus getPendingOrderTransferStatus(final OrderTransportMethod transferMethod) {
        if (OrderTransportMethod.EMAIL.equals(transferMethod)) {
            return OrderTransferStatus.ORDER_AWAITING_EMAIL_TRANSFER;
        } else if (OrderTransportMethod.FTP.equals(transferMethod)) {
            return OrderTransferStatus.ORDER_AWAITING_FTP_TRANSFER;
        } else if (OrderTransportMethod.EAI.equals(transferMethod)) {
            return OrderTransferStatus.ORDER_AWAITING_EAI_TRANSFER;
        } else if (OrderTransportMethod.JOP.equals(transferMethod)) {
            return OrderTransferStatus.ORDER_TRANSFERRED_NOT_REQUIRED;
        } else {
            return OrderTransferStatus.ORDER_STATUS_UNKNOWN;
        }
    }

    /**
     * @param status the value representing the enum in the database
     * @return the enum associated with the supplied database value
     */
    public static OrderTransferStatus value(final String status) {
        OrderTransferStatus ots = null;
        if (status == null) {
            return ots;
        }
        final OrderTransferStatus[] values = values();
        for (final OrderTransferStatus orderTransferStatus : values) {
            if (status.equals(orderTransferStatus.status)) {
                ots = orderTransferStatus;
                break;
            }
        }
        return ots;
    }
}
