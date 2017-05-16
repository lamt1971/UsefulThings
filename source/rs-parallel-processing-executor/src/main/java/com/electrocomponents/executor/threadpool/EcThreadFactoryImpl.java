package com.electrocomponents.executor.threadpool;

import org.jboss.logging.MDC;

/**
 *
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sharma Jampani
 * Created : 30 Jul 2013 at 15:51:29
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
 * This is the Implementation class for EcThreadFactory.
 * @author c0950667
 */
public final class EcThreadFactoryImpl implements EcThreadFactory {

    /** The Thread Pool Factory. **/
    private static final EcThreadFactoryImpl EC_THREAD_FACTORY_IMPL = new EcThreadFactoryImpl();

    /** Empty Constructor.*/
    private EcThreadFactoryImpl() {

    }

    /**
     * Get an instance of the Thread Pool.
     * @return An instance of the Thread Pool.
     */
    public static EcThreadFactoryImpl getInstance() {
        return EC_THREAD_FACTORY_IMPL;
    }

    @Override
    public Thread newThread(final Runnable runnable) {
        String sessionId = (String) MDC.get("JSESSIONID");
        return EcCustomThreadPoolImpl.getInstance().getThread(runnable, sessionId);
    }

    @Override
    public void releaseThread(final Runnable runnable) {
        EcCustomThreadPoolImpl.getInstance().releaseThread(runnable);
    }

}
