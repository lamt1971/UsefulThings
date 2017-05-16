package com.electrocomponents.executor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.commons.cache.CacheKeyGenerator;
import com.electrocomponents.executor.exceptions.EcCommandLimitException;
import com.electrocomponents.executor.exceptions.EcCommandNotValidException;
import com.electrocomponents.executor.exceptions.EcCommandResultNotFound;
import com.electrocomponents.model.domain.Locale;
import com.electrocomponents.service.async.cache.interfaces.CommandCacheAccessorService;
import com.electrocomponents.service.core.client.ApplicationNameResolver;
import com.electrocomponents.service.core.client.ServiceLocator;

/**
 * The Command Manager provides methods to execute {@link EcCommand}s immediately or defer the execution until the results are needed. In
 * addition to this, it also provides methods to get the result of the executed EcCommands and also to shutdown the executor. More details
 * on this methods can be seen at the method level javadocs.
 * @author Bhavesh Kavia
 * @param <V>
 */
public abstract class EcCommandManager<V> {
    /** A Commons Logger. */
    private static final Log LOG = LogFactory.getLog(EcCommandManager.class);

    /** The Command Path Type. */
    private static final String PATH_TYPE_COMMAND = "command";

    /** The Future Path Type. */
    private static final String PATH_TYPE_FUTURE = "future";

    /** A String representing Sessions. */
    private static final String SESSIONS = "sessions";

    /** A Separator for the cache path. */
    private static final String CACHE_PATH_SEPARATOR = "/";

    /** The Maximum number of Commands to be cached per page. */
    private final Integer commandCreationMax;

    /** The Maximum number of Futures to be cached per page. */
    private final Integer futureCreationMax;

    /** The Cache path Root. */
    private final String cacheRootPath;

    /** The Command Manager Id. This should be the session ID */
    private final String commandManagerId;

    /** The Executor for the Manager. */
    private BackgroundTaskCommandExecutor<V> executor;

    /** The Locale the session belongs to. */
    private final Locale locale;

    /** The results timeout. */
    private final Long resultTimeout;

    /** The List to hold page Ids held in Cache for Commands. */
    private final LinkedList<String> deferedSessionPageIds = new LinkedList<String>();

    /** The List to Hold page Ids held in Cache for Futures. */
    private final LinkedList<String> queuedSessionPageIds = new LinkedList<String>();

    /** The maximum number page Ids that are to be help. */
    private final Integer pageCreationMaxInt;

    /** The command priority. */
    private int cmdPriority = 1000;
    
    /** cache key for commands.   */
    private String commandsCacheKey;  
    
	/** cache key for futures. */
    private String commandFuturesCacheKey;

    /**
     * The Constructor.
     * @param cacheRootPath The Cache root path
     * @param commandManagerId The Command manager Id (or Session Id)
     * @param executor The {@link AsynchronousCommandExecutor}
     * @param resultTimeout The result timeout in milliseconds.
     * @param locale The {@link Locale}
     * @param commandCreationMax The Number of {@link EcCommand} that can be cached.
     * @param futureCreationMax The Number of {@link EcCommand} result {@link Future}s that can be cached.
     * @param pageCreationMaxInt The Number of Page Ids to hold in the session.
     */
    public EcCommandManager(final String cacheRootPath, final String commandManagerId, final BackgroundTaskCommandExecutor<V> executor,
            final Long resultTimeout, final Locale locale, final Integer commandCreationMax, final Integer futureCreationMax,
            final Integer pageCreationMaxInt) {
        this.cacheRootPath = cacheRootPath;
        this.commandManagerId = commandManagerId;
        this.executor = executor;
        this.resultTimeout = resultTimeout;
        this.locale = locale;
        this.commandCreationMax = commandCreationMax;
        this.futureCreationMax = futureCreationMax;
        this.pageCreationMaxInt = pageCreationMaxInt;
        this.commandFuturesCacheKey=CacheKeyGenerator.generateCacheKey(cacheRootPath, commandManagerId, PATH_TYPE_FUTURE );
        this.commandsCacheKey=CacheKeyGenerator.generateCacheKey(cacheRootPath, commandManagerId, PATH_TYPE_COMMAND );
        
        /**
         * workaround to initialise the application name before any background threads starts execution.
         * If Application name is not initialised, then the service lookups from Commands / Background Threads will fail.
         * The application name should have been already initialised by other service lookups, this is just as a safety net.
        */
        ApplicationNameResolver.getInstance().getApplicationName();
        }
    
