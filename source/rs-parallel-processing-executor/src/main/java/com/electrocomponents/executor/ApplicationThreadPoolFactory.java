package com.electrocomponents.executor;

import java.util.concurrent.ThreadFactory;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Zende
 * Created : 1 Mar 2012 at 13:39:38
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
 * Class used to create thread with unique name (${SESSIONID}-Event) .
 * @author s.zende
 */
public class ApplicationThreadPoolFactory implements ThreadFactory {

    /**
     * Pool name.
     */
    private final String poolName;

    /**
     * Constructor.
     * @param poolName name for the pool
     */
    public ApplicationThreadPoolFactory(final String poolName) {
        this.poolName = poolName;
    }

    /**
     * Create a new thread using passed name.
     * @param runnable a runnable
     * @return a new thread created with the pool name
     */
    public Thread newThread(final Runnable runnable) {
        return new ApplicationThread(runnable, poolName);
    }

}
