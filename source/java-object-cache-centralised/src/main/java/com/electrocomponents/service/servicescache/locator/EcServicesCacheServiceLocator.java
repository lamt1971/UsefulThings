package com.electrocomponents.service.servicescache.locator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.commons.cache.CentralisedCacheJndiConstants;
import com.electrocomponents.model.domain.Locale;
import com.electrocomponents.service.core.client.BaseLocator;
import com.electrocomponents.service.servicescache.interfaces.EcServicesCacheService;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Rakesh Kumar
 * Created : 1 May 2012 at 10:47:19
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
 * TA class to locate instance of EcCacheServiceService.
 * @author Rakesh Kumar
 */
public final class EcServicesCacheServiceLocator extends BaseLocator<EcServicesCacheService> {

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(EcServicesCacheServiceLocator.class);

    /** The singleton instance. */
    private static EcServicesCacheServiceLocator serviceLocator = new EcServicesCacheServiceLocator();

    /** Constructor. */
    private EcServicesCacheServiceLocator() {
    	this.setApplicationName(resolveApplicationName());
    }

    /**
     * @return the locator
     */
    public static EcServicesCacheServiceLocator getLocator() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getLocator");
        }

       

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getLocator");
        }
        return serviceLocator;
    }

    /**
     * Locate and retrieve an instance of a EcCacheService.
     * This will force a local lookup first and then if not found does a RMI lookup.
     * @param locale the Locale
     * @return A EcCacheService service instance.
     */
    public EcServicesCacheService locate(final Locale locale) {
    	    final String jndi = buildEjbJndiName(this.getApplicationName(), CentralisedCacheJndiConstants.EJB_MODULE_NAME, EcServicesCacheService.SERVICE_NAME,
    	    		EcServicesCacheService.class.getCanonicalName());
            return super.locate(jndi, locale);
        }
       
    
    /**
     * Locate and retrieve an instance of a DocumentGeneratorService.
     * @param appName The name of the application service to locate
     * @param locale The locale we wish to retrieve a DocumentGeneratorService for.
     * @return A DocumentGenerator Service that supports the specifed locale.
     */
    public EcServicesCacheService locate(final String appName, final Locale locale) {
        final String jndi = buildEjbJndiName(appName, CentralisedCacheJndiConstants.EJB_MODULE_NAME, EcServicesCacheService.SERVICE_NAME,
        		EcServicesCacheService.class.getCanonicalName());
        return super.locate(jndi, locale);
    }

}
