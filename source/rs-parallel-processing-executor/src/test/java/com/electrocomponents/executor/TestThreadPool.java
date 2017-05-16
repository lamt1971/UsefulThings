package com.electrocomponents.executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.electrocomponents.executor.rejectionhandlers.EcRejectedExecutionHandlerImpl;
import com.electrocomponents.executor.threadpool.EcCustomThreadPoolImpl;
import com.electrocomponents.executor.threadpool.EcThread;
import com.electrocomponents.executor.threadpool.EcThreadFactory;
import com.electrocomponents.executor.threadpool.EcThreadFactoryImpl;
import com.electrocomponents.executor.threadpool.EcThreadPool;
import com.electrocomponents.executor.threadpool.EcThreadUsage;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Berkan Kurtoglu
 * Created : 26 Jul 2013 at 13:12:23
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
 * Class to test the Thread Pool.
 * @author Berkan Kurtoglu
 */
public class TestThreadPool {

    /** A commons logger. */
    private static final Log LOG = LogFactory.getLog(TestThreadPool.class);

    /** maxThreadPoolSizeInt. */
    int maxThreadPoolSizeInt = 10;

    /** theadIdleTimeoutInt. */
    int theadIdleTimeoutInt = 3000;

    /**
     * Test to run testSingleExecuterInSingleThreadedMode.
     */
    @Test
    public void testSingleExecuterInSingleThreadedMode() {

        LOG.debug("Start testSingleExecuterInSingleThreadedMode.");

        int threadPoolSize = 10;
        int theadIdleTimeoutInt = 3000;

        boolean isSuccess = EcCustomThreadPoolImpl.getInstance().create(threadPoolSize, theadIdleTimeoutInt);
        assertTrue("isSuccess flag should be true", isSuccess);

        int numberOfExecuter = 1;
        int numberOfTasksPerExecuter = 10;
        long threadRunTime = 1000;

        int corePoolSize = 0;
        int maxThreadPoolSize = 5;
        int blockingQueueDepth = 10;
        long blockingQueueTimeout = 4000;

        executeExecuters(threadPoolSize, numberOfExecuter, numberOfTasksPerExecuter, threadRunTime, corePoolSize, maxThreadPoolSize,
                blockingQueueDepth, blockingQueueTimeout, 1);

        sleep(2000);

        EcThreadPool ecThreadPool = EcCustomThreadPoolImpl.getInstance();
        assertEquals("Only 1 thread should be active!", 1, ecThreadPool.getActiveThreadCount());
        assertEquals("Only 9 thread should be free!", 9, ecThreadPool.getFreeThreadCount());

        for (int i = 1; i < 10; i++) {
            EcThread ecThread = ecThreadPool.getThread(new TestTask(1000), "1" + i);
            assertNotNull("Thread should not be null!", ecThread);
        }

        ecThreadPool.shutdown();

        sleep(10000);

        assertEquals("All the threads should have been freed!", 0, ecThreadPool.getFreeThreadCount());
        assertEquals("All the threads should have been freed!", 0, ecThreadPool.getActiveThreadCount());

        LOG.debug("End testSingleExecuterInSingleThreadedMode.");

    }

