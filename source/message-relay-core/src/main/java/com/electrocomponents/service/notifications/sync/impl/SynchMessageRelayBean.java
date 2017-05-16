package com.electrocomponents.service.notifications.sync.impl;

import java.util.Enumeration;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.commons.components.general.io.PropsLoader;
import com.electrocomponents.commons.exception.ElectroException;
import com.electrocomponents.messagerelay.process.MessageProcessor;
import com.electrocomponents.messagerelay.support.BusinessMessage;
import com.electrocomponents.messagerelay.support.ErrorInfo;
import com.electrocomponents.service.notifications.sync.interfaces.SynchMessageRelay;
import com.electrocomponents.service.notifications.sync.interfaces.SynchMessageRelayLocal;
import com.electrocomponents.service.notifications.sync.interfaces.SynchMessageRelayRemote;

/**
 * Copyright (c) Electrocomponents Plc.
 * Author : e0180383
 * Creation Date : 16-Jun-2006
 * Creation Time : 11:26:08
 * IDE : IntelliJ IDEA 5
 * *******************************************************************************************************************
 * Description : A EJB3
 * Stateless Session Bean that can be executed by remote systems to relay BusinessMessages to remote hosts.
 * *******************************************************************************************************************
 * Change History
 * *******************************************************************************************************************
 * * Change * Author   * Date        * Description
 * *******************************************************************************************************************
 * * New    * e0180383 * 16-Jun-2006 * New class created
 * *******************************************************************************************************************
 * * 39202  * e0180383 * 13-Apr-2007 * Add code to allow access to a custom JKS SSL keystore for outgoing HTTPS.
 * *******************************************************************************************************************
 * * GEN-1228*e0161085 * 26-Aug-2010 * Tidied Code.
 * *******************************************************************************************************************
 * @author e0180383
 */
@Stateless(name = SynchMessageRelay.SERVICE_NAME)
@Remote(SynchMessageRelayRemote.class)
@Local(SynchMessageRelayLocal.class)
public class SynchMessageRelayBean implements SynchMessageRelayRemote, SynchMessageRelayLocal {

    private static final String COMMON_PROPERTY_FILENAME = "common.properties";

    /**
     * Log4J.
     */
    private static Log log = LogFactory.getLog(SynchMessageRelayBean.class);

    /**
     * Properties.
     */
    private Properties props;

    /**
     * Do initialisation on SSB construction by the container, called by container due to PostConstruct annotation.
     */
    @PostConstruct
    public synchronized void init() {
        if (log.isDebugEnabled()) {
            log.debug("init : Method Start");
        }

        props = PropsLoader.loadPropertiesFileFromResource(COMMON_PROPERTY_FILENAME);

        // Print the props loaded to log / console at creation...
        if (log.isDebugEnabled()) {
            Enumeration pkeys = props.keys();
            while (pkeys.hasMoreElements()) {
                String pname = (String) pkeys.nextElement();
                String pvalue = props.getProperty(pname);
                log.debug("init : Name=" + pname + ", Value=" + pvalue);
            }
        }

        // Setup SSL TrustStore if configured in properties (used to access certificates when accessing https URLs)

        if (props.getProperty("javax.net.ssl.truststore") != null) {
            log.debug("init : Security Setup : javax.net.ssl.trustStore=" + props.getProperty("javax.net.ssl.truststore"));
            log.debug("init : Security Setup : javax.net.ssl.trustStorePassword=" + props.getProperty("javax.net.ssl.truststorePassword"));
            System.setProperty("javax.net.ssl.trustStorePassword", props.getProperty("javax.net.ssl.truststorePassword"));
            System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        }

        if (log.isDebugEnabled()) {
            log.debug("init : Method Finish");
        }
    }

    /**
     * Process the incoming BusinessMessage.
     * @param message The incoming BusinessMessage object to be processed.
     * @return processed Message.
     */
    public BusinessMessage processMessage(final BusinessMessage message) {
        if (log.isDebugEnabled()) {
            log.debug("processMessage : Method Start");
        }

        BusinessMessage responseMsg = null;
        MessageProcessor processor;

        String senderClassName = message.getProcessorClass();

        try {
            // Instantiate the sender class described in the inbound message.
            processor = (MessageProcessor) Class.forName(senderClassName).newInstance();

            // Send the message.
            responseMsg = processor.processMessage(message);
        } catch (Exception e) {
            if (responseMsg == null) {
                responseMsg = new BusinessMessage();
            }
            String exceptionClassName = e.getClass().getName().substring(e.getClass().getName().lastIndexOf(".") + 1);
            String error = "processMessage : A " + exceptionClassName + " was thrown sending the message";
            if (e instanceof com.electrocomponents.commons.exception.ElectroException) {
                ElectroException ee = (ElectroException) e;
                error += " message was : " + ee.getUserMessage() + ", original exception info was : " + ee.getOriginalException()
                        .getMessage();
            } else {
                error += " message was : " + e.getMessage();
            }
            log.fatal(error);
            responseMsg.setError(new ErrorInfo(ErrorInfo.FATAL, this, "SynchMessageRelayBean." + error));
        } finally {
            responseMsg = MessageProcessor.copyResponseDetailsFromOutboundMsg(message, responseMsg);
            // Set the host ID in the response message.
            responseMsg.setConfigParameter(BusinessMessage.CONFIG_GENERATOR_HOSTNAME, props.getProperty("env.general.hostname.full"));
        }

        if (log.isDebugEnabled()) {
            log.debug("processMessage : Method Finish");
        }
        return responseMsg;
    }
}
