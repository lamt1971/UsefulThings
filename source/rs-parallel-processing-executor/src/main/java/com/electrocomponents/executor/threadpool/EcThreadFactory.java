package com.electrocomponents.executor.threadpool;

import java.util.concurrent.ThreadFactory;

/**
 *
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sharma Jampani
 * Created : 30 Jul 2013 at 15:45:21
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
 * Interface for EcThreadFactory.
 * @author c0950667
 */
public interface EcThreadFactory extends ThreadFactory {

    /**
     * Method to release the Thread.
     * @param runnable the Runnable
     */
    void releaseThread(Runnable runnable);

}
