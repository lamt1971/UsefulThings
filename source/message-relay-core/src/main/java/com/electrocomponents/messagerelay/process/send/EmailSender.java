package com.electrocomponents.messagerelay.process.send;

import com.electrocomponents.messagerelay.support.BusinessMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Copyright (c) Electrocomponents Plc.
 * Author         : e0180383
 * Creation Date  : 15-Jun-2006
 * Creation Time  : 16:04:35
 * IDE            : IntelliJ IDEA 5
 * *******************************************************************************************************************
 * Description :
 * Send a message as an email.
 * *******************************************************************************************************************
 * Change History
 * *******************************************************************************************************************
 * * Change   * Author   * Date         * Description
 * *******************************************************************************************************************
 * * New      * e0180383 *  15-Jun-2006 * New class created
 * *******************************************************************************************************************
 * * GEN-1228 * e0161085 *  26-Aug-2010 * Tidied Code.
 * *******************************************************************************************************************
 *
 * @author e0180383
 */
public class EmailSender extends Sender {

    /**
     * Log4J.
     */
    private static Log log = LogFactory.getLog(EmailSender.class);

    /**
     * Process the Message.
     * @param message The Business Message.
     * @return Response Message.
     */
    public BusinessMessage processMessage(final BusinessMessage message) {
       BusinessMessage responseMsg = new BusinessMessage();
       //todo implement!!
       return responseMsg;
    }
}
