package com.electrocomponents.executor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.electrocomponents.executor.support.TestCommandHelper;


/**
 *
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Bhavesh Kavia
 * Created : 20 Jan 2011 at 14:48:33
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
 * Class to test the BackgroundTaskCommandExecutor.
 * @author Bhavesh Kavia
 */
public class TestBackgroundTaskCommandExecutor {

    /** A commons logger. */
    private static final Log LOG = LogFactory.getLog(TestBackgroundTaskCommandExecutor.class);

    /** The executor. */
    private BackgroundTaskCommandExecutor executor = null;

    /** The command priority. */
    private int cmdPriority = 1000;


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
     * The tearDown Method.
     * @throws Exception Any Exception that may be thrown.
     */
    @After
    public void tearDown() throws Exception {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    /**
     * Test to test the ExecutionSubmissionService with a Command.
     */
    @Test
    public void testExecuteCommand() {
        // No Config Needed.
        Map<String, Object> config = new HashMap<String, Object>();
        // Create Commands for the executor
        final String id = "cmd1";
        EcCommand command = TestCommandHelper.createCommand(TestCommandHelper.CommandType.TEST_AVP, id, config);
        command.setPriority(cmdPriority);
        Future<EcCommand> future = executor.executeCommand(command);

        EcCommand cmd = TestCommandHelper.getCommand(future);
        Assert.assertNotNull("EcCommand was NULL. This was not expected.", cmd);
        Assert.assertEquals("EcCommand result is not equal.", "TestAVPResult_avppageId_cmd1", cmd.getResult());
    }

    /**
     * Test to test the ExecutionSubmissionService with a List of Commands.
     */
    @Test
    public void testExecuteCommands() {

        Map<String, Object> config = new HashMap<String, Object>();
        final int noOfCommandsToCreate = 5;
        final String id = "cmd2";
        List<EcCommand> commands =
                TestCommandHelper.createCommands(noOfCommandsToCreate, TestCommandHelper.CommandType.TEST_AVP, id, config);
        try {
            List<Future<EcCommand>> futures = executor.executeCommands(commands);
            Assert.assertNotNull("List of Futures was NULL. This was not expected.", futures);

            int futureSize = futures.size();
            Assert.assertEquals("EcCommand result is not equal.", noOfCommandsToCreate, futureSize);
        } catch (InterruptedException e) {
            String message = "An InterruptedException occurred:" + e.getMessage();
            LOG.error(message, e);
            Assert.fail(message);
        }

    }

    /**
     * Test the shutdownNow method.
     */
    @Test
    public void testShutdownNow() {

        try {
            Field declaredField = executor.getClass().getDeclaredField("executor");
            declaredField.setAccessible(Boolean.TRUE);
            ExecutorService service = (ExecutorService) declaredField.get(executor);

            Assert.assertSame("Executor was shutdown already. Could not perform test.", service.isShutdown(), Boolean.FALSE);

            executor.shutdownNow();

            Assert.assertSame("Executor was not shutdown as expected.", service.isShutdown(), Boolean.TRUE);
        } catch (SecurityException e) {
            String message = "A SecurityException occurred:" + e.getMessage();
            LOG.error(message, e);
            Assert.fail(message);
        } catch (NoSuchFieldException e) {
            String message = "A NoSuchFieldException occurred:" + e.getMessage();
            LOG.error(message, e);
            Assert.fail(message);
        } catch (IllegalArgumentException e) {
            String message = "A IllegalArgumentException occurred:" + e.getMessage();
            LOG.error(message, e);
            Assert.fail(message);
        } catch (IllegalAccessException e) {
            String message = "A IllegalAccessException occurred:" + e.getMessage();
            LOG.error(message, e);
            Assert.fail(message);
        }
    }

}
