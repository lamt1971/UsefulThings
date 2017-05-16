package com.electrocomponents.executor.management;


/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Michael Reilly
 * Created : 20 Jun 2013 at 10:04:59
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
 * Interface for the Thread Configuration MBean.
 * @author Michael Reilly
 */
public interface ThreadConfigurationMBean {

    /**
     * Get the connection timeout for the current main thread
     * which needs an async thread from the pool which does not have
     * one. The pool puts the current main thread into wait with the connection timeout.
     * The value of the connection time can be obtained from the Java D parameter.
     * The connection timeout can be overriden buy the Jmx Console.
     * @return The Connection Timeout.
     */
    int getConnectionTimeout();

    /**
     * Get the maximum thread from the the Java D Parameter.
     * The Maximum thread cannot be overwritten by the JMX Console.
     * @return The maximum Thread that can be obtained.
     */
    int getMaxThread();

}
