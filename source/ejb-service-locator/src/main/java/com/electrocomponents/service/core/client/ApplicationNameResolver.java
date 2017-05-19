package com.electrocomponents.service.core.client;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author C0950079
 * singleton utility to initialise and access the application name.
 */
public class ApplicationNameResolver {
	
	/** A commons logging logger. */
    private static final Log LOG = LogFactory.getLog(ApplicationNameResolver.class);
    
    /** singleton instance.*/
    private static ApplicationNameResolver instance = new ApplicationNameResolver();
	
	/**
     * global application name; initialised once from the JNDI context and reused.
     */
    private String globalApplicationName = "";

	/**
	 * private constructor
	 */
	private ApplicationNameResolver() {
	}
	
	public static ApplicationNameResolver getInstance() {
		return instance;
	}
	
	/**
     * Looking up java:app/AppName from JNDI context.
     * @return The application name as configured in ear/application.xml.
     */
    public String getApplicationName() {
        if (StringUtils.isNotBlank(this.globalApplicationName)) {
        	return this.globalApplicationName;
        }	else {
        	return initialiseApplicationName();
        }
    }
    
    /**
     * Looking up java:app/AppName from JNDI context and caches it.
     * @return The application name as configured in ear/application.xml.
     */
    public String initialiseApplicationName() {
        if (StringUtils.isBlank(globalApplicationName)) {
        	
        	//initialise the application name.
        	InitialContext localInitCtx = null;
            try {
                localInitCtx = new InitialContext();
                this.globalApplicationName = (String) localInitCtx.lookup(LocatorConstants.APP_NAME_JNDI);            
            } catch (NamingException e) {
                LOG.info("Failed to resolve appname");
            } finally {
                if (localInitCtx != null) {
                    try {
                        localInitCtx.close();
                    } catch (NamingException e) {
                        LOG.warn("Failed to close initial context");
                    }
                }
            }
        }
        return globalApplicationName;
    }

}
