package com.electrocomponents.model.domain;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author : David Duffy Created : 24 Jul 2008 at 12:00:00
 *
 * ************************************************************************************************
 */

/**
 * An enumeration for application source used when creating service exceptions.
 * @author David Duffy
 */
public enum ApplicationSource {

    /** RS web site application source. */
    RS_WEB_SITE(0),
    /** Purchasing manager application source. */
    PURCHASING_MANAGER(1),
    /** adam interface as application source. **/
    ADAM_INTERFACE(2),
    /** Quotation manager application source. */
    QUOTE_MANAGER(4),
    /** SHAPE Services. **/
    SHAPE(5),
    /** Java order pipeline application. **/
    JOP(7),
    /** E_NOTIFICATIONS application. **/
    E_NOTIFICATIONS(8);
    /** The database value used to represent the enum. */
    private final int value;

    /**
     * @param value {@link #value}
     */
    private ApplicationSource(final int value) {
        this.value = value;
    }

    /**
     * @return {@link #value}
     */
    public int getValue() {
        return this.value;
    }

    /**
     * @param value the value representing the enum in the database
     * @return the enum associated with the supplied database value
     */
    public static ApplicationSource valueOf(final int value) {
        ApplicationSource as = null;
        final ApplicationSource[] values = values();
        for (final ApplicationSource applicationSource : values) {
            if (value == applicationSource.value) {
                as = applicationSource;
                break;
            }
        }
        return as;
    }
}
