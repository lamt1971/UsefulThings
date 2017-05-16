package com.electrocomponents.continuouspublishing.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Semwal
 * Created : 25th Sep 2007 at 16:20:00
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
 * The class which loads the publication belonging to europe, noneurope and international regions from the properties file.
 * @author sanjay semwal
 */
public final class LoadProperties {

    /** The Map which contains the publications for different regions. */
    private static Map<String, List<String>> map = null;

    /** The property file name. */
    private static String propertiesFileName = "com.electrocomponents.continuouspublishing.utility.region";

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(LoadProperties.class);

    /** LoadProperties instance. */
    private static LoadProperties loadProperties = new LoadProperties();

    /** Constructor. */
    private LoadProperties() {

        loadSupportedRegions();
    }

    /**
     * @return LoadProperties instance
     */
    public static LoadProperties getInstance() {

        if (loadProperties == null) {
            loadProperties = new LoadProperties();
        }
        return loadProperties;
    }

    /**
     * The map which contains list of publications for a region (Regions are europe, noneurope, international).
     */
    private static void loadSupportedRegions() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start loadSupportedRegions.");
        }

        if (map != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Finish loadSupportedRegions(1).");
            }
            return;
        } else {

            map = new HashMap<String, List<String>>();

            try {

                ResourceBundle rb = ResourceBundle.getBundle(propertiesFileName);
                List<String> europeRegionSupportList = new ArrayList<String>();

                String europeRegions = (String) rb.getObject(NameValueMapping.EUROPE_REGION);
                String[] europeRegionList = europeRegions.split(",");

                for (int i = 0; i < europeRegionList.length; i++) {

                    europeRegionSupportList.add(europeRegionList[i]);
                }
                map.put("europeRegionSupportList", europeRegionSupportList);

                String nonEuropeRegions = (String) rb.getObject(NameValueMapping.NON_EUROPE_REGION);
                String[] nonEuropeRegionList = nonEuropeRegions.split(",");
                List<String> nonEuropeRegionSupportList = new ArrayList<String>();
                for (int i = 0; i < nonEuropeRegionList.length; i++) {
                    nonEuropeRegionSupportList.add(nonEuropeRegionList[i]);
                }
                map.put("nonEuropeRegionSupportList", nonEuropeRegionSupportList);

                String internationalRegions = (String) rb.getObject(NameValueMapping.INTERNATIONAL_REGION);
                String[] internationalRegionList = internationalRegions.split(",");
                List<String> internationalRegionSupportList = new ArrayList<String>();
                for (int i = 0; i < internationalRegionList.length; i++) {

                    internationalRegionSupportList.add(internationalRegionList[i]);
                }
                map.put("internationalRegionSupportList", internationalRegionSupportList);

                // return map;
            } catch (Exception e) {
                String errorMeggage = "Fatal error in loading Properties file.";
                LOG.fatal(errorMeggage, e);

            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Finish loadSupportedRegions(2).");
            }

        }
    }

    /**
     * @param publicationName Publication name
     * @return String Region to which the a particular publication belongs to
     */

    public static String returnRegion(final String publicationName) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Start returnRegion.");
        }
        // default region is EUROPE
        String publicationRegion = NameValueMapping.EUROPE_REGION;
        try {

            List<String> europeRegionSupportList = (List<String>) map.get("europeRegionSupportList");

            for (String publication : europeRegionSupportList) {

                if (publication.equalsIgnoreCase(publicationName)) {
                    publicationRegion = NameValueMapping.EUROPE_REGION;
                }
            }

            List<String> nonEuropeRegionSupportList = (List<String>) map.get("nonEuropeRegionSupportList");

            for (String publication : nonEuropeRegionSupportList) {

                if (publication.equalsIgnoreCase(publicationName)) {
                    publicationRegion = NameValueMapping.NON_EUROPE_REGION;
                }
            }
            List<String> internationalRegionSupportList = (List<String>) map.get("internationalRegionSupportList");
            for (String publication : internationalRegionSupportList) {

                if (publication.equalsIgnoreCase(publicationName)) {
                    publicationRegion = NameValueMapping.INTERNATIONAL_REGION;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return publicationRegion;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("publicationregion is  " + publicationRegion);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish returnRegion.");
        }
        return publicationRegion;
    }
}
