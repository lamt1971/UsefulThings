package com.electrocomponents.executor.threadpool;


/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Michael Reilly
 * Created : 20 Jun 2013 at 14:32:26
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
 * The EC Thread Pool Factory.
 * @author Michael Reilly
 */
public final class EcThreadPoolFactory {

    /** The Thread Pool Factory. **/
    private static final EcThreadPoolFactory EC_THREAD_POOL_FACTORY = new EcThreadPoolFactory();

    /** Empty Constructor.*/
    private EcThreadPoolFactory() {

    }

    /**
     * Get an instance of the Thread Pool.
     * @return An instance of the Thread Pool.
     */
    public static EcThreadPoolFactory getInstance() {
        return EC_THREAD_POOL_FACTORY;
    }

    /**
     * Get the Thread Pool.
     * @return The Thread Pool.
     */
    public EcThreadPool getThreadPool() {
        return EcCustomThreadPoolImpl.getInstance();
    }
}