    /**
     * @return commandsCacheKey commandsCacheKey.
     */
    public String getCommandsCacheKey() {
		return commandsCacheKey;
	}
    
    /**
     * @return commandFuturesCacheKey commandFuturesCacheKey.
     */
    public String getCommandFuturesCacheKey() {
		return commandFuturesCacheKey;
	}

    /**
     * Method does not execute the created {@link EcCommand} immediately it is created but defers it until the results are to retrieved. It
     * validates the {@link EcCommandSpecification} and puts the builts {@link EcCommand} into cache.
     * @param ecCommandSpecification The {@link EcCommandSpecification}
     */
    public final void deferExecution(final EcCommandSpecification ecCommandSpecification) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start deferExecution(1).");
        }
        String pageId = ecCommandSpecification.getPageId();
        String commandType = ecCommandSpecification.getCommandType();
        String commandRole = ecCommandSpecification.getCommandRole();
        
        LOG.info("commandsCacheKeyBySessionId : " + getCommandsCacheKey());

        CommandCacheAccessorService cacheService = ServiceLocator.locateLocalOrRemote(CommandCacheAccessorService.class, locale);
        int pageCount = cacheService.getPageCommandCount(getCommandsCacheKey(), pageId);
        int commandCount = pageCount + 1;

        if (commandCount <= commandCreationMax) {
            EcCommand<V> command = validateAndBuild(ecCommandSpecification);
            cacheService.putEcCommand(getCommandsCacheKey(), pageId, commandType, command);
        } else {
            throw new EcCommandLimitException("Number of EcCommands in cache exceeded the value set of " + commandCreationMax
                    + " for PageId[" + pageId + "] CommandType[" + commandType + "] CommandRole[" + commandRole + "]");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish deferExecution(1).");
        }
    }

    /**
     * Method does not execute the {@link EcCommand}s in the List immediately but defers it until the results are retrieved.
     * @param ecCommandSpecifications A List of {@link EcCommandSpecification} Objects
     */
    public final void deferExecution(final List<EcCommandSpecification> ecCommandSpecifications) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start deferExecution(2).");
        }

        for (EcCommandSpecification ecCommandSpecification : ecCommandSpecifications) {
            deferExecution(ecCommandSpecification);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start deferExecution(2).");
        }
    }

    /**
     * The {@link EcCommandSpecification} will first be validated and the {@link EcCommand} build from it will be executed immediately by
     * the executor. The resulting {@link Future} will then be cached.
     * @param ecCommandSpecification The {@link EcCommandSpecification}
     */
    public final void queueExecution(final EcCommandSpecification ecCommandSpecification) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start queueExecution(1).");
        }

        String pageId = ecCommandSpecification.getPageId();
        String commandType = ecCommandSpecification.getCommandType();
        String commandRole = ecCommandSpecification.getCommandRole();

        CommandCacheAccessorService cacheService = ServiceLocator.locateLocalOrRemote(CommandCacheAccessorService.class, locale);
        int pageCount = cacheService.getPageCommandFuturesCount(getCommandFuturesCacheKey(), pageId);
        int commandCount = pageCount + 1;

        if (commandCount <= futureCreationMax) {
            EcCommand command = validateAndBuild(ecCommandSpecification);
            Future<EcCommand> future = executor.executeCommand(command);
            cacheService.putFuture(getCommandFuturesCacheKey(),pageId, commandType, future);
        } else {
            throw new EcCommandLimitException("Number of Futures in cache exceeded the value set of " + futureCreationMax + " for PageId["
                    + pageId + "] CommandType[" + commandType + "] CommandRole[" + commandRole + "]");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish queueExecution(1).");
        }
    }

    /**
     * The Runnable will be executed immediately by the executor.
     * @param runMe The Runnable.
     */
    public final void queueExecution(final Runnable runMe) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start queueExecution(Runnable).");
            LOG.debug("PERF_TIME_START " + "Start queueExecution(" + runMe.toString() + "). CURRENT QUEUE SIZE : "
                    + getExecutorQueueCurrentSize() + " :Time @ Into the Queue : " + System.currentTimeMillis());
        }

        Future future = executor.executeRunnable(runMe);

        if (LOG.isDebugEnabled()) {
            LOG.debug("PERF_TIME_START " + "Finish queueExecution(" + runMe.toString() + "). CURRENT QUEUE SIZE : "
                    + getExecutorQueueCurrentSize());
            LOG.debug("Finish queueExecution(Runnable).");
        }
    }

    /**
     * The {@link EcCommand} generated from the List of {@link EcCommandSpecification} are immediately executed and the {@link Future}
     * results are cached.
     * @param ecCommandSpecifications A List of {@link EcCommandSpecification} Objects
     */
    public final void queueExecution(final List<EcCommandSpecification> ecCommandSpecifications) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start queueExecution(2).");
        }
        for (EcCommandSpecification ecCommandSpecification : ecCommandSpecifications) {
            queueExecution(ecCommandSpecification);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish queueExecution(2).");
        }
    }

    /**
     * Validates and builds the {@link EcCommand} from the specified {@link EcCommandSpecification}.
     * @param ecCommandSpecification The {@link EcCommandSpecification}
     * @return The {@link EcCommand} built from the {@link EcCommandSpecification}
     */
    private EcCommand<V> validateAndBuild(final EcCommandSpecification ecCommandSpecification) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start validateAndBuild.");
        }
        if (ecCommandSpecification == null) {
            throw new EcCommandNotValidException("EcCommandSpecification cannot be NULL.");
        }

        ecCommandSpecification.validateAll();
        EcCommand<V> command = ecCommandSpecification.buildCommand();
        command.setPriority(cmdPriority);

        if (command == null) {
            throw new EcCommandNotValidException("EcCommand cannot be NULL.");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish validateAndBuild.");
        }

        return command;
    }

    /**
     * Shutdown the executor and clears the cache paths.
     */
    public final void shutdownNow() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start shutdownNow.");
        }
        // Shutdown the executor.
        executor.shutdownNow();

        CommandCacheAccessorService cacheService = ServiceLocator.locateLocalOrRemote(CommandCacheAccessorService.class, locale);
        //clear the cache - command futures.
        cacheService.clearCache(getCommandFuturesCacheKey());
        //clear the cache - commands.
        cacheService.clearCache(getCommandsCacheKey());

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish shutdownNow.");
        }
    }

    
    /**
     * Checks to see if the sessionPageIdsMax has been reached. If it has, then it clears the oldest pageIds cache.
     * @param pageId The new Page Id.
     */
    public void clearFutureHistoryCacheForPageId(final String pageId) {
        clearHistoryCacheForPageId(getCommandFuturesCacheKey(), queuedSessionPageIds, pageId);
    }

    /**
     * Checks to see if the sessionPageIdsMax has been reached. If it has, then it clears the oldest pageIds cache.
     * @param pageId The new Page Id.
     */
    public void clearCommandHistoryCacheForPageId(final String pageId) {
        clearHistoryCacheForPageId(getCommandsCacheKey(), deferedSessionPageIds, pageId);
    }

    /**
     * Checks to see if the sessionPageIdsMax has been reached. If it has, then it clears the oldest pageIds cache.
     * @param pageIdList the pageIdList
     * @param pageId The new Page Id.
     * @param cacheType the cacheType
     */
    private synchronized void clearHistoryCacheForPageId(final String cacheKeyBySessionId, final LinkedList<String> pageIdList, final String pageId) {
        CommandCacheAccessorService cacheAccessorService = ServiceLocator.locateLocalOrRemote(CommandCacheAccessorService.class, locale);
        if (cacheAccessorService != null && pageId != null) {
            if (pageIdList.size() >= pageCreationMaxInt) {
                String pollFirstPageId = pageIdList.pollFirst();
                if (pollFirstPageId != null) {
                    try {
                        cacheAccessorService.clearCache(cacheKeyBySessionId, pollFirstPageId );
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("EcManager Page Id Limit Exceeded for " + cacheKeyBySessionId + ". Removed Page Id[" + pollFirstPageId
                                    + "] with its cache path[" + pollFirstPageId + "] and attempting to add new Page Id[" + pageId
                                    + "] to Page Limit Cache.");
                        }
                    } catch (Exception ex) {
                        LOG.debug("Could not clear cache for page Id[" + pageId + "]" + ex);
                    }
                }
            }
            if (!pageIdList.contains(pageId)) {
                pageIdList.add(pageId);
            }
        } else {
            LOG.warn("EcCommandManger could not clear the cache because of a NULL reference. Cache Service[" + cacheAccessorService
                    + "] PageId[" + pageId + "]");
        }
    }

    /**
     * Attempts to get {@link EcCommand} result from the {@link Future} and returns the Object. If not found, it retrieves the cached
     * command and executes it via the executer and then returns the result. This method throw a EcCommandResultNotFound Exeception if both
     * the Future result and cached EcCommand are not found in cache.
     * @param pageId The Page Id
     * @param commandType The Command Type
     * @param commandRole The Command Role
     * @return the {@link EcCommand} result
     */
    public final EcCommand<V> getEcCommandResult(final String pageId, final String commandType, final String commandRole) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getEcCommandResult.");
        }

        CommandCacheAccessorService cacheService = ServiceLocator.locateLocalOrRemote(CommandCacheAccessorService.class, locale);
        Future<EcCommand> future = cacheService.getFuture(getCommandFuturesCacheKey(), pageId, commandType);

        EcCommand<V> command = null;
        if (future == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Could not find Future from cache so retrieving EcCommand to execute.");
            }

            EcCommand<V> cachedEcCommand = cacheService.getEcCommand(getCommandsCacheKey(), pageId, commandType);

            if (cachedEcCommand != null) {
                if (LOG.isDebugEnabled()) {
                    String commandName = cachedEcCommand.getClass().getName();
                    LOG.debug("Retrieved EcCommand[" + commandName + "] from location[Session Key : " + getCommandsCacheKey() + ", pageid : " + pageId+", commandType : "+ commandType+"] and attempting to execute.");
                }

                future = executor.executeCommand(cachedEcCommand);

                cacheService.putFuture(getCommandFuturesCacheKey(), pageId, commandType, future);

                if (LOG.isDebugEnabled()) {
                    String commandName = cachedEcCommand.getClass().getName();
                    LOG.debug("EcCommand[" + commandName + "] successfully executed and Future stored at location[Session Key : " + getCommandsCacheKey() + ", pageid : " + pageId+", commandType : "+ commandType 
                            + "].");
                }
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Could not find EcCommand at location [Session Key : " + getCommandsCacheKey() + ", pageid : " + pageId+", commandType : "+ commandType + "] in Cache service.");
                }
            }
        }

        // Get the EcCommand from the Future.
        command = getCommand(future, commandType);

        if (command == null) {
            throw new EcCommandResultNotFound("No Result Found for pageId[" + pageId + "] commandType[" + commandType
                    + "] and commandRole[" + commandRole + "]");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getEcCommandResult.");
        }
        return command;
    }

    /**
     * This method calls the getEcCommandResult to obtain the result returned from executing a command, the value will be stored within the
     * Type that is return.
     * @param <T> is Type.
     * @param pageId The Page Id
     * @param commandType Class containing the Command Type
     * @param commandRole The Command Role
     * @return T a Type which extends EcCommand.
     */
    public final <T extends EcCommand> T getEcCommandResult(final String pageId, final Class<T> commandType, final String commandRole) {
        EcCommand command = getEcCommandResult(pageId, commandType.getName(), commandRole);
        return commandType.cast(command);
    }

    /**
     * This method calls the getEcCommandResult to obtain the result returned from executing a command, the value will be stored within the
     * Type that is return.
     * @param <T> is Type.
     * @param specification {@link EcCommandSpecification} contains the specification used within the command
     * @param commandType Class containing the Command Type
     * @return T a Type which extends {@link EcCommand}.
     */
    public final <T extends EcCommand> T getEcCommandResult(final EcCommandSpecification specification, final Class<T> commandType) {
        EcCommand command = getEcCommandResult(specification.getPageId(), specification.getCommandType(), specification.getCommandRole());
        return commandType.cast(command);
    }

    /**
     * This method calls the getEcCommandResult to obtain the result returned from executing a command, the value will be stored within
     * the Type that is return.
     * @param <T> is Type.
     * @param specification {@link EcCommandSpecification} contains the specification used within the command
     * @param resultType Class containing the Result Type
     * @return T a Type which extends {@link Object}.
     */
    public <T extends Object> T getResultFromEcCommand(final EcCommandSpecification specification, final Class<T> resultType) {
        EcCommand<V> command =
                getEcCommandResult(specification.getPageId(), specification.getCommandType(), specification.getCommandRole());
        return resultType.cast(command.getResult());
    }

    /**
     * Get the {@link EcCommand} from the given {@link Future}.
     * @param future The {@link Future} from which the {@link EcCommand} is to be retrieved
     * @param commandType is Command Type.
     * @return the retrieved {@link EcCommand}
     */
    private EcCommand<V> getCommand(final Future<EcCommand> future, final String commandType) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCommand.");
        }
        EcCommand<V> command = null;
        // Must handle NULL.
        if (future != null) {
            long startTime = 0;
            long endTime = 0;
            StringBuilder commandExecutionStatus = new StringBuilder();
            try {
                startTime = System.currentTimeMillis();
                command = future.get(resultTimeout, TimeUnit.MILLISECONDS);
                endTime = System.currentTimeMillis();
                if (LOG.isInfoEnabled()) {
                    commandExecutionStatus.append("Command Result: OK, Command Type: ").append(commandType).append(
                            ", Execution Time (ms): ").append((endTime - startTime)).append(", Locale: ").append(this.locale);
                    LOG.info(commandExecutionStatus.toString());
                }
            } catch (InterruptedException e) {
                endTime = System.currentTimeMillis();
                if (LOG.isInfoEnabled()) {
                    commandExecutionStatus.append("Command Result: FAIL, Command Type: ").append(commandType).append(
                            ", Execution Time (ms): ").append((endTime - startTime)).append(",  Exception Class:").append(e.getClass())
                            .append(", Exception Message: ").append(e.getMessage()).append(", Locale: ").append(this.locale);
                    LOG.info(commandExecutionStatus.toString());
                }
                LOG.error("An InterruptedException occurred:", e);
            } catch (ExecutionException e) {
                endTime = System.currentTimeMillis();
                if (LOG.isInfoEnabled()) {
                    commandExecutionStatus.append("Command Result: FAIL, Command Type: ").append(commandType).append(
                            ", Execution Time (ms): ").append((endTime - startTime)).append(",  Exception Class:").append(e.getClass())
                            .append(", Exception Message: ").append(e.getMessage()).append(", Locale: ").append(this.locale);
                    LOG.info(commandExecutionStatus.toString());
                }
                LOG.error("An ExecutionException occurred:", e);
            } catch (TimeoutException e) {
                endTime = System.currentTimeMillis();
                if (LOG.isInfoEnabled()) {
                    commandExecutionStatus.append("Command Result: FAIL, Command Type: ").append(commandType).append(
                            ", Execution Time (ms): ").append((endTime - startTime)).append(",  Exception Class:").append(e.getClass())
                            .append(", Exception Message: ").append(e.getMessage()).append(", Locale: ").append(this.locale);
                    LOG.info(commandExecutionStatus.toString());
                }
                LOG.error("A TimeoutException occurred:", e);
            }
        } else {
            LOG.error("Future is NULL for command type : " + commandType);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCommand.");
        }
        return command;
    }

    /**
     * Method to get Executor Queue Current Size.
     * @return priorityThreadPoolExecutor.
     */
    private int getExecutorQueueCurrentSize() {

        return (((EcPriorityThreadPoolExecutor) (executor.executor)).getQueue().size());

    }
}
