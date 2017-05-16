package com.electrocomponents.commons.components.general.io;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : e0180383
 * Created : 20 Jun 2006 at 12:30:10
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date        * Description
 * ************************************************************************************************
 * New      * e0180383 * 20-Jun-2006 * Utility methods for Properties loading and manipulation.
 * ************************************************************************************************
 * </pre>
 */

/**
 * An utility class for Properties loading and manipulation.
 * 
 * @author e0180383
 */
public class PropsLoader {

	/** logger. */
    private static final Log log = LogFactory.getLog(PropsLoader.class);
	
	private PropsLoader(){}

    /**
     * Creates a Properties object which contains a subset of all available System properties. 
     * Only properties who's keys start with the propertyFilename (minus the .properties bit) are added to this
     * set. This is to preserve the existing properties loading functionality, where a client can 
     * ask for a properties file by name. The properties files have now been merged into the system properties (in 
     * standalone.xml or domain.xml), and each of these merged properties has its key prefixed with the properties file where it came from.
     * 
     * @param propertyFilename
     * @return
     */
    public static Properties loadPropertiesFileFromResource(final String propertyFilename) {
        if (StringUtils.isBlank(propertyFilename)){
            log.error("Unable to retrieve file as a resource stream, propertyFilename : " + propertyFilename);
            throw new RuntimeException("Unable to retrieve file as a resource stream, propertyFilename : " + propertyFilename);            
        }
        
        // get the prefix to search for - the propertyFilename up to the "."
        String propertyPrefix;
        if (propertyFilename.contains(".")) {
            propertyPrefix = propertyFilename.substring(0, propertyFilename.indexOf("."));
        } else {
            propertyPrefix = propertyFilename;
        }
        
        Properties props = new Properties();
        
        // iterate over the system properties and add any which start with the propertyPrefix
        int matchingPropertiesCount = 0;
        Properties systemProps = System.getProperties();
        for (Object systemPropKey : systemProps.keySet()) {
            if (systemPropKey instanceof String) {                
                String systemPropString = (String) systemPropKey;
                if (systemPropString.startsWith(propertyPrefix)) {
                    matchingPropertiesCount++;
                    // the key in the returned set must not contain the prefix
                    String keyWithoutPrefix = systemPropString.substring(propertyPrefix.length() + 1);
                    props.put(keyWithoutPrefix, systemProps.get(systemPropKey));
                }
            }
        }
        log.info("matched " + matchingPropertiesCount + " for set " + propertyFilename);
        if (matchingPropertiesCount > 0) {
            return props;
        } else {
            log.warn("No system properties found with prefix " + propertyPrefix+ ". "
                    + "Attempting to load property file " + propertyFilename + " from classpath");
            return loadPropertiesFileFromClasspath(propertyFilename);
        }
    }
    
    /**
     * Load Properties from a file as a resource from the classpath. 
     * @param propertyFilename, e.g. 'common.properties'
     * @return content of file converted to java.util.Properties if the file exists otherwise a RuntimeException is thrown. 
     * 
     * @deprecated all properties should loaded into the system properties - see loadPropertiesFileFromResource. We will fall back to this method while 
     * the system properties are added to the relevant jboss standalone.xml / domain.xml files.
     */
    @Deprecated
    private static Properties loadPropertiesFileFromClasspath(final String propertyFilename) {
        log.info("Retrieving content of properties file : " + propertyFilename);
        if (StringUtils.isBlank(propertyFilename)){
            log.error("Unable to retrieve file as a resource stream, propertyFilename : " + propertyFilename);
            throw new RuntimeException("Unable to retrieve file as a resource stream, propertyFilename : " + propertyFilename);            
        };
        
        Properties props = new Properties();
        // try to load this resource from the thread's contextClassLoader and fall back to the class's classloader if 
        // this hasn't been set.
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            if (log.isDebugEnabled()) {
                log.debug("using default class loader to load properties resource");
            }
            classLoader = PropsLoader.class.getClassLoader();
        }
        try (InputStream resourceInputStream = classLoader.getResourceAsStream(propertyFilename)) {
            props.load(resourceInputStream);
        } catch (Exception e) {
            log.error("Unable to retrieve file as a resource stream, propertyFilename : " + propertyFilename, e);
            throw new RuntimeException("Unable to retrieve file as a resource stream, propertyFilename : " + propertyFilename);
        }
        log.info("Returning content of properties file : " + propertyFilename);
        return props;
    }

	/**
	 * Merge 2 properties files.
	 * 
	 * @param props
	 *            A Properties file to be merged.
	 * @param props2
	 *            A Properties file to be merged.
	 * @return The resulting merged Properties object.
	 */
	public static Properties mergeProperties(final Properties props, final Properties props2) {
		if (log.isDebugEnabled()) {
			log.debug("mergeProperties : Method Start");
		}
		if (log.isDebugEnabled()) {
			log.debug("mergeProperties : props = " + props);
		}
		if (log.isDebugEnabled()) {
			log.debug("mergeProperties : props1 = " + props2);
		}

		Enumeration names = props2.keys();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String value = props2.getProperty(name);
			props.put(name, value);
		}

		if (log.isDebugEnabled()) {
			log.debug("mergeProperties : Merged props = " + props);
		}
		if (log.isDebugEnabled()) {
			log.debug("mergeProperties : Method Finish");
		}
		return props;
	}
}
