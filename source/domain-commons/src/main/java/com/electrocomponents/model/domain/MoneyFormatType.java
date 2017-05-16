/**
 * 
 */
package com.electrocomponents.model.domain;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sandeep Kumar Jain
 * Created : 1 Apr 2015 at 16:10:26
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
 * MoneyFormatType enumeration is used to specify the money format type which can be used as Identifier.
 * Currently it is used to format the display of Unit Price values.
 * In future it can be extended to format/round display the values of line price, goods total, VAT, etc.
 * @author Sandeep Kumar Jain
 */
public enum MoneyFormatType {

    /** Unit Price. */
    UNIT_PRICE("UNIT_PRICE");

    /** * Money format type value. */
    private String moneyFormatType;

    /**
     * Default Constructor.
     * @param formatType the moneyFormatType.
     */
    private MoneyFormatType(final String formatType) {
        this.moneyFormatType = formatType;
    }

    /**
     * Method to get value.
     * @return the Enum value.
     */
    public String getValue() {
        return moneyFormatType;
    }

    /**
     * Method to get value of MoneyFormatType.
     * @param value the value representing the enum
     * @return the MoneyFormatType enum object
     */
    public static MoneyFormatType value(final String value) {
        MoneyFormatType moneyFormatType = null;
        if (value != null) {
            final MoneyFormatType[] values = values();
            for (final MoneyFormatType formatType : values) {
                if (value.equals(formatType.moneyFormatType)) {
                    moneyFormatType = formatType;
                    break;
                }
            }
        }
        return moneyFormatType;
    }

    /**
     * Method to get values of all money format types.
     * @return Array of money format type.
     */
    public static MoneyFormatType[] getValues() {
        return values();
    }
}
