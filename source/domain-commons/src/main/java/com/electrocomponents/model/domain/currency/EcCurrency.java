package com.electrocomponents.model.domain.currency;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut
 * Created : 14 May 2009 at 13:01:00
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
 * An enum representing ISO 4217 Currency Codes.
 * @author Ganesh raut
 */
public enum EcCurrency {
    /** ISO 4217 Currency Code for Australia currency. */
    AUD("AUD"),
    /** ISO 4217 Currency Code for Canada currency. */
    CAD("CAD"),
    /** ISO 4217 Currency Code for Chile currency. */
    CLP("CLP"),
    /** ISO 4217 Currency Code for Hong kong currency. */
    HKD("HKD"),
    /** ISO 4217 Currency Code for India currency. */
    INR("INR"),
    /** ISO 4217 Currency Code for Japan currency. */
    JPY("JPY"),
    /** ISO 4217 Currency Code for Malaysia currency. */
    MYR("MYR"),
    /** ISO 4217 Currency Code for New Zealand currency. */
    NZD("NZD"),
    /** ISO 4217 Currency Code for Singapore currency. */
    SGD("SGD"),
    /** ISO 4217 Currency Code for United States currency. */
    USD("USD"),
    /** ISO 4217 Currency Code for Denmark currency. */
    DKK("DKK"),
    /** ISO 4217 Currency Code for Sweden currency. */
    SEK("SEK"),
    /** ISO 4217 Currency Code for Switzerland currency. */
    CHF("CHF"),
    /** ISO 4217 Currency Code for South Africa currency. */
    ZAR("ZAR"),
    /** ISO 4217 Currency Code for European currency. */
    EUR("EUR"),
    /** ISO 4217 Currency Code for Phillipines currency. */
    PHP("PHP"),
    /** ISO 4217 Currency Code for Taiwan currency. */
    TWD("TWD"),
    /** ISO 4217 Currency Code for China currency. */
    CNY("CNY"),
    /** ISO 4217 Currency Code for Norway currency. */
    NOK("NOK"),
    /** ISO 4217 Currency Code for Thailand currency. */
    THB("THB"),
    /** ISO 4217 Currency Code for UK currency. */
    GBP("GBP"),
    /** ISO 4217 Currency Code for Hungarian currency. */
    HUF("HUF"),
    /** ISO 4217 Currency Code for Poland currency. */
    PLN("PLN"),
    /** ISO 4217 Currency Code for Czech Republic currency. */
    CZK("CZK"),
    /** ISO 4217 Currency Code for Korea currency. */
    KRW("KRW"),
    /** ISO 4217 Currency Code for China currency. */
    RMB("RMB"),
    /** ISO 4217 Currency Code for Taiwan currency. */
    NTD("NTD");
    /** The database value used to represent the enum. */
    private final String value;

    /**
     * @param value {@link #value}
     */
    private EcCurrency(final String value) {
        this.value = value;
    }

    /**
     * @return {@link #value}
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @param value the value representing the enum in the database
     * @return the enum associated with the supplied database value
     */
    public static EcCurrency value(final String value) {
        EcCurrency c = null;
        final EcCurrency[] values = values();
        for (final EcCurrency currency : values) {
            if (currency.value.equals(value)) {
                c = currency;
                break;
            }
        }
        return c;
    }
}
