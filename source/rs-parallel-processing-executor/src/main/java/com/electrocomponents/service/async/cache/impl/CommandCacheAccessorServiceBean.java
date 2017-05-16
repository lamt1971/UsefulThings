package com.electrocomponents.service.async.cache.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.commons.cache.CacheName;
import com.electrocomponents.executor.EcCommand;
import com.electrocomponents.service.async.cache.interfaces.CommandCacheAccessorService;
import com.electrocomponents.service.async.cache.interfaces.CommandCacheAccessorServiceLocal;
import com.electrocomponents.service.async.cache.interfaces.CommandCacheAccessorServiceRemote;
import com.electrocomponents.service.core.validation.ValidationUtility;
import com.electrocomponents.service.objectcache.interfaces.CacheFacade;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Vijay Swarnkar
 * Created : 19 Jan 2011 at 13:25:07
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
 * An implementation class to store the command objects in the cache and retrieve them back.
 * @author Vijay Swarnkar
 */
@Singleton(name=CommandCacheAccessorService.SERVICE_NAME)
@Local(CommandCacheAccessorServiceLocal.class)
@Remote(CommandCacheAccessorServiceRemote.class)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class CommandCacheAccessorServiceBean implements CommandCacheAccessorServiceLocal {

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(CommandCacheAccessorServiceBean.class);

    @EJB
    CacheFacade inMemoryCache;

    /** {@inheritDoc} */
    public EcCommand getEcCommand(final String sessionCacheKey, final String pageId, final String commandKey) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getEcCommand.");
        }
        ValidationUtility.validateMandatoryParameter("cacheKeyBySessionId", sessionCacheKey, CommandCacheAccessorServiceBean.class.getName());
        ValidationUtility.validateMandatoryParameter("pageId", pageId, CommandCacheAccessorServiceBean.class.getName());
        ValidationUtility.validateMandatoryParameter("commandKey", commandKey, CommandCacheAccessorServiceBean.class.getName());

        EcCommand ecCommand = null;
        /* Get the EcCommand object from the cache. */
        Map<String, EcCommand> pageCommands = getEcCommands(sessionCacheKey, pageId);
        if(pageCommands != null) {
        	ecCommand = pageCommands.get(commandKey);
        }        

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getEcCommand.");
        }
        return ecCommand;
    }
    
    
    /** {@inheritDoc} */
    public Map<String, EcCommand> getEcCommands(final String cacheKeyBySessionId, final String pageId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getPageEcCommands.");
        }
        ValidationUtility.validateMandatoryParameter("cacheKeyBySessionId", cacheKeyBySessionId, CommandCacheAccessorServiceBean.class.getName());
        ValidationUtility.validateMandatoryParameter("pageId", pageId, CommandCacheAccessorServiceBean.class.getName());

        Map<String, EcCommand> cachedPageComamnds = null;        
        Map<String, Map<String, EcCommand>> sessionsComamndsCache = getEcCommands(cacheKeyBySessionId);

        if(sessionsComamndsCache == null ){
        	LOG.warn("The key: " + cacheKeyBySessionId + "does not exist in cache..");        	
        } else {
        	cachedPageComamnds = (Map<String, EcCommand>) sessionsComamndsCache.get(pageId);
            if (cachedPageComamnds==null && cachedPageComamnds.keySet().isEmpty()) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("The cacheKeyBySessionId : " + cacheKeyBySessionId + ", pageId : "+ pageId + " does not have any child nodes in cache..");
                }
            }
        }
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getPageEcCommands.");
        }
        return cachedPageComamnds;
    }
    
    /** {@inheritDoc} */
    public Map<String, Map<String, EcCommand>> getEcCommands(final String cacheKeyBySessionId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getPageEcCommands.");
        }
        ValidationUtility.validateMandatoryParameter("cacheKeyBySessionId", cacheKeyBySessionId, CommandCacheAccessorServiceBean.class.getName());

        
        /* Map with command type as key, containing the commandRole hash map. */
        Map<String, Map<String, EcCommand>> sessionsComamndsCache = (Map<String, Map<String, EcCommand>>) inMemoryCache.get(CacheName.CACHE_NAME_RS_PARALLEL_PROCESSING_EXECUTOR, cacheKeyBySessionId);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getPageEcCommands.");
        }
        return sessionsComamndsCache;
    }

    /** {@inheritDoc} */
    public void putEcCommand(final String sessionCacheKey, final String pageId, final String commandKey, final EcCommand ecCommand) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start putEcCommand.");
        }
        ValidationUtility.validateMandatoryParameter("sessionCacheKey", sessionCacheKey, CommandCacheAccessorServiceBean.class.getName());
        ValidationUtility.validateMandatoryParameter("pageId", pageId, CommandCacheAccessorServiceBean.class.getName());
        ValidationUtility.validateMandatoryParameter("commandKey", commandKey, CommandCacheAccessorServiceBean.class.getName());
        ValidationUtility.validateMandatoryParameter("ecCommand", ecCommand, CommandCacheAccessorServiceBean.class.getName());
        if (LOG.isDebugEnabled()) {
            LOG.debug("Cache access key - sessionCacheKey : " + sessionCacheKey + ",pageId : " + pageId + ",commandKey: " + commandKey );
        }
       
        //Map<PageId, Map<CommanId, Command>> 
        Map<String, Map<String, EcCommand>> cachedSessionComamnds =  getEcCommands(sessionCacheKey);
        Map<String, EcCommand> pageCommands = null;
        if(cachedSessionComamnds == null) {
        	cachedSessionComamnds = new HashMap<String, Map<String, EcCommand>>();
        	inMemoryCache.put(CacheName.CACHE_NAME_RS_PARALLEL_PROCESSING_EXECUTOR, sessionCacheKey, cachedSessionComamnds);
        }
        
        pageCommands = cachedSessionComamnds.get(pageId);
        
        if(pageCommands == null) {
        	pageCommands = new HashMap<String, EcCommand>();
        	cachedSessionComamnds.put(pageId, pageCommands);
        }
        pageCommands.put(commandKey, ecCommand);
        
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish putEcCommand.");
        }

    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public Future<EcCommand> getFuture(final String sessionCacheKey, final String pageId, final String commandKey) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getFuture.");
        }
        ValidationUtility.validateMandatoryParameter("cacheKeyBySessionId", sessionCacheKey, CommandCacheAccessorServiceBean.class.getName());
        ValidationUtility.validateMandatoryParameter("pageId", pageId, CommandCacheAccessorServiceBean.class.getName());
        ValidationUtility.validateMandatoryParameter("commandKey", commandKey, CommandCacheAccessorServiceBean.class.getName());

        Future<EcCommand> future = null;
        /* Get the EcCommand object from the cache. */
        Map<String, Future<EcCommand>> pageCommandFutures = getFutures(sessionCacheKey, pageId);
        if(pageCommandFutures != null) {
        	future = pageCommandFutures.get(commandKey);
        }    
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getFuture.");
        }
        return future;
    }

    /** {@inheritDoc} */
    public Map<String, Map<String, Future<EcCommand>>> getFutures(final String sessionCacheKey) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getFutures.");
        }
        ValidationUtility.validateMandatoryParameter("sessionCacheKey", sessionCacheKey, CommandCacheAccessorServiceBean.class.getName());

        /* Map with command type as key, containing the commandRole hash map. */
        Map<String, Map<String, Future<EcCommand>>> sessionsComamndsCache = (Map<String, Map<String, Future<EcCommand>>>) inMemoryCache.get(CacheName.CACHE_NAME_RS_PARALLEL_PROCESSING_EXECUTOR, sessionCacheKey);

        if(sessionsComamndsCache == null ){
        	LOG.info("The sessionCacheKey : " + sessionCacheKey + "does not exist in cache..");        	
        } 
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getFutures.");
        }
        return sessionsComamndsCache;
    }
    
    /** {@inheritDoc} */
    public Map<String, Future<EcCommand>> getFutures(final String cacheKeyBySessionId, final String pageId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getFutures.");
        }
        ValidationUtility.validateMandatoryParameter("cacheKeyBySessionId", cacheKeyBySessionId, CommandCacheAccessorServiceBean.class.getName());
        ValidationUtility.validateMandatoryParameter("pageId", pageId, CommandCacheAccessorServiceBean.class.getName());

        Map<String, Future<EcCommand>> cachedPageComamndFutures = null;        
        Map<String, Map<String, Future<EcCommand>>> sessionsComamndsCacheFutures = getFutures(cacheKeyBySessionId);

        if(sessionsComamndsCacheFutures == null ){
        	LOG.info("The cacheKeyBySessionId: " + cacheKeyBySessionId + "does not exist in cache..");        	
        } else {
        	cachedPageComamndFutures = (Map<String, Future<EcCommand>>) sessionsComamndsCacheFutures.get(pageId);
        }
        
        return cachedPageComamndFutures;
    }
    
    

    /** {@inheritDoc} */
    public void putFuture(final String sessionCacheKey, final String pageId, final String commandKey, final Future<EcCommand> ecCommand) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start putFuture.");
        }
        ValidationUtility.validateMandatoryParameter("sessionCacheKey", sessionCacheKey, CommandCacheAccessorServiceBean.class.getName());
        ValidationUtility.validateMandatoryParameter("pageId", pageId, CommandCacheAccessorServiceBean.class.getName());
        ValidationUtility.validateMandatoryParameter("commandKey", commandKey, CommandCacheAccessorServiceBean.class.getName());
        ValidationUtility.validateMandatoryParameter("EcCommand", ecCommand, CommandCacheAccessorServiceBean.class.getName());

        if (LOG.isDebugEnabled()) {
            LOG.debug("Cache access path is sessionCacheKey : " + sessionCacheKey + ", pageId: " + pageId + ", commandKey : " + commandKey);
        }
        
        
      //Map<PageId, Map<CommanId, Command>> 
        Map<String, Map<String, Future<EcCommand>>> cachedSessionComamndFutures =  getFutures(sessionCacheKey);
        Map<String, Future<EcCommand>> pageCommandFutures = null;
        if(cachedSessionComamndFutures == null) {
        	cachedSessionComamndFutures = new HashMap<String, Map<String, Future<EcCommand>>>();
        	inMemoryCache.put(CacheName.CACHE_NAME_RS_PARALLEL_PROCESSING_EXECUTOR, sessionCacheKey, cachedSessionComamndFutures);
        }
        
        pageCommandFutures = cachedSessionComamndFutures.get(pageId);
        
        if(pageCommandFutures == null) {
        	pageCommandFutures = new HashMap<String, Future<EcCommand>>();
        	cachedSessionComamndFutures.put(pageId, pageCommandFutures);
        }
        pageCommandFutures.put(commandKey, ecCommand);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish putFuture.");
        }
    }

    /** {@inheritDoc} */
    public int getPageCommandCount(final String sessionCacheKey, final String pageId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getPageCommandCount.");
        }
        ValidationUtility.validateMandatoryParameter("sessionCacheKey", sessionCacheKey, CommandCacheAccessorServiceBean.class.getName());
        ValidationUtility.validateMandatoryParameter("pageId", pageId, CommandCacheAccessorServiceBean.class.getName());
        int pageCount = 0;
        Map<String, EcCommand> pageCommands =  getEcCommands(sessionCacheKey, pageId);
        
        if(pageCommands != null) {
            if(pageCommands != null) {
            	pageCount = pageCommands.keySet().size();	
            }            
        }
               
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getPageCommandCount.");
        }
        return pageCount;
    }
    
    /** {@inheritDoc} */
    public int getPageCommandFuturesCount(final String sessionCacheKey, final String pageId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getPageCommandFuturesCount.");
        }
        ValidationUtility.validateMandatoryParameter("sessionCacheKey", sessionCacheKey, CommandCacheAccessorServiceBean.class.getName());
        ValidationUtility.validateMandatoryParameter("pageId", pageId, CommandCacheAccessorServiceBean.class.getName());
        int pageCount = 0;
        Map<String, Future<EcCommand>> pageCommandFutures =  getFutures(sessionCacheKey, pageId);
        
        if(pageCommandFutures != null) {
            if(pageCommandFutures != null) {
            	pageCount = pageCommandFutures.keySet().size();	
            }            
        }
               
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getPageCommandFuturesCount.");
        }
        return pageCount;
    }
    
    /** {@inheritDoc} */
    public void clearCaches(final String cacheKeyBySessionId, final List<String> pageIdList) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start clearCaches.");
        }
        
        ValidationUtility.validateMandatoryParameter("cacheKeyBySessionId", cacheKeyBySessionId, CommandCacheAccessorServiceBean.class.getName());
        ValidationUtility.validateMandatoryParameter("pageIdList", pageIdList, CommandCacheAccessorServiceBean.class.getName());
        
        /* Removes the node from the tree. */
        if (pageIdList!=null && !pageIdList.isEmpty()) {
            for (String pageId : pageIdList) {
                /*
                 * Iterate the list for all the cache path, If the path is invalid showing a warning message and continuing deleting other
                 * cache paths.
                 */
                try {
                    clearCache(cacheKeyBySessionId, pageId);
                } catch (Exception exception) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Problem in clearing the cache for session : " + cacheKeyBySessionId + ", pageId:"+ pageId, exception);
                    }
                }
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish clearCaches.");
        }

    }
    
    /** {@inheritDoc} */
    public void clearCache(final String cacheKeyBySessionId, final String pageId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start clearCaches.");
        }
        ValidationUtility.validateMandatoryParameter("cacheKeyBySessionId", cacheKeyBySessionId, CommandCacheAccessorServiceBean.class.getName());
        ValidationUtility.validateMandatoryParameter("pageIdList", pageId, CommandCacheAccessorServiceBean.class.getName());
        /* Removes the node from the tree. */
        if (inMemoryCache.containsKey(CacheName.CACHE_NAME_RS_PARALLEL_PROCESSING_EXECUTOR, cacheKeyBySessionId)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("clearing the page commands from session id : " + cacheKeyBySessionId + ", page-id :" + pageId);
            }
            Map<String, Map<String, Object>> cacheEntry =  (Map<String, Map<String, Object>>) inMemoryCache.get(CacheName.CACHE_NAME_RS_PARALLEL_PROCESSING_EXECUTOR, cacheKeyBySessionId);
            cacheEntry.remove(pageId);            
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish clearCaches.");
        }

    }

    /** {@inheritDoc} */
    public void clearCache(final String cacheKeyBySessionId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start clearCachePath.");
        }

        if (cacheKeyBySessionId != null) {
            
            try {
                if (inMemoryCache.containsKey(CacheName.CACHE_NAME_RS_PARALLEL_PROCESSING_EXECUTOR, cacheKeyBySessionId)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("clearing the commands cache for key : " + cacheKeyBySessionId);
                    }
                    Map<String, Map<String, Object>> cacheEntry =  (Map<String, Map<String, Object>>) inMemoryCache.get(CacheName.CACHE_NAME_RS_PARALLEL_PROCESSING_EXECUTOR, cacheKeyBySessionId);
                    cacheEntry.clear();
                    inMemoryCache.remove(CacheName.CACHE_NAME_RS_PARALLEL_PROCESSING_EXECUTOR,cacheKeyBySessionId);
                    cacheEntry = null;	
                }
            } catch (Exception e) {
                LOG.warn("Problem removing object from cache from the node: " + cacheKeyBySessionId + ".  Problem: " + e, e);
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish clearCachePath.");
        }

    }

}
