package com.electrocomponents.messagerelay.process.persist;

import com.electrocomponents.messagerelay.process.MessageProcessor;
import com.electrocomponents.messagerelay.support.BusinessMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

/**
 * Copyright (c) Electrocomponents Plc.
 * Author         : e0180383
 * Creation Date  : 22-Jun-2006
 * Creation Time  : 14:49:50
 * IDE            : IntelliJ IDEA 5
 * *******************************************************************************************************************
 * Description :
 * Simple test class for testing the Business Messaging system. Prints the message on the RELAY hosts log.
 * *******************************************************************************************************************
 * Change History
 * *******************************************************************************************************************
 * * Change   * Author   * Date         * Description
 * *******************************************************************************************************************
 * * New      * e0180383 *  22-Jun-2006 * New class created
 * *******************************************************************************************************************
 * * GEN-1228 * e0161085 *  26-Aug-2010 * Tidied Code.
 * *******************************************************************************************************************
 *
 * @author e0180383
 */
public class LoopBackProcessor extends MessageProcessor {

    /**
     * Log4J.
     */
    private static Log log = LogFactory.getLog(LoopBackProcessor.class);

   /**
    * Echo the incoming message to the RELAY host log..
    *
    * @param message the incoming mesage to be echoed to the log.
    * @return A test response message.
    */
    public BusinessMessage processMessage(final BusinessMessage message) {

       log.info("processMessage : INCOMING MESSAGE :- \n\n" + message.toString());

       message.setConfigParameter("TEST_CONFIG_PARAM_ADDED_BY_LOOPBACK", "!! Message Looped Back at " + new Date().toString());

       log.info("processMessage : REPONSE MESSAGE :- \n\n" + message.toString());

       return message;
    }


}
