/**
 * 
 */
package com.electrocomponents.service.objectcache.locator;

import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.electrocomponents.commons.cache.ObjectCacheJndiConstants;
import com.electrocomponents.service.core.client.BaseLocator;
import com.electrocomponents.service.objectcache.interfaces.CacheFacade;

/**
 * @author C0950079
 * 
 * This locator should only be sued when we can't inject the cachefacade using @EJB annotation.
 *
 */

public class CacheFacadeLocator extends BaseLocator<CacheFacade>{
	
	 /** A commons logging logger. */
    private static final Log LOG = LogFactory.getLog(CacheFacadeLocator.class);
    
    private static final String JNDI_GROBAL_CONTEXT = "java:global/"; 

	/** The singleton instance. */
    private static CacheFacadeLocator cacheFacadeLocator = null;
    
    private String localJndiName = null;

    /** Constructor. */
    private CacheFacadeLocator() {
        setApplicationName(resolveApplicationName());
        this.localJndiName =  JNDI_GROBAL_CONTEXT + buildEjbJndiName(this.getApplicationName(), ObjectCacheJndiConstants.EJB_MODULE_NAME, 
        		CacheFacade.SERVICE_NAME, CacheFacade.class.getCanonicalName());
    }

    /**
     * @return the locator
     */
    public static CacheFacadeLocator getLocator() {
        if (cacheFacadeLocator == null) {
        	cacheFacadeLocator = new CacheFacadeLocator();
        }
        return cacheFacadeLocator;
    }

    /**
     * Locate and retrieve an instance of a CacheFacade singleton implementation.
     * @return A locally deployed instance of {@link}CacheFacade service.
     */
    public CacheFacade locate() {      
        InitialContext localInitCtx = null;
        Object locatedService = null;
        
        try {

            if (LOG.isDebugEnabled()) {
                LOG.debug("Attempting to lookup local service on JNDI: " + localJndiName + " on LOCALHOST...");
            }
            localInitCtx = new InitialContext();
            locatedService = localInitCtx.lookup(localJndiName);
            if (locatedService != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Successful local lookup of JNDI url: " + localJndiName);
                }
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("local jndi lookup gave null but no exception???");
                }
            }
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Failed local lookup of JNDI location: " + localJndiName + ", exception detail was:\n" + e);
            }
        } finally {
            if (localInitCtx != null) {
                try {
                    localInitCtx.close();
                } catch (Exception e) {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn("Failed to close initialContext for local JNDI lookup");
                    }
                }
            }
        }
        return (CacheFacade) locatedService;
    } // end local lookup
    
}
