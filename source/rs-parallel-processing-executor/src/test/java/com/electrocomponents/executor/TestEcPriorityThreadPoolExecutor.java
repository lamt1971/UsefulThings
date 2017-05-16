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
import org.junit.Ignore;
import org.junit.Test;

import com.electrocomponents.executor.rejectionhandlers.EcRejectedExecutionHandlerImpl;
import com.electrocomponents.executor.support.TestCommandHelper;
import com.electrocomponents.executor.threadpool.EcThreadFactory;
import com.electrocomponents.executor.threadpool.EcThreadFactoryImpl;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : <<<<user name>>>>
 * Created : 28 Jun 2013 at 12:23:25
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
 * TODO add class-level javadoc.
 * @author <<<<user name>>>>
 */
public class TestEcPriorityThreadPoolExecutor {

    /** A commons logger. */
    private static final Log LOG = LogFactory.getLog(TestEcPriorityThreadPoolExecutor.class);

    /**
     * previousSubmissionOrder The Submission Order of the last command checked.
     */
    long previousSubmissionOrder = 0;

    /** Init. Bounded Blocking Queue */
    BoundedBlockingPriorityQueue<Callable> queue = null;

    /** Init. Bounded Blocking Queue */
    BoundedBlockingPriorityQueue<Runnable> queue2 = null;

    /** Init. PriorityThreadPoolExecutor */
    EcPriorityThreadPoolExecutor ecPriorityThreadPoolExecutor = null;

    /** Init. PriorityThreadPoolExecutor */
    EcPriorityThreadPoolExecutor ecPriorityThreadPoolExecutor2 = null;

    /** EcThreadFactory.*/
    EcThreadFactory ecThreadFactory = null;

    /** Dummy Session Id.*/
    String sessionId = "1233456";

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

        ecThreadFactory = EcThreadFactoryImpl.getInstance();

        LOG.debug("End Setup.");
    }

    /**
     * Test to run testThreadsExecuteInOrder.
     */
    @Ignore
    @Test
    public void testThreadsExecuteInOrderCallable() {

        LOG.debug("Start testThreadsExecuteInOrder.");

        // Generate a random session number.
        Random sessionId = new Random(10000);
        String session = String.valueOf(sessionId.nextInt());

        queue = new BoundedBlockingPriorityQueue<Callable>(corePoolSizeInt, theadIdleTimeoutInt, TimeUnit.MILLISECONDS);

        ecPriorityThreadPoolExecutor =
                new EcPriorityThreadPoolExecutor(corePoolSizeInt, corePoolSizeInt, theadIdleTimeoutInt, TimeUnit.MILLISECONDS, queue,
                        ecThreadFactory, new EcRejectedExecutionHandlerImpl(), session);
        Random generator = new Random();

        for (int i = 1; i <= corePoolSizeInt.intValue(); i++) {
            int rand = generator.nextInt(corePoolSizeInt.intValue());
            EcCommand command = TestCommandHelper.createCommand(TestCommandHelper.CommandType.TEST_AVP, "id" + rand, config);
            command.setPriority(rand);
            command.setSubmissionOrder(1L);

            Future future = ecPriorityThreadPoolExecutor.submit(command);

            EcCommand cmd = TestCommandHelper.getCommand(future);
            assertNotNull("EcCommand was NULL. This was not expected.", cmd);
            assertEquals("EcCommand result is not equal.", "TestAVPResult_avppageId_" + "id" + rand, cmd.getResult());

            boolean correctOrder = checkOrder(cmd);
            assertTrue("", correctOrder);

        }
        assertEquals(" Completed task count doesn't match expected number.", String.valueOf(ecPriorityThreadPoolExecutor
                .getCompletedTaskCount()), String.valueOf(corePoolSizeInt.intValue()));

        LOG.debug("End testThreadsExecuteInOrder.");

    }

    /**
     * Test to run testThreadsExecuteInOrder.
     */
    @Ignore
    @Test
    public void testThreadsExecuteInOrderRunnable() {

        LOG.debug("Start testThreadsExecuteInOrder.");

        // Generate a random session number.
        Random sessionId = new Random(10000);
        String session = String.valueOf(sessionId.nextInt());

        queue2 = new BoundedBlockingPriorityQueue<Runnable>(corePoolSizeInt, theadIdleTimeoutInt, TimeUnit.MILLISECONDS);

        ecPriorityThreadPoolExecutor2 =
                new EcPriorityThreadPoolExecutor(corePoolSizeInt, corePoolSizeInt, theadIdleTimeoutInt, TimeUnit.MILLISECONDS, queue2,
                        ecThreadFactory, new EcRejectedExecutionHandlerImpl(), session);

        Random generator = new Random();

        for (int i = 1; i <= corePoolSizeInt.intValue(); i++) {
            int rand = generator.nextInt(corePoolSizeInt.intValue());
            EcCommand command = TestCommandHelper.createCommand(TestCommandHelper.CommandType.TEST_AVP, "id" + rand, config);
            command.setPriority(rand);

            Future future = ecPriorityThreadPoolExecutor2.submit(command);

            EcCommand cmd = TestCommandHelper.getCommand(future);
            assertNotNull("EcCommand was NULL. This was not expected.", cmd);
            assertEquals("EcCommand result is not equal.", "TestAVPResult_avppageId_" + "id" + rand, cmd.getResult());

            boolean correctOrder = checkOrder(cmd);
            assertTrue("", correctOrder);

        }
        assertEquals(" Completed task count doesn't match expected number.", String.valueOf(ecPriorityThreadPoolExecutor2
                .getCompletedTaskCount()), String.valueOf(corePoolSizeInt.intValue()));

        LOG.debug("End testThreadsExecuteInOrder.");

    }

    /**
     * Test the shutdownNow method.
     */
    @Ignore
    @Test
    public void testShutdownNow() {

        LOG.debug("Start testShutdownNow.");

        try {
            assertSame("Executor was shutdown already. Could not perform test.", ecPriorityThreadPoolExecutor.isShutdown(), Boolean.FALSE);

            ecPriorityThreadPoolExecutor.shutdownNow();

            assertSame("Executor was not shutdown as expected.", ecPriorityThreadPoolExecutor.isShutdown(), Boolean.TRUE);
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
