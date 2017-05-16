package com.electrocomponents.executor.threadpool;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Michael Reilly
 * Created : 19 Jun 2013 at 11:04:55
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
 * Used to hold details of the Thread Usage.
 * @author Michael Reilly
 */
public class EcThreadUsage {

    /**
     * The time taken by the threads between the Active and Free Pool.
     * This can be used to then determine how long the thread has been used by the Executor.
     */
    private long maxUsageTimeInMillis;

    /**
     * The Session Id when the thread usage in milliseconds exceeds the currently
     * active configured usage time.
     * Used to monitor session activity.
     */
    private String sessionId;

    /**
     * The name of the borrower Thread.
     */
    private String borrowerThreadName;

    /**
     * The name of the Thread.
     */
    private String threadName;

    /**
     * The current time in milliseconds that the thread is allocated to the Executor.
     */
    private long allocatedTimeInMillis;

    /**
     * The name of the borrower Thread after exceeding the max thread usage time.
     */
    private String threadBorrowedByBeyondMaxUsage;

    /**
     * The Thread allocated Date.
     */
    private long allocatedDate;

    /**
     * The Thread released Date.
     */
    private long releasedDate;

    /**
     * Session Id after exceeding the max thread usage time.
     */
    private String sessionIdBeyongMaxUsage;

    /**
     * @return the maxUsageTimeInMillis
     */
    public final long getMaxUsageTimeInMillis() {
        return maxUsageTimeInMillis;
    }

    /**
     * @param maxUsageTimeInMillis the maxUsageTimeInMillis to set
     */
    public final void setMaxUsageTimeInMillis(final long maxUsageTimeInMillis) {
        this.maxUsageTimeInMillis = maxUsageTimeInMillis;
    }

    /**
     * @return the sessionId
     */
    public final String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public final void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return the threadName
     */
    public final String getThreadName() {
        return threadName;
    }

    /**
     * @param threadName the threadName to set
     */
    public final void setThreadName(final String threadName) {
        this.threadName = threadName;
    }

    /**
     * @return the allocatedTimeInMillis
     */
    public final long getAllocatedTimeInMillis() {
        return allocatedTimeInMillis;
    }

    /**
     * @param allocatedTimeInMillis the allocatedTimeInMillis to set
     */
    public final void setAllocatedTimeInMillis(final long allocatedTimeInMillis) {
        this.allocatedTimeInMillis = allocatedTimeInMillis;
    }

    /**
     * @return the borrowerThreadName
     */
    public String getBorrowerThreadName() {
        return borrowerThreadName;
    }

    /**
     * @param borrowerThreadName the borrowerThreadName to set
     */
    public void setBorrowerThreadName(final String borrowerThreadName) {
        this.borrowerThreadName = borrowerThreadName;
    }

    /**
     * @return the threadBorrowedByBeyondMaxUsage
     */
    public String getThreadBorrowedByBeyondMaxUsage() {
        return threadBorrowedByBeyondMaxUsage;
    }

    /**
     * @param threadBorrowedByBeyondMaxUsage the threadBorrowedByBeyondMaxUsage to set
     */
    public void setThreadBorrowedByBeyondMaxUsage(final String threadBorrowedByBeyondMaxUsage) {
        this.threadBorrowedByBeyondMaxUsage = threadBorrowedByBeyondMaxUsage;
    }

    /**
     * @return the allocatedDate
     */
    public long getAllocatedDate() {
        return allocatedDate;
    }

    /**
     * @param allocatedDate the allocatedDate to set
     */
    public void setAllocatedDate(final long allocatedDate) {
        this.allocatedDate = allocatedDate;
    }

    /**
     * @return the releasedDate
     */
    public long getReleasedDate() {
        return releasedDate;
    }

    /**
     * @param releasedDate the releasedDate to set
     */
    public void setReleasedDate(final long releasedDate) {
        this.releasedDate = releasedDate;
    }

    /**
     * @return the sessionIdBeyongMaxUsage
     */
    public String getSessionIdBeyongMaxUsage() {
        return sessionIdBeyongMaxUsage;
    }

    /**
     * @param sessionIdBeyongMaxUsage the sessionIdBeyongMaxUsage to set
     */
    public void setSessionIdBeyongMaxUsage(final String sessionIdBeyongMaxUsage) {
        this.sessionIdBeyongMaxUsage = sessionIdBeyongMaxUsage;
    }

}
