package com.electrocomponents.service.objectcache.impl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.commons.cache.CacheName;
import com.electrocomponents.executor.BoundedBlockingQueue;
import com.electrocomponents.model.domain.ApplicationSource;
import com.electrocomponents.model.domain.DateTime;
import com.electrocomponents.model.domain.Locale;
import com.electrocomponents.model.domain.servicescache.EcServicesCache;
import com.electrocomponents.service.core.client.ServiceLocator;
import com.electrocomponents.service.exception.ServiceException;
import com.electrocomponents.service.objectcache.interfaces.CacheFacade;
import com.electrocomponents.service.objectcache.interfaces.DatabaseStoreCacheFacade;
import com.electrocomponents.service.objectcache.interfaces.DatabaseStoreCacheFacadeLocal;
import com.electrocomponents.service.servicescache.interfaces.EcServicesCacheService;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut
 * Created : 3 May 2012 at 11:04:04
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
 * A controller class for database driven caching.
 * @author Ganesh Raut
 */
@Singleton(name=DatabaseStoreCacheFacadeImpl.SERVICE_NAME)
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Local({DatabaseStoreCacheFacadeLocal.class, DatabaseStoreCacheFacade.class})
public class DatabaseStoreCacheFacadeImpl implements DatabaseStoreCacheFacade  {

	/** The maximum number of times to attempt to retrieve the object from the cache. */
    public static final int MAX_CALL_RETRIES = 1;

    /** The properties file - has cache TTL configuration for supported cache paths/keys.*/
    private static final String CACHE_MAPPING_PROPERTIES_PATH = "/com/electrocomponents/service/core/database-backed-cache.properties";
    
    @EJB(beanName = "JavaHeapCache")
    CacheFacade objectCache;

    /** The ExecutorService instance. */
    public static ExecutorService executor;

    /** The cache path and eviction mapping properties. */
    private static Properties cachePathAndEvictionTimeMapping;

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(DatabaseStoreCacheFacadeImpl.class);
    
    final static String SERVICE_NAME = "DatabaseStoreCacheFacade";

