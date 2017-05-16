package com.electrocomponents.executor.exceptions;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Raja Govindharaj
 * Created : 24 Jan 2011 at 09:43:31
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
 * A handler for tasks that cannot be executed by a ThreadPoolExecutor.
 * @author Raja Govindharaj
 */
public class EcCommandRejectionExceptionHandler implements RejectedExecutionHandler {

    /** The Commons Logger. */
    private static final Log LOG = LogFactory.getLog(EcCommandRejectionExceptionHandler.class);

    /**
     * Method that may be invoked by a ThreadPoolExecutor when execute method of ThreadPoolExecutor cannot accept a task. This may occur
     * when no more threads or queue slots are available because their bounds would be exceeded, or upon shutdown of the Executor.
     * @see java.util.concurrent.RejectedExecutionHandler#rejectedExecution(java.lang.Runnable, java.util.concurrent.ThreadPoolExecutor)
     * @param runnable is Runnable Instance.
     * @param executor should be instance of AsyncCommandExecutor.
     */
    public void rejectedExecution(final Runnable runnable, final ThreadPoolExecutor executor) {
        if (LOG.isWarnEnabled()) {
            String logInfo = generateLogInfo(runnable, executor);
            LOG.warn(logInfo);
        }
    }

    /**
     * Generate string representation of current state of ThreadPoolExecutor.
     * @param runnable is Runnable Instance.
     * @param executor should be instance of AsyncCommandExecutor.
     * @return String the string representation of executor
     */
    private String generateLogInfo(final Runnable runnable, final ThreadPoolExecutor executor) {
        StringBuilder builder = new StringBuilder();

        builder.append("EcCommand Job has been rejected due to queue may not have space to enqueue. The rejected command is : ");
        builder.append(runnable);

        BlockingQueue<Runnable> blockingQueue = executor.getQueue();

        builder.append("\nQueue Capacity :");
        builder.append(blockingQueue.size());

        builder.append("\nQueue Remaining Capacity :");
        builder.append(blockingQueue.remainingCapacity());

        builder.append("\nActive Count:");
        builder.append(executor.getActiveCount());

        builder.append("\nCompleted Task Count:");
        builder.append(executor.getCompletedTaskCount());

        builder.append("\nCore Pool Size:");
        builder.append(executor.getCorePoolSize());

        builder.append("\nKeep Alive Time:");
        builder.append(executor.getKeepAliveTime(TimeUnit.MILLISECONDS));

        builder.append("\nLargest Pool Size:");
        builder.append(executor.getLargestPoolSize());

        builder.append("\nMaximum Pool Size:");
        builder.append(executor.getMaximumPoolSize());

        builder.append("\nPool Size:");
        builder.append(executor.getPoolSize());

        builder.append("\nTask Count:");
        builder.append(executor.getTaskCount());

        return builder.toString();
    }
}
