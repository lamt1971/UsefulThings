package com.electrocomponents.executor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.electrocomponents.executor.support.TestCommandHelper;


/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Nick Burnham
 * Created : 20 Apr 2012 at 14:02:35
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
 * Class to test the BoundedBlockingPriorityQueue.
 * @author Nick Burnham
 */
public class TestBoundedBlockingPriorityQueue {

    /** A commons logger. */
    private static final Log LOG = LogFactory.getLog(TestBoundedBlockingPriorityQueue.class);

 
    /** Init. Bounded Blocking Queue */
    BoundedBlockingPriorityQueue<Callable> queue = null;

    /** Create Map. */
    Map<String, Object> config;

    /** corePoolSizeInt Size of the Queue. */
    Integer corePoolSizeInt = Integer.valueOf(500);

    /**
     * previousPriority The Priority of the last Command checked.
     */
    int previousPriority = 0;

    /**
     * previousSubmissionOrder The Submission Order of the last command checked.
     */
    long previousSubmissionOrder = 0;

    /** theadIdleTimeoutInt.*/
    Integer theadIdleTimeoutInt = Integer.valueOf(5000);

    /** Sleep time smaller than timeout. */
    private final long sleepTimeSmallerThanTimeout = 1000;

    /** Sleep time bigger than timeout. */
    private final long sleepTimeBiggerThanTimeout = 6000;

    /**
     * A method to setup the test data.
     */
    @Before
    public void setUp() {

        LOG.debug("Starting Setup.");

        queue = new BoundedBlockingPriorityQueue<Callable>(corePoolSizeInt, theadIdleTimeoutInt, TimeUnit.MILLISECONDS);
        config = new HashMap<String, Object>();

        LOG.debug("End Setup.");
    }

    /**
     * Test to test the queue Ordering.
     */
    @Ignore
    @Test
    public void testQueueOrder() {

        LOG.debug("Starting testQueueOrder.");

        Random generator = new Random();

        for (int i = 1; i <= corePoolSizeInt.intValue(); i++) {
            int rand = generator.nextInt(corePoolSizeInt.intValue());
            EcCommand command = TestCommandHelper.createCommand(TestCommandHelper.CommandType.TEST_AVP, "id" + rand, config);
            command.setPriority(rand);
            command.setSubmissionOrder(i);

            boolean isQueued = queue.offer(command);
            assertTrue("", isQueued);
        }

        int x = 1;
        while (!queue.isEmpty()) {

            EcCommand type = (EcCommand) queue.take();

            LOG.info("Order:" + x++ + " Priority value : " + type.getPriority() + "  Submission Order : " + type.getSubmissionOrder());

            boolean correctOrder = checkOrder(type);
            assertTrue("", correctOrder);
        }

    }

    /**
     * @param type The EcCommand to test order against
     * @return Is the order Valid.
     */
    private boolean checkOrder(final EcCommand type) {
        boolean valid = false;

        if (previousPriority == 0) {
            // first test.
            valid = true;
        } else if (previousPriority > type.getPriority()) {
            valid = true;
        } else if (previousPriority == type.getPriority()) {
            if (previousSubmissionOrder < type.getSubmissionOrder()) {
                valid = true;
            }
        }
        previousPriority = type.getPriority();
        previousSubmissionOrder = type.getSubmissionOrder();
        return valid;

    }

    /**
     * Test to insert new command into Full Queue.
     */
    @Ignore
    @Test
    public void testFullQueueWithSingleCommand() {
        LOG.debug("Start testAddingSingleCommandIntoFullQueue.");

        EcCommand command;
        for (int i = 1; i <= corePoolSizeInt.intValue(); i++) {
            command = TestCommandHelper.createCommand(TestCommandHelper.CommandType.TEST_AVP, "id" + i, config);
            command.setPriority(i);
            command.setSubmissionOrder(i);

            boolean isQueued = queue.offer(command);
            assertTrue("", isQueued);
        }

        int extraThreadId = corePoolSizeInt.intValue() + 1;
        command = TestCommandHelper.createCommand(TestCommandHelper.CommandType.TEST_AVP, "id" + extraThreadId, config);
        command.setPriority(extraThreadId);
        command.setSubmissionOrder(extraThreadId);

        boolean isQueued = queue.offer(command);
        assertFalse("Queue should not accept extra thread into Queue!", isQueued);

        LOG.debug("Finish testAddingSingleCommandIntoFullQueue.");
    }

