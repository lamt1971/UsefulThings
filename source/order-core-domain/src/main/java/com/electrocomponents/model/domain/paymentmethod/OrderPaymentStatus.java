/**
 *
 */
package com.electrocomponents.model.domain.paymentmethod;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Bharat Bonde
 * Created : 28 Jul 2010 at 11:55:35
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
 * An enum class for payment status. As of now, it has status's corresponding to Alipay payment processor. New status can be added in here
 * for new payment processor.
 * @author Bharat Bonde
 */
public enum OrderPaymentStatus {

    /** Indicates Payment awaiting. */
    AWATING_PAYMENT("AWATING_PAYMENT"),
    /** Indicates Payment failed. */
    FAILED_PAYMENT("FAILED_PAYMENT"),
    /** Indicates Payment complete. */
    COMPLETED_PAYMENT("COMPLETED_PAYMENT"),
    /** ERP_HANDLED Payment. */
    ERP_HANDLED("ERP_HANDLED"),
    /** Indicates Cancelled payment.*/
    CANCELLED_PAYMENT("CANCELLED_PAYMENT");

    /**
     *
     */
    private String value;

    /**
     * @param value String
     */
    private OrderPaymentStatus(final String value) {
        this.value = value;
    }

    /**
     * @return {@link #value}
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @param paymentStatus String
     * @return the enum associated with the supplied value
     */
    public static OrderPaymentStatus value(final String paymentStatus) {
        OrderPaymentStatus tempStatus = null;
        for (final OrderPaymentStatus status : values()) {
            if (paymentStatus.equals(status.value)) {
                tempStatus = status;
                break;
            }
        }
        if (tempStatus == null) {
            tempStatus = OrderPaymentStatus.ERP_HANDLED;
        }
        return tempStatus;
    }

}
