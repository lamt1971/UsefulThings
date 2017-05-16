package com.electrocomponents.messagerelay.process;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.commons.exception.ElectroException;
import com.electrocomponents.messagerelay.support.BusinessMessage;

/**
 * Copyright (c) Electrocomponents Plc.
 * Author         : e0180383
 * Creation Date  : 15-Jun-2006
 * Creation Time  : 16:03:53
 * IDE            : IntelliJ IDEA 5
 * *******************************************************************************************************************
 * Description :
 * Base class for Message processing
 * *******************************************************************************************************************
 * Change History
 * *******************************************************************************************************************
 * * Change   * Author   * Date         * Description
 * *******************************************************************************************************************
 * * New      * e0180383 *  15-Jun-2006 * New class created
 * *******************************************************************************************************************
 * *          * e0161085 *  28-Jun-2006 * Added a set clause to set the Hibernate Connection Factory
 * *******************************************************************************************************************
 * * 10.1ELS  * e0161085 *  22-Jun-2009 * Removed Hibernate Connection Factory.
 * *******************************************************************************************************************
 * * GEN-1228 * e0161085 *  26-Aug-2010 * Tidied Code.
 * *******************************************************************************************************************
 *
 * @author e0180383
 */
public abstract class MessageProcessor {

    /**
     * Log4J.
     */
    private static Log log = LogFactory.getLog(MessageProcessor.class);

    /**
    * Process a BusinessMessage.
    *
    * @param message The BusinessMessage to be processed
    * @return The response from the target system as a BusinessMessage
    * @throws ElectroException Exception.
    **/
    public abstract BusinessMessage processMessage(BusinessMessage message) throws ElectroException;

    /**
    * Copy mandatory configuration from the outbound message to the response message (and nest the outbound message into the inbound).
    *
    * @param outboundMessage The outbound BusinessMessage.
    * @param responseMessage The response BusinessMessage.
    * @return The response BusinessMessage
    **/
    public static BusinessMessage copyResponseDetailsFromOutboundMsg(final BusinessMessage outboundMessage,
            final BusinessMessage responseMessage) {
        if (log.isDebugEnabled()) {
            log.debug("copyResponseDetailsFromOutboundMsg : Method Start");
        }

        // Nest the outbound message back in the response message so we can pick details out of it when we process the response..
        responseMessage.setOutboundMessage(outboundMessage);

        // Copy some core core attributes across.
        if (outboundMessage.getConfigParameter(BusinessMessage.CONFIG_TARGET_URL) != null) {
            responseMessage.setConfigParameter(BusinessMessage.CONFIG_SENDER_URL, outboundMessage.getConfigParameter(
                    BusinessMessage.CONFIG_TARGET_URL));
        } else {
            if (log.isDebugEnabled()) {
                log.debug("copyResponseDetailsFromOutboundMsg :"
                        + " outboundMessage.getConfigParameter(BusinessMessage.CONFIG_TARGET_URL) = null");
            }
        }

        if (outboundMessage.getConfigParameter(BusinessMessage.CONFIG_PAYLOAD_JAVA_CHARSET) != null) {
            responseMessage.setConfigParameter(BusinessMessage.CONFIG_PAYLOAD_JAVA_CHARSET, outboundMessage.getConfigParameter(
                    BusinessMessage.CONFIG_PAYLOAD_JAVA_CHARSET));
        } else {
            if (log.isDebugEnabled()) {
                log.debug("copyResponseDetailsFromOutboundMsg :"
                        + " outboundMessage.getConfigParameter(BusinessMessage.CONFIG_PAYLOAD_JAVA_CHARSET) = null");
            }
        }

        if (outboundMessage.getConfigParameter(BusinessMessage.CONFIG_PERSISTANCE_FILENAME) != null) {
            responseMessage.setConfigParameter(BusinessMessage.CONFIG_PERSISTANCE_FILENAME, outboundMessage.getConfigParameter(
                    BusinessMessage.CONFIG_PERSISTANCE_FILENAME));
        } else {
            if (log.isDebugEnabled()) {
                log.debug("copyResponseDetailsFromOutboundMsg :"
                        + " outboundMessage.getConfigParameter(BusinessMessage.CONFIG_PERSISTANCE_FILENAME) = null");
            }
        }

        if (outboundMessage.getConfigParameter(BusinessMessage.CONFIG_TRANSACTION_FORMAT) != null) {
            responseMessage.setConfigParameter(BusinessMessage.CONFIG_TRANSACTION_FORMAT, outboundMessage.getConfigParameter(
                    BusinessMessage.CONFIG_TRANSACTION_FORMAT));
        } else {
            if (log.isDebugEnabled()) {
                log.debug("copyResponseDetailsFromOutboundMsg :"
                        + " outboundMessage.getConfigParameter(BusinessMessage.CONFIG_TRANSACTION_FORMAT) = null");
            }
        }

        if (outboundMessage.getConfigParameter(BusinessMessage.CONFIG_TRANSACTION_TYPE) != null) {
            responseMessage.setConfigParameter(BusinessMessage.CONFIG_TRANSACTION_TYPE, outboundMessage.getConfigParameter(
                    BusinessMessage.CONFIG_TRANSACTION_TYPE));
        } else {
            if (log.isDebugEnabled()) {
                log.debug("copyResponseDetailsFromOutboundMsg : "
                        + "outboundMessage.getConfigParameter(BusinessMessage.CONFIG_TRANSACTION_TYPE) = null");
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("copyResponseDetailsFromOutboundMsg : response message class is " + outboundMessage.getResponseProcessorClass());
        }
        responseMessage.setProcessorClass(outboundMessage.getResponseProcessorClass());

        if (log.isDebugEnabled()) {
            log.debug("copyResponseDetailsFromOutboundMsg : Method Finish");
        }
        return responseMessage;
    }
}
