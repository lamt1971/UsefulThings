package com.electrocomponents.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.electrocomponents.executor.support.TestAVPCommandRunnable;
import com.electrocomponents.executor.support.TestCommandHelper;


/**
 *
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Bhavesh Kavia
 * Created : 25 Jan 2011 at 16:03:05
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
 * Class to test the BackgroundTaskCommandExecutor in a MultiThreaded environment.
 * @author Bhavesh Kavia
 */
public class TestBackgroundTaskCommandExecutorThreaded {
    /** A commons logger. */
    private static final Log LOG = LogFactory.getLog(TestBackgroundTaskCommandExecutorThreaded.class);

    /** The executor. */
    private BackgroundTaskCommandExecutor executor = null;

    /** The command priority. */
    private final int cmdPriority = 1000;


    /**
     * A method to setup the test data.
     */
    @Before
    public void setUp() {
        // ThreadPoolExecutor values.
        int coreThreadPoolSize = 1;
        int maxPoolSize = 1;
        long keepAliveTime = 0L;

        // BoundedBlockingQueue values.
        int cappacity = 5;
        long timeOut = 4000L;
        String sessionIdent = "sessionID-Background";

        // Create the Executor
        executor = new BackgroundTaskCommandExecutor(coreThreadPoolSize, maxPoolSize, keepAliveTime, cappacity, timeOut, sessionIdent);

    }

    /**
     * The tearDown method.
     * @throws Exception Any Exception thrown.
     */
    @After
    public void tearDown() throws Exception {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    /**
     * Test to test the ExecutionSubmissionService with a Command with multiple threads.
     */
    @Test
    public void testExecuteCommandMT() {

        List<TestAVPCommandRunnable> crs = new ArrayList<TestAVPCommandRunnable>(5);
        crs.add(new TestAVPCommandRunnable(executor, "cr_t0", 2, "t0"));
        crs.add(new TestAVPCommandRunnable(executor, "cr_t1", 5, "t1"));
        crs.add(new TestAVPCommandRunnable(executor, "cr_t2", 10, "t2"));
        crs.add(new TestAVPCommandRunnable(executor, "cr_t3", 5, "t3"));
        crs.add(new TestAVPCommandRunnable(executor, "cr_t4", 11, "t4"));

        // Create Threads to execute
        List<Thread> threads = new ArrayList<Thread>(5);
        threads.add(new Thread(crs.get(0), "t0"));
        threads.add(new Thread(crs.get(1), "t1"));
        threads.add(new Thread(crs.get(2), "t2"));
        threads.add(new Thread(crs.get(3), "t3"));
        threads.add(new Thread(crs.get(4), "t4"));

        // Start the threads
        for (Thread thread : threads) {
            thread.start();
        }

        // Give some time for EcCommands to execute.
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            LOG.info("An InterruptedException occurred:" + e.getMessage(), e);
        }

        // Check the Numbers of EcCommands generated match and also the result contains the thread name.
        for (TestAVPCommandRunnable cr : crs) {
            List<Future<EcCommand>> futures = cr.getFutures();
            int size = futures.size();
            Assert.assertEquals("Number of EcCommand created and number of Futures do not match.", cr.getNoOfCommands(), size);

            for (Future<EcCommand> future : futures) {
                EcCommand ecCommand = TestCommandHelper.getCommand(future);
                ecCommand.setPriority(cmdPriority);
                String result = (String) ecCommand.getResult();
                String threadName = cr.getThreadName();
                boolean contains = result.contains(threadName);
                Assert.assertTrue("Result not for the correct thread[" + threadName + "]. Result was[" + result + "]", contains);
            }

        }

    }
}
