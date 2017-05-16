package com.electrocomponents.service.servicescache;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.model.data.servicescache.EcServicesCacheEntity;
import com.electrocomponents.model.domain.Locale;
import com.electrocomponents.model.domain.servicescache.EcServicesCache;
import com.electrocomponents.service.core.client.ServiceLocator;
import com.electrocomponents.service.servicescache.interfaces.EcServicesCacheService;
import com.electrocomponents.service.servicescache.locator.EcServicesCacheServiceLocator;

/**
 * 
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : JIm Britton
 * Created : 20 Jun 2016 at 14:25:57
 *
 * ************************************************************************************************
 * </pre>
 * 
 * Integration tests will only be run when the integration-test goal is executed. They are run by the 
 * Maven Failsafe plugin and should be named *IT.java (this convention comes from the Failsafe plugin).
 * 
 * TODO: delete me.
 */
public class EcServicesCacheServiceBeanIT extends TestCase {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(EcServicesCacheServiceBeanIT.class);

    /** Test service object. */
    private EcServicesCacheService ecServicesCacheService = null;

    /** Test String cacheKey. */
    private String cacheKey = "KEY1234567890";

    /** Test String cacheAccessPath. */
    private String cacheAccessPath = "/service/crm/metada/TEST";
    
    private static String applicationName="java-object-cache-centralised";

    /**
     * @return the test suite
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(EcServicesCacheServiceBeanIT.class);
    }

    /**
     * A method to setup data required for test case.
     */
    public void setUp() {

        LOG.info("Start Set up data for the test.");

        ecServicesCacheService = ServiceLocator.locateRemote(applicationName, EcServicesCacheService.class);
        assertNotNull("EcCacheService is Null", ecServicesCacheService);

        LOG.info("End Set up for the test.");
    }

    /**
     * TTP:- This method tests the getInstance() method of the Service, should return an instance of EcServicesCacheEntity.
     */
    public void testGetInstance() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start testGetInstance");
        }
        EcServicesCache ecServicesCache = ecServicesCacheService.getInstance(EcServicesCache.class);
        assertNotNull(ecServicesCache);
        assertTrue(ecServicesCache instanceof EcServicesCacheEntity);
        if (LOG.isDebugEnabled()) {
            LOG.debug("End testGetInstance");
        }
    }

    /**
     * TTF:- This method tests the getInstance() method of the Service, should not return an instance of EcServicesCacheEntity.
     */
    public void testGetInstance2() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("End testGetInstance2");
        }
        EcServicesCache ecServicesCache = ecServicesCacheService.getInstance(null);
        assertNull(ecServicesCache);
        if (LOG.isDebugEnabled()) {
            LOG.debug("End testGetInstance2");
        }
    }

    /**
     * TTP: Test case to create EcCacheService.
     */
    public void testSaveEcCacheService() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Method testSaveEcCacheService Start");
        }
        EcServicesCache servicesCache = ecServicesCacheService.getServicesCache(cacheAccessPath, cacheKey);
        if (servicesCache != null) {
            Boolean isDeleted = ecServicesCacheService.deleteServicesCache(servicesCache);
            if (LOG.isDebugEnabled() && isDeleted) {
                LOG.debug("Deleted existing EcServicesCache object with testing values.");
            }
        }
        EcServicesCache ecServicesCache = ecServicesCacheService.getInstance(EcServicesCache.class);
        assertNotNull(ecServicesCache);
        ecServicesCache.setCacheKey(cacheKey);
        ecServicesCache.setCacheAccessPath(cacheAccessPath);
        String testString = "This is EcServices testing data for the Value column";
        ecServicesCache.setValue(testString.getBytes());

        EcServicesCache newServicesCache = ecServicesCacheService.saveServicesCache(ecServicesCache);
        assertNotNull(newServicesCache);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Method testSaveEcCacheService End");
        }
    }

    /**
     * TTP: Test case to update existing EcCacheService.
     */
    public void testSaveEcCacheService2() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Method testSaveEcCacheService2 Start");
        }
        EcServicesCache ecServicesCache = ecServicesCacheService.getInstance(EcServicesCache.class);
        assertNotNull(ecServicesCache);
        ecServicesCache.setCacheKey(cacheKey);
        ecServicesCache.setCacheAccessPath(cacheAccessPath);
        String testString = "This is EcServices testing data for the Value column";
        ecServicesCache.setValue(testString.getBytes());

        EcServicesCache servicesCache = ecServicesCacheService.saveServicesCache(ecServicesCache);
        assertNotNull(servicesCache);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Method testSaveEcCacheService2 End");
        }
    }

    /**
     * TTF: Test case to create EcCacheService with invalid parameters.
     */
    public void testSaveEcCacheService3() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start testSaveEcCacheService3.");
        }
        EcServicesCache ecServicesCache = ecServicesCacheService.getInstance(EcServicesCache.class);
        ecServicesCache.setCacheKey(null);
        ecServicesCache.setCacheAccessPath(null);
        String testString = "This is EcServices testing data for the Value column";
        ecServicesCache.setValue(testString.getBytes());
        boolean saveSuccessful = true;
        try {
            EcServicesCache servicesCache = ecServicesCacheService.saveServicesCache(ecServicesCache);
            assertNull(servicesCache);
        } catch (Exception e) {
            LOG.error(e);
            saveSuccessful = false;
        }
        assertFalse(saveSuccessful);
        if (LOG.isDebugEnabled()) {
            LOG.debug("End testSaveEcCacheService3.");
        }
    }

    /**
     * TTP: Test case to retrieve EcCacheService.
     */
    public void testGetEcCacheService() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Method testGetEcCacheService Start");
        }

        EcServicesCache servicesCache = ecServicesCacheService.getServicesCache(cacheAccessPath, cacheKey);
        assertNotNull(servicesCache);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Method testGetEcCacheService End");
        }
    }

    /**
     * TTF: Test case to retrieve EcCacheService with invalid parameters.
     */
    public void testGetEcCacheService2() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Method testGetEcCacheService2 Start");
        }
        boolean findSuccessful = true;
        try {
            EcServicesCache servicesCache = ecServicesCacheService.getServicesCache(null, null);
            assertNull(servicesCache);
        } catch (Exception e) {
            LOG.error(e);
            findSuccessful = false;
        }
        assertFalse(findSuccessful);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Method testGetEcCacheService2 End");
        }
    }

    /**
     * TTP: Test case to delete EcCacheService.
     */
    public void testDeleteEcCacheService() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start testDeleteEcCacheService.");
        }

        EcServicesCache servicesCache = ecServicesCacheService.getServicesCache(cacheAccessPath, cacheKey);
        assertNotNull(servicesCache);

        Boolean isDeleted = ecServicesCacheService.deleteServicesCache(servicesCache);
        assertEquals("EcServicesCache deletion Flag is false", isDeleted, Boolean.TRUE);

        if (LOG.isDebugEnabled()) {
            LOG.debug("End testDeleteEcCacheService.");
        }
    }

}
