package com.electrocomponents.messagerelay.support;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.commons.email.SendMailLite;

/**
 * Copyright (c) Electrocomponents Plc.
 * Author         : e0180383
 * Creation Date  : 19-Jul-2006
 * Creation Time  : 13:52:58
 * IDE            : IntelliJ IDEA 5
 * *******************************************************************************************************************
 * Description :
 * Various support methods for the messagerelay system
 * *******************************************************************************************************************
 * Change History
 * *******************************************************************************************************************
 * * Change   * Author   * Date         * Description
 * *******************************************************************************************************************
 * * New      * e0180383 *  19-Jul-2006 * New class created
 * *******************************************************************************************************************
 * * 9.3      * e0161085 *  07-Apr-2009 * Tidied to Stylesheet requirements.
 * *******************************************************************************************************************
 * * 10.1     * e0161085 *  07-Jul-2009 * Added JMSJNDI Properties.
 * *******************************************************************************************************************
 *
 * @author e0180383
 */
public class MessageRelaySupport {

    /**
     * Log4J.
     */
    private static Log log = LogFactory.getLog(MessageRelaySupport.class);

    private static final String CONTENT_TYPE = "text/plain";

    private static final String CHAR_SET = "iso-8859-1";

    /**
    * Echo the incoming message to the RELAY host log..
    *
    * @param responseMessage the incoming reponse message contains details of the error that occurred
    * @param props Properties oject containing necessary environment specific information (email SMTP server name etc)
    */
    public void emailErrorToAdmin(final BusinessMessage responseMessage, final Properties props) {
        if (log.isDebugEnabled()) {
            log.debug("emailErrorToAdmin : Method Start");
        }

        List emailContent = new LinkedList();

        emailContent.add("ELECTROCOMPONENTS ECOMMERCE BUSINESS MESSAGING SYSTEM");
        emailContent.add("\n------------------ Error Email v1.0 -----------------");
        emailContent.add("\nAn error occurred Message Relay system running on the " + props.getProperty("env.general.hostname.full")
                + " server.");
        emailContent.add("\nMessage Initiator Host : " + responseMessage.getOutboundMessage().getConfigParameter(
                BusinessMessage.CONFIG_GENERATOR_HOSTNAME) + "\n");
        emailContent.add("\nMessage Relay Host     : " + responseMessage.getConfigParameter(BusinessMessage.CONFIG_GENERATOR_HOSTNAME)
                + "\n\n");
        emailContent.add("\nThe original, OUTBOUND MESSAGE was :-");
        emailContent.add("\n\n" + responseMessage.getOutboundMessage().toString());
        emailContent.add("\n\nThe RESPONSE MESSAGE, including error detail was :-");
        emailContent.add("\n\n" + responseMessage.toString());

        final String adminToEmailAddress = props.getProperty("env.email.adminEmailAddress");
        final String fromEmailAddress = props.getProperty("env.email.adminEmailAddress");
        final String emailSmtpHost = props.getProperty("env.email.smtpEmailHost");
        final String subject = "Message Relay System Error On " + props.getProperty("env.general.hostname.full");

        SendMailLite.send(emailSmtpHost, new String[] { adminToEmailAddress }, new String[] {}, new String[] {}, fromEmailAddress, subject,
                emailContent.toString(), CONTENT_TYPE, CHAR_SET, null);

        if (log.isDebugEnabled()) {
            log.debug("emailErrorToAdmin : Method Finish");
        }
    }
}
