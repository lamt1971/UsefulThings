package com.electrocomponents.service.core.client;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.model.domain.Locale;

/**
 * Service locator for all local and remote EJB lookups.
 * 
 * The lookup code assumes that all beans use the unqualified business interface name for their stateless name - 
 * e.g. @Stateless(name = AccountService.SERVICE_NAME), where AccountService.SERVICE_NAME is "AccountService". This has to be
 * a constant String, so it's up to the service bean implementation developer to ensure consistency here.
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class ServiceLocator {
    
    private static final String SERVICE_NAME_FIELD_NAME = "SERVICE_NAME";
    
    private static final String EJB_MODULE_FIELD_NAME = "EJB_MODULE_NAME";

    private static final String EJB_JNDI_VIEW_INTERFACE_SEPARATOR = "!";

    private static final String JNDI_PATH_SEPARATOR = "/";
    
    private static final String APP_ALIAS_SEPARATOR = "-";
    
    private static final String DEFAULT_REMOTE_APP_NAME = "rmi-services";

    /**
     * string to indicate if the locale is not yet known, used by the SiteControl filter.
     */
    public static final String UNKNOWN_LOCALE = "unknown_locale";
    
    private static final String[] UNKNOWN_LOCALES = new String[] {UNKNOWN_LOCALE, "xxxx"};

    /** A commons logging logger. */
    private static final Log LOG = LogFactory.getLog(ServiceLocator.class);

    /** JNDI factory class name. */
    private static final String JNDI_FACTORY_CLASS = "org.jboss.naming.remote.client.InitialContextFactory";

    /**EJB naming client context jndi property name*/
    private static final String JBOSS_NAMING_CLIENT_EJB_CONTEXT = "jboss.naming.client.ejb.context";

    /**EJB client naming jndi property name*/
    private static final String JBOSS_EJB_CLIENT_NAMING = "org.jboss.ejb.client.naming";

    /** The region that the current LOCALHOST is part of. */
    private static String LOCAL_REGION_HOST = null;

    /** Cache for Services available on local JNDI. */
    private static Map<String, CachedServiceWrapper> LOCAL_SERVICE_CACHE = new ConcurrentHashMap<>();
    
    /** Cache for Services available on remote JNDI. */
    private static Map<String, CachedServiceWrapper> REMOTE_SERVICE_CACHE = new ConcurrentHashMap<>();
    
    /** initial context properties for remote lookups. */
    private static Properties REMOTE_IC_PROPS;

    /** Cache local lookups indefinitely. */
    private static final long LOCAL_CACHE_TTL = Long.MAX_VALUE;
    
    /** Cache remove lookups for 1 minute. */
    private static final long REMOTE_CACHE_TTL = 1000L * 60L;
    
    /**
     * 
     * region group enum. Note there's no 'global' here - RMI-services are either APAC or they're EMEA.
     * We default to the current region if the code can't determine what region is being requested.
     */
    private enum Region {
        emea, apac;
    }

    /** map of locale names to region code. */
    private static Map<String, Region> LOCALE_NAME_TO_REGION_CODE = new HashMap<>();

    /**
     * Lazily initialised ejb application (ear) name.
     */
    private static String JNDI_APPLICATION_NAME = null;
    
    /**
     * Overridden application name - when set this will be used in preference to the JNDI application name.
     */
    private static String OVERRIDDEN_APPLICATION_NAME = null;
    
    /**
     * Most calls will be for local beans, so cache their names.
     */
    private static Map<Class, String> LOCAL_JNDI_NAME_CACHE = new ConcurrentHashMap<>();
    
    /*
     * Read and parse the external config file - only performed once at classload time. 
     * If the config file can't be read we use default values here (emea locale, default region locales).
     */
    static {
        REMOTE_IC_PROPS = new Properties();
        REMOTE_IC_PROPS.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY_CLASS);
        // PROVIDER_URL isn't used by the EJB client
        REMOTE_IC_PROPS.put(Context.PROVIDER_URL, "localhost");
        REMOTE_IC_PROPS.put(Context.URL_PKG_PREFIXES, JBOSS_EJB_CLIENT_NAMING);
        REMOTE_IC_PROPS.put(JBOSS_NAMING_CLIENT_EJB_CONTEXT, true);
        
        List<String> apacRegionSupportedLocales = null;
        List<String> emeaRegionSupportedLocales = null;
        
        try {
            Map<String, Object> map = LoadProperties.loadSupportedLocales();
            /* Setting the local Region. */
            LOCAL_REGION_HOST = (String) map.get("localRegion");
            emeaRegionSupportedLocales = (List) map.get("emeaRegionSupportedLocales");
            apacRegionSupportedLocales = (List) map.get("apacRegionSupportedLocales");
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully loaded service-config properties, localRegionName is: " + LOCAL_REGION_HOST);
            }
        } catch (Exception e) {
            LOG.warn("Failed to retrieve service-config properties", e);
        }  
        
        // use default values for missing / unavailable properties.
        if (LOCAL_REGION_HOST == null) {
            LOG.warn("Failed to retrieve local region settings. Defaulting to emea");
            LOCAL_REGION_HOST = Region.emea.toString();
        }
        if (apacRegionSupportedLocales == null) {
            LOG.warn("Failed to retrieve apac locale region mappings, using defaults");
            // note this default list may have the 'unknown' locale added, so needs to be mutable (hence copying it to a new ArrayList)
            apacRegionSupportedLocales = new ArrayList<>(Arrays.asList(new String[]
                    {"au", "ax", "cn", "hk", "hk01", "hk02", "hkc1", "jp", "my", "nz", "ph", "sg", "th", "tw", "tw01", "tw02", "xa"}));
        }
        if (emeaRegionSupportedLocales == null) {
            LOG.warn("Failed to retrieve emea locale region mappings, using defaults");
            // note this default list may have the 'unknown' locale added, so needs to be mutable (hence copying it to a new ArrayList)
            emeaRegionSupportedLocales = new ArrayList<>(Arrays.asList(new String[] 
                    {"at", "be", "be01", "be02", "cz", "de", "dech", "dk", "es", "f1", "fr", "hu", "ie", "it", "nl", "no", "pl", "pt", "se", "uk", "za"}));
        }
        
        /*
         * Add unknown locales ("unknown_locale" and "xxxx") so SiteFilter can get started.
         */
        if (Region.emea.toString().equals(LOCAL_REGION_HOST)) {
            emeaRegionSupportedLocales.addAll(Arrays.asList(UNKNOWN_LOCALES));
        } else if (Region.apac.toString().equals(LOCAL_REGION_HOST)) {
            apacRegionSupportedLocales.addAll(Arrays.asList(UNKNOWN_LOCALES));
        }
        
        // now add the locales for each region
        for (String emeaLocale : emeaRegionSupportedLocales) {
            LOCALE_NAME_TO_REGION_CODE.put(emeaLocale, Region.emea);
        }     
        for (String apacLocale : apacRegionSupportedLocales) {
            LOCALE_NAME_TO_REGION_CODE.put(apacLocale, Region.apac);
        } 
    }
    
    /**
     * Utility method to allow services to determine if the localhost can support the request.
     * @param locale The locale we wish to determine support for.
     * @return true if the locale can be supported by the local host.
     */
    public static boolean isLocaleSupportedByLocalHost(final Locale locale) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("isLocaleSupportedByLocalHost - Method Start");
        }
        boolean supported = false;
        if (locale != null)  {
            Region supportingRegion = LOCALE_NAME_TO_REGION_CODE.get(locale.getLocaleString());
            if (supportingRegion != null && supportingRegion.toString().equals(LOCAL_REGION_HOST)) {
                supported = true;
            }
        } else {
            LOG.warn("No local specified for SupportedByLocalHost check");
            supported = true;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("isLocaleSupportedByLocalHost - Method Finish - returning " + supported);
        }
        return supported;
        
    }  
    
    // -------------------------------------------------------------------------
    // Methods to retrieve an EJB from a client running inside an EJB container.
    // These should be used in most cases.
    // -------------------------------------------------------------------------
    /**
     * Lookup a local EJB.
     * 
     * @param businessInterface
     * @return
     */
    public static <T> T locateLocal(Class<T> businessInterface) {
        String localJndiName = LOCAL_JNDI_NAME_CACHE.get(businessInterface);        
        if (localJndiName == null) {
            // if the overridden app name has been set within this classloader, use it. Otherwise get the app name from jndi
            localJndiName = buildLocalEjbJndiName(OVERRIDDEN_APPLICATION_NAME == null ? getJndiApplicationName() : OVERRIDDEN_APPLICATION_NAME, businessInterface);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Local lookup, derived JNDI name for interface " + businessInterface.getName() + " = " + localJndiName);
            }
            LOCAL_JNDI_NAME_CACHE.put(businessInterface, localJndiName);
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Local lookup, cached JNDI name for interface " + businessInterface.getName() + " = " + localJndiName);
            }
        }
        Object service = locateLocal(localJndiName);
        // if we fail to get the service from local JNDI, *and* we have overridden the app name, allow fallback to a remote lookup
        if (service == null && OVERRIDDEN_APPLICATION_NAME != null) {
            service = locateRemote(OVERRIDDEN_APPLICATION_NAME, businessInterface);
        }
        return businessInterface.cast(service);
    }
    
    /**
     * Lookup a remote EJB in the rmi-services application on a host which matches the supplied locale.
     * If a previous invocation to this ServiceLocator has overridden the application name, this will be used instead.
     * 
     * @param businessInterface
     * @param locale
     * @return
     */
    public static <T> T locateRemote(Class<T> businessInterface, Locale locale) {
        String remoteJndiName = buildRemoteEjbJndiName(OVERRIDDEN_APPLICATION_NAME == null ?
                getRMIServicesName(locale) : OVERRIDDEN_APPLICATION_NAME, businessInterface);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Remote lookup, derived JNDI name for interface" + businessInterface.getName() + " = " + remoteJndiName);
        }
        return businessInterface.cast(locateRemote(remoteJndiName));
    }
    
    /**
     * If the current host matches the supplied local, lookup a local EJB. Otherwise lookup a remote EJB 
     * in the rmi-services application on a host which matches this locale.
     * 
     * @param businessInterface
     * @param locale
     * @return
     */
    public static <T> T locateLocalOrRemote(Class<T> businessInterface, Locale locale) {
        if (isLocaleSupportedByLocalHost(locale)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Locale " + locale.getLocaleString() + " is supported by this host, doing local lookup");
            }
            return locateLocal(businessInterface);
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Locale " + locale.getLocaleString() + " is not supported by this host, doing remote lookup");
            }
            return locateRemote(businessInterface, locale);
        }
    }
    
    
    // -------------------------------------------------------------------------
    // Methods to retrieve an EJB from a client when running outside of an EJB 
    // container or when you want a specific non-RMI services application name.
    // These should be used for integration tests and quartz schedulers and any 
    // other cases where the current classloader doesn't have a JNDI 'application'
    // -------------------------------------------------------------------------
    
    /**
     * Lookup an EJB deployed in the named application as a local interface.
     * 
     * Only use this for integration testing or cases where the classloader has no EJB context (e.g. from Quartz jobs).
     * All other lookups for remote EJBs should use the locateRemote(String ejbModuleName, Class<T> businessInterface, Locale locale)
     * or locateLocalOrRemote(String ejbModuleName, Class<T> businessInterface, Locale locale) which will 
     * select the rmi-services application instance appropriate for the provided locale.
     * 
     * Calling this method has the side effect of switching off automatic appName resolution (via JNDI) if (and only if)
     * the current classloader has no access to the JNDI 'app name', so the current classloader's ServiceLocator will *always* 
     * use the overridden appName. This should only be used when there's no local JNDI tree to make this lookup 
     * (i.e. integration tests and quartz schedulers).
     * 
     * @param applicationName
     * @param businessInterface
     * @return
     */
    public static <T> T locateLocal(String applicationName, Class<T> businessInterface) {
        String localJndiName = LOCAL_JNDI_NAME_CACHE.get(businessInterface);
        if (localJndiName == null) {
            // set the class-level overridden applicationName to force subsequent invocations to the locator from this classloader 
            // to always use this applicationName, if there's no JNDI app name available.
            if (StringUtils.isBlank(getJndiApplicationName())) {
                ServiceLocator.OVERRIDDEN_APPLICATION_NAME = applicationName;
            }
            localJndiName = buildLocalEjbJndiName(applicationName, businessInterface);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Local lookup with overridden application name " + applicationName + 
                        ", derived JNDI name for interface " + businessInterface.getName() + " = " + localJndiName);
            }
            LOCAL_JNDI_NAME_CACHE.put(businessInterface, localJndiName);
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Local lookup with overridden application name " + applicationName + 
                        ", cached JNDI name for interface " + businessInterface.getName() + " = " + localJndiName);
            }
        }
        Object service = locateLocal(localJndiName);
        if (service == null) {
            service = locateRemote(applicationName, businessInterface);
        }
        return businessInterface.cast(service);
    }
    
    /**
     * Lookup an EJB deployed in the named application, as a local or a remote interface.
     * 
     * Only use this for integration testing, cases where the classloader has no EJB context (e.g. from Quartz jobs)
     * or cases where you want to make a call to a named application (ear file) *other than rmi-services*.
     * All other lookups for remote EJBs should use the locateRemote(String ejbModuleName, Class<T> businessInterface, Locale locale)
     * or locateLocalOrRemote(String ejbModuleName, Class<T> businessInterface, Locale locale) which will 
     * select the rmi-services application instance appropriate for the provided locale.
     * 
     * Calling this method has the side effect of switching off automatic appName resolution (via JNDI) if (and only if)
     * the current classloader has no access to the JNDI 'app name', so the current classloader's ServiceLocator will *always* 
     * use the overridden appName. This should only be used when there's no local JNDI tree to make this lookup 
     * (i.e. integration tests and quartz schedulers).
     * 
     * @param applicationName
     * @param businessInterface
     * @param locale
     * @return
     */
    public static <T> T locateLocalOrRemote(String applicationName, Class<T> businessInterface, Locale locale) {
        if (isLocaleSupportedByLocalHost(locale)) {
            return locateLocal(applicationName, businessInterface);
        } else {
            return locateRemote(applicationName, businessInterface);
        }
    }
    
    /**
     * Lookup a remote EJB deployed in the named application.
     * 
     * Only use this for integration testing, cases where the classloader has no EJB context (e.g. from Quartz jobs)
     * or cases where you want to make a call to a named application (ear file) *other than rmi-services*.
     * All other lookups for remote EJBs should use the locateRemote(String ejbModuleName, Class<T> businessInterface, Locale locale)
     * or locateLocalOrRemote(String ejbModuleName, Class<T> businessInterface, Locale locale) which will 
     * select the rmi-services application instance appropriate for the provided locale.
     * 
     * Calling this method has the side effect of switching off automatic appName resolution (via JNDI) if (and only if)
     * the current classloader has no access to the JNDI 'app name', so the current classloader's ServiceLocator will *always* 
     * use the overridden appName. This should only be used when there's no local JNDI tree to make this lookup 
     * (i.e. integration tests and quartz schedulers).
     * 
     * @param applicationName
     * @param businessInterface
     * @return
     */
    public static <T> T locateRemote(String applicationName, Class<T> businessInterface) {
        // set the class-level overridden applicationName to force subsequent invocations to the locator from this classloader 
        // to always use this applicationName, if there's no JNDI app name available.
        if (StringUtils.isBlank(getJndiApplicationName())) {
            ServiceLocator.OVERRIDDEN_APPLICATION_NAME = applicationName;
        }
        String remoteJndiName = buildRemoteEjbJndiName(applicationName, businessInterface);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Remote lookup with overridden application name " + applicationName + 
                    ", derived JNDI name for interface " + businessInterface.getName() + " = " + remoteJndiName);
        }
        return businessInterface.cast(locateRemote(remoteJndiName));
    }
    
    

    /**
     * Get the application name of the current ejb context. 
     * 
     * @return
     */
    private static String getJndiApplicationName() {
        if (JNDI_APPLICATION_NAME == null) {
            JNDI_APPLICATION_NAME = ApplicationNameResolver.getInstance().getApplicationName();            
        }
        return JNDI_APPLICATION_NAME;
    }
    
    /**
     * Get the rmi-services application name - this will be rmi-services-emea, rmi-service-apac or rmi-services-global.
     * This must be configured in the hosting JBoss server instance as a JNDI alias to the rmi-services application name.
     * 
     * @param locale
     * @return
     */
    private static String getRMIServicesName(Locale locale) {
        // if the locale is null, use the local region host - there's no 'global' RMI services application.
        if (locale == null) {
            return DEFAULT_REMOTE_APP_NAME + APP_ALIAS_SEPARATOR + LOCAL_REGION_HOST;
        }
        Region region = LOCALE_NAME_TO_REGION_CODE.get(locale.getLocaleString());
        if (region != null) {
            return DEFAULT_REMOTE_APP_NAME + APP_ALIAS_SEPARATOR + region.toString();
        } else {
            LOG.warn("No region found for locale " + locale + ", using local region of " + LOCAL_REGION_HOST);
            // use local.            
            return DEFAULT_REMOTE_APP_NAME + APP_ALIAS_SEPARATOR + LOCAL_REGION_HOST;
        }
    }     
    
    /**
     * Build a local EJB lookup jndi name.
     * 
     * @param appName
     * @param ejbModuleName
     * @param businessInterfaceName
     * @param businessInterfaceFQN
     * @return
     */
    private static String buildLocalEjbJndiName(String appName, Class businessInterface) {        
        return new StringBuilder().append(LocatorConstants.JNDI_GLOBAL_CONTEXT).append(appName).append(JNDI_PATH_SEPARATOR).append(getEJBModuleName(businessInterface)).
                append(JNDI_PATH_SEPARATOR).append(getServiceName(businessInterface)).append(EJB_JNDI_VIEW_INTERFACE_SEPARATOR).
                append(businessInterface.getName()).append(getLocalSuffix(businessInterface)).toString();
    }
    
    /**
     * Build a remote EJB lookup jndi name.
     * 
     * @param appName
     * @param ejbModuleName
     * @param businessInterfaceName
     * @param businessInterfaceFQN
     * @return
     */
    private static String buildRemoteEjbJndiName(String appName, Class businessInterface) {
        return new StringBuilder().append(LocatorConstants.JNDI_EJB_CONTEXT).append(appName).append(JNDI_PATH_SEPARATOR).append(getEJBModuleName(businessInterface)).
                append(JNDI_PATH_SEPARATOR).append(getServiceName(businessInterface)).append(EJB_JNDI_VIEW_INTERFACE_SEPARATOR).
                append(businessInterface.getName()).append(getRemoteSuffix(businessInterface)).toString();
    } 
    
    /**
     * get the Local interface name suffix or an empty String if the interface class name 
     * already ends with this text ("Local").
     * @param businessInterface
     * @return
     */
    private static String getLocalSuffix(Class businessInterface) {
        return businessInterface.getName().endsWith(LocatorConstants.JNDI_EJB_LOCAL_BINDING_NAME_SUFFIX) ? "" :
            LocatorConstants.JNDI_EJB_LOCAL_BINDING_NAME_SUFFIX;
    }
    
    /**
     * get the Remote interface name suffix or an empty String if the interface class name 
     * already ends with this text ("Remote").
     * @param businessInterface
     * @return
     */
    private static String getRemoteSuffix(Class businessInterface) {
        return businessInterface.getName().endsWith(LocatorConstants.JNDI_EJB_REMOTE_BINDING_NAME_SUFFIX) ? "" :
            LocatorConstants.JNDI_EJB_REMOTE_BINDING_NAME_SUFFIX;
    }
    
    /**
     * Locate an EJB in the local jndi tree with the specified address.
     * @param localJndiName
     * @return
     */
    private static Object locateLocal(String localJndiName) {
        Object service = null;
        CachedServiceWrapper cachedServiceWrapper = LOCAL_SERVICE_CACHE.get(localJndiName);        
        if (cachedServiceWrapper == null || System.currentTimeMillis() - cachedServiceWrapper.insertionTime > LOCAL_CACHE_TTL) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Local service not cached or expired, doing lookup");
            }
            InitialContext initialContext = null;            
            try {
                initialContext = new InitialContext();
                service = initialContext.lookup(localJndiName);
                if (service != null) {
                    LOCAL_SERVICE_CACHE.put(localJndiName, new CachedServiceWrapper(System.currentTimeMillis(), service));
                }
            } catch (NamingException e) {
                LOG.warn("Unable to locate " + localJndiName + " in local JNDI tree");
                
                // if this failed lookup is for a DAO bean, retry without the 'Local' part of the JNDI name.
                // TODO: remove this hack once the code has stablised.
                if (initialContext != null && localJndiName.endsWith("DaoLocal")) {
                    // replace the DaoLocal part with Dao
                    String daoWithNoLocalPart = localJndiName.substring(0, localJndiName.lastIndexOf("DaoLocal")) + "Dao";
                    LOG.warn("attempting lookup DAO bean using JNDI name " + daoWithNoLocalPart + 
                            " instead of expected JNDI name " + localJndiName);
                    try {
                        service = initialContext.lookup(daoWithNoLocalPart);
                        if (service != null) {
                            LOCAL_SERVICE_CACHE.put(localJndiName, new CachedServiceWrapper(System.currentTimeMillis(), service));
                        }
                    } catch (NamingException e2) {
                        LOG.warn("Unable to locate " + daoWithNoLocalPart + " in local JNDI tree");  
                    }
                }
                
            } finally {
                try {
                    if (initialContext != null) {
                        initialContext.close();
                    }
                } catch (NamingException e) {
                    LOG.warn("unable to close InitialContext", e);
                }
            }
        } else {
            service = cachedServiceWrapper.service;
        }
        return service;
    }         
    
    /**
     * Locate a remote EJB using the specified application.
     * 
     * @param remoteJndiName
     * @return
     */
    private static Object locateRemote(String remoteJndiName) { 
        Object service = null;
        CachedServiceWrapper cachedServiceWrapper = REMOTE_SERVICE_CACHE.get(remoteJndiName);
        if (cachedServiceWrapper == null || System.currentTimeMillis() - cachedServiceWrapper.insertionTime > REMOTE_CACHE_TTL) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Remote service not cached or expired, doing lookup");
            }
            InitialContext initialContext = null;
            try {
                initialContext = new InitialContext(REMOTE_IC_PROPS);
                service = initialContext.lookup(remoteJndiName);
                if (service != null) {
                    REMOTE_SERVICE_CACHE.put(remoteJndiName, new CachedServiceWrapper(System.currentTimeMillis(), service));
                }
            } catch (NamingException e) {
                LOG.error("Unable to locate " + remoteJndiName + " in remote JNDI tree", e);
             
                // if this failed lookup is for a DAO bean, retry without the 'Remote' part of the JNDI name.
                // TODO: remove this hack once the code has stablised.
                if (initialContext != null && remoteJndiName.endsWith("DaoRemote")) {
                    // replace the DaoLocal part with Dao
                    String daoWithNoRemotePart = remoteJndiName.substring(0, remoteJndiName.lastIndexOf("DaoRemote")) + "Dao";
                    LOG.warn("attempting lookup DAO bean using JNDI name " + daoWithNoRemotePart + 
                            " instead of expected JNDI name " + remoteJndiName);
                    try {
                        service = initialContext.lookup(daoWithNoRemotePart);
                        if (service != null) {
                            REMOTE_SERVICE_CACHE.put(remoteJndiName, new CachedServiceWrapper(System.currentTimeMillis(), service));
                        }
                    } catch (NamingException e2) {
                        LOG.warn("Unable to locate " + daoWithNoRemotePart + " in remote JNDI tree");  
                    }
                }
            } finally {
                try {
                    if (initialContext != null) {
                        initialContext.close();
                    }
                } catch (NamingException e) {
                    LOG.warn("unable to close InitialContext", e);
                }
            }
        } else {
            service = cachedServiceWrapper.service;
        }
        return service;
    }
    
    /**
     * 
     * @param businessInterface
     * @return
     */
    private static String getServiceName(Class businessInterface) {
        try {
            Field serviceNameField = businessInterface.getDeclaredField(SERVICE_NAME_FIELD_NAME);
            return (String) serviceNameField.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Interface " + businessInterface.getName() + " has no " + SERVICE_NAME_FIELD_NAME + " field");
        }
    }
    
    /**
     * 
     * @param businessInterface
     * @return
     */
    private static String getEJBModuleName(Class businessInterface) {
        try {
            Field ejbModuleNameField = businessInterface.getDeclaredField(EJB_MODULE_FIELD_NAME);
            return (String) ejbModuleNameField.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Interface " + businessInterface.getName() + " has no " + EJB_MODULE_FIELD_NAME + " field");
        }
    }
    
    /**
     * Value object to hold a service lookup along with the time it was located.
     */
    private static class CachedServiceWrapper {
        public long insertionTime;
        public Object service;
        
        public CachedServiceWrapper(long insertionTime, Object service) {
            this.insertionTime = insertionTime;
            this.service = service;
        }
    }
}
