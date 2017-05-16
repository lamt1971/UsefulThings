package com.electrocomponents.model.domain;

import java.io.Serializable;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Chris Butlin
 * Created : 24 Jul 2007 at 12:20:24
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
 * A class to give the site corresponding to a web site. also to distinguish between locale and site. e.g. be is a site, be01 is a locale
 * within the be site.
 * @author Chris Butlin
 */
public class Site implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -1572962930123915580L;

    /**
     * a string giving the id of the web site.
     */
    private String siteString;

    /**
     * empty constructor.
     */
    public Site() {

    }

    /**
     * Create the class using the site_name.
     * @param siteString the site name to use
     */
    public Site(final String siteString) {
        this.siteString = siteString;
    }

    /**
     * @return the siteString
     */
    public String getSiteString() {
        return siteString;
    }

    /**
     * @param siteString the siteString to set
     */
    public void setSiteString(final String siteString) {
        this.siteString = siteString;
    }

    /**
     * Returns a string used to prefix user_aliases. e.g. 'UK_' for site uk.
     * @return the prefix to use
     */
    public String getUserPrefix() {
        return this.siteString.toUpperCase() + "_";
    }

    /**
     * Returns a string used to prefix user_aliases for PM organisations. e.g. 'UK_' for site uk
     * @return the prefix to use
     */
    public String getPmOrgPrefix() {
        return this.siteString.toUpperCase() + "PM_";
    }

    /**
     * the string representation of the site object.
     * @return String representation of the site object
     */
    public String toString() {
        return this.siteString;
    }

    /** {@inheritDoc} */
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((siteString == null) ? 0 : siteString.hashCode());
        return result;
    }

    /** {@inheritDoc} */
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
        final Site other = (Site) obj;
        if (siteString == null) {
            if (other.siteString != null) {
                return false;
            }
        } else if (!siteString.equals(other.siteString)) {
            return false;
        }
        return true;
    }
}
