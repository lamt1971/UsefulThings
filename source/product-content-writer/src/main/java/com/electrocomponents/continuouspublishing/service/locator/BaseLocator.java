package com.electrocomponents.continuouspublishing.service.locator;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.service.core.client.ApplicationNameResolver;

/*
 * ********************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Semwal
 * Created : 3rd Sep 2007 at 10:57:04
 *
 * ********************************************************************************************************
 * * Change History
 * ********************************************************************************************************
 * * Ref      * Who      * Date       * Description
 * ********************************************************************************************************
 * *          *          *            *
 * ********************************************************************************************************
 */

/**
 * A base ServiceLocator class that attempts to lookup a service only in local context.
 * @param <T> the type returned by the locator
 * @author Sanjay Semwal
 */
public abstract class BaseLocator<T> {

    /** A commons logging logger. */
    private static final Log LOG = LogFactory.getLog(BaseLocator.class);

    private static final String EJB_JNDI_VIEW_INTERFACE_SEPARATOR = "!";

    private static final String JNDI_PATH_SEPARATOR = "/";

    /** Cache for Services available on local JNDI. */
    private static ConcurrentHashMap<String, Future<CachedService>> serviceCache = new ConcurrentHashMap<String, Future<CachedService>>();

    ApplicationNameResolver applicationNameResolver = ApplicationNameResolver.getInstance();

    protected String getApplicationName() {
        return applicationNameResolver.getApplicationName();
    }

    /**
     * Retrieve an instance of a service. This is a global service lookup (not based on Locale).
     * 
     * @param jndiLocation the partial JNDI string
     * @return The service
     */
    @SuppressWarnings("unchecked")
    protected T locate(final String jndiLocation) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("proper locate - Method Start - " + "Requested Service : " + jndiLocation);
        }

        Object service = null;

        /*
         * Assume that a local version of the service has been cached this will be the long term steady-state for local services. Although
         * this is an incorrect assumption for remote clients, the internal overhead will be minimal compared to network traffic time.
         * Besides, we really should be using HA cluster for remote clients...
         */

        while (true) {

            String cacheKey = jndiLocation + "force-local";

            Future<CachedService> futureCachedService = serviceCache.get(cacheKey);

            if (futureCachedService == null) {
                Callable<CachedService> callableCachedService = new CallableGlobalServiceLocator(jndiLocation);
                FutureTask<CachedService> ft = new FutureTask<CachedService>(callableCachedService);
                futureCachedService = serviceCache.putIfAbsent(cacheKey, ft);
                if (futureCachedService == null) {
                    futureCachedService = ft;
                    ft.run();
                }
            }

            try {
                CachedService cachedService = futureCachedService.get();

                if (!cachedService.serviceLocated) {

                    /* Removing the cached future object from cache as the service lookup wasn't successful. */
                    serviceCache.remove(cacheKey);

                    throw new UnsupportedOperationException("No service located with JNDI: " + jndiLocation + ". Reason: "
                            + cachedService.errorMessage);
                }

                service = cachedService.service;
                break;

            } catch (InterruptedException e) {
                LOG.error("InterruptedException trying to find JNDI: " + jndiLocation + " .", e);
                throw new UnsupportedOperationException("Error doing service lookup", e);
            } catch (ExecutionException e) {
                LOG.error("ExecutionException trying to find JNDI: " + jndiLocation + " .", e);
                throw new UnsupportedOperationException("Error doing service lookup", e);
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Locate - Method End - " + "Requested Service : " + jndiLocation + ". Located: " + service);
        }

        return (T) service;
    }

    /**
     * Ensure that only local EJB interfaces are used when accessing service from local JNDI context and remote interfaces are used when
     * looking up from remote machines. ServiceLocator subclasses should omit the interface reference in the JNDI location they specify for
     * example: "ejb/core/PropertyServiceBean" not "ejb/core/PropertyServiceBean/remote" or "java:ejb/core/PropertyServiceBean/local"
     * @param serviceJndiLocation The partial JNDI location of the service to be accessed.
     * @return The full JNDI location including /local interface ref.
     */
    private String buildJndiLocation(final String serviceJndiLocation) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("buildJndiLocation - Method Start");
        }

        String finalJndiLocation = serviceJndiLocation;

        finalJndiLocation = "java:global/" + finalJndiLocation + "Local";
        System.out.println(finalJndiLocation);

        if (LOG.isDebugEnabled()) {
            LOG.debug("buildJndiLocation - Method Finish");
        }

        return finalJndiLocation;
    }

    protected String buildEjbJndiName(String appName, String moduleName, String serviceBeanName, String serviceInterfaceName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Method Start buildEjbJndiName");
        }

        String finalJndiLocation = new StringBuilder().append(appName).append(JNDI_PATH_SEPARATOR).append(moduleName).append(
                JNDI_PATH_SEPARATOR).append(serviceBeanName).append(EJB_JNDI_VIEW_INTERFACE_SEPARATOR).append(serviceInterfaceName)
                .toString();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Method Finish buildEjbJndiName");
        }

        return finalJndiLocation;
    }

    /**
     * Looking up java:app/AppName from JNDI context.
     * @return The application name as configured in application.xml.
     */
    protected String resolveApplicationName() {
    	return applicationNameResolver.getApplicationName();
    }

    /**
     * CallableGlobalServiceLocator.
     */
    private class CallableGlobalServiceLocator implements Callable<CachedService> {

        /** . */
        private String serviceJndi;

        /**
         * CallableServiceLocator.
         * @param serviceJndi serviceJndi
         * @param localhost localhost
         */
        public CallableGlobalServiceLocator(final String serviceJndi) {
            this.serviceJndi = serviceJndi;
        }

        /**
         * call.
         * @return CachedService CachedService
         */
        public CachedService call() {

            if (LOG.isDebugEnabled()) {
                LOG.debug("Method Start - call - jndi: " + serviceJndi);
            }

            CachedService cachedService = new CachedService();
            cachedService.serviceLocated = false;

            Object locatedService = null;

            // local lookup
            InitialContext localInitCtx = null;
            String localJndiLocation = buildJndiLocation(serviceJndi);

            try {

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Attempting to lookup local service on JNDI: " + localJndiLocation);
                }

                localInitCtx = new InitialContext();
                locatedService = localInitCtx.lookup(localJndiLocation);

                if (locatedService != null) {

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Successful local lookup of JNDI url: " + localJndiLocation);
                    }

                    cachedService.service = locatedService;
                    cachedService.serviceLocated = true;

                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("local jndi lookup gave null but no exception???");
                    }
                }
            } catch (Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Failed local lookup of JNDI location: " + localJndiLocation + ", exception detail was:\n" + e);
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

            return cachedService;
        }

    }

    /**
     * Object to allow a Service and it's cache insertion time to be cached.
     */
    private static final class CachedService {

        /**
         * was the service found - false means a lookup has been attempted but not sucessful.
         */
        private boolean serviceLocated = false;

        /** . */
        private String errorMessage = null;

        /** The cached service. */
        private Object service = null;

        /** . */
        public CachedService() {
        }
    }
}