    /**
     * Test to run testSingleExecuterInMultiThreadedMode.
     */
    @Test
    public void testSingleExecuterInMultiThreadedMode() {

        LOG.debug("Start testThreadsExecuteInOrder.");

        int threadPoolSize = 5;
        int theadIdleTimeoutInt = 1000;

        boolean isSuccess = EcCustomThreadPoolImpl.getInstance().create(threadPoolSize, theadIdleTimeoutInt);
        assertTrue("isSuccess flag should be true", isSuccess);

        int numberOfExecuter = 1;
        int numberOfTasksPerExecuter = 10;
        long threadRunTime = 5000;

        int corePoolSize = 5;
        int maxThreadPoolSize = 5;
        int blockingQueueDepth = 5;
        long blockingQueueTimeout = 4000;

        executeExecuters(threadPoolSize, numberOfExecuter, numberOfTasksPerExecuter, threadRunTime, corePoolSize, maxThreadPoolSize,
                blockingQueueDepth, blockingQueueTimeout, 1);

        sleep(5000);

        EcThreadPool ecThreadPool = EcCustomThreadPoolImpl.getInstance();
        assertEquals("Only 5 thread should be active!", 5, ecThreadPool.getActiveThreadCount());
        assertEquals("Only 0 thread should be free!", 0, ecThreadPool.getFreeThreadCount());

        for (int i = 1; i < 10; i++) {
            EcThread ecThread = ecThreadPool.getThread(new TestTask(1000), "1" + i);
            assertNull("Thread should be null!", ecThread);
        }

        ecThreadPool.shutdown();

        sleep(10000);

        assertEquals("All the threads should have been freed!", 0, ecThreadPool.getFreeThreadCount());
        assertEquals("All the threads should have been freed!", 0, ecThreadPool.getActiveThreadCount());

        LOG.debug("End testSingleExecuterInMultiThreadedMode.");

    }

    /**
     * Test to run testThreadPoolExecutersWorkLikeSingleThreaded.
     */
    @Test
    public void testThreadPoolExecutersWorkLikeSingleThreaded() {

        LOG.debug("Start testThreadsExecuteInOrder.");

        boolean isSuccess = EcCustomThreadPoolImpl.getInstance().create(maxThreadPoolSizeInt, theadIdleTimeoutInt);
        assertTrue("isSuccess flag should be true", isSuccess);

        int numberOfExecuter = 10;
        int numberOfThreads = 10;
        long threadRunTime = 1000;

        int corePoolSize = 0;
        int maxThreadPoolSize = 5;
        int blockingQueueDepth = 3100;
        long blockingQueueTimeout = 4000;

        for (int i = 0; i < numberOfExecuter; i++) {
            Runnable myExecuterRunnable =
                    new TestExecutorRunnable(threadRunTime, numberOfThreads, corePoolSize, maxThreadPoolSize, blockingQueueDepth,
                            theadIdleTimeoutInt, blockingQueueTimeout, getBackgroundTaskId("session-" + i));

            Thread myThread = new Thread(myExecuterRunnable);

            myThread.start();
        }

        sleep(5000);

        EcThreadPool ecThreadPool = EcCustomThreadPoolImpl.getInstance();

        assertEquals("All threads should be allocated!", this.maxThreadPoolSizeInt, ecThreadPool.getActiveThreadCount());
        System.out.println("All threads are allocated!");

        assertEquals("No thread should be free!", 0, ecThreadPool.getFreeThreadCount());
        System.out.println("No thread is free!");

        sleep(10000);

        assertEquals("Threads should be returned to application thread pool!", 0, ecThreadPool.getActiveThreadCount());
        System.out.println("All threads returned to application thread pool!");

        assertEquals("All the threads should be free!", this.maxThreadPoolSizeInt, ecThreadPool.getFreeThreadCount());
        System.out.println("All the threads are free!");

        ecThreadPool.shutdown();

        LOG.debug("End testThreadPoolExecutersWorkLikeSingleThreaded.");

    }

