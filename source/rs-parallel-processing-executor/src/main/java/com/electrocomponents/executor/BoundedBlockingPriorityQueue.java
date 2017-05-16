package com.electrocomponents.executor;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import akka.util.BoundedBlockingQueue;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Nick Burnham
 * Created : 17 Apr 2012 at 14:45:22
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
 * Offer adds to end of priority queue.
 *
 * If the queue is full, both offer methods will block for a timeout.
 *
 * Once a task is in the queue it will remain in the queue (until it is executed). Attempting to add a high priority task
 * to a full queue will block, rather than removing (and discarding) an already queued low priority task.
 *
 * It is possible that low priority tasks will suffer "starvation" if higher priority tasks are added at a sufficiently
 * rapid rate to fully occupy the associated executor thread pool.
 * @author Nick Burnham
 * @param <E>
 */
public class BoundedBlockingPriorityQueue<E> extends BoundedBlockingQueue<E> {

    /** The Commons Logger. */
    private static final Log LOG = LogFactory.getLog(BoundedBlockingPriorityQueue.class);

    /** The blocking time. */
    private final long blockingTime;

    /** The Time Unit. */
    private final TimeUnit timeUnit;

    /**
    * Constructor for the Bounded Blocking Queue.
    * @param capacity The Queue Capacity
    * @param blockingTime The Blocking Time
    * @param timeUnit The Time Unit
    */
    public BoundedBlockingPriorityQueue(final int capacity, final long blockingTime, final TimeUnit timeUnit) {
        super(capacity, new PriorityBlockingQueue<E>());
        this.blockingTime = blockingTime;
        this.timeUnit = timeUnit;
    }

    /**
     * Method to insert a specified element at the tail of this queue, waiting if necessary up to the specified wait time for space to become
     * available.
     * @param e The type of element to be inserted.
     * @return <tt>true</tt> if successful, or <tt>false</tt> if the specified waiting time elapses before space is available.
     */
    @Override
    public boolean offer(final E e) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start offer.");
        }

        boolean isSuccessful = offer(e, blockingTime, timeUnit);

        if (LOG.isDebugEnabled()) {
            LOG.debug("End offer.");
        }
        return isSuccessful;
    }

}
