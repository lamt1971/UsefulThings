package com.electrocomponents.executor.threadpool;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Michael Reilly
 * Created : 19 Jun 2013 at 10:26:03
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
 * The lock manager is a class that is assigned to each ec thread class instances.
 * The class provides the options to put the thread wait state and also signal to awake.
 * @author Michael Reilly
 */
public class LockManager {

    /** Commons logger. */
    private static final Log LOG = LogFactory.getLog(LockManager.class);

    /**
     * Lock.
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * The Condition instance for the wait and signal.
     */
    private final Condition condition = lock.newCondition();

    /**
     * Indicates the status of the await.
     */
    private volatile boolean awaitStatus = false;

    /**
     * The method propose the associated thread into wait state.
     * It sets TRUE to awaitStatus instance variable.
     * This operation should be performed after java.util.concurrent.locks.ReentrantLock
     * has been taken and the lock should released after the performed the operation.
     */
    public void await() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start await");
        }
        lock.lock();

        try {
            this.awaitStatus = true;
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("await : Error has occured " + e);
            }
        } finally {
            lock.unlock();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish await");
        }
    }

    /**
     * The method puts the associated thread into wait state by checking awaitStatus instance variable.
     * If it is TRUE, it will calls java.util.concurrent.locks.Condition.await().
     * This operation should be performed after java.util.concurrent.locks.ReentrantLock has been taken
     * and the lock should released after the performed the operation.
     * @throws InterruptedException Interrupted Exception thrown.
     */
    public void awaitifNeeded() throws InterruptedException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start awaitIfNeeded");
        }
        lock.lock();

        try {
            if (awaitStatus) {
                condition.await();
            }
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("await : Error has occured : " + e);
            }
        } finally {
            lock.unlock();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish awaitIfNeeded");
        }
    }

    /**
     * The method awakes the associated thread from wait state.
     * This operation should be performed after java.util.concurrent.locks.ReentrantLock
     * has been taken and the lock should released after the performed the operation.
     */
    public void signal() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start signal");
        }
        lock.lock();

        try {
            if (awaitStatus) {
                this.awaitStatus = false;
                condition.signal();
            }
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("signal : Exception has occured : " + e);
            }
        } finally {
            lock.unlock();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish signal");
        }
    }

    /**
     * @return the awaitStatus
     */
    public boolean isAwaitStatus() {
        return awaitStatus;
    }

}