    /** Initialising the executor and other required configuration. */
    @PostConstruct
    public void setup() {
        /* As we do not have a global config/property service, these values are hard coded. */
        BoundedBlockingQueue workQueue = new BoundedBlockingQueue(1000, 2000, TimeUnit.MILLISECONDS);
        int corePoolSize = 0;
        int maximumPoolSize = 5;
        int keepAliveTime = 60000;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue);
        cachePathAndEvictionTimeMapping = new Properties();
        try {
            cachePathAndEvictionTimeMapping.load(DatabaseStoreCacheFacadeImpl.class.getResourceAsStream(CACHE_MAPPING_PROPERTIES_PATH));
        } catch (IOException e) {
            LOG.error("Failed to load database backed cache properties!");
        }	
    }
    
    
    /**
     * @param cacheName The path to the node the the cache in which we wish to store / retrieve the Object.
     * @param key The leaf level node key name.
     * @param servicesCacheCallableWrapper A Callable object which we use to retrieve the Object from the original source if we do not find it in
     *        the cache.
     * @return The requested Object if we have been able to retrieve it.
     */
    public Object putIfAbsentAndGet(CacheName cacheName, String key, ServicesCacheCallableWrapper callableWrapper) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getObject");
        }

        Locale locale = callableWrapper.getLocale();
        Callable sourceObjectAccessor = callableWrapper.getCallable();
        GetFromCacheStoreOrGetFromOrigin goMakeCallOrUseStale = new GetFromCacheStoreOrGetFromOrigin(cacheName.getCacheName(), key, sourceObjectAccessor, locale);
        Object object = objectCache.putIfAbsentAndGet(cacheName,  key, goMakeCallOrUseStale);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getObject");
        }
        return object;
    }

    /**
     * A method to get the max call retries.
     * @return max call retries value
     */
    public int getMaxCallRetries() {
        return MAX_CALL_RETRIES;
    }

    /**
     * <pre>
     * ************************************************************************************************
     * Copyright (c) Electrocomponents Plc.
     *
     * Author  : Ganesh Raut
     * Created : 8 May 2012 at 15:24:52
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
    public class GetFromCacheStoreOrGetFromOrigin implements Callable {

        /** Constant for default cache path. */
        private static final String SERVICES_CACHE_PATH_DEFAULT = "/_default_";

        /** Constant for forward slash. */
        private static final String FORWARD_SLASH = "/";

        /** The cache access path. */
        private String cacheName;

        /** The cache key. */
        private String cacheKey;

        /** The source object accessor. */
        private Callable sourceObjectAccessor;

        /** The locale. */
        private Locale locale;

        /**
         * Constructor.
         * @param cacheName the cacheName
         * @param key the cache key
         * @param sourceObjectAccessor the sourceObjectAccessor
         * @param locale the locale
         */
        public GetFromCacheStoreOrGetFromOrigin(final String cacheName, final String key, final Callable sourceObjectAccessor,
                final Locale locale) {
            this.cacheName = cacheName;
            this.cacheKey = key;
            this.sourceObjectAccessor = sourceObjectAccessor;
            this.locale = locale;
        }

        /**
         * @return the cached object.
         * @throws Exception exception
         */
        public Object call() throws Exception {

            Object result = null;

            /* Get the cached result from database and check if it is valid to use. */
            EcServicesCache ecServicesCache = getL2CachedObject(cacheName, cacheKey, locale);
            if (ecServicesCache != null) {
            	LOG.info("Cache hit in database store.");
                result = getResultIfCacheIsValid(ecServicesCache);
            }

            /* If the valid result is not obtained, Invoke the wrapped source callable to get the result. */
            if (result == null) {
                try {
                    result = this.sourceObjectAccessor.call();
                } catch (Throwable throwable) {
                    StringBuilder error = new StringBuilder("Exception while executing the source callable object.");
                    error.append(" cacheName = ").append(cacheName);
                    error.append(", cacheKey = ").append(cacheKey).append(", locale = ").append(locale);
                    error.append(", Exception = ").append(throwable);
                    LOG.warn(error);
                }

                /* If the result is obtained from the above call, store the result into L2(database) cache. */
                if (result != null) {
                    setL2CachedObject(ecServicesCache, cacheName, cacheKey, result, locale);
                } else {

                    /* If the valid result is not obtained, use the stale value from the database cache if available. */
                    if (ecServicesCache != null) {
                        byte[] cachedObject = ecServicesCache.getValue();
                        result = deserailiseCachedObject(cachedObject);
                    }

                    /* If the valid result is not obtained, throw a service exception. */
                    if (result == null) {
                        StringBuilder error = new StringBuilder();
                        error.append("Failed to obtained the result. ");
                        error.append("cacheName = ").append(cacheName);
                        error.append(", cacheKey = ").append(cacheKey);
                        error.append(", locale = ").append(locale);
                        LOG.error(error);
                        throw new ServiceException(error.toString(), ApplicationSource.RS_WEB_SITE);
                    }
                }
            }

            return result;
        }

        /**
         * @param ecServicesCache the ecServicesCache
         * @return object if valid
         */
        private Object getResultIfCacheIsValid(final EcServicesCache ecServicesCache) {
            Object result = null;

            try {
                /* Get the cache expiry time. */
                Long expiryTimeInSeconds = getL2ExpiryTimeForCacheAccessPath(cacheName,cacheKey, locale);
                long cacheTimeInMillis = expiryTimeInSeconds * 1000;
                long cacheValidUntilTimeInMillis = ecServicesCache.getLastModifiedTime().getTimeInMillis() + cacheTimeInMillis;
                DateTime cacheValidUntil = new DateTime(cacheValidUntilTimeInMillis);
                DateTime currentTime = new DateTime();

                /* If current date is before the cacheValidUntil date, the cache is still valid. */
                if (currentTime.before(cacheValidUntil)) {
                    byte[] cachedObject = ecServicesCache.getValue();
                    result = deserailiseCachedObject(cachedObject);
                    LOG.info("database stored object is not expirec yet.");
                }
            } catch (Throwable throwable) {
                StringBuilder error = new StringBuilder("Exception while checking if L2 cached object is valid.");
                error.append(" cacheName = ").append(cacheName);
                error.append(", cacheKey = ").append(cacheKey).append(", locale = ").append(locale);
                error.append(", Exception = ").append(throwable);
                LOG.warn(error);
            }

            return result;
        }

        /**
         * @param cacheName the cacheName to get the expiry time for.
         * @param locale the locale
         * @return cache expiry time
         */
        private Long getL2ExpiryTimeForCacheAccessPath(final String cacheName,final String cacheKey, final Locale locale) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Start getL2ExpiryTimeForCacheAccessPath");
            }

            String cacheTimeString = null;
            String newCachePath = cacheName+cacheKey;
            while (true) {
                cacheTimeString = cachePathAndEvictionTimeMapping.getProperty(newCachePath);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("NewCachePath: " + newCachePath + ", CacheTimeString:  " + cacheTimeString);
                }
                
                //LOG.info("NewCachePath: " + newCachePath + ", CacheTimeString:  " + cacheTimeString);

                if (StringUtils.isBlank(cacheTimeString)) {
                    if (newCachePath.lastIndexOf(FORWARD_SLASH) > 0) {
                        /* No cache time found for full path, exclude the last element in cache path. */
                        newCachePath = newCachePath.substring(0, newCachePath.lastIndexOf(FORWARD_SLASH));
                    } else {
                        /* No cache configuration found, using the default cache time. */
                        cacheTimeString = cachePathAndEvictionTimeMapping.getProperty(SERVICES_CACHE_PATH_DEFAULT);
                        break;
                    }
                } else {
                    /* Cache time found, breaking the loop. */
                    break;
                }
            }

            Long cacheTime = 0L;
            try {
                cacheTime = Long.valueOf(cacheTimeString);
            } catch (Exception exception) {
                StringBuilder error = new StringBuilder("Invalid caching time configured in ");
                error.append(CACHE_MAPPING_PROPERTIES_PATH);
                error.append(", Cache time = ").append(cacheTimeString);
                LOG.error(error);
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Finish getL2ExpiryTimeForCacheAccessPath");
            }
            return cacheTime;
        }

        /**
         * @param cacheName the cacheName to get the expiry time for.
         * @param cacheKey the cacheKey
         * @param locale the locale
         * @return cache expiry time
         */
        private EcServicesCache getL2CachedObject(final String cacheName, final String cacheKey, final Locale locale) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Start getL2CachedObject");
            }

            EcServicesCache ecServicesCache = null;
            try {
                EcServicesCacheService ecServicesCacheService = ServiceLocator.locateLocalOrRemote(EcServicesCacheService.class, locale);
                ecServicesCache = ecServicesCacheService.getServicesCache(cacheName, cacheKey);
            } catch (Throwable throwable) {
                StringBuilder error = new StringBuilder("Exception while retrieving L2 cached object from database.");
                error.append(" cacheName = ").append(cacheName);
                error.append(", cacheKey = ").append(cacheKey).append(", locale = ").append(locale);
                error.append(", Exception = ").append(throwable);
                LOG.warn(error);
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Finish getL2CachedObject");
            }
            return ecServicesCache;
        }

        /**
         * @param ecServicesCache the ecServicesCache
         * @param cacheName the cacheName
         * @param cacheKey the cacheKey
         * @param result the result
         * @param locale the locale
         */
        private void setL2CachedObject(final EcServicesCache ecServicesCache, final String cacheName, final String cacheKey,
                final Object result, final Locale locale) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Start setL2CachedObject");
            }

            try {
                GoAndSaveObjectToCacheStore goAndSaveObjectToL2Cache = new GoAndSaveObjectToCacheStore(ecServicesCache, cacheName, cacheKey,
                        result, locale);
                executor.submit(goAndSaveObjectToL2Cache);
            } catch (Throwable throwable) {
                StringBuilder error = new StringBuilder("Exception while saving the object to L2(Database) cache.");
                error.append(" cacheName = ").append(cacheName);
                error.append(", cacheKey = ").append(cacheKey).append(", locale = ").append(locale);
                error.append(", Exception = ").append(throwable);
                LOG.error(error, throwable);
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Finish setL2CachedObject");
            }
        }

        /**
         * <pre>
         * ************************************************************************************************
         * Copyright (c) Electrocomponents Plc.
         *
         * Author  : Ganesh Raut
         * Created : 8 May 2012 at 15:24:52
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
        public class GoAndSaveObjectToCacheStore implements Runnable {

            /** ecServicesCache. */
            private EcServicesCache ecServicesCache;
            /** The cache access path. */
            private String cacheName;
            /** The cache key. */
            private String cacheKey;
            /** The result object to cache. */
            private Object result;
            /** ecServicesCache. */
            private Locale locale;

            /**
             * @param ecServicesCache the ecServicesCache
             * @param cacheName the cacheName
             * @param cacheKey the cacheKey
             * @param result the result
             * @param locale the locale
             */
            public GoAndSaveObjectToCacheStore(final EcServicesCache ecServicesCache, final String cacheName, final String cacheKey,
                    final Object result, final Locale locale) {
                this.ecServicesCache = ecServicesCache;
                this.cacheName = cacheName;
                this.cacheKey = cacheKey;
                this.result = result;
                this.locale = locale;
            }

            @Override
            public void run() {
                try {
                    EcServicesCacheService ecServicesCacheService = ServiceLocator.locateLocalOrRemote(EcServicesCacheService.class, locale);
                    EcServicesCache ecServicesCacheToUpdate = ecServicesCache;

                    /* If ecServicesCacheToUpdate is null, create a new instance of EcServicesCache entity. */
                    if (ecServicesCacheToUpdate == null) {
                        ecServicesCacheToUpdate = ecServicesCacheService.getInstance(EcServicesCache.class);
                    }
                    byte[] serailisedObject = serailiseObjectToCache(result);
                    ecServicesCacheToUpdate.setCacheAccessPath(cacheName);
                    ecServicesCacheToUpdate.setCacheKey(cacheKey);
                    ecServicesCacheToUpdate.setValue(serailisedObject);
                    ecServicesCacheToUpdate.setLastModifiedTime(new DateTime());
                    ecServicesCacheService.saveServicesCache(ecServicesCacheToUpdate);
                } catch (Throwable throwable) {
                    StringBuilder error = new StringBuilder("Exception while saving the object to L2(Database) cache.");
                    error.append(" cacheName = ").append(cacheName);
                    error.append(", cacheKey = ").append(cacheKey).append(", locale = ").append(locale);
                    error.append(", Exception = ").append(throwable);
                    LOG.error(error, throwable);
                }
            }
        }

        /**
         * A method to convert the byte[] to an object.
         * @param cachedObject a cachedObject to deserailise
         * @return deserailiseCachedd object
         */
        private Object deserailiseCachedObject(final byte[] cachedObject) {
            Object deserailisedObject = null;

            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(cachedObject));
                deserailisedObject = objectInputStream.readObject();
            } catch (Throwable throwable) {
                StringBuilder error = new StringBuilder("Exception while deserailising the L2(database) cached object. ");
                error.append(" cacheName = ").append(cacheName);
                error.append(", cacheKey = ").append(cacheKey).append(", locale = ").append(locale);
                error.append(", Exception = ").append(throwable);
                LOG.warn(error);
            }

            return deserailisedObject;
        }

        /**
         * A method to convert the object to byte[].
         * @param object the object
         * @return byte[]
         */
        private byte[] serailiseObjectToCache(final Object object) {
            byte[] serailisedObject = null;
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(byteArrayOutputStream));
                objectOutputStream.flush();
                objectOutputStream.writeObject(object);
                objectOutputStream.flush();
                serailisedObject = byteArrayOutputStream.toByteArray();
                objectOutputStream.close();
            } catch (Throwable throwable) {
                StringBuilder error = new StringBuilder("Exception while serailising the object. ");
                error.append(" cacheName = ").append(cacheName);
                error.append(", cacheKey = ").append(cacheKey).append(", locale = ").append(locale);
                error.append(", Exception = ").append(throwable);
                LOG.warn(error);
            }
            return serailisedObject;
        }
    }
}
