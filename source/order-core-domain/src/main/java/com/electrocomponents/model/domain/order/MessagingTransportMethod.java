/**
 */
package com.electrocomponents.model.domain.order;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : <<<<user name>>>>
 * Created : 12 Feb 2010 at 14:02:37
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
 * TODO add class-level javadoc.
 * @author C0950079
 */
public enum MessagingTransportMethod {

    /**  */
    NONE(""),
    /**  */
    EMAIL("EMAIL"),
    /**  */
    SMS("SMS");

    /** The database value used to represent the enum. */
    private final String messagingMethod;

    /**
     * @param messagingMethod {@link #messagingMethod}
     */
    private MessagingTransportMethod(final String messagingMethod) {
        this.messagingMethod = messagingMethod;
    }

    /**
     * @return {@link #value}
     */
    public String getMessagingTransportMethod() {
        return this.messagingMethod;
    }

    /**
     * @param msgMethod the value representing the enum in the database.
     * @return the enum associated with the supplied database value
     */
    public static MessagingTransportMethod value(final String msgMethod) {
        MessagingTransportMethod tmpMsgMethod = null;

        final MessagingTransportMethod[] values = values();
        for (final MessagingTransportMethod msgingMethod : values) {
            if (msgMethod.equals(msgingMethod.messagingMethod)) {
                tmpMsgMethod = msgingMethod;
                break;
            }
        }
        return tmpMsgMethod;
    }

}