    /**
     * Test to run testThreadPoolExecutersWorkLikeSingleThreaded1.
     */
    @Test
    public void testThreadPoolExecutersWorkLikeSingleThreaded1() {

        LOG.debug("Start testThreadsExecuteInOrder.");

        int maxThreadPoolSizeInt = 10;
        int theadIdleTimeoutInt = 3000;

        boolean isSuccess = EcCustomThreadPoolImpl.getInstance().create(maxThreadPoolSizeInt, theadIdleTimeoutInt);
        assertTrue("isSuccess flag should be true", isSuccess);

        int numberOfExecuter = 10;
        int numberOfTasksPerExecuter = 10;
        long threadRunTime = 1000;

        int corePoolSize = 0;
        int maxThreadPoolSize = 5;
        int blockingQueueDepth = 3100;
        long blockingQueueTimeout = 4000;

        for (int i = 0; i < numberOfExecuter; i++) {
            Runnable executer =
                    new TestExecutorRunnable(threadRunTime, numberOfTasksPerExecuter, corePoolSize, maxThreadPoolSize, blockingQueueDepth,
                            theadIdleTimeoutInt, blockingQueueTimeout, getBackgroundTaskId("session-" + i));

            Thread myThread = new Thread(executer);

            myThread.start();
        }

        sleep(5000);

        EcThreadPool ecThreadPool = EcCustomThreadPoolImpl.getInstance();

        assertEquals("All threads should be allocated!", maxThreadPoolSizeInt, ecThreadPool.getActiveThreadCount());
        System.out.println("All threads are allocated!");

        assertEquals("No thread should be free!", 0, ecThreadPool.getFreeThreadCount());
        System.out.println("No thread is free!");

        sleep(10000);

        assertEquals("Threads should be returned to application thread pool!", 0, ecThreadPool.getActiveThreadCount());
        System.out.println("All threads returned to application thread pool!");

        assertEquals("All the threads should be free!", maxThreadPoolSizeInt, ecThreadPool.getFreeThreadCount());
        System.out.println("All the threads are free!");

        List<EcThreadUsage> threadUsageList = ecThreadPool.getThreadsUsage();
        for (EcThreadUsage threadUsage : threadUsageList) {

            long threadMaxUsage = threadRunTime * numberOfTasksPerExecuter;
            assertTrue("Thread usage should be bigger than " + threadMaxUsage, threadUsage.getMaxUsageTimeInMillis() > threadMaxUsage);
        }

        ecThreadPool.shutdown();

        assertEquals("All the threads should have been freed!", 0, ecThreadPool.getFreeThreadCount());
        assertEquals("All the threads should have been freed!", 0, ecThreadPool.getActiveThreadCount());

        LOG.debug("End testThreadPoolExecutersWorkLikeSingleThreaded1.");

    }

    /**
     * Test to run testThreadPoolReturningNullThread.
     */
    @Test
    public void testThreadPoolReturningNullThread() {

        LOG.debug("Start testThreadsExecuteInOrder.");

        int threadPoolSize = 5;
        int theadIdleTimeoutInt = 1000;

        boolean isSuccess = EcCustomThreadPoolImpl.getInstance().create(threadPoolSize, theadIdleTimeoutInt);
        assertTrue("isSuccess flag should be true", isSuccess);

        int numberOfExecuter = 5;
        int numberOfTasksPerExecuter = 1;
        long threadRunTime = 5000;

        int corePoolSize = 0;
        int maxThreadPoolSize = 5;
        int blockingQueueDepth = 10;
        long blockingQueueTimeout = 1000;

        executeExecuters(threadPoolSize, numberOfExecuter, numberOfTasksPerExecuter, threadRunTime, corePoolSize, maxThreadPoolSize,
                blockingQueueDepth, blockingQueueTimeout, 1);

        sleep(1000);
        EcThreadPool ecThreadPool = EcCustomThreadPoolImpl.getInstance();

        for (int i = 0; i < 5; i++) {
            EcThread ecThread = ecThreadPool.getThread(new TestTask(1000), "1");
            assertNull("Thread should be null!", ecThread);
        }

        sleep(5000);

        ecThreadPool.shutdown();

        assertEquals("All the threads should have been freed!", 0, ecThreadPool.getFreeThreadCount());
        assertEquals("All the threads should have been freed!", 0, ecThreadPool.getActiveThreadCount());

        LOG.debug("End testThreadPoolReturningNullThread.");

    }

