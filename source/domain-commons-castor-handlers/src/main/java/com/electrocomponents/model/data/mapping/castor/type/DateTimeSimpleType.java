package com.electrocomponents.model.data.mapping.castor.type;

import java.io.Serializable;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 * Author  : Yogesh Patil.
 * Created : 18 Sep 2008 at 16:20:08
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 * *          *            *
 * ************************************************************************************************
 * </pre>
 */

/**
 * This class acts as simple wrapper for DateTime field.
 * @author Yogesh Patil.
 */
public class DateTimeSimpleType implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6471376665873081151L;

    /** The date format. */
    private String dateFormat;

    /** The date string. */
    private String dateString;

    /** The timezone id. */
    private String timezoneId;

    /** The locale language. */
    private String localeLanguage;

    /** The locale country. */
    private String localeCountry;

    /** The locale variant. */
    private String localeVariant;

    /**
     * Gets the date format.
     * @return the dateFormat
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * Sets the date format.
     * @param dateFormat the dateFormat to set
     */
    public void setDateFormat(final String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * Gets the date string.
     * @return the dateString
     */
    public String getDateString() {
        return dateString;
    }

    /**
     * Sets the date string.
     * @param dateString the dateString to set
     */
    public void setDateString(final String dateString) {
        this.dateString = dateString;
    }

    /**
     * Gets the timezone id.
     * @return the timezoneId
     */
    public String getTimezoneId() {
        return timezoneId;
    }

    /**
     * Sets the timezone id.
     * @param timezoneId the timezoneId to set
     */
    public void setTimezoneId(final String timezoneId) {
        this.timezoneId = timezoneId;
    }

    /**
     * Gets the locale country.
     * @return the localeCountry
     */
    public String getLocaleCountry() {
        return localeCountry;
    }

    /**
     * Sets the locale country.
     * @param localeCountry the localeCountry to set
     */
    public void setLocaleCountry(final String localeCountry) {
        this.localeCountry = localeCountry;
    }

    /**
     * Gets the locale language.
     * @return the localeLanguage
     */
    public String getLocaleLanguage() {
        return localeLanguage;
    }

    /**
     * Sets the locale language.
     * @param localeLanguage the localeLanguage to set
     */
    public void setLocaleLanguage(final String localeLanguage) {
        this.localeLanguage = localeLanguage;
    }

    /**
     * Gets the locale variant.
     * @return the localeVariant
     */
    public String getLocaleVariant() {
        return localeVariant;
    }

    /**
     * Sets the locale variant.
     * @param localeVariant the localeVariant to set
     */
    public void setLocaleVariant(final String localeVariant) {
        this.localeVariant = localeVariant;
    }

}
