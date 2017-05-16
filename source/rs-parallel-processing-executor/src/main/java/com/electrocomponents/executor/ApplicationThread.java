package com.electrocomponents.executor;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Zende
 * Created : 29 Feb 2012 at 15:31:00
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
 * Thread for use in application, taken from Java Concurrency in Practice (listing 8.7).
 * <p/>
 * Adds an uncaught exception handler which logs uncaught Throwable.
 * @author Andy Redhead/s.zende
 */
public class ApplicationThread extends Thread {

    /** Default thread name. */
    public static final String DEFAULT_NAME = "ApplicationThread";

    /** Debug lifecycle. */
    private static volatile boolean debugLifecycle = false;

    /** Count indicates number of threads created. */
    private static final AtomicInteger CREATED_THREADS = new AtomicInteger();

    /** Count indicates number of alive thread. */
    private static final AtomicInteger ALIVE_THREADS = new AtomicInteger();

    /** Commons logger. */
    private static final Log LOG = LogFactory.getLog(ApplicationThread.class);

    /**
     * Default constructor.
     * @param runnable runnable
     */
    public ApplicationThread(final Runnable runnable) {
        this(runnable, DEFAULT_NAME);
    }

    /**
     * Constructor to allocates a new Thread object with the passed name.
     * @param runnable runnable
     * @param name thread name
     */
    public ApplicationThread(final Runnable runnable, final String name) {
        super(runnable, name + "-" + CREATED_THREADS.incrementAndGet());
        setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(final Thread t, final Throwable e) {
                LOG.error("uncaughtException in thread: " + t.getName(), e);
            }
        });
        // M. Reilly 1/7/2013 DTRAC:GEN-6981 Added the following line :-
        LOG.info("A thread is created by Executor:" + name);
    }

    /**
     * Run.
     */
    public void run() {

        boolean debug = LOG.isDebugEnabled();
        if (debug) {
            LOG.debug("Created Thread : " + getName());
        }
        try {
            ALIVE_THREADS.incrementAndGet();
            super.run();
        } finally {
            ALIVE_THREADS.decrementAndGet();
        }
    }

    /** Return created threads count.
     * @return threads created
     */
    public static int getThreadsCreated() {
        return CREATED_THREADS.get();
    }

    /** Return threads alive count.
     * @return threads alive
     */
    public static int getThreadsAlive() {
        return ALIVE_THREADS.get();
    }

    /**
     * Return debug level flag value.
     * @return debug flag
     */
    public static boolean getDebug() {
        return debugLifecycle;
    }

    /** Set debugFlag level flag.
     * @param debugFlag debug flag state
     */
    public static void setDebug(final boolean debugFlag) {
        debugLifecycle = debugFlag;
    }

}
