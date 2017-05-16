package com.electrocomponents.executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import com.electrocomponents.executor.support.TestCommandHelper;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Nick Burnham
 * Created : 25 Apr 2012 at 12:11:35
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
 * Class to test the PriorityThreadPoolExecutor.
 * @author Nick Burnham
 */
public class TestPriorityThreadPoolExecutor {

    /** A commons logger. */
    private static final Log LOG = LogFactory.getLog(TestPriorityThreadPoolExecutor.class);

    /**
     * previousSubmissionOrder The Submission Order of the last command checked.
     */
    long previousSubmissionOrder = 0;

    /** Init. Bounded Blocking Queue */
    BoundedBlockingPriorityQueue<Callable> queue = null;

    /** Init. Bounded Blocking Queue */
    BoundedBlockingPriorityQueue<Runnable> queue2 = null;

    /** Init. PriorityThreadPoolExecutor */
    PriorityThreadPoolExecutor priorityThreadPoolExecutor = null;

    /** Init. PriorityThreadPoolExecutor */
    PriorityThreadPoolExecutor priorityThreadPoolExecutor2 = null;

    /** Init. ApplicationThreadPoolFactory */
    ApplicationThreadPoolFactory applicationThreadPoolFactory = null;

    /** Create Map. */
    Map<String, Object> config;

    /** corePoolSizeInt Size of the Queue. */
    Integer corePoolSizeInt = Integer.valueOf(100);

    /** theadIdleTimeoutInt.*/
    Integer theadIdleTimeoutInt = Integer.valueOf(5000);

    /**
     * A method to setup the test data.
     */
    @Before
    public void setUp() {

        LOG.debug("Starting Setup.");

        config = new HashMap<String, Object>();

        String poolName = "PriorityThreadPoolExecutorTest";

        applicationThreadPoolFactory = new ApplicationThreadPoolFactory(poolName);

        LOG.debug("End Setup.");
    }

    /**
     * Test to run testThreadsExecuteInOrder.
     */
    @Test
    public void testThreadsExecuteInOrderCallable() {

        LOG.debug("Start testThreadsExecuteInOrder.");

        queue = new BoundedBlockingPriorityQueue<Callable>(corePoolSizeInt, theadIdleTimeoutInt, TimeUnit.MILLISECONDS);

        priorityThreadPoolExecutor =
                new PriorityThreadPoolExecutor(corePoolSizeInt, corePoolSizeInt, theadIdleTimeoutInt, TimeUnit.MILLISECONDS, queue,
                        applicationThreadPoolFactory);
        Random generator = new Random();

        for (int i = 1; i <= corePoolSizeInt.intValue(); i++) {
            int rand = generator.nextInt(corePoolSizeInt.intValue());
            EcCommand command = TestCommandHelper.createCommand(TestCommandHelper.CommandType.TEST_AVP, "id" + rand, config);
            command.setPriority(rand);
            command.setSubmissionOrder(1L);

            Future future = priorityThreadPoolExecutor.submit(command);

            EcCommand cmd = TestCommandHelper.getCommand(future);
            assertNotNull("EcCommand was NULL. This was not expected.", cmd);
            assertEquals("EcCommand result is not equal.", "TestAVPResult_avppageId_" + "id" + rand, cmd.getResult());

            boolean correctOrder = checkOrder(cmd);
            assertTrue("", correctOrder);

        }
        assertEquals(" Completed task count doesn't match expected number.", String.valueOf(priorityThreadPoolExecutor
                .getCompletedTaskCount()), String.valueOf(corePoolSizeInt.intValue()));

        LOG.debug("End testThreadsExecuteInOrder.");

    }

    /**
     * Test to run testThreadsExecuteInOrder.
     */
    @Test
    public void testThreadsExecuteInOrderRunnable() {

        LOG.debug("Start testThreadsExecuteInOrder.");

        queue2 = new BoundedBlockingPriorityQueue<Runnable>(corePoolSizeInt, theadIdleTimeoutInt, TimeUnit.MILLISECONDS);

        priorityThreadPoolExecutor2 =
                new PriorityThreadPoolExecutor(corePoolSizeInt, corePoolSizeInt, theadIdleTimeoutInt, TimeUnit.MILLISECONDS, queue2,
                        applicationThreadPoolFactory);
        Random generator = new Random();

        for (int i = 1; i <= corePoolSizeInt.intValue(); i++) {
            int rand = generator.nextInt(corePoolSizeInt.intValue());
            EcCommand command = TestCommandHelper.createCommand(TestCommandHelper.CommandType.TEST_AVP, "id" + rand, config);
            command.setPriority(rand);

            Future future = priorityThreadPoolExecutor2.submit(command);

            EcCommand cmd = TestCommandHelper.getCommand(future);
            assertNotNull("EcCommand was NULL. This was not expected.", cmd);
            assertEquals("EcCommand result is not equal.", "TestAVPResult_avppageId_" + "id" + rand, cmd.getResult());

            boolean correctOrder = checkOrder(cmd);
            assertTrue("", correctOrder);

        }
        assertEquals(" Completed task count doesn't match expected number.", String.valueOf(priorityThreadPoolExecutor2
                .getCompletedTaskCount()), String.valueOf(corePoolSizeInt.intValue()));

        LOG.debug("End testThreadsExecuteInOrder.");

    }

    /**
     * Test the shutdownNow method.
     */
    @Test
    public void testShutdownNow() {

        LOG.debug("Start testShutdownNow.");

        try {
            queue = new BoundedBlockingPriorityQueue<Callable>(corePoolSizeInt, theadIdleTimeoutInt, TimeUnit.MILLISECONDS);

            priorityThreadPoolExecutor =
                    new PriorityThreadPoolExecutor(corePoolSizeInt, corePoolSizeInt, theadIdleTimeoutInt, TimeUnit.MILLISECONDS, queue,
                            applicationThreadPoolFactory);
            
            assertSame("Executor was shutdown already. Could not perform test.", priorityThreadPoolExecutor.isShutdown(), Boolean.FALSE);

            priorityThreadPoolExecutor.shutdownNow();

            assertSame("Executor was not shutdown as expected.", priorityThreadPoolExecutor.isShutdown(), Boolean.TRUE);
        } catch (SecurityException e) {
            String message = "A SecurityException occurred:" + e.getMessage();
            LOG.error(message, e);
            fail(message);
        } catch (IllegalArgumentException e) {
            String message = "A IllegalArgumentException occurred:" + e.getMessage();
            LOG.error(message, e);
            fail(message);
        }

        LOG.debug("End testShutdownNow.");
    }

    /**
     * @param type The EcCommand to test order against
     * @return Is the order Valid.
     */
    private boolean checkOrder(final EcCommand type) {
        boolean valid = false;

        if (previousSubmissionOrder < type.getSubmissionOrder()) {
            valid = true;
        }
        previousSubmissionOrder = type.getSubmissionOrder();
        return valid;

    }

}