    /**
     * Test to insert multiple commands into Full Queue.
     */
    @Ignore
    @Test
    public void testFullQueueWithMultipleQueueAdder() {
        LOG.debug("Start testAddingMultipleCommandsIntoFullQueue.");

        for (int i = 1; i <= corePoolSizeInt.intValue(); i++) {
            EcCommand command = TestCommandHelper.createCommand(TestCommandHelper.CommandType.TEST_AVP, "id" + i, config);
            command.setPriority(i);
            command.setSubmissionOrder(i);

            boolean isQueued = queue.offer(command);
            assertTrue("", isQueued);
        }

        int numOfThreads = 10;

        List<Thread> adderThreadList = new ArrayList<Thread>();
        List<FutureTask<Boolean>> adderFutureTaskList = new ArrayList<FutureTask<Boolean>>();

        int nextThreadId = corePoolSizeInt.intValue() + 1;

        for (int j = 0; j < numOfThreads; j++) {
            FutureTask<Boolean> adderFutureTask = new FutureTask<Boolean>(new QueueAdder(nextThreadId++, nextThreadId));
            Thread adderThread = new Thread(adderFutureTask);
            adderFutureTaskList.add(adderFutureTask);
            adderThreadList.add(adderThread);
            adderThread.start();
        }

        // Main thread waits for all child threads.
        for (Iterator<Thread> iterator = adderThreadList.iterator(); iterator.hasNext();) {
            Thread thread = iterator.next();
            try {
                thread.join();
            } catch (InterruptedException exp) {
                exp.printStackTrace();
                fail(exp.getMessage());
            }
        }

        // Get all success values from child threads.
        for (Iterator<FutureTask<Boolean>> iterator = adderFutureTaskList.iterator(); iterator.hasNext();) {
            Future<Boolean> futureTask = iterator.next();
            try {
                assertFalse("Adder thread should not add command into the queue because it is already full!", futureTask.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
                fail(e.getMessage());
            } catch (ExecutionException e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
        }

        LOG.debug("Finish testAddingMultipleCommandsIntoFullQueue.");
    }

    /**
     * Test to run multiple QueueAdder while QueueRemover are allocating space from Full Queue.
     */
    @Ignore
    @Test
    public void testFullQueueWithMultipleQueueAdderAndQueueRemover() {

        LOG.debug("Start testFullQueueWithMultipleQueueAdderAndQueueRemover.");

        for (int i = 1; i <= corePoolSizeInt.intValue(); i++) {
            EcCommand command = TestCommandHelper.createCommand(TestCommandHelper.CommandType.TEST_AVP, "id" + i, config);
            command.setPriority(i);
            command.setSubmissionOrder(i);

            boolean isQueued = queue.offer(command);
            assertTrue("", isQueued);
        }

        int numOfThreads = 10;

        List<Thread> removerThreadList = new ArrayList<Thread>();
        List<Thread> adderThreadList = new ArrayList<Thread>();

        List<FutureTask<Boolean>> removerFutureTaskList = new ArrayList<FutureTask<Boolean>>();
        List<FutureTask<Boolean>> adderFutureTaskList = new ArrayList<FutureTask<Boolean>>();

        long startTime = System.currentTimeMillis();

        int nextThreadId = corePoolSizeInt.intValue() + 1;

        for (int j = 0; j < numOfThreads; j++) {
            FutureTask<Boolean> adderFutureTask = new FutureTask<Boolean>(new QueueAdder(nextThreadId++, nextThreadId));
            Thread adderThread = new Thread(adderFutureTask);
            adderFutureTaskList.add(adderFutureTask);
            adderThreadList.add(adderThread);
            adderThread.start();

            FutureTask<Boolean> removerFutureTask = new FutureTask<Boolean>(new QueueRemover(sleepTimeSmallerThanTimeout));
            Thread removerThread = new Thread(removerFutureTask);
            removerFutureTaskList.add(removerFutureTask);
            removerThreadList.add(removerThread);
            removerThread.start();
        }

        for (Iterator<Thread> iterator = removerThreadList.iterator(); iterator.hasNext();) {
            Thread thread = iterator.next();
            try {
                thread.join();
            } catch (InterruptedException exp) {
                exp.printStackTrace();
                fail(exp.getMessage());
            }
        }

        for (Iterator<Thread> iterator = adderThreadList.iterator(); iterator.hasNext();) {
            Thread thread = iterator.next();
            try {
                thread.join();
            } catch (InterruptedException exp) {
                exp.printStackTrace();
                fail(exp.getMessage());
            }
        }

        long endTime = System.currentTimeMillis();
        long totalExecutionTimeMillis = endTime - startTime;
        LOG.debug("" + totalExecutionTimeMillis);

        for (Iterator<FutureTask<Boolean>> iterator = adderFutureTaskList.iterator(); iterator.hasNext();) {
            Future<Boolean> futureTask = iterator.next();
            try {
                assertTrue("QueueAdder could not add into Queue!", futureTask.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
                fail(e.getMessage());
            } catch (ExecutionException e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
        }

        for (Iterator<FutureTask<Boolean>> iterator = removerFutureTaskList.iterator(); iterator.hasNext();) {
            Future<Boolean> futureTask = iterator.next();
            try {
                assertTrue("QueueRemover could not remove from Queue!", futureTask.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
                fail(e.getMessage());
            } catch (ExecutionException e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
        }

        LOG.debug("Finish testFullQueueWithMultipleQueueAdderAndQueueRemover.");
    }

    /**
     * Queue Remover Callable implementation to remove Callable instance from the Full Queue.
     * @author Nick Burnham
     */
    private final class QueueRemover extends QueueCallable {

        /**
         * Constructor.
         * @param sleepTime the long sleep time
         */
        public QueueRemover(final long sleepTime) {
            this.sleepTime = sleepTime;
        }

        @Override
        public Boolean call() throws Exception {
            try {
                Thread.sleep(sleepTime);
                EcCommand type = (EcCommand) queue.poll();
                successfull = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return successfull;
        }
    }

    /**
     * Queue Adder Callable implementation to insert Callable instance into Queue.
     * @author Nick Burnham
     */
    private final class QueueAdder extends QueueCallable {

        /**
         * Constructor.
         * @param priority the priority value
         * @param submissionOrder the submission Order value
         */
        public QueueAdder(final int priority, final int submissionOrder) {
            this.priority = priority;
            this.submissionOrder = submissionOrder;
        }

        @Override
        public Boolean call() throws Exception {

            EcCommand command = TestCommandHelper.createCommand(TestCommandHelper.CommandType.TEST_AVP, "id" + priority, config);
            command.setPriority(priority);
            command.setSubmissionOrder(submissionOrder);

            successfull = queue.offer(command);
            return successfull;
        }
    }

    /**
     * Abstract class to keep common attributes.
     * @author Nick Burnham
     */
    protected abstract class QueueCallable implements Callable<Boolean> {

        /** Flag to keep if thread is successfull or not.*/
        protected boolean successfull = false;

        /** Sleep time.*/
        protected long sleepTime;

        /** Task Priority.*/
        protected int priority;

        /** Task submission order.*/
        protected int submissionOrder;
    }

}
