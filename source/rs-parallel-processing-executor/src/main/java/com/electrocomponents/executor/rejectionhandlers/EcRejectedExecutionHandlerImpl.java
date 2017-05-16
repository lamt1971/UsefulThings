package com.electrocomponents.executor.rejectionhandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.executor.threadpool.EcThreadPoolExecutor;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Michael Reilly
 * Created : 24 Jun 2013 at 15:46:12
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
 * Logs when a runnable task has been rejected.
 * @author Michael Reilly
 */
public class EcRejectedExecutionHandlerImpl implements EcRejectedExecutionHandler {

    /** Commons logger. */
    private static final Log LOG = LogFactory.getLog(EcRejectedExecutionHandlerImpl.class);

    /**
     * The method to call when the runnable command has been rejected by the Executor.
     * @param runnable Runnable.
     * @param ecThreadPoolExecutor The Thread Pool Executor Instance.
     */
    @Override
    public void rejectedExecution(final Runnable runnable, final EcThreadPoolExecutor ecThreadPoolExecutor) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("rejectedExecution : Start");
        }

        LOG.error("Runnable Task has been rejected " + runnable.toString() + " . Session : " + ecThreadPoolExecutor.getSessionId());

        if (LOG.isDebugEnabled()) {
            LOG.debug("rejectedExecution : Finish");
        }

    }

}
