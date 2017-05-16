/**
 *
 */
package com.electrocomponents.model.domain;

import java.io.Serializable;

/*
 * ************************************************************************************************ Copyright (c)
 * Electrocomponents Plc. Author : Chris Butlin Created : 12 Jul 2007 at 09:43:03
 * ************************************************************************************************ Change History
 * ************************************************************************************************ Ref * Who * Date *
 * Description ************************************************************************************************ C Butlin
 * 13/07/07 Added constructors C Butlin 24/07/07 Made Serailazable, modify methods
 * ************************************************************************************************
 */

/**
 * Simple class holding a single string usually used to indicate the particular locale within the web sites e.g. be01 indicates the Belgian
 * web site displayed in French
 * @author Chris Butlin
 */
public class Locale implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 3171413440218941050L;

    /**
     * a string used to indicate the particular locale within the web sites e.g. be01 indicates the Belgian web site displayed in French
     */
    private String locale;

    /**
     * Empty constructor.
     */
    public Locale() {
    }

    /**
     * Create a Locale object with a locale string.
     * @param locale the locale string to set
     */
    public Locale(final String locale) {
        setLocale(locale);
    }

    /**
     * @deprecated
     * @return the locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     * @return the locale
     */
    public String getLocaleString() {
        return locale;
    }

    /**
     * @param locale the locale string to set
     */
    public void setLocale(final String locale) {
        this.locale = locale;
    }

    /**
     * @return String representation of Locale
     */
    public String toString() {
        return locale;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((locale == null) ? 0 : locale.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Locale other = (Locale) obj;
        if (locale == null) {
            if (other.locale != null) {
                return false;
            }
        } else if (!locale.equals(other.locale)) {
            return false;
        }
        return true;
    }
}
