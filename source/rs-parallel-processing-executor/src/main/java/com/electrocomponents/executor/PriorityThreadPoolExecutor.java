package com.electrocomponents.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Nick Burnham
 * Created : 17 Apr 2012 at 14:54:58
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
 *
 * Sub-class of ThreadPoolExecutor which solves the problem of comparing queued FutureTask instances based on the wrapped callable.
 * Based on code from: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6539720
 *
 * The newTaskFor[Runnable | Callable] methods have been implemented to provide a sub-class of FutureTask that implements
 * PriorityAndSubmissionOrder<V>
 *
 * This sub-class allows the future tasks to be compared. It also has responsibility for setting submission order (the submission
 * order needs to be set by the executor because this is the component which handles task submission (you cannot handle submission
 * order on the queue because tasks will go straight to executor threads if they are idle, completely bypassing the queue).
 *
 * If a sumbmitted task can't be queued a RejectedExecutionException (runtime exception) will be thrown.
 *
 * @author Nick Burnham
 */
public class PriorityThreadPoolExecutor extends ThreadPoolExecutor {

    /** The submissionOrderCounter an AtomicLong.*/
    private final AtomicLong submissionOrderCounter = new AtomicLong(0);

    /**
     * @param coreThreadPoolSize The coreThreadPoolSize
     * @param maximumPoolSize The MaximumPoolSize
     * @param keepAliveTime The KeepAliveTimes
     * @param unit The Unit
     * @param workQueue The WorkQueue
     * @param applicationThreadPoolFactory The ApplicationThreadPoolFactory
     */
    public PriorityThreadPoolExecutor(final int coreThreadPoolSize, final int maximumPoolSize, final long keepAliveTime,
            final TimeUnit unit, final BoundedBlockingPriorityQueue workQueue,
            final ApplicationThreadPoolFactory applicationThreadPoolFactory) {
        super(coreThreadPoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, applicationThreadPoolFactory);
    }

    /**
     * Overrides method to return a RunnableFuture for the given callable task.
     * @param runnable The Runnable
     * @param <T> The Type
     * @param value The Value
     * @return <T> The Type
     */
    @Override
    protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T value) {
        return new ComparableFutureTask<T>(runnable, value);
    }

    /**
     * Overrides method to return a RunnableFuture for the given callable task.
     * @param callable The callable
     * @param <T> The Type
     * @return <T> The Type
     */
    @Override
    protected <T> RunnableFuture<T> newTaskFor(final Callable<T> callable) {
        return new ComparableFutureTask<T>(callable);
    }

    /**
     * <pre>
     * ************************************************************************************************
     * Copyright (c) Electrocomponents Plc.
     *
     * Author  : Nick Burnham
     * Created : 17 Apr 2012 at 15:04:47
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
     * Inner Class to Maintain Queue Order.
     * @author Nick Burnham
     */

    private final class ComparableFutureTask<V> extends FutureTask<V> implements PriorityAndSubmissionOrder<V> {

        /**
         * comparable The Comparable.
         */
        Comparable comparable;

        /**
        * Instantiate PriorityAndSubmissionOrder.
        */
        PriorityAndSubmissionOrder priorityAndSubmissionOrder;

        /**
         * @param callable The callable
         */
        ComparableFutureTask(final Callable callable) {
            super(callable);
            comparable = (Comparable) callable;
            priorityAndSubmissionOrder = (PriorityAndSubmissionOrder) callable;
            synchronized (submissionOrderCounter) {
                long taskSubmissionOrder = submissionOrderCounter.incrementAndGet();
                priorityAndSubmissionOrder.setSubmissionOrder(taskSubmissionOrder);
            }

        }

        /**
         * @param runnable The Runnable
         * @param result The Result
         */
        ComparableFutureTask(final Runnable runnable, final V result) {
            super(runnable, result);
            comparable = (Comparable) runnable;
            priorityAndSubmissionOrder = (PriorityAndSubmissionOrder) runnable;
            synchronized (submissionOrderCounter) {
                long taskSubmissionOrder = submissionOrderCounter.incrementAndGet();
                priorityAndSubmissionOrder.setSubmissionOrder(taskSubmissionOrder);
            }
        }

        /**
         * Method to compare tasks on their priority and submission order.
         * @param ftask The ComparableFutureTask
         * @return int
         */
        @SuppressWarnings("unchecked")
        public int compareTo(final ComparableFutureTask<V> ftask) {
            return priorityAndSubmissionOrder.compareTo(ftask.comparable);

        }

        /**
         * Method to compare tasks on their priority and submission order.
         * @param prioritySubmissionOrder The PriorityAndSubmissionOrder
         * @return int
         */
        public int compareTo(final PriorityAndSubmissionOrder<V> prioritySubmissionOrder) {
            return priorityAndSubmissionOrder.compareTo(prioritySubmissionOrder);
        }

        @Override
        public long getSubmissionOrder() {
            return priorityAndSubmissionOrder.getSubmissionOrder();
        }

        @Override
        public void setSubmissionOrder(final long submissionNumber) {
            priorityAndSubmissionOrder.setSubmissionOrder(submissionNumber);
        }

        @Override
        public int getPriority() {
            return priorityAndSubmissionOrder.getPriority();
        }

        @Override
        public void setPriority(final int priority) {
            priorityAndSubmissionOrder.setPriority(priority);
        }
    }
}
