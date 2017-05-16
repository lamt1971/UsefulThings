/**
 */
package com.electrocomponents.model.domain.order;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : <<<<user name>>>>
 * Created : 12 Feb 2010 at 14:14:12
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
public enum MessagingRequirement {
    /**  */
    NONE(""),
    /**  */
    ORDER_AND_DISPATCH("OD");

    /** The database value used to represent the enum. */
    private final String messagingRequirement;

    /**
     * @param messagingRequirement {@link #messagingRequirement}
     */
    private MessagingRequirement(final String messagingRequirement) {
        this.messagingRequirement = messagingRequirement;
    }

    /**
     * @return {@link #value}
     */
    public String getMessagingRequirement() {
        return this.messagingRequirement;
    }

    /**
     * @param msgRqmt the value representing the enum in the database.
     * @return the enum associated with the supplied database value
     */
    public static MessagingRequirement value(final String msgRqmt) {
        MessagingRequirement tmpmsgRqmt = null;

        final MessagingRequirement[] values = values();
        for (final MessagingRequirement messagingsgRqmt : values) {
            if (msgRqmt.equals(messagingsgRqmt.messagingRequirement)) {
                tmpmsgRqmt = messagingsgRqmt;
                break;
            }
        }
        return tmpmsgRqmt;
    }

}
