package com.electrocomponents.executor.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.executor.BackgroundTaskCommandExecutor;
import com.electrocomponents.executor.EcCommand;

/**
 * A Runnable Class to create some commands and execute it.
 * @author Bhavesh Kavia
 */
public class TestAVPCommandRunnable implements Runnable {
    /** A commons logger. */
    private static final Log LOG = LogFactory.getLog(TestAVPCommandRunnable.class);

    /** The executor. */
    private BackgroundTaskCommandExecutor executor = null;

    /** List of EcCommands to execute. */
    private final List<EcCommand> commands;

    /** List of Futures returned. */
    private List<Future<EcCommand>> futures;

    /** Number of EcCommands Created. */
    private final int noOfCommands;

    /** Thread Name. */
    private final String threadName;

    /** The command priority. */
    private int cmdPriority = 1000;

    /**
     * Constructor.
     * @param executor - The BackgroundTaskCommandExecutor
     * @param id - The Identifiers to distinguish
     * @param noOfCommands - The number of commands to be created.
     * @param threadName - The Thread Name associated with this Runnable.
     */
    public TestAVPCommandRunnable(final BackgroundTaskCommandExecutor executor, final String id, final int noOfCommands,
            final String threadName) {
        this.executor = executor;
        this.noOfCommands = noOfCommands;
        this.threadName = threadName;
        // No config needed to create TestAVP Commands
        Map<String, Object> config = new HashMap<String, Object>();
        List<EcCommand> ecCcommands =
                TestCommandHelper.createCommands(this.noOfCommands, TestCommandHelper.CommandType.TEST_AVP, "cmdmt_" + id + "_"
                        + this.getThreadName(), config);

        for (EcCommand ecCommand : ecCcommands) {
            ecCommand.setPriority(cmdPriority);
        }

        this.commands = new ArrayList<EcCommand>(this.noOfCommands);
        this.commands.addAll(ecCcommands);

    }

    /** {@inheritDoc} */
    public void run() {
        LOG.info("Running Thread...." + Thread.currentThread().getName());

        try {
            this.futures = executor.executeCommands(commands);
        } catch (InterruptedException e) {
            LOG.error("An InterruptedException occurred", e);
        }

    }

    /**
     * @return the futures
     */
    public List<Future<EcCommand>> getFutures() {
        return this.futures;
    }

    /**
     * @return the noOfCommands
     */
    public int getNoOfCommands() {
        return this.noOfCommands;
    }

    /**
     * @return the threadName
     */
    public String getThreadName() {
        return threadName;
    }
}
