package com.electrocomponents.service.async.cache.locator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.model.domain.Locale;
import com.electrocomponents.rsparallelprocessingexecutor.JndiConstants;
import com.electrocomponents.service.async.cache.interfaces.CommandCacheAccessorService;
import com.electrocomponents.service.core.client.BaseLocator;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : <<<<user name>>>>
 * Created : 19 Jan 2011 at 16:12:46
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
 * A locator implementation responsible for obtaining an instance of CacheAccessorService.
 * @author Vijay Swarnkar
 */
public final class CommandCacheAccessorServiceLocator extends BaseLocator<CommandCacheAccessorService> {

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(CommandCacheAccessorServiceLocator.class);

    /** The singleton instance. */
    private static CommandCacheAccessorServiceLocator cacheAccessorServiceLocator = null;
    
    /** ejb local interface binding jndi name */
    private String localEjbJndiName = null;

    /** Private constructor required to make locator as singleton. */
    private CommandCacheAccessorServiceLocator() {
    	localEjbJndiName = buildEjbJndiName(this.getApplicationName(), JndiConstants.EJB_MODULE_NAME, CommandCacheAccessorService.SERVICE_NAME, CommandCacheAccessorService.class.getCanonicalName());
    }

    /**
     * @return the CacheAccessorServiceLocator
     */
    public static CommandCacheAccessorServiceLocator getLocator() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getLocator.");
        }
        if (cacheAccessorServiceLocator == null) {
            cacheAccessorServiceLocator = new CommandCacheAccessorServiceLocator();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getLocator.");
        }
        return cacheAccessorServiceLocator;
    }

    /**
     * Locate and retrieve an instance of a CacheAccessorService.
     * @param locale The locale for which CacheAccessorService to be retrieved.
     * @return CacheAccessorService that supports the specified locale.
     */
    public CommandCacheAccessorService locate(final Locale locale) {
        return super.locate(localEjbJndiName, locale, true);
    }
    
    /**
     * Locate and retrieve an instance of a CacheAccessorService.
     * @param locale The locale for which CacheAccessorService to be retrieved.
     * @return CacheAccessorService that supports the specified locale.
     */
    public CommandCacheAccessorService locate(final String appName, final Locale locale) {
        String jndi = buildEjbJndiName(appName, JndiConstants.EJB_MODULE_NAME, CommandCacheAccessorService.SERVICE_NAME, CommandCacheAccessorService.class.getCanonicalName());
        return super.locate(jndi, locale);
    }

}