    /**
     * Test to run testExecuterWithReturningNullThread.
     */
    @Test
    public void testExecuterWithReturningNullThread() {

        LOG.debug("Start testThreadsExecuteInOrder.");

        int threadPoolSize = 5;
        int theadIdleTimeoutInt = 1000;

        boolean isSuccess = EcCustomThreadPoolImpl.getInstance().create(threadPoolSize, theadIdleTimeoutInt);
        assertTrue("isSuccess flag should be true", isSuccess);

        // 6th thread will not get null thread.
        int numberOfExecuter = 6;
        int numberOfTasksPerExecuter = 1;
        long threadRunTime = 5000;

        int corePoolSize = 0;
        int maxThreadPoolSize = 5;
        int blockingQueueDepth = 10;
        long blockingQueueTimeout = 1000;

        executeExecuters(threadPoolSize, numberOfExecuter, numberOfTasksPerExecuter, threadRunTime, corePoolSize, maxThreadPoolSize,
                blockingQueueDepth, blockingQueueTimeout, 1);

        sleep(10000);
        EcThreadPool ecThreadPool = EcCustomThreadPoolImpl.getInstance();

        ecThreadPool.shutdown();

        assertEquals("All the threads should have been freed!", 0, ecThreadPool.getFreeThreadCount());
        assertEquals("All the threads should have been freed!", 0, ecThreadPool.getActiveThreadCount());

        LOG.debug("End testExecuterWithReturningNullThread.");

    }

    /**
     * Test to run testExecuter10Times.
     */
    @Test
    public void testExecuter10Times() {

        LOG.debug("Start testExecuter10Times.");

        int threadPoolSize = 10;
        int theadIdleTimeoutInt = 3000;

        boolean isSuccess = EcCustomThreadPoolImpl.getInstance().create(threadPoolSize, theadIdleTimeoutInt);
        assertTrue("isSuccess flag should be true", isSuccess);

        int numberOfExecuter = 10;
        int numberOfTasksPerExecuter = 10;
        long threadRunTime = 1000;

        int corePoolSize = 0;
        int maxThreadPoolSize = 5;
        int blockingQueueDepth = 3100;
        long blockingQueueTimeout = 4000;

        for (int i = 0; i < 10; i++) {
            executeAndTestExecuters(maxThreadPoolSize, numberOfExecuter, numberOfTasksPerExecuter, threadRunTime, corePoolSize,
                    maxThreadPoolSize, blockingQueueDepth, blockingQueueTimeout, i);

            sleep(2000);
        }

        EcThreadPool ecThreadPool = EcCustomThreadPoolImpl.getInstance();
        ecThreadPool.shutdown();

        assertEquals("All the threads should have been freed!", 0, ecThreadPool.getFreeThreadCount());
        assertEquals("All the threads should have been freed!", 0, ecThreadPool.getActiveThreadCount());

        LOG.debug("End testExecuter10Times.");

    }

    /**
     * Test to run testExecuter5Times.
     */
    @Test
    public void testExecuter5Times() {

        LOG.debug("Start testThreadsExecuteInOrder.");

        int threadPoolSize = 10;
        int theadIdleTimeoutInt = 3000;

        boolean isSuccess = EcCustomThreadPoolImpl.getInstance().create(threadPoolSize, theadIdleTimeoutInt);
        assertTrue("isSuccess flag should be true", isSuccess);

        int numberOfExecuter = 10;
        int numberOfTasksPerExecuter = 10;
        long threadRunTime = 1000;

        int corePoolSize = 0;
        int maxThreadPoolSize = 5;
        int blockingQueueDepth = 3100;
        long blockingQueueTimeout = 4000;

        for (int i = 0; i < 5; i++) {
            executeAndTestExecuters(threadPoolSize, numberOfExecuter, numberOfTasksPerExecuter, threadRunTime, corePoolSize,
                    maxThreadPoolSize, blockingQueueDepth, blockingQueueTimeout, i);

            sleep(2000);
        }

        EcThreadPool ecThreadPool = EcCustomThreadPoolImpl.getInstance();
        ecThreadPool.shutdown();

        assertEquals("All the threads should have been freed!", 0, ecThreadPool.getFreeThreadCount());
        assertEquals("All the threads should have been freed!", 0, ecThreadPool.getActiveThreadCount());

        LOG.debug("End testExecuter100Times.");

    }

