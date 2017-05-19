package com.electrocomponents.service.core.client;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.model.domain.Locale;

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

    /**
     * string to indicate if the locale is not yet known, used by the SiteControl filter.
     */
    public static final String UNKNOWN_LOCALE = "unknown_locale";

    /** A commons logging logger. */
    private static final Log LOG = LogFactory.getLog(BaseLocator.class);

    /** JNDI factory class name. */
    private static final String JNDI_FACTORY_CLASS = "org.jboss.naming.remote.client.InitialContextFactory";

    /**EJB naming client context jndi property name*/
    private static final String JBOSS_NAMING_CLIENT_EJB_CONTEXT = "jboss.naming.client.ejb.context";

    /**EJB client naming jndi property name*/
    private static final String JBOSS_EJB_CLIENT_NAMING = "org.jboss.ejb.client.naming";

    /** The region that the current LOCALHOST is part of. */
    private static String localRegionName = null;
    
    /** Cache for Services available on local JNDI. */
    private static ConcurrentHashMap<String, Future<CachedService>> serviceCache = new ConcurrentHashMap<String, Future<CachedService>>();

    /** . */
    private static Region apacRegion;

    /** . */
    private static Region emeaRegion;

    /** . */
    private static Region globalRegion;

    /** . */
    private static Map<String, Region> localeNameToRegion = new ConcurrentHashMap<String, Region>();

    ApplicationNameResolver applicationNameResolver = ApplicationNameResolver.getInstance();

    /**
     * returns application name. 
     * @return
     */
    public String getApplicationName() {
        return applicationNameResolver.getApplicationName();
    }
    
    /**     
     * @param applicationName
     */
    @Deprecated
    protected void setApplicationName(final String applicationName) {
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
        globalRegion = new Region(LocatorConstants.REGION_NAME_GLOBAL, (String) map.get("globalRegionHosts"));

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


    }

    /**
     * Empty the cache.
     */
    public static void clear() {
        serviceCache.clear();
    }

    /**
     * Utility method to allow services to determine if the localhost can support the request.
     * @param locale The locale we wish to determine support for.
     * @return true if the locale can be supported by the local host.
     */
    public boolean isLocaleSupportedByLocalHost(final Locale locale) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("isLocaleSupportedByLocalHost - Method Start");
        }
        boolean supported = false;

        Region supportingRegion = this.determineHostRegionForLocale(locale);
        if (supportingRegion != null && supportingRegion.getRegionName().equals(localRegionName)) {
            supported = true;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("isLocaleSupportedByLocalHost - Method Finish - returning " + supported);
        }
        return supported;
    }

    /**
     * Retrieve an instance of a service. If the locale requested is supported by the local host then attempt a local JNDI lookup for the
     * "local" interface of that service. If the service cannot be found locally or the requested local is not supported locally then
     * perform remote JNDI lookups on EITHER remote hosts which support the same locales as the local host OR remote hosts which support the
     * remote region.
     * @param jndiLocation the partial JNDI location name (/local or /remote is added by this method.
     * @param locale the locale we wish to use the service for.
     * @return The service
     */
    @SuppressWarnings("unchecked")
    protected T locate(final String jndiLocation, final Locale locale) {
        Object foundObj = this.locate(jndiLocation, locale, false);
        return ((T) foundObj);
    }

    /**
     * Retrieve an instance of a service. If the locale requested is supported by the local host then attempt a local JNDI lookup for the
     * "local" interface of that service. If the service cannot be found locally or the requested local is not supported locally then
     * perform remote JNDI lookups on EITHER remote hosts which support the same locales as the local host OR remote hosts which support the
     * remote region.
     * @param jndiLocation the partial JNDI location name (/local or /remote is added by this method.
     * @return The service
     */
    @SuppressWarnings("unchecked")
    protected T locate(final String jndiLocation) {
        Object foundObj = this.locate(jndiLocation, false);
        return ((T) foundObj);
    }

    /**
     * Retrieve an instance of a service. If the locale requested is supported by the local host then attempt a local JNDI lookup for the
     * "local" interface of that service. If the service cannot be found locally or the requested local is not supported locally then
     * perform remote JNDI lookups on EITHER remote hosts which support the same locales as the local host OR remote hosts which support the
     * remote region.
     * @param jndiLocation the partial JNDI location name (/local or /remote is added by this method.
     * @param locale the locale we wish to use the service for.
     * @param forceLocalhostLookup If true a lookup for the service on the localhost will always occur even if the locale is not supported
     *        by the localhost's region (used by Property and Label service that have a local cache for all regions).
     * @return The service
     */
    @SuppressWarnings("unchecked")
    protected T locate(final String jndiLocation, final Locale locale, final boolean forceLocalhostLookup) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("proper locate - Method Start - " + "Requested Service : " + jndiLocation + ", Requested Locale : " + locale
                    .getLocaleString() + ", force local lookup: " + forceLocalhostLookup);
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
                cacheKey = jndiLocation + locale.getLocaleString();
            }

            Future<CachedService> futureCachedService = serviceCache.get(cacheKey);

            if (futureCachedService == null) {
                Callable<CachedService> callableCachedService = new CallableServiceLocator(jndiLocation, locale, forceLocalhostLookup);
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

                    throw new UnsupportedOperationException("No service located with JNDI: " + jndiLocation + " in locale: " + locale
                            + ". Reason: " + cachedService.errorMessage);
                }

                service = cachedService.service;
                break;

            } catch (InterruptedException e) {
                LOG.error("InterruptedException trying to find JNDI: " + jndiLocation + " in locale: " + locale, e);
                throw new UnsupportedOperationException("Error doing service lookup", e);
            } catch (ExecutionException e) {
                LOG.error("ExecutionException trying to find JNDI: " + jndiLocation + " in locale: " + locale, e);
                throw new UnsupportedOperationException("Error doing service lookup", e);
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("proper locate - Method End - " + "Requested Service : " + jndiLocation + ", Requested Locale : " + locale
                    .getLocaleString() + ", force local lookup: " + forceLocalhostLookup + ". Located: " + service);
        }

        return (T) service;
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
     * @return The application name as configured in ear/application.xml.
     */
    protected String resolveApplicationName() {        
        return applicationNameResolver.getApplicationName();
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
            finalJndiLocation = LocatorConstants.JNDI_EJB_CONTEXT + finalJndiLocation + LocatorConstants.JNDI_EJB_REMOTE_BINDING_NAME_SUFFIX;
        } else {
            finalJndiLocation = LocatorConstants.JNDI_GLOBAL_CONTEXT + finalJndiLocation + LocatorConstants.JNDI_EJB_LOCAL_BINDING_NAME_SUFFIX;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("buildJndiLocation - Method Finish");
        }

        return finalJndiLocation;
    }

    /**
     * Determine which region supports the requested locale.
     * @param locale The locale we wish to retrieve the service for.
     * @return The designation of the supporting region, i.e. emea, apac, amer.
     */
    private Region determineHostRegionForLocale(final Locale locale) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("determineHostRegionForLocale - Method Start - locale: " + locale);
        }
        Region supportingRegion = null;

        supportingRegion = localeNameToRegion.get(locale.getLocaleString());

        if (supportingRegion == null) {
            LOG.warn("failed to find supportingRegion for locale: " + locale);
        }

        if (LOG.isDebugEnabled()) {
            if (supportingRegion == null) {
                LOG.debug("determineHostRegionForLocale - Method Finish" + " - Supporting region not found for  " + locale
                        .getLocaleString());
            } else {
                LOG.debug("determineHostRegionForLocale - Method Finish" + " - Supporting region for locale: " + locale.getLocaleString()
                        + " is: " + supportingRegion.getRegionName());
            }
        }

        return supportingRegion;
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

        /** Comma separated list of host JNP URLS that support the region. */
        private String supportingHostsJnpUrls;

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
         * @return the supportingHostsJnpUrls
         */
        public String getSupportingHostsJnpUrls() {
            return supportingHostsJnpUrls;
        }

        /**
         * @param supportingHostsJnpUrls the supportingHostsJnpUrls to set
         */
        public void setSupportingHostsJnpUrls(final String supportingHostsJnpUrls) {
            this.supportingHostsJnpUrls = supportingHostsJnpUrls;
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

        /**
         * @return the service
         */
        public Object getService() {
            return service;
        }

        /**
         * @param service the service to set
         */
        public void setService(final Object service) {
            this.service = service;
        }

        /**
         * isLocalService.
         * @return boolean
         */
        public boolean isLocalService() {
            return localService;
        }

        /**
         * setLocalService.
         * @param local boolean
         */
        public void setLocalService(final boolean local) {
            localService = local;
        }

        /**
         * isServiceLocated.
         * @return boolean boolean
         */
        public boolean isServiceLocated() {
            return serviceLocated;
        }

        /**
         * setServiceLocated.
         * @param found boolean
         */
        public void setServiceLocated(final boolean found) {
            serviceLocated = found;
        }

        /**
         * getErrorMessage.
         * @return errorMessage
         */
        public String getErrorMessage() {
            return errorMessage;
        }

        /**
         * setErrorMessage.
         * @param msg String
         */
        public void setErrorMessage(final String msg) {
            errorMessage = msg;
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
     * CallableServiceLocator.
     */
    private class CallableServiceLocator implements Callable<CachedService> {

        /** . */
        private String serviceJndi;

        /** . */
        private Locale locale;

        /** . */
        private boolean forceLocalhost;

        /**
         * CallableServiceLocator.
         * @param serviceJndi serviceJndi
         * @param locale locale
         * @param localhost localhost
         */
        public CallableServiceLocator(final String serviceJndi, final Locale locale, final boolean localhost) {
            this.serviceJndi = serviceJndi;
            this.locale = locale;
            this.forceLocalhost = localhost;
        }

        /**
         * call.
         * @return CachedService CachedService
         */
        public CachedService call() {

            if (LOG.isDebugEnabled()) {
                LOG.debug("Method Start - call - jndi: " + serviceJndi + ", locale: " + locale + ", force localhost: " + forceLocalhost);
            }

            CachedService cachedService = new CachedService();
            cachedService.serviceLocated = false;

            Object locatedService = null;

            Region supportingRegion = determineHostRegionForLocale(locale);

            // If there is no support for the requested Locale fail.
            if (supportingRegion == null) {

                String errMsg = "The " + locale.getLocaleString() + " Locale is not supported by any of the configured Regions.";
                cachedService.errorMessage = errMsg;
                cachedService.serviceLocated = false;

                if (LOG.isDebugEnabled()) {
                    LOG.debug("No supporting region found for locale: " + locale);
                }
                // region is ok
            } else {

                if (LOG.isDebugEnabled()) {
                    LOG.debug("CachedService call - Supporting Region is: " + supportingRegion.regionName);
                }
                // try
                if (supportingRegion.getRegionName().equals(localRegionName) || forceLocalhost) {
                    // local
                    // lookup

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Requested LOCALE " + locale + "supported by LOCAL REGION: " + localRegionName + " jndi: " + serviceJndi
                                + ", locale: " + locale + ", force localhost: " + forceLocalhost);
                    }

                    InitialContext localInitCtx = null;
                    String localJndiLocation = buildJndiLocation(serviceJndi, false);

                    try {

                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Attempting to lookup local service on JNDI: " + localJndiLocation + ", locale: " + locale
                                    + ", force localhost: " + forceLocalhost + " on LOCALHOST...");
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
                    LOG.debug("trying to locate service on remote host " + ", locale: " + locale + ", force localhost: " + forceLocalhost);
                }

                String hostUrls = supportingRegion.getSupportingHostsJnpUrls();
                String remoteJndiLocation = buildJndiLocation(serviceJndi, true);

                if (hostUrls != null) {
                    for (String currentHostUrl : breakUpCommaSeperatedString(hostUrls)) {

                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Attempting to lookup REMOTE service interface on " + supportingRegion.getRegionName()
                                    + " region host..." + currentHostUrl + ", locale: " + locale + ", force localhost: " + forceLocalhost);
                        }

                        Properties props = new Properties();
                        props.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY_CLASS);
                        props.put(Context.PROVIDER_URL, currentHostUrl);
                        props.put(Context.URL_PKG_PREFIXES, JBOSS_EJB_CLIENT_NAMING);
                        props.put(JBOSS_NAMING_CLIENT_EJB_CONTEXT, true);

                        InitialContext remoteInitCtx = null;
                        try {

                            remoteInitCtx = new InitialContext(props);
                            locatedService = remoteInitCtx.lookup(remoteJndiLocation);

                            cachedService.localService = false;
                            cachedService.serviceLocated = true;
                            cachedService.service = locatedService;
                            break;

                        } catch (NamingException e) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("failed remote lookup of JNDI location: " + remoteJndiLocation + " on host: " + currentHostUrl
                                        + ". Trying next host on the list.");
                            }
                            LOG.error("Failed to find: " + remoteJndiLocation + ", on: " + currentHostUrl
                                    + ". Trying next host on the list.");

                            cachedService.serviceLocated = false;
                            cachedService.errorMessage = "Failed to find: " + remoteJndiLocation + ", on: " + currentHostUrl;

                        } finally {
                            if (remoteInitCtx != null) {
                                try {
                                    remoteInitCtx.close();
                                } catch (Exception e) {
                                    if (LOG.isWarnEnabled()) {
                                        LOG.warn("Failed to close remote InitialContext for REMOTE JNDI lookup, service: "
                                                + remoteJndiLocation + " in locale: " + locale + ", host: " + currentHostUrl);
                                    }
                                }
                            }
                        }
                    }
                } else {

                    LOG.error("There are no REMOTE hosts configured for the " + supportingRegion.getRegionName() + " region.");

                    cachedService.serviceLocated = false;
                    cachedService.errorMessage = "Failed to find: " + remoteJndiLocation + " in locale: " + locale
                            + ". There are no REMOTE hosts configured for the " + supportingRegion.getRegionName() + " region.";

                } // end remote lookup

            } // end region ok

            if (LOG.isDebugEnabled()) {
                LOG.debug("Method End - call - jndi: " + serviceJndi + ", locale: " + locale + ", force localhost: " + forceLocalhost
                        + " located: " + cachedService.serviceLocated);
            }

            return cachedService;
        }

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
            if (globalRegion.getRegionName().equals(localRegionName) || forceLocalhost) {
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

            String hostUrls = globalRegion.getSupportingHostsJnpUrls();
            String remoteJndiLocation = buildJndiLocation(serviceJndi, true);

            LOG.info("Local region is : " + localRegionName + ". No Local Service found. Doing Remote lookup..!!");

            if (hostUrls != null) {
                for (String currentHostUrl : breakUpCommaSeperatedString(hostUrls)) {

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Attempting to lookup REMOTE service interface on " + globalRegion.getRegionName() + " region host(s)..."
                                + ", force localhost: " + forceLocalhost);
                    }

                    Properties props = new Properties();
                    props.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY_CLASS);
                    props.put(Context.PROVIDER_URL, currentHostUrl);
                    props.put(Context.URL_PKG_PREFIXES, JBOSS_EJB_CLIENT_NAMING);
                    props.put(JBOSS_NAMING_CLIENT_EJB_CONTEXT, true);

                    InitialContext remoteInitCtx = null;
                    try {

                        remoteInitCtx = new InitialContext(props);
                        locatedService = remoteInitCtx.lookup(remoteJndiLocation);

                        cachedService.localService = false;
                        cachedService.serviceLocated = true;
                        cachedService.service = locatedService;
                        break;

                    } catch (NamingException e) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Global: failed remote lookup of JNDI location: " + remoteJndiLocation + " on host: " + currentHostUrl
                                    + ". Trying next host on the list.");
                        }
                        LOG.error("Global: Failed to find: " + remoteJndiLocation + ", on: " + currentHostUrl
                                + ". Trying next host on the list.");

                        cachedService.serviceLocated = false;
                        cachedService.errorMessage = "Failed to find: " + remoteJndiLocation + ", on: " + currentHostUrl;

                    } finally {
                        if (remoteInitCtx != null) {
                            try {
                                remoteInitCtx.close();
                            } catch (Exception e) {
                                if (LOG.isWarnEnabled()) {
                                    LOG.warn("Failed to close remote InitialContext for REMOTE JNDI lookup, service: " + remoteJndiLocation
                                            + " host: " + currentHostUrl);
                                }
                            }
                        }
                    }
                }
            } else {

                LOG.error("There are no REMOTE hosts configured for the " + globalRegion.getRegionName() + " region.");
                cachedService.serviceLocated = false;
                cachedService.errorMessage = "Failed to find: " + remoteJndiLocation + ". There are no REMOTE hosts configured for the "
                        + globalRegion.getRegionName() + " region.";
            } // end remote lookup

            if (LOG.isDebugEnabled()) {
                LOG.debug("Method End - call - jndi: " + serviceJndi + ", force localhost: " + forceLocalhost + " located: "
                        + cachedService.serviceLocated);
            }

            return cachedService;
        }

    }

    /**
     * Break up a config file line into separate values.
     * @param csvStr the input string
     * @return a List of type String with zero or more elements.
     */
    protected List<String> breakUpCommaSeperatedString(final String csvStr) {
        List<String> list = new Vector<String>();

        if (csvStr != null) {
            StringTokenizer strTok = new StringTokenizer(csvStr.trim(), ",");
            while (strTok.hasMoreTokens()) {
                String hostAndPortWithProtocol = strTok.nextToken().trim();
                if (hostAndPortWithProtocol.indexOf("remote://") != 0) {
                    throw new RuntimeException("Config value for server: " + hostAndPortWithProtocol
                            + " has unknown jndi protocol (should be remote:// )!");
                }
                list.add(hostAndPortWithProtocol);
            }
        }

        return list;
    }

}
