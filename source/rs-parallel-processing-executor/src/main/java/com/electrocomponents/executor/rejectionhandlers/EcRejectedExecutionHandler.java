package com.electrocomponents.executor.rejectionhandlers;

import com.electrocomponents.executor.threadpool.EcThreadPoolExecutor;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Michael Reilly
 * Created : 24 Jun 2013 at 15:43:49
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
 * Interface for Rejection exteciotn Handlers.
 * @author Michael Reilly
 */
public interface EcRejectedExecutionHandler {

    /**
     * The method to call when the runnable command has been rejected by the Executor.
     * @param runnable Runnable.
     * @param ecThreadPoolExecutor The Thread Pool Executor Instance.
     */
    void rejectedExecution(Runnable runnable, EcThreadPoolExecutor ecThreadPoolExecutor);

}