    /**
     * Method to execute executers.
     * @param threadPoolSize the threadPoolSize
     * @param numberOfExecuter the numberOfExecuter
     * @param numberOfTasksPerExecuter the numberOfTasksPerExecuter
     * @param threadRunTime the threadRunTime
     * @param corePoolSize the corePoolSize
     * @param maxThreadPoolSize the maxThreadPoolSize
     * @param blockingQueueDepth the blockingQueueDepth
     * @param blockingQueueTimeout the blockingQueueTimeout
     * @param sessionId the sessionId
     */
    private void executeAndTestExecuters(final int threadPoolSize, final int numberOfExecuter, final int numberOfTasksPerExecuter,
            final long threadRunTime, final int corePoolSize, final int maxThreadPoolSize, final int blockingQueueDepth,
            final long blockingQueueTimeout, final int sessionId) {

        executeExecuters(threadPoolSize, numberOfExecuter, numberOfTasksPerExecuter, threadRunTime, corePoolSize, maxThreadPoolSize,
                blockingQueueDepth, blockingQueueTimeout, sessionId);

        sleep(5000);

        EcThreadPool ecThreadPool = EcCustomThreadPoolImpl.getInstance();

        assertEquals("All threads should be allocated!", threadPoolSize, ecThreadPool.getActiveThreadCount());
        System.out.println("All threads are allocated!");

        assertEquals("No thread should be free!", 0, ecThreadPool.getFreeThreadCount());
        System.out.println("No thread is free!");

        long threadMaxUsage = threadRunTime * numberOfTasksPerExecuter;

        sleep((int) threadMaxUsage);

        assertEquals("Threads should be returned to application thread pool!", 0, ecThreadPool.getActiveThreadCount());
        System.out.println("All threads returned to application thread pool!");

        assertEquals("All the threads should be free!", threadPoolSize, ecThreadPool.getFreeThreadCount());
        System.out.println("All the threads are free!");

        List<EcThreadUsage> threadUsageList = ecThreadPool.getThreadsUsage();
        for (EcThreadUsage threadUsage : threadUsageList) {
            assertTrue("Thread usage should be bigger than " + threadMaxUsage, threadUsage.getMaxUsageTimeInMillis() > threadMaxUsage);
        }
    }

    /**
     * Method to execute executers.
     * @param threadPoolSize the threadPoolSize
     * @param numberOfExecuter the numberOfExecuter
     * @param numberOfTasksPerExecuter the numberOfTasksPerExecuter
     * @param threadRunTime the threadRunTime
     * @param corePoolSize the corePoolSize
     * @param maxThreadPoolSize the maxThreadPoolSize
     * @param blockingQueueDepth the blockingQueueDepth
     * @param blockingQueueTimeout the blockingQueueTimeout
     * @param sessionId the sessionId
     */
    private void executeExecuters(final int threadPoolSize, final int numberOfExecuter, final int numberOfTasksPerExecuter,
            final long threadRunTime, final int corePoolSize, final int maxThreadPoolSize, final int blockingQueueDepth,
            final long blockingQueueTimeout, final int sessionId) {

        for (int i = 0; i < numberOfExecuter; i++) {
            Runnable executer =
                    new TestExecutorRunnable(threadRunTime, numberOfTasksPerExecuter, corePoolSize, maxThreadPoolSize, blockingQueueDepth,
                            theadIdleTimeoutInt, blockingQueueTimeout, getBackgroundTaskId("session-" + sessionId + "-" + i));

            Thread myThread = new Thread(executer);

            myThread.start();
        }
    }

