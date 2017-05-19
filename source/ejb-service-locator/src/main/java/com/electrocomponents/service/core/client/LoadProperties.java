package com.electrocomponents.service.core.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.commons.components.general.io.PropsLoader;
/**
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut
 * Created : 24 Aug 2007 at 14:44:56
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *          *          *            *
 * ************************************************************************************************
 */
import com.electrocomponents.model.domain.ApplicationSource;
import com.electrocomponents.service.exception.ServiceException;

/**
 * A class which loads the supported locales in the map for respective regions from the properties file. These list are being used in base
 * locator to locate the different services.
 * @see BaseLocator
 * @author Ganesh Raut
 */
public final class LoadProperties {

    /** Key in map to identify the list of locales in europe region. */
    public static final String MAP_KEY_EMEA_REGION_SUPPORTED_LOCALES = "emeaRegionSupportedLocales";

    /** Key in map to identify the list of locales in asia region. */
    public static final String MAP_KEY_APAC_REGION_SUPPORTED_LOCALES = "apacRegionSupportedLocales";

    /** Key in map to identify the list of locales in usa locales. */
    public static final String MAP_KEY_AMER_REGION_SUPPORTED_LOCALES = "amerRegionSupportedLocales";

    /** Key in map to identify the region in which this host belongs (emea/apac/amer/disable). */
    public static final String MAP_KEY_LOCAL_REGION = "localRegion";

    /** Key in map to identify hosts in europe. */
    public static final String MAP_KEY_EMEA_REGION_HOSTS = "emeaRegionHosts";

    /** Key in map to identify hosts in asia. */
    public static final String MAP_KEY_APAC_REGION_HOSTS = "apacRegionHosts";

    /** Key in map to identify hosts in america. */
    public static final String MAP_KEY_AMER_REGION_HOSTS = "amerRegionHosts";

    /** Key in map to identify global hosts. */
    public static final String MAP_KEY_GLOBAL_REGION_HOSTS = "globalRegionHosts";

    /** Value in map for region name. */
    public static final String MAP_VALUE_REGION_NAME_EMEA = "emea";

    /** Value in map for region name. */
    public static final String MAP_VALUE_REGION_NAME_APAC = "apac";

    /** Value in map for region name. */
    public static final String MAP_VALUE_REGION_NAME_AMER = "amer";

    /** Value in map for region name. */
    public static final String MAP_VALUE_REGION_NAME_GLOBAL = "global";

    /** Value in map when the current host is not in a region. */
    public static final String MAP_VALUE_REGION_NAME_DISABLE = "disable";

    /** The Map which contains the configuration details for service locators. */
    private static Map<String, Object> map = null;

    /** The Map which contains the configuration details for service locators. */
    public static final String SERVICE_CONFIG_PROPERTIES_FILE_NAME = "service-config.properties";

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(LoadProperties.class);

    /** Constructor. */
    private LoadProperties() {
    }

