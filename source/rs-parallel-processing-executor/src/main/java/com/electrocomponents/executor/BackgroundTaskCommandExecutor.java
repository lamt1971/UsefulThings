package com.electrocomponents.executor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.executor.rejectionhandlers.EcRejectedExecutionHandlerImpl;
import com.electrocomponents.executor.threadpool.EcThreadFactory;
import com.electrocomponents.executor.threadpool.EcThreadFactoryImpl;
import com.electrocomponents.executor.threadpool.EcThreadPoolExecutor;

/**
 *
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Bhavesh Kavia
 * Created : 18 Jan 2011 at 14:53:22
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
 * This class accepts {@link EcCommand} objects and Runnable Objects and submits it to the {@link PriorityThreadPoolExecutor}.
 * An Instance of {@link BoundedBlockingPriorityQueue} is created an passed to the encapsulated {@link ThreadPoolExecutor}.
 * @author Bhavesh Kavia
 * @param <V>
 */
public class BackgroundTaskCommandExecutor<V> {

    /** The Commons Logger. */
    private static final Log LOG = LogFactory.getLog(BackgroundTaskCommandExecutor.class);

    /** The ExecutorService instance. */
    public ExecutorService executor;

    /**
     * The Constructor.
     * @param corePoolSize The {@link ThreadPoolExecutor} core pool size
     * @param maxPoolSize The {@link ThreadPoolExecutor} maximum pool size
     * @param keepAliveTime The {@link ThreadPoolExecutor} keep alive time
     * @param noOfThreads The {@link BlockingQueue} number of threads
     * @param timeOut The {@link BlockingQueue} timeout
     * @param sessionIdent The Background Task Identifier
     */
    public BackgroundTaskCommandExecutor(final int corePoolSize, final int maxPoolSize, final long keepAliveTime, final int noOfThreads,
            final long timeOut, final String sessionIdent) {

        BoundedBlockingPriorityQueue<Runnable> workQueue =
                new BoundedBlockingPriorityQueue<Runnable>(noOfThreads, timeOut, TimeUnit.MILLISECONDS);

        ApplicationThreadPoolFactory applicationThreadPoolFactory = new ApplicationThreadPoolFactory(sessionIdent);

        executor =
                new PriorityThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue,
                        applicationThreadPoolFactory);

    }

    /**
     * The Constructor.
     * @param corePoolSize The {@link ThreadPoolExecutor} core pool size
     * @param maxPoolSize The {@link ThreadPoolExecutor} maximum pool size
     * @param keepAliveTime The {@link ThreadPoolExecutor} keep alive time
     * @param noOfThreads The {@link BlockingQueue} number of threads
     * @param timeOut The {@link BlockingQueue} timeout
     * @param sessionIdent The Background Task Identifier
     * @param applicationThreadPoolEnabled applicationThreadPoolEnabled
     * @param allowCoreThreadTimeout allowCoreThreadTimeout
     */
    public BackgroundTaskCommandExecutor(final int corePoolSize, final int maxPoolSize, final long keepAliveTime, final int noOfThreads,
            final long timeOut, final String sessionIdent, final boolean applicationThreadPoolEnabled, final boolean allowCoreThreadTimeout) {

        BoundedBlockingPriorityQueue<Runnable> workQueue =
                new BoundedBlockingPriorityQueue<Runnable>(noOfThreads, timeOut, TimeUnit.MILLISECONDS);

        if (applicationThreadPoolEnabled) {

            EcThreadFactory threadFactory = EcThreadFactoryImpl.getInstance();
            EcThreadPoolExecutor ecThreadPoolExecutor =
                    new EcPriorityThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue,
                            threadFactory, new EcRejectedExecutionHandlerImpl(), sessionIdent);
            ecThreadPoolExecutor.allowCoreThreadTimeOut(allowCoreThreadTimeout);
            executor = ecThreadPoolExecutor;

        } else {

            ApplicationThreadPoolFactory applicationThreadPoolFactory = new ApplicationThreadPoolFactory(sessionIdent);

            PriorityThreadPoolExecutor prioritythreadPoolExecutor =
                    new PriorityThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue,
                            applicationThreadPoolFactory);
            prioritythreadPoolExecutor.allowCoreThreadTimeOut(allowCoreThreadTimeout);
            executor = prioritythreadPoolExecutor;
        }

    }

    /**
     * Execute the specified {@link EcCommand} object using the executor. The {@link ThreadPoolExecutor}s <tt>submit</tt> method is invoked.
     * @param command The {@link EcCommand} Object
     * @return The {@link Future} of the executed {@link EcCommand}
     */
    public Future<EcCommand> executeCommand(final EcCommand command) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start executeCommand.");
        }

        Future<EcCommand> future = executor.submit(command);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish executeCommand.");
        }

        return future;
    }

    /**
     * Execute the specified Runnable object using the executor. The {@link ThreadPoolExecutor}s <tt>submit</tt> method is invoked.
     * @param runMe The Runnable Object
     * @return The {@link Future} of the executed Runnable
     */
    public Future executeRunnable(final Runnable runMe) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start executeCommand.");
        }

        Future future = executor.submit(runMe);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish executeCommand.");
        }

        return future;
    }

    /**
     * Execute the specified List of {@link EcCommand} objects using the executor. The {@link ThreadPoolExecutor}s <tt>invokeAll</tt> method
     * is invoked.
     * @param commands A List of {@link EcCommand} Object to execute
     * @return A Link to {@link Future} Objects for the {@link EcCommand} Objects
     * @throws InterruptedException if interrupted while waiting, in which case unfinished tasks are cancelled.
     */
    public List<Future<EcCommand<V>>> executeCommands(final List<EcCommand<V>> commands) throws InterruptedException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start executeCommand.");
        }

        List<Future<EcCommand<V>>> futures = executor.invokeAll(commands);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish executeCommand.");
        }

        return futures;
    }

    /**
     * Shuts down the {@link ExecutorService}. This method will Log(WARN) all tasks that were not executed after calling shutdownNow. The
     * {@link SecurityException} thrown by the method will also be logged.
     */
    public void shutdownNow() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start shutdownNow.");
        }

        try {
            List<Runnable> tasks = executor.shutdownNow();
            for (Runnable runnable : tasks) {
                LOG.warn("Runnable task[" + runnable.getClass() + "] did not commence execution:" + runnable.toString());
            }
        } catch (SecurityException se) {
            LOG.error("A SecurityException occurred:", se);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish shutdownNow.");
        }
    }
}
