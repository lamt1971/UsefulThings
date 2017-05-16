package com.electrocomponents.service.notifications.sync.locator;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.service.core.client.LoadProperties;
import com.electrocomponents.service.core.client.LocatorConstants;

/*
 * ********************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Stuart Sim
 * Created : 28 Jun 2007 at 10:57:04
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
 * A base ServiceLocator class that attempts to lookup a service from local JNDI if it is relevent to do so, or if the supplied locale is
 * supported by machines on the remote region then it attempts to lookup the required service one a set of remote region machines. If the
 * required service is supported by the local region but cannot be found on the local host the BaseLocator then attempts to lookup the
 * service on one or more remote machines of the same region (if configured). Caching is implemented such that once a service is retrieved
 * from JNDI for a given region the reference to it is cached locally. For services deployed on the local host these are cached
 * indefinately. For services located on remote hosts a time to live mechanism is implemented so that the cached services are removed from
 * the cache and the lookup repeated after a configurable period. This is designed to allow the local host to recover from the loss of a
 * remote host which is supplying a service (though recovery will not be immediate).
 * @param <T> the type returned by the locator
 * @author Stuart Sim
 */
public abstract class BaseLocator<T> {

    private static final String EJB_JNDI_VIEW_INTERFACE_SEPARATOR = "!";

    private static final String JNDI_PATH_SEPARATOR = "/";

    /** JNDI factory class name. */
    private static final String JNDI_FACTORY_CLASS = "org.jboss.naming.remote.client.InitialContextFactory";

    /**EJB naming client context jndi property name*/
    private static final String JBOSS_NAMING_CLIENT_EJB_CONTEXT = "jboss.naming.client.ejb.context";

    /**EJB client naming jndi property name*/
    private static final String JBOSS_EJB_CLIENT_NAMING = "org.jboss.ejb.client.naming";

    private static final String REMOTE_PROVIDER_URL = "remote://localhost:4447";

    /**
     * string to indicate if the locale is not yet known, used by the SiteControl filter.
     */
    public static final String UNKNOWN_LOCALE = "unknown_locale";

    /** A commons logging logger. */
    private static final Log LOG = LogFactory.getLog(BaseLocator.class);

    /** The region that the current LOCALHOST is part of. */
    private static String localRegionName = null;

    /** Cache for Services available on local JNDI. */
    private static ConcurrentHashMap<String, Future<CachedService>> serviceCache = new ConcurrentHashMap<String, Future<CachedService>>();

    /** . */
    private static Region amerRegion;

    /** . */
    private static Region apacRegion;

    /** . */
    private static Region emeaRegion;

    /** . */
    private static Map<String, Region> localeNameToRegion = new ConcurrentHashMap<String, Region>();

    private String applicationName;

    public String getApplicationName() {
        return applicationName;
    }

    protected void setApplicationName(final String applicationName) {
        this.applicationName = applicationName;
    }

    /*
     * Read and parse the external config file - only performed once at classload time
     */

    static {

        Map<String, Object> map = LoadProperties.loadSupportedLocales();

        /* Setting the local Region. */
        localRegionName = (String) map.get("localRegion");
        if (LOG.isDebugEnabled()) {
            LOG.debug("localRegionName is: " + localRegionName);
        }

        emeaRegion = new Region(LocatorConstants.REGION_NAME_EMEA, (String) map.get("emeaRegionHosts"));
        apacRegion = new Region(LocatorConstants.REGION_NAME_APAC, (String) map.get("apacRegionHosts"));
        amerRegion = new Region(LocatorConstants.REGION_NAME_AMER, (String) map.get("amerRegionHosts"));

        /*
         * Setting supported locales list UNKNOWN_LOCALE is supported by local region (so SiteFilter can get started)
         */

        List<String> emeaRegionSupportedLocales = (List) map.get("emeaRegionSupportedLocales");
        if (emeaRegion.getRegionName().equals(localRegionName)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("");
            }
            emeaRegionSupportedLocales.add(UNKNOWN_LOCALE);
        }
        addLocalesForRegion(emeaRegion, emeaRegionSupportedLocales);

        List<String> apacRegionSupportedLocales = (List) map.get("apacRegionSupportedLocales");
        if (apacRegion.getRegionName().equals(localRegionName)) {
            apacRegionSupportedLocales.add(UNKNOWN_LOCALE);
        }
        addLocalesForRegion(apacRegion, apacRegionSupportedLocales);