    /**
     * @return The map which contains list of supported locales with the region name as a key.
     */
    public static synchronized Map<String, Object> loadSupportedLocales() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Method Start");
        }
        
        if (map != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Method Finish - Cached Properties found.");
            }
            return map;
        } else {
            ;
            map = new ConcurrentHashMap<String, Object>();
            Properties props = null;
            try {
            	props = PropsLoader.loadPropertiesFileFromResource(SERVICE_CONFIG_PROPERTIES_FILE_NAME);
                String emeaRegionSupportedLocales = (String) props.get(MAP_KEY_EMEA_REGION_SUPPORTED_LOCALES);
                String[] emeaLocaleList = emeaRegionSupportedLocales.split(",");
                List<String> emeaRegionSupportedLocalesList = new ArrayList<String>();
                for (int i = 0; i < emeaLocaleList.length; i++) {
                    // emeaRegionSupportedLocalesList.add(emeaLocaleList[i]);
                    addStringToList(emeaRegionSupportedLocalesList, emeaLocaleList, i);
                }
                map.put(MAP_KEY_EMEA_REGION_SUPPORTED_LOCALES, emeaRegionSupportedLocalesList);

                String apacRegionSupportedLocales = (String) props.get(MAP_KEY_APAC_REGION_SUPPORTED_LOCALES);
                String[] apacLocaleList = apacRegionSupportedLocales.split(",");
                List<String> apacRegionSupportedLocalesList = new ArrayList<String>();
                for (int i = 0; i < apacLocaleList.length; i++) {
                    // apacRegionSupportedLocalesList.add(apacLocaleList[i]);
                    addStringToList(apacRegionSupportedLocalesList, apacLocaleList, i);
                }
                map.put(MAP_KEY_APAC_REGION_SUPPORTED_LOCALES, apacRegionSupportedLocalesList);

                String amerRegionSupportedLocales = (String) props.get(MAP_KEY_AMER_REGION_SUPPORTED_LOCALES);
                String[] amerLocaleList = amerRegionSupportedLocales.split(",");
                List<String> amerRegionSupportedLocalesList = new ArrayList<String>();
                for (int i = 0; i < amerLocaleList.length; i++) {
                    // amerRegionSupportedLocalesList.add(amerLocaleList[i]);
                    addStringToList(amerRegionSupportedLocalesList, amerLocaleList, i);
                }
                map.put(MAP_KEY_AMER_REGION_SUPPORTED_LOCALES, amerRegionSupportedLocalesList);

                String localRegion = (String) props.get(MAP_KEY_LOCAL_REGION);
                if (localRegion != null) {
                    map.put(MAP_KEY_LOCAL_REGION, localRegion.trim());
                }

                String emeaRegionHosts = (String) props.get(MAP_KEY_EMEA_REGION_HOSTS);
                if (emeaRegionHosts != null) {
                    map.put(MAP_KEY_EMEA_REGION_HOSTS, emeaRegionHosts.trim());
                }
                String apacRegionHosts = (String) props.get(MAP_KEY_APAC_REGION_HOSTS);
                if (apacRegionHosts != null) {
                    map.put(MAP_KEY_APAC_REGION_HOSTS, apacRegionHosts.trim());
                }
                String amerRegionHosts = (String) props.get(MAP_KEY_AMER_REGION_HOSTS);
                if (amerRegionHosts != null) {
                    map.put(MAP_KEY_AMER_REGION_HOSTS, amerRegionHosts.trim());
                }
                String globalRegionHosts = (String) props.get(MAP_KEY_GLOBAL_REGION_HOSTS);
                if (globalRegionHosts != null) {
                    map.put(MAP_KEY_GLOBAL_REGION_HOSTS, globalRegionHosts.trim());
                }

                // Get the Purchasing Manager hosts if they exist
                String pmEmeaRegionHosts = (String) props.get("pm_" + MAP_KEY_EMEA_REGION_HOSTS);
                if (pmEmeaRegionHosts != null) {
                    map.put("pm_" + MAP_KEY_EMEA_REGION_HOSTS, pmEmeaRegionHosts.trim());
                }
                String pmApacRegionHosts = (String) props.get("pm_" + MAP_KEY_APAC_REGION_HOSTS);
                if (pmApacRegionHosts != null) {
                    map.put("pm_" + MAP_KEY_APAC_REGION_HOSTS, pmApacRegionHosts.trim());
                }
                String pmAmerRegionHosts = (String) props.get("pm_" + MAP_KEY_AMER_REGION_HOSTS);
                if (pmAmerRegionHosts != null) {
                    map.put("pm_" + MAP_KEY_AMER_REGION_HOSTS, pmAmerRegionHosts.trim());
                }

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Method Finish - Loaded from classpath.");
                }
                return map;

            } catch (Exception e) {
                String errorMeggage = "Fatal error in loading Properties file.";
                LOG.fatal(errorMeggage, e);
                throw new ServiceException(e, ApplicationSource.RS_WEB_SITE);
            }
        }
    }

    /**
     * List all locales in the emea region. Assumes that loadSupportedLocales() has already been called.
     * @return the list.
     */
    @SuppressWarnings("unchecked")
    public static List<String> getEmeaLocales() {
        return (List<String>) map.get(MAP_KEY_EMEA_REGION_SUPPORTED_LOCALES);
    }

    /**
     * @param strList the strings list
     * @param source the string to add
     * @param index position
     */
    protected static void addStringToList(final List<String> strList, final String[] source, final int index) {
        String val = source[index];
        if (val != null) {
            val = val.trim();
            if (val.length() > 0) {
                strList.add(val);
            }
        }
    }

}
