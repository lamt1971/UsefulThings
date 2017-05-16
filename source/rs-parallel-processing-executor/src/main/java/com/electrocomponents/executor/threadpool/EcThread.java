package com.electrocomponents.executor.threadpool;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Michael Reilly
 * Created : 19 Jun 2013 at 10:22:53
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
 * Represents the Ec Thread.
 * @author Michael Reilly
 */
public class EcThread extends java.lang.Thread implements java.lang.Comparable<EcThread> {

    /** Commons logger. */
    private static final Log LOG = LogFactory.getLog(EcThread.class);

    /**
     * Determine if the threads is alive or terminated.
     */
    private boolean terminate;

    /**
     * Holds the first runnable command that was assigned to the thread
     * when assigned.
     */
    private Runnable firstRunnableCommand;

    /**
     * The Task  Thread.
     */
    private final ConcurrentLinkedQueue<Runnable> taskThread = new ConcurrentLinkedQueue<Runnable>();

    /**
     * The instance of the the Lock Manager which is used to and signal the Thread.
     */
    private LockManager lockManager;

    /**
     * Takes the the thread name which is set in the Super Class and
     * assigns an instance of the Lock Manager.
     * @param threadName Name of the Thread.
     * @param lockManager The Lock Manager.
     */
    public EcThread(final String threadName, final LockManager lockManager) {
        super(threadName);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start EcThread");
        }
        this.lockManager = lockManager;

        setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            public void uncaughtException(final Thread t, final Throwable e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error("EcThread : uncaughtException in thread: " + t.getName() + " - Message : " + e.getMessage());
                }
                e.printStackTrace();
                EcThreadPoolFactory.getInstance().getThreadPool().releaseThread(t);

            }

        });
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish EcThread");
        }
    }

    /**
     * The run method that will loop until terminate flag is set to TRUE.
     */
    @Override
    public void run() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start run");
        }

        while (!terminate) {
            try {
                lockManager.awaitifNeeded();
            } catch (InterruptedException ie) {
                LOG.error("Exception has occured while releasing Thread :  " + ie.getMessage(), ie);
                EcThreadPoolFactory.getInstance().getThreadPool().releaseThread(this);
            }

            Runnable runnable = taskThread.poll();
            if (runnable != null) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Running First Command Task.");
                }
                runnable.run();
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish run");
        }
    }

    /**
     * @return the terminate
     */
    public boolean isTerminate() {
        return terminate;
    }

    /**
     * @param terminate Set to true to terminate.
     */
    public void setTerminate(final boolean terminate) {
        this.terminate = terminate;
    }

    /**
     * @return the firstRunnableCommand
     */
    public Runnable getFirstRunnableCommand() {
        return firstRunnableCommand;
    }

    /**
     * @param firstRunnableCommand the firstRunnableCommand to set
     */
    public void setFirstRunnableCommand(final Runnable firstRunnableCommand) {
        // this.firstRunnableCommand = firstRunnableCommand;
        if (firstRunnableCommand != null) {
            taskThread.add(firstRunnableCommand);
        }
    }

    /**
     * @return the lockManager
     */
    public LockManager getLockManager() {
        return lockManager;
    }

    /**
     * @param lockManager the lockManager to set
     */
    public void setLockManager(final LockManager lockManager) {
        this.lockManager = lockManager;
    }

    /**
    * @return the taskThread
    */
    public ConcurrentLinkedQueue<Runnable> getTaskThread() {
        return taskThread;
    }

    @Override
    public int compareTo(final EcThread o) {
        int returnValue = 0;

        if (this == o) {
            returnValue = 1;
        }
        return returnValue;
    }

}
