package com.electrocomponents.model.domain.order;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Shruti Yadav
 * Created : 9 Aug 2007 at 10:16:32
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
 * An Enumeration to check whether the user's password matches password on the SAP account.
 * @author Shruti Yadav
 */
public enum OrderPasswordMatch {

    /** Indicates the passwords have matched. */
    PASSED("PASSED"),
    /** Indicates the passwords have not matched. */
    FAILED("FAILED"),
    /** Indicates there is no password.. */
    IGNORED("IGNORED");

    /** The database value used to represent the enum. */
    private final String passwordMatch;

    /**
     * @param passwordMatch {@link #passwordMatch}
     */
    private OrderPasswordMatch(final String passwordMatch) {
        this.passwordMatch = passwordMatch;
    }

    /**
     * @return the passwordMatch
     */
    public String getPasswordMatch() {
        return passwordMatch;
    }

    /**
     * @param passwordMatch the value representing the enum in the database.
     * @return the enum associated with the supplied database value
     */
    public static OrderPasswordMatch value(final String passwordMatch) {
        OrderPasswordMatch tempOrderPasswordMatch = null;
        final OrderPasswordMatch[] values = values();
        for (final OrderPasswordMatch orderPasswordMatch : values) {
            if (passwordMatch.equals(orderPasswordMatch.passwordMatch)) {
                tempOrderPasswordMatch = orderPasswordMatch;
                break;
            }
        }
        return tempOrderPasswordMatch;
    }
}
