package com.electrocomponents.executor.management;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Michael Reilly
 * Created : 20 Jun 2013 at 10:02:05
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
 * The interface for the Thread Pool Monitor MBean.
 * @author Michael Reilly
 */
public interface ThreadPoolMonitorMBean {

    /**
     * Get the Active Thread Count.
     * @return The Active Thread Count.
     */
    int getActiveThreadCount();

    /**
     * Get the number of dead Threads.
     * @return The number of dead Threads.
     */
    int getDeadThreadCount();

    /**
     * Get the Thread Usage Information from the Thread Pool
     * and return the information to display on the JMX Console.
     * @return The Thread Usage information.
     */
    String printDeadLockInfo();

    /**
     * Provides Dead Lock information about the EC_APP_TH_XXX_XXX thread
     * and may also return other threads. The returned string should be
     * displayed in the JMX Console.
     * @return Dead Lock Information about the EC_APP_TH_XXX_XXX thread.
     */
    String printThreadUsage();

    /**
     * Get the number of free threads.
     * @return The number of free threads.
     */
    int getFreeThreadCount();

}
