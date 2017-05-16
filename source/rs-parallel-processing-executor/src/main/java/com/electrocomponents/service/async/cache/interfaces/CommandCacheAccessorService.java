package com.electrocomponents.service.async.cache.interfaces;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.electrocomponents.executor.EcCommand;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Vijay Swarnkar
 * Created : 19 Jan 2011 at 13:24:36
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
 * A service class to support the caching of command objects in non transaction cache.
 * @author Vijay Swarnkar
 */
public interface CommandCacheAccessorService {
    
    String EJB_MODULE_NAME = "rs-parallel-processing-executor";

    String SERVICE_NAME = "CommandCacheAccessorService";
    
    /**
     * A method to set the EcCommand object in cache.
     * @param cacheKeyBySessionId cacheKeyBySessionId.
     * @param pageId pageId.
     * @param commandKey commandKey.
     * @param callableObject EcCommand object.s
     */
    void putEcCommand(final String cacheKeyBySessionId, final String pageId, final String commandKey, final EcCommand callableObject);

    /**
     * A method to retrieve the EcCommand object from the cache for a command-key.
     * @param cacheKeyBySessionId cacheKeyBySessionId.
     * @param pageId pageId.
     * @param commandKey commandKey.
     * @return the Command object.
     */
    EcCommand getEcCommand(final String cacheKeyBySessionId, final String pageId, final String commandKey);
    
    /**
     * A method to retrieve the EcCommand object from the cache for a given page-id.
     * @param cacheKeyBySessionId cacheKeyBySessionId.
     * @param pageId pageId.
     * @return the Command object.
     */
    Map<String, EcCommand> getEcCommands(final String cacheKeyBySessionId, final String pageId);
    
    /**
     * A method to retrieve the EcCommand object from the cache.
     * @param cacheKeyBySessionId cacheKeyBySessionId.
     * @return the Command object.
     */
    Map<String, Map<String, EcCommand>> getEcCommands(final String cacheKeyBySessionId);

    /**
     * A method to set the Future object in cache.
     * @param cacheKeyBySessionId cacheKeyBySessionId.
     * @param pageId pageId.
     * @param futureKey futureKey.
     * @param callableObject Future<EcCommand> object.s
     */
    void putFuture(final String cacheKeyBySessionId, final String pageId, final String futureKey, final Future<EcCommand> callableObject);

    /**
     * A method to retrieve the Future object from the cache.
     * @param cacheKeyBySessionId cacheKeyBySessionId.
     * @param pageId pageId.
     * @param futureKey futureKey.
     * @return the Command object.
     */
    Future<EcCommand> getFuture(final String cacheKeyBySessionId, final String pageId, final String futureKey);

    /**
     * A method to fetch the commands from the requested cache path.
     * @param cacheKeyBySessionId cacheKeyBySessionId.
     * @param pageId pageId.
     * @return Map
     */
    Map<String, Future<EcCommand>> getFutures(final String cacheKeyBySessionId, final String pageId);
    
    /**
     * A method to fetch all futures from cache for a given session-id.
     * @param cacheKeyBySessionId cacheKeyBySessionId.
     * @return Map
     */
    Map<String, Map<String, Future<EcCommand>>> getFutures(final String cacheKeyBySessionId);

    /**
     * A method to return the total number of nodes under the specified page-id.
     * @param cachePath cache path.
     * @param pageId pageId.
     * @return count of total number of commands.
     */
    int getPageCommandCount(final String cacheKeyBySessionId, final String pageId);
    
    
    /**
     * A method to return the total number of futures under the specified page-id.
     * @param sessionCacheKey sessionCacheKey.
     * @param pageId pageId.
     * @return count of total number of futures.
     */
    int getPageCommandFuturesCount(final String sessionCacheKey, final String pageId);

    
    /**
     * A method to clear the cache content from the memory, for a given list of page ids.
     * @param sessionCacheKey sessionCacheKey.
     * @param pageIdList list of page ids.
     */
    void clearCaches(final String sessionCacheKey, final List<String> pageIdList);
    
    /**
     * A method to clear the cache content from the memory, for a given page id.
     * @param cachePathList list of cache path from where to clear.
     */
    void clearCache(final String sessionCacheKey, final String pageId);
    
    /**
     * A method to clear the cache content from the memory for a given session id.
     * @param sessionCacheKey sessionCacheKey for which the cache to be cleared.
     */
    void clearCache(final String sessionCacheKey);

}
