package com.electrocomponents.continuouspublishing.utility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.electrocomponents.model.domain.Locale;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Semwal
 * Created : 3rd Oct 2007 at 11:56:00
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
 * RegionUtility is a utility class used for getting the region the publication belongs to.
 * @see LoadProperties
 * @author sanjay semwal
 */
public final class RegionUtility {

    /** Constant for europe region. */
    private static final String REGIONEUROPE = "EUROPE";

    /** Constant for non europe region. */
    private static final String REGIONNONEUROPE = "NONEUROPE";

    /** Constant for international region. */
    private static final String REGIONINTERNATIONAL = "INTERNATIONAL";

    /** Logger LOG. */

    private static final Log LOG = LogFactory.getLog(RegionUtility.class);

    /** Constructor. */
    private RegionUtility() {
    }

    /**
     * @param document Document object
     * @return String Region to which the publication belongs to.
     */
    public static String getRegion(final Document document) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start of Method getRegion.");
        }
        String publication = "";
        NodeList list = document.getElementsByTagName("Publication");

        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            publication = element.getAttribute("ID");

        }

        String region = LoadProperties.returnRegion(publication);

        if (region.equalsIgnoreCase(REGIONEUROPE)) {
            region = NameValueMapping.EUROPE_REGION;
        } else if (region.equalsIgnoreCase(REGIONNONEUROPE)) {
            region = NameValueMapping.NON_EUROPE_REGION;
        } else if (region.equalsIgnoreCase(REGIONINTERNATIONAL)) {
            region = NameValueMapping.INTERNATIONAL_REGION;
        } else {
            region = NameValueMapping.EUROPE_REGION;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("End of Method getRegion");
        }
        return region;
    }

    /**
     * This method returns the region name(Europe/Asia) based on Locale name.
     * @param locale locale name
     * @return String Region to which the publication belongs to.
     */
    public static String getRegion(final String locale) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start of Method getRegion");
        }

        String region = LoadProperties.returnRegion(locale);

        if (region.equalsIgnoreCase(REGIONEUROPE)) {
            region = NameValueMapping.EUROPE_REGION;
        } else if (region.equalsIgnoreCase(REGIONNONEUROPE)) {
            region = NameValueMapping.NON_EUROPE_REGION;
        } else if (region.equalsIgnoreCase(REGIONINTERNATIONAL)) {
            region = NameValueMapping.INTERNATIONAL_REGION;
        } else {
            region = NameValueMapping.EUROPE_REGION;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("End of Method getRegion");
        }

        return region;
    }

    /**
     * @param locale locale
     * @return jndiName
     */
    public static String getJndiName(final Locale locale) {

        if (LOG.isInfoEnabled()) {
            LOG.info("Start of Method getJndiName");
        }
        String jndiNameUsed = null;

        String region = RegionUtility.getRegion(locale.toString());

        if (region.equalsIgnoreCase(NameValueMapping.EUROPE_REGION)) {
            jndiNameUsed = NameValueMapping.ENTITY_MANAGER_JNDI_EUROPE;
        } else if (region.equalsIgnoreCase(NameValueMapping.NON_EUROPE_REGION)) {
            jndiNameUsed = NameValueMapping.ENTITY_MANAGER_JNDI_NON_EUROPE;
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("End of Method getJndiName");
        }

        return jndiNameUsed;
    }
}
