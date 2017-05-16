package com.electrocomponents.model.domain;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : C0950079
 * Created : 5 Sep 2008 at 17:23:28
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
 * VAT Code used in Product and Customer Entities.
 * @author C0950079
 */
public enum VatCode {

    /** */
    EXEMPT("0"),
    /** */
    FULL_RATE("1"),
    /** */
    LOW_RATE("2"),
    /** */
    ZERO_RATE("3"),
    /** */
    SERVICE_FULL_RATE("4");

    /** The database value used to represent the enum. */
    private final String value;

    /**
     * @param value {@link #value}
     */
    private VatCode(final String value) {
        this.value = value;
    }

    /**
     * @return {@link #value}
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @param value the value representing the Vatcode value in the database
     * @return the enum associated with the supplied database value
     */
    public static VatCode valueOf(final int value) {
        VatCode vatCode = null;
        final VatCode[] values = values();
        for (final VatCode vatCodeTemp : values) {
            if (String.valueOf(value).equalsIgnoreCase(vatCodeTemp.value)) {
                vatCode = vatCodeTemp;
                break;
            }
        }
        return vatCode;
    }
}
