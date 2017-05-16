package com.electrocomponents.model.domain.paymentmethod;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : bharat bonde
 * Created : 27 July 2010 at 16:36:35
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *          * E0161085 * 26/07/2011 * Added Cybersource China and Payease.
 * ************************************************************************************************
 * </pre>
 */

/**
 * An enum class for different payment vendors. For instance Alipay. In future, may add others. e.g.PAYPAL.
 * @author bharat bonde
 */
public enum PaymentProcessor {
    /** External Payment processor is Alipay. */
    ALIPAY("ALIPAY"),
    /** No external payment processor. */
    ERP("ERP");

    /**
     *
     */
    private String value;

    /**
     * @param value String
     */
    private PaymentProcessor(final String value) {
        this.value = value;
    }

    /**
     * @return {@link #value}
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @param payProcessor String
     * @return the enum associated with the supplied value
     */
    public static PaymentProcessor value(final String payProcessor) {
        PaymentProcessor tempProcessor = null;
        for (final PaymentProcessor name : values()) {
            if (payProcessor.equals(name.value)) {
                tempProcessor = name;
                break;
            }
        }
        if (tempProcessor == null) {
            tempProcessor = PaymentProcessor.ERP;
        }
        return tempProcessor;
    }

}
