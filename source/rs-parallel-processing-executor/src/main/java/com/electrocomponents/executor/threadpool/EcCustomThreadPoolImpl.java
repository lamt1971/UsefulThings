package com.electrocomponents.executor.threadpool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.electrocomponents.commons.components.general.util.JvmRouteUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.executor.management.ThreadConfiguration;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Michael Reilly
 * Created : 19 Jun 2013 at 16:59:52
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
 * Custom Thread Pool Implementation. This class holds an Active and a Free Pool from which
 * threads are allocated from.
 * The Free thread pool is the collection of threads that are ready to use.
 * The active threads have been allocated to the Executor.
 * @author Michael Reilly
 */
public final class EcCustomThreadPoolImpl implements EcThreadPool {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(EcCustomThreadPoolImpl.class);

    /** Instance of this class. */
    private static final EcCustomThreadPoolImpl EC_CUSTOM_THREAD_POOL = new EcCustomThreadPoolImpl();

    /** The free Thread Pool.*/
    private final ConcurrentLinkedQueue<EcThread> freeThreadPool = new ConcurrentLinkedQueue<EcThread>();

    /** The Active Thread Pool.*/
    private final ConcurrentLinkedQueue<EcThread> activeThreadPool = new ConcurrentLinkedQueue<EcThread>();

    /** A list of threads which are in use. */
    private final ConcurrentHashMap<String, EcThreadUsage> threadUsageList = new ConcurrentHashMap<String, EcThreadUsage>();

    /** A list of threads which are DEAD. */
    private final List<EcThreadUsage> deadThreadUsageList = new ArrayList<EcThreadUsage>();

    /** The Thread Prefix. */
    private static final String THREAD_PREFIX = "EC_APP_TH_";

    /** The number of Dead threads. */
    private final AtomicLong deadThreadCount = new AtomicLong(0);

    /** The maximum Thread Number. */
    private volatile int maxThread = 0;

    /** The Connection Timeout. */
    private volatile int connectionTimeout = 0;

    /** Lock. */
    private final ReentrantLock lock = new ReentrantLock();

    /** Condition. */
    private final Condition condition = lock.newCondition();

    /** Constant for underscore. */
    private static final String UNDERSCORE = "_";

    /** Constant for UNKNOWN_JVM_ROUTE. */
    private static final String UNKNOWN_JVM_ROUTE = "UNKNOWN_JVM_ROUTE";

    /** Flag to check if the pool is created.*/
    private boolean isPoolCreated = false;

    /**
     * Private default constructor.
     */
    private EcCustomThreadPoolImpl() {

    }

    /**
     * Get the instance of this class.
     * @return An instance of this class.
     */
    public static EcThreadPool getInstance() {
        return EC_CUSTOM_THREAD_POOL;
    }

