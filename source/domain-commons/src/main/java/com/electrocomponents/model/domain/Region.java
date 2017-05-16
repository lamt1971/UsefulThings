package com.electrocomponents.model.domain;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : C0950079
 * Created : 23 Jul 2008 at 11:59:26
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
 * Simple class holding a single string used to indicate the the region i.e EUROPE/ASIA.
 * @author C0950079
 */
public enum Region {

    /** region EUROPE. */
    EMEA("EMEA"),

    /** region ASIA. */
    APAC("APAC");

    /** region America. */
    // AMER ("AMER");

    /** Holds the Region. */
    private final String region;

    /**
     * Constructs the enumeration.
     * @param region The action to take.
     */
    private Region(final String region) {
        this.region = region;
    }

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param regionIn the value representing the enum
     * @return the enum associated with the supplied value.
     */
    public static Region value(final String regionIn) {
        Region tempRegion = null;

        final Region[] values = values();
        if (regionIn != null && !"".equals(regionIn)) {
            for (final Region region : values) {
                if (region.region.equalsIgnoreCase(regionIn)) {
                    tempRegion = region;
                    break;
                }
            }
        }
        return tempRegion;
    }
}
