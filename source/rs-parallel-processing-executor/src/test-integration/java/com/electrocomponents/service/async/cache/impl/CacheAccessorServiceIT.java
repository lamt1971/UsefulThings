package com.electrocomponents.service.async.cache.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.executor.EcCommand;
import com.electrocomponents.executor.EcCommandSpecification;
import com.electrocomponents.executor.exceptions.EcCommandNotValidException;
import com.electrocomponents.model.domain.Locale;
import com.electrocomponents.service.async.cache.interfaces.CommandCacheAccessorService;
import com.electrocomponents.service.core.client.ServiceLocator;

import junit.framework.TestCase;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Vijay Swarnkar
 * Created : 20 Jan 2011 at 09:00:11
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
 * A test class to test CacheAccessorService service.
 * @author Vijay Swarnkar
 */
public class CacheAccessorServiceIT extends TestCase {

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(CacheAccessorServiceIT.class);

    /** Locale object. */
    private static Locale locale;

    /** Catalogue Service instance. */
    private static CommandCacheAccessorService cacheAccessorService;

    /** Default command role string. */
    private static String commandRole = "commandRole";
    
    private static final String APP_NAME="rs-parallel-processing-executor";

    /** Default Cache path. */
    private static final String CACHE_PATH = "cacheRootPath/command/session/";

    /**
     * The set up method for initialising data for test case.
     */
    @Override
    protected void setUp() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start setUp.");
        }
        
        locale = new Locale("uk");
        cacheAccessorService = ServiceLocator.locateRemote(APP_NAME, CommandCacheAccessorService.class);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish setUp.");
        }
    }

    /**
     * TTP: A test case to test put service method.
     */
    public void testPutEcCommand() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start testPutEcCommand.");
        }
        
        EcCommand ecCommand = getEcCommand();
        assertNotNull(ecCommand);
        String sessionId = "Session-1";
        String pageId = "Page-123";
        cacheAccessorService.putEcCommand(sessionId,pageId, commandRole, ecCommand);
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish testPutEcCommand.");
        }
    }

    /**
     * TTP: A test case to test getEcCommand service method.
     */
    public void testGetEcCommand() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start testGetEcCommand.");
        }
        
        String sessionId = "Session-1";
        String pageId = "Page-123";
        EcCommand ecCommand = cacheAccessorService.getEcCommand(sessionId,pageId, commandRole);
        assertNotNull(ecCommand);
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish testGetEcCommand.");
        }
    }

    /**
     * TTP: A test case to test getPageCount method.
     */

    public void testGetPageCommandCount() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start testGetPageCommandCount.");
        }
        
        String sessionId = "Session-1";
        String pageId = "Page-123";
        int pageCount = cacheAccessorService.getPageCommandCount(sessionId, pageId);
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Page count for the requested cache path == " + pageCount);
            LOG.debug("Finish testGetPageCommandCount");
        }
    }

    

    /** TTP: A test case to test getPageEcCommands method. */
    public void testGetPageEcCommands() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start testGetPageEcCommands.");
        }
        String sessionId = "Session-1";
        Map<String, Map<String, EcCommand>> ecCommandsMap = cacheAccessorService.getEcCommands(sessionId);
           if (ecCommandsMap != null) {
                LOG.info(ecCommandsMap.size());
            }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish testGetPageEcCommands.");
        }

    }

    /**
     * TTP: A test case to test clearCaches method.
     */
    public void testClearCaches() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start testClearCaches.");
        }
        
        List<String> cacheList = new ArrayList<String>();
        String sessionId = "Session-1";
        for (int i = 0; i < 5; i++) {
            String pageId = String.valueOf(i);
            String cachePath = pageId;
            cacheList.add(cachePath);
        }

        cacheAccessorService.clearCaches(sessionId, cacheList);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish testClearCaches.");
        }
    }

    /**
     * TTP: A test case to test getEcCommand service method.
     */
    public void testClearCachePath() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start testClearCachePath.");
        }

        String sessionId = "Session-1";
        cacheAccessorService.clearCache(sessionId);
        
        Map<String, Map<String, EcCommand>> ecCommandsMap = cacheAccessorService.getEcCommands(sessionId);
        assertNull(ecCommandsMap);
        if (ecCommandsMap != null) {
             LOG.info(ecCommandsMap.size());
         }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish testClearCachePath.");
        }
    }

    /**
     * A method to construct the default cache path.
     * @param sessionId session ID.
     * @param pageId page ID.
     * @param commandType command type.
     * @return cache path.
     */
    private String buildCachePath(final String sessionId, final String pageId, final String commandType) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start buildCachePath.");
        }

        String cachePath = CACHE_PATH + sessionId + "/" + pageId + "/" + commandType;

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish buildCachePath.");
        }
        
        return cachePath;
    }

    private final class DummyCommandSpecification extends EcCommandSpecification implements Serializable{
        public DummyCommandSpecification(){
            super("123456789","28",commandRole,"",locale);
        }
        
        @Override
        public void validate() throws EcCommandNotValidException {
        }

        @Override
        public EcCommand buildCommand() {
            return new DummyCommand();
        }
        
    }
    
    private final class DummyCommand extends EcCommand{

        @Override
        public EcCommand execute() {
            return this;
        }

        @Override
        public Object getResult() {
            return new Object();
        }

        @Override
        public Throwable getError() {
            return null;
        }

        @Override
        public String toString() {
            return "dummyString";
        }
        
    }
    
    private EcCommand getEcCommand(){
        return new DummyCommandSpecification().buildCommand();
    }
}