        List<String> amerRegionSupportedLocales = (List) map.get("amerRegionSupportedLocales");
        if (amerRegion.getRegionName().equals(localRegionName)) {
            amerRegionSupportedLocales.add(UNKNOWN_LOCALE);
        }
        addLocalesForRegion(amerRegion, amerRegionSupportedLocales);

    }

    /**
     * Empty the cache.
     */
    public static void clear() {
        serviceCache.clear();
    }

    /**
     * Retrieve an instance of a service. This is a global service lookup (nit based on Locale). If the service cannot be found locally then
     * perform remote JNDI lookups on other GLOBAL region hosts.
     * 
     * @param jndiLocation the partial JNDI location name (/local or /remote is added by this method.
     * @param forceLocalhostLookup If true a lookup for the service on the localhost will always occur even if the locale is not supported
     *        by the localhost's region (used by Property and Label service that have a local cache for all regions).
     * @return The service
     */
    @SuppressWarnings("unchecked")
    protected T locate(final String jndiLocation, final boolean forceLocalhostLookup) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("proper locate - Method Start - " + "Requested Service : " + jndiLocation + ", force local lookup: "
                    + forceLocalhostLookup);
        }

        Object service = null;

        /*
         * Assume that a local version of the service has been cached this will be the long term steady-state for local services. Although
         * this is an incorrect assumption for remote clients, the internal overhead will be minimal compared to network traffic time.
         * Besides, we really should be using HA cluster for remote clients...
         */

        while (true) {

            String cacheKey = null;

            if (forceLocalhostLookup) {
                cacheKey = jndiLocation + "force-local";
            } else {
                cacheKey = jndiLocation + "global";
            }

            Future<CachedService> futureCachedService = serviceCache.get(cacheKey);

            if (futureCachedService == null) {
                Callable<CachedService> callableCachedService = new CallableGlobalServiceLocator(jndiLocation, forceLocalhostLookup);
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
            LOG.debug("proper locate - Method End - " + "Requested Service : " + jndiLocation + ", force local lookup: "
                    + forceLocalhostLookup + ". Located: " + service);
        }

        return (T) service;
    }

    /**
     * Looking up java:app/AppName from JNDI context.
     * @return The application name as configured in application.xml.
     */
    protected String resolveApplicationName() {
        InitialContext localInitCtx = null;
        String localAppName = "";
        try {
            localInitCtx = new InitialContext();
            localAppName = (String) localInitCtx.lookup(LocatorConstants.APP_NAME_JNDI);
        } catch (NamingException e) {
            LOG.warn("Failed to resolve appname, be aware this will cause service lookup failure");
        } finally {
            if (localInitCtx != null) {
                try {
                    localInitCtx.close();
                } catch (NamingException e) {
                    LOG.warn("Failed to close initial context");
                }
            }
        }
        return localAppName;
    }

    /**
     * Ensure that only local EJB interfaces are used when accessing service from local JNDI context and remote interfaces are used when
     * looking up from remote machines. ServiceLocator subclasses should omit the interface reference in the JNDI location they specify for
     * example: "ejb/core/PropertyServiceBean" not "ejb/core/PropertyServiceBean/remote" or "java:ejb/core/PropertyServiceBean/local"
     * @param serviceJndiLocation The partial JNDI location of the service to be accessed.
     * @param remote true if we wish to reference the remote interface (i.e. do a remote lookup...
     * @return The full JNDI location including /local or /remote interface ref.
     */
    private String buildJndiLocation(final String serviceJndiLocation, final boolean remote) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("buildJndiLocation - Method Start");
        }

        String finalJndiLocation = serviceJndiLocation;

        if (remote) {
            finalJndiLocation = "ejb:" + finalJndiLocation + "Remote";
        } else {
            finalJndiLocation = "java:global/" + finalJndiLocation + "Local";
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("buildJndiLocation - Method Finish");
        }

        return finalJndiLocation;
    }

    /**
     * addLocalesForRegion.
     * @param region Region
     * @param regionLocales List
     */
    private static void addLocalesForRegion(final Region region, final List<String> regionLocales) {
        if ((region != null) && (regionLocales != null)) {
            for (int kk = 0; kk < regionLocales.size(); kk++) {
                String localeStr = regionLocales.get(kk).trim();
                localeNameToRegion.put(localeStr, region);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("addLocalesForRegion - adding locale: " + regionLocales.get(kk) + " to region: " + region.getRegionName());
                }
            }
        }
    }

    /**
     * Represents the infrastructure configuration for a Region.
     */
    private static final class Region {

        /** Region name "emea", "apac", "amer". */
        private String regionName;

        /**
         * Construct a new Region instance.
         * @param regionName The name of a region, e.g. emea, apac, amer.
         * @param supportingHostsJnpUrls Comma separated list of JNP JNDI host URLs.
         */
        public Region(final String regionName, final String supportingHostsJnpUrls) {
            this.setRegionName(regionName);
            this.setSupportingHostsJnpUrls(supportingHostsJnpUrls);
        }

        /**
         * @return the regionName
         */
        public String getRegionName() {
            return regionName;
        }

        /**
         * @param regionName the regionName to set
         */
        public void setRegionName(final String regionName) {
            this.regionName = regionName;
        }

        /**
         * @param supportingHostsJnpUrls the supportingHostsJnpUrls to set
         */
        public void setSupportingHostsJnpUrls(final String supportingHostsJnpUrls) {
        }
    }

    /**
     * Object to allow a Service and it's cache insertion time to be cached.
     */
    private static final class CachedService {

        /** true if the service is running "in-vm", false otherwise. */
        private boolean localService = false;

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
     * CallableGlobalServiceLocator.
     */
    private class CallableGlobalServiceLocator implements Callable<CachedService> {

        /** . */
        private String serviceJndi;

        /** . */
        private boolean forceLocalhost;

        /**
         * CallableServiceLocator.
         * @param serviceJndi serviceJndi
         * @param localhost localhost
         */
        public CallableGlobalServiceLocator(final String serviceJndi, final boolean localhost) {
            this.serviceJndi = serviceJndi;
            this.forceLocalhost = localhost;
        }

        /**
         * call.
         * @return CachedService CachedService
         */
        public CachedService call() {

            if (LOG.isDebugEnabled()) {
                LOG.debug("Method Start - call - jndi: " + serviceJndi + ", force localhost: " + forceLocalhost);
            }

            CachedService cachedService = new CachedService();
            cachedService.serviceLocated = false;

            Object locatedService = null;
            // try
            if (forceLocalhost) {
                // local lookup

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Local region is : " + localRegionName + ". Doing local lookup..!!");
                }

                InitialContext localInitCtx = null;
                String localJndiLocation = buildJndiLocation(serviceJndi, false);

                try {

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Attempting to lookup local service on JNDI: " + localJndiLocation + ", force localhost: "
                                + forceLocalhost + " on LOCALHOST...");
                    }

                    localInitCtx = new InitialContext();
                    locatedService = localInitCtx.lookup(localJndiLocation);

                    if (locatedService != null) {

                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Successful local lookup of JNDI url: " + localJndiLocation);
                        }

                        cachedService.service = locatedService;
                        cachedService.serviceLocated = true;
                        cachedService.localService = true;

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
            } // end local lookup

            if (cachedService.serviceLocated) {
                return cachedService;
            }

            // no local service, try remote lookup
            if (LOG.isDebugEnabled()) {
                LOG.debug("trying to locate service on remote host " + ", force localhost: " + forceLocalhost);
            }

            String remoteJndiLocation = buildJndiLocation(serviceJndi, true);

            LOG.info("Local region is : " + localRegionName + ". No Local Service found. Doing Remote lookup..!!");

            Properties props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY_CLASS);
            props.put(Context.PROVIDER_URL, REMOTE_PROVIDER_URL);
            props.put(Context.URL_PKG_PREFIXES, JBOSS_EJB_CLIENT_NAMING);
            props.put(JBOSS_NAMING_CLIENT_EJB_CONTEXT, true);

            InitialContext remoteInitCtx = null;
            try {

                remoteInitCtx = new InitialContext(props);
                locatedService = remoteInitCtx.lookup(remoteJndiLocation);

                cachedService.localService = false;
                cachedService.serviceLocated = true;
                cachedService.service = locatedService;
            } catch (NamingException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Global: failed remote lookup of JNDI location: " + remoteJndiLocation + " on host: " + REMOTE_PROVIDER_URL
                            + ". Trying next host on the list.");
                }
                LOG.error("Global: Failed to find: " + remoteJndiLocation + ", on: " + REMOTE_PROVIDER_URL
                        + ". Trying next host on the list.");

                cachedService.serviceLocated = false;
                cachedService.errorMessage = "Failed to find: " + remoteJndiLocation + ", on: " + REMOTE_PROVIDER_URL;

            } finally {
                if (remoteInitCtx != null) {
                    try {
                        remoteInitCtx.close();
                    } catch (Exception e) {
                        if (LOG.isWarnEnabled()) {
                            LOG.warn("Failed to close remote InitialContext for REMOTE JNDI lookup, service: " + remoteJndiLocation
                                    + " host: " + REMOTE_PROVIDER_URL);
                        }
                    }
                }
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Method End - call - jndi: " + serviceJndi + ", force localhost: " + forceLocalhost + " located: "
                        + cachedService.serviceLocated);
            }

            return cachedService;
        }

    }
}