    @Override
    public boolean create(final int maxThread, final int connectionTimeout) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start Create");
        }

        boolean poolCreated = false;

        this.maxThread = maxThread;
        this.connectionTimeout = connectionTimeout;

        String jvmRoute = JvmRouteUtil.JVM_ROUTE;
        if (StringUtils.isBlank(jvmRoute)) {
            LOG.error("Java Route has not been set for this machine. Is the 'shorthostname' property defined ?");
            jvmRoute = UNKNOWN_JVM_ROUTE;
        }

        try {
            for (int i = 1; i <= maxThread; i++) {
                String threadName = THREAD_PREFIX + jvmRoute + UNDERSCORE + i;
                EcThread ecThread = createNewThread(threadName);
                freeThreadPool.add(ecThread);
            }

            LOG.info("Java Thread Pool has been created ! Maximum Pool size is " + maxThread);

            poolCreated = true;

        } catch (Exception e) {
            LOG.error("Error creating pool!", e);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish Create");
        }

        isPoolCreated = poolCreated;
        return poolCreated;
    }

    /**
     * Create a new thread with the provided name.
     * @param threadName The Thread Name.
     * @return A new Thread.
     */
    private EcThread createNewThread(final String threadName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start createNewThread");
        }

        LockManager lockManager = new LockManager();
        EcThread ecThread = new EcThread(threadName, lockManager);

        if (ThreadConfiguration.isThreadUsageMonitored()) {
            EcThreadUsage ecThreadUsage = new EcThreadUsage();
            ecThreadUsage.setThreadName(threadName);
            threadUsageList.put(ecThread.getName(), ecThreadUsage);
        }

        ecThread.start();
        ecThread.getLockManager().await();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish createNewThread");
        }
        return ecThread;
    }

    @Override
    public EcThread getThread(final Runnable firstTask, final String sessionId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getThread");
        }
        EcThread ecThread = null;

        if (isPoolCreated) {
            ecThread = freeThreadPool.poll();

            if (null == ecThread) {
                lock.lock();
                try {
                    condition.await(connectionTimeout, TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    LOG.error("getThread : Error Awaiting : " + e.getMessage(), e);
                } finally {
                    lock.unlock();
                }

                ecThread = freeThreadPool.poll();
                if (ecThread == null) {
                    LOG.error("All threads are still busy and unable to acquire the thread from pool after waiting time elapsed."
                            + " Null thread will be returned for : " + firstTask.toString());
                }

            }

            if (ecThread != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Thread [" + ecThread.getName() + "] Status : " + ecThread.getLockManager().isAwaitStatus());
                }

                activeThreadPool.add(ecThread);

                if (ThreadConfiguration.isThreadUsageMonitored()) {
                    EcThreadUsage ecThreadUsage = threadUsageList.get(ecThread.getName());
                    ecThreadUsage.setAllocatedTimeInMillis(System.currentTimeMillis());
                    ecThreadUsage.setSessionId(sessionId);
                    ecThreadUsage.setBorrowerThreadName(Thread.currentThread().getName());
                }

                ecThread.setFirstRunnableCommand(firstTask);
                ecThread.getLockManager().signal();

                LOG.info("Allocated EcThread from the Free Thread Pool !" + "with Thread Name:::" + ecThread.getName());

                /* This Log format pattern is setup for log grabber. */
                LOG.info("Free Thread Pool Size: " + freeThreadPool.size() + ", Active Thread Pool Size: " + activeThreadPool.size()
                        + ", Current time: " + System.currentTimeMillis());

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Thread is allocated to executor : " + ecThread.getName());
                }
            }

            if (firstTask == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("First Runnable Task is NULL");
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Finish getThread");
            }
        } else {

            if (LOG.isWarnEnabled()) {
                LOG.warn("Thread Pool is not created as the Max Thread is " + getMaxThread());
            }

        }
        return ecThread;
    }

    @Override
    public void releaseThread(final Runnable thread) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start releaseThread");
        }

        if (thread != null && thread instanceof EcThread) {

            EcThread ecThread = (EcThread) thread;
            EcThreadUsage ecThreadUsage = null;
            if (ecThread.isAlive()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("EcThread is still alive !");
                }
                ecThread.getLockManager().await();
                ecThread.setFirstRunnableCommand(null);

                if (ThreadConfiguration.isThreadUsageMonitored()) {

                    ecThreadUsage = threadUsageList.get(ecThread.getName());
                    long start = ecThreadUsage.getAllocatedTimeInMillis();
                    long end = System.currentTimeMillis();
                    long timeDiff = end - start;
                    long maxUsageTimeInMillis = ecThreadUsage.getMaxUsageTimeInMillis();

                    if (timeDiff > maxUsageTimeInMillis) {
                        ecThreadUsage.setMaxUsageTimeInMillis(timeDiff);
                        ecThreadUsage.setThreadBorrowedByBeyondMaxUsage(ecThreadUsage.getBorrowerThreadName());
                        ecThreadUsage.setAllocatedDate(start);
                        ecThreadUsage.setReleasedDate(end);
                        ecThreadUsage.setSessionIdBeyongMaxUsage(ecThreadUsage.getSessionId());
                    }
                    ecThreadUsage.setSessionId(null);
                    ecThreadUsage.setBorrowerThreadName(null);
                    ecThreadUsage.setAllocatedTimeInMillis(0);
                }
                // Move the thread from the Active to the Free Thread Pool.
                activeThreadPool.remove(ecThread);
                freeThreadPool.add(ecThread);

                LOG.info("EcThread has been returned back to the Free Thread Pool !" + "With Thread Name:::" + ecThread.getName());

                /* This Log format pattern is setup for log grabber. */
                LOG.info("Free Thread Pool Size: " + freeThreadPool.size() + ", Active Thread Pool Size: " + activeThreadPool.size()
                        + ", Current time: " + System.currentTimeMillis());

                if (LOG.isDebugEnabled()) {
                    LOG.debug("EcThread has been returned backed to the Free Thread Pool !");
                }

            } else {
                LOG.error("Retured thread (" + ecThread.getName() + ") is not alive. New thread will be created and added into Pool");

                // Updating the Dead Thread List
                if (ThreadConfiguration.isThreadUsageMonitored()) {
                    ecThreadUsage = threadUsageList.get(ecThread.getName());
                    deadThreadUsageList.add(ecThreadUsage);
                }
                // Removing the thread from Active Pool and creating a new Thread.
                activeThreadPool.remove(ecThread);
                freeThreadPool.add(createNewThread(ecThread.getName()));

                // Incrementing the Dead Thread Counter.
                deadThreadCount.incrementAndGet();

            }

        } else {
            LOG.error("NULL or Unknown thread has been returned to releaseThread Method : " + thread);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish releaseThread");
        }

    }

    @Override
    public int getActiveThreadCount() {
        return activeThreadPool.size();
    }

    @Override
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    @Override
    public List<EcThreadUsage> getThreadsUsage() {
        return new ArrayList<EcThreadUsage>(this.threadUsageList.values());
    }

    @Override
    public int getMaxThread() {
        String maxThreadString = System.getProperty(EcThreadPoolConstants.EC_THREAD_POOL_MAX);
        if (maxThreadString != null && !maxThreadString.trim().isEmpty()) {
            maxThread = Integer.valueOf(maxThreadString).intValue();
        }
        return maxThread;
    }

    @Override
    public int getDeadThreadCount() {
        return deadThreadCount.intValue();
    }

    @Override
    public int getFreeThreadCount() {
        return freeThreadPool.size();
    }

    @Override
    public String getThreadPoolStatus() {

        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        builder.append("Max Threads :" + maxThread);
        builder.append("\n");
        builder.append("Free Threads :" + getFreeThreadCount());
        builder.append("\n");
        builder.append("Actice Threads :" + getActiveThreadCount());
        builder.append("\n");
        builder.append("Dead Threads :" + deadThreadCount.get());
        builder.append("\n");

        List<EcThreadUsage> threadUsageList = getThreadsUsage();

        for (EcThreadUsage ecThreadUsage : threadUsageList) {
            builder.append("*****************[ " + ecThreadUsage.getThreadName() + " ]*****************");
            builder.append("\n");
            builder.append("Session ID :" + ecThreadUsage.getSessionId());
            builder.append("\n");
            builder.append("MaxUsageInMilli :" + ecThreadUsage.getMaxUsageTimeInMillis());
            builder.append("\n");
            builder.append("Thread Allocated since :" + ecThreadUsage.getAllocatedTimeInMillis());
            builder.append("\n");
        }

        return builder.toString();
    }

    @Override
    public boolean shutdown() {
        Iterator<EcThread> iterator = freeThreadPool.iterator();
        if (iterator != null) {
            while (iterator.hasNext()) {
                EcThread ecThread = iterator.next();
                ecThread.setTerminate(true);
                ecThread.getLockManager().signal();
                if (ThreadConfiguration.isThreadUsageMonitored() && threadUsageList.containsValue(ecThread.getName())) {
                    threadUsageList.remove(ecThread.getName());
                }
                freeThreadPool.remove(ecThread);

            }
        }
        iterator = activeThreadPool.iterator();
        if (iterator != null) {
            while (iterator.hasNext()) {
                EcThread ecThread = iterator.next();
                ecThread.setTerminate(true);
                if (ThreadConfiguration.isThreadUsageMonitored() && threadUsageList.containsValue(ecThread.getName())) {
                    threadUsageList.remove(ecThread.getName());
                }
                activeThreadPool.remove(ecThread);

            }
        }

        return true;
    }

    @Override
    public List<EcThreadUsage> getDeadThreadsUsage() {
        return deadThreadUsageList;
    }

    /**
     * @return the isPoolCreated
     */
    public boolean isPoolCreated() {
        return isPoolCreated;
    }

}
