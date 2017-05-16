package com.electrocomponents.executor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.electrocomponents.executor.support.TestCommandHelper;


/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Nick Burnham
 * Created : 16 Apr 2012 at 11:14:19
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
 * Class to test the BasePriorityAndSubmissionOrder.
 * @author Nick Burnham
 * @param <V>
 */
public class TestBasePriorityAndSubmissionOrder<V> {

    /** A commons logger. */
    private static final Log LOG = LogFactory.getLog(TestBasePriorityAndSubmissionOrder.class);

    /** Thread Idle timeout. */
    private final int priority = 1000;


    /** Create Map. */
    Map<String, Object> config;

    /** id1 The name of Command1. */
    final String id1 = "cmd1";

    /** id2 The name of Command2. */
    final String id2 = "cmd2";

    /**
     * A method to setup the test data.
     */
    @Before
    public void setUp() {

        config = new HashMap<String, Object>();
    }

    /**
     * Test to test the ExecutionSubmissionService with a Command.
     */
    @Test
    public void testComparePriorityFirstHigher() {

        EcCommand command1 = TestCommandHelper.createCommand(TestCommandHelper.CommandType.TEST_AVP, id1, config);
        command1.setPriority(1000);
        command1.setSubmissionOrder(1);

        EcCommand command2 = TestCommandHelper.createCommand(TestCommandHelper.CommandType.TEST_AVP, id2, config);
        command2.setPriority(100);
        command2.setSubmissionOrder(2);

        int compareValue = command1.compareTo(command2);

        Assert.assertEquals("Command 1 should rate as higher : ", -1, compareValue);

    }

    /**
     * Test to test the BasePriorityAndSubmissionOrder Compare.
     */
    @Test
    public void testComparePrioritySecondHigher() {

        EcCommand command1 = TestCommandHelper.createCommand(TestCommandHelper.CommandType.TEST_AVP, id1, config);
        command1.setPriority(100);
        command1.setSubmissionOrder(1);

        EcCommand command2 = TestCommandHelper.createCommand(TestCommandHelper.CommandType.TEST_AVP, id2, config);
        command2.setPriority(1000);
        command2.setSubmissionOrder(2);

        int compareValue = command1.compareTo(command2);

        Assert.assertEquals("Command 1 should rate as minus : ", 1, compareValue);

    }

    /**
     * Test to test the BasePriorityAndSubmissionOrder Compare.
     */
    @Test
    public void testPriorityEqualSubmissionPriorityFirstHigher() {

        EcCommand command1 = TestCommandHelper.createCommand(TestCommandHelper.CommandType.TEST_AVP, id1, config);
        command1.setPriority(1000);
        command1.setSubmissionOrder(1);

        EcCommand command2 = TestCommandHelper.createCommand(TestCommandHelper.CommandType.TEST_AVP, id2, config);
        command2.setPriority(1000);
        command2.setSubmissionOrder(2);

        int compareValue = command1.compareTo(command2);

        Assert.assertEquals("Command 1 should rate as Higher : ", -1, compareValue);

    }

    /**
     * Test to test the BasePriorityAndSubmissionOrder Compare.
     */
    @Test
    public void testPriorityEqualSubmissionPrioritySecondHigher() {

        EcCommand command1 = TestCommandHelper.createCommand(TestCommandHelper.CommandType.TEST_AVP, id1, config);
        command1.setPriority(1000);
        command1.setSubmissionOrder(2);

        EcCommand command2 = TestCommandHelper.createCommand(TestCommandHelper.CommandType.TEST_AVP, id2, config);
        command2.setPriority(1000);
        command2.setSubmissionOrder(1);

        int compareValue = command1.compareTo(command2);

        Assert.assertEquals("Command 1 should rate as Minus : ", 1, compareValue);

    }

    /**
     * Test to test the BasePriorityAndSubmissionOrder Compare.
     */
    @Test
    public void testCompareToNull() {

        EcCommand command1 = TestCommandHelper.createCommand(TestCommandHelper.CommandType.TEST_AVP, id1, config);
        command1.setPriority(1000);
        command1.setSubmissionOrder(2);

        boolean thrown = false;

        int compareValue;
        try {
            compareValue = command1.compareTo(null);
        } catch (NullPointerException e) {
            thrown = true;
        }

        Assert.assertTrue("Null Pointer was raised for null compareTo argument.", thrown);

    }
}