    /**
     * The Background Task Identifier.
     * @param sessionId the sessionId
     * @return backgroundTaskId The Background Task Identifier.
     */
    private String getBackgroundTaskId(final String sessionId) {
        return sessionId + "-Background";
    }

    /**
     * The sleep method.
     * @param milisecond the milisecond
     */
    private void sleep(final int milisecond) {

        try {
            Thread.sleep(milisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     * ************************************************************************************************
     * Copyright (c) Electrocomponents Plc.
     *
     * Author  : Berkan Kurtoglu
     * Created : 23 Jul 2013 at 11:58:43
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
     * Test class to test sleeping thread.
     * @author Berkan Kurtoglu
     */
    private class TestTask extends BasePriorityAndSubmissionOrder implements Runnable {

        /** Time to keep thread sleeping. */
        long time;

        /**
         * Constructor.
         * @param time the sleeping time
         */
        public TestTask(final long time) {
            super();
            this.time = time;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " is getting ready to sleep!");

                Thread.sleep(time);

                System.out.println(Thread.currentThread().getName() + " is waking up!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <pre>
     * ************************************************************************************************
     * Copyright (c) Electrocomponents Plc.
     *
     * Author  : Berkan Kurtoglu
     * Created : 23 Jul 2013 at 11:57:57
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
     * Test class to test ThreadPoolExecutor.
     * @author Berkan Kurtoglu
     */
    private class TestExecutorRunnable implements Runnable {

        /** threadRunTime. */
        long threadRunTime;

        /** noOfThreads. */
        int noOfThreads;

        /** corePoolSize. */
        int corePoolSize;

        /** maxThreadPoolSize. */
        int maxThreadPoolSize;

        /** blockingQueueDepth. */
        int blockingQueueDepth;

        /** theadIdleTimeout. */
        int theadIdleTimeout;

        /** blockingQueueTimeout. */
        long blockingQueueTimeout;

        /** sessionIdentifier. */
        String sessionIdentifier;

        /**
         * Constructor.
         * @param threadRunTime the threadRunTime
         * @param noOfThreads the noOfThreads
         * @param corePoolSize the corePoolSize
         * @param maxThreadPoolSize the maxThreadPoolSize
         * @param blockingQueueDepth the blockingQueueDepth
         * @param theadIdleTimeout the theadIdleTimeout
         * @param blockingQueueTimeout the blockingQueueTimeout
         * @param sessionIdentifier the sessionIdentifier
         */
        public TestExecutorRunnable(final long threadRunTime, final int noOfThreads, final int corePoolSize, final int maxThreadPoolSize,
                final int blockingQueueDepth, final int theadIdleTimeout, final long blockingQueueTimeout, final String sessionIdentifier) {

            super();

            this.threadRunTime = threadRunTime;
            this.noOfThreads = noOfThreads;
            this.corePoolSize = corePoolSize;
            this.maxThreadPoolSize = maxThreadPoolSize;
            this.blockingQueueDepth = blockingQueueDepth;
            this.theadIdleTimeout = theadIdleTimeout;
            this.blockingQueueTimeout = blockingQueueTimeout;
            this.sessionIdentifier = sessionIdentifier;
        }

        @Override
        public void run() {

            EcThreadFactory threadFactory = EcThreadFactoryImpl.getInstance();

            BoundedBlockingPriorityQueue<Runnable> workQueue =
                    new BoundedBlockingPriorityQueue<Runnable>(blockingQueueDepth, blockingQueueTimeout, TimeUnit.MILLISECONDS);

            ExecutorService executor =
                    new EcPriorityThreadPoolExecutor(corePoolSize, maxThreadPoolSize, theadIdleTimeout, TimeUnit.MILLISECONDS, workQueue,
                            threadFactory, new EcRejectedExecutionHandlerImpl(), sessionIdentifier);

            for (int i = 0; i < noOfThreads; i++) {
                Runnable task = new TestTask(threadRunTime);

                executor.submit(task);
            }

        }
    }
}
