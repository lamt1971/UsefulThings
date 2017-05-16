package com.electrocomponents.messagerelay.asynch;

import java.util.Enumeration;
import java.util.Properties;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.commons.components.general.io.PropsLoader;
import com.electrocomponents.commons.exception.ElectroException;
import com.electrocomponents.messagerelay.process.MessageProcessor;
import com.electrocomponents.messagerelay.support.BusinessMessage;
import com.electrocomponents.messagerelay.support.ErrorInfo;
import com.electrocomponents.messagerelay.support.MessageRelaySupport;
import com.electrocomponents.model.domain.message.ECObjectMessage;
import com.electrocomponents.service.core.client.ServiceLocator;
import com.electrocomponents.service.message.producer.interfaces.MessageProducerService;

/**
 * Copyright (c) Electrocomponents Plc.
 * Author         : e0180383
 * Creation Date  : 12-Jun-2006
 * Creation Time  : 14:14:22
 * IDE            : IntelliJ IDEA 5
 * *******************************************************************************************************************
 * Description :
 * A EJB3 Message Driven Bean which reads BusinessMessage objects (eInvoices / PORs etc) from a specifed JMS queue
 * instantiates the correct MessageProcessor and then, if required forward the processors response via another JMS queue
 * *******************************************************************************************************************
 * Change History
 * *******************************************************************************************************************
 * * Change   * Author   * Date         * Description
 * *******************************************************************************************************************
 * * New      * e0180383 *  12-Jun-2006 * New class created
 * *******************************************************************************************************************
 * * New      * e0161085 *  30-Jun-2006 * Added connection to database.
 * *******************************************************************************************************************
 * * 39202    * e0180383 *  13-Apr-2007 * Add code to allow access to a custom JKS SSL keystore for outgoing HTTPS.
 * *******************************************************************************************************************
 * * 10.1ELS  * e0161085 *  22-Jun-2009 * Removed EJB Create and added additional error handling / logging.
 * *******************************************************************************************************************
 * * 10.1ELS  * e0161085 *  07-Jul-2009 * Added more resilince.
 * *******************************************************************************************************************
 *
 * @author e0180383

  YOU MUST DEFINE THE FOLLOWING  <env-entry> ENVIRONMENT ENTRIES IN THE ejb-jar.xml DEPLOYMENT DESCRIPTOR


  deploymentMode    = Can be "RELAY" or "RECEIVER".
  inboundQueueName  = The fully qualifed inbound queue name on the JMS queue host.

  OPTIONAL PROPS ENTRIES

  conf.errors.notificationMethod = Switch between JMS and EMAIL notification of errors - defaults to EMAIL.

 */

public class AsynchMessageConsumer extends MessageRelaySupport implements MessageDrivenBean, MessageListener {

    private static final String COMMON_PROPERTY_FILENAME = "common.properties";

    private static final String MESSAGE_RELAY_PROPERTY_FILENAME = "messagerelay.properties";

    /**
    * Log4J.
    */
    private static Log log = LogFactory.getLog(AsynchMessageConsumer.class);

    /**
    * Message Driven Context.
    */
    private MessageDrivenContext ctx;

    /**
    * System Properties.
    */
    private Properties props;

    /**
    * Set a member variable to indicate the deloyment mode..
    *
    * The word "RECEIVER" implies that we are running internally,(i.e. INSIDE the firewall) and
    *  simply are processing messages (writing to disk / updating DB etc.
    *
    * The word "RELAY" implies that we are running in the DMZ,(i.e. OUTSIDE the firewall) and
    *  are forwarding messages to external clients and then returning the client responses to the internal reciever.
    *
    */
    private String deploymentMode = null;

    /**
    *  The name of the JMS ConnectionFactory which we will use to return response messages from external
    *  clients if necessary (only when in RELAY mode).
    */
    private String jmsConnectionFactory = null;

    /**
    *  The name of the JMS queue which we will use to return response messages from external clients if necessary (only when in RELAY mode).
    */
    private String inboundQueueName;

    /**
     * Clean up DB connectivity on MDB destruction by container.
     * @throws EJBException Exception.
     */
    public void ejbRemove() throws EJBException {
        if (log.isDebugEnabled()) {
            log.debug("ejbRemove : Method Start");
        }

        log.info("ejbRemove : MDB destroyed by container...");

        if (log.isDebugEnabled()) {
            log.debug("ejbRemove: Method Finish");
        }
    }

    /**
    * Called by container when JMS messages arrives.
    * @param m Message.
    */
    public void onMessage(final Message m) {
        if (log.isDebugEnabled()) {
            log.debug("onMessage : Method Start");
        }
        processMessage(m);
        if (log.isDebugEnabled()) {
            log.debug("onMessage : Method Finish");
        }
    }

    /**
    * Process the incoming BusinessMessage.
    * @param m The incoming JMS Message object.
    */
    private void processMessage(final Message m) {
        if (log.isDebugEnabled()) {
            log.debug("processMessage1 : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("processMessage : Version 18");
        }

        BusinessMessage message = null;
        BusinessMessage responseMsg = new BusinessMessage();
        MessageProcessor processor;

        props = PropsLoader.mergeProperties(PropsLoader.loadPropertiesFileFromResource(COMMON_PROPERTY_FILENAME), PropsLoader
                .loadPropertiesFileFromResource(MESSAGE_RELAY_PROPERTY_FILENAME));

        // Print the props loaded to log / console at startup...
        Enumeration pkeys = props.keys();
        while (pkeys.hasMoreElements()) {
            String pname = (String) pkeys.nextElement();
            String pvalue = props.getProperty(pname);
            log.info("loadProperties : Name=" + pname + ", Value=" + pvalue);
        }

        boolean relayErrorsViaJms = "JMS".equalsIgnoreCase(props.getProperty("conf.errors.notificationMethod"));

        if (log.isDebugEnabled()) {
            log.debug("processMessage :Mode is  " + deploymentMode);
        }
        if ("RELAY".equalsIgnoreCase(deploymentMode)) {
            if (props.getProperty("javax.net.ssl.trustStore") != null && props.getProperty("javax.net.ssl.trustStorePassword") != null) {
                log.debug("init : Security Setup : javax.net.ssl.trustStore=" + props.getProperty("javax.net.ssl.trustStore"));
                log.debug("init : Security Setup : javax.net.ssl.trustStorePassword=" + props.getProperty(
                        "javax.net.ssl.trustStorePassword"));

                System.setProperty("javax.net.ssl.trustStore", props.getProperty("javax.net.ssl.trustStore"));
                System.setProperty("javax.net.ssl.trustStorePassword", props.getProperty("javax.net.ssl.trustStorePassword"));
                System.setProperty("javax.net.ssl.trustStoreType", "JKS");
            }

        }

        try {
            ObjectMessage om = (ObjectMessage) m;

            if (log.isDebugEnabled()) {
                log.debug("processMessage : Process Step 1");
            }

            message = (BusinessMessage) om.getObject();

            // Instantiate the processor class described in the inbound message.
            String processorClassName = message.getProcessorClass();

            if (processorClassName == null) {
                log.fatal("processMessage : Fatal Error, processorClassName was not set in the BusinessMessage ");
            }

            processor = (MessageProcessor) Class.forName(processorClassName).newInstance();

            // Send the message.
            responseMsg = processor.processMessage(message);

        } catch (Exception e) {
            String exceptionClassName = e.getClass().getName().substring(e.getClass().getName().lastIndexOf(".") + 1);
            String error = "processMessage : A " + exceptionClassName + " was thrown processing the BusinessMessage,";
            if (e instanceof com.electrocomponents.commons.exception.ElectroException) {
                ElectroException ee = (ElectroException) e;
                error += " error message was : " + ee.getUserMessage() + ",original exception info was : " + ee.getOriginalException()
                        .getMessage();
            } else {
                error += " error message was : " + e.getMessage();
            }
            log.fatal(error);
            responseMsg.setError(new ErrorInfo(ErrorInfo.FATAL, this, "AsynchMessageConsumer." + error));

        } finally {

            // JB: this finally block is quite broken - it's executed even if the preceding code fails, leading to
            // null point exceptions if 'message' is null. Why is it in a finally block anyway?

            // this method returns a BusinessMessage object, but it also populates the one passed in as a side-effect. Why?
            // Why do we discard the return value here?
            MessageProcessor.copyResponseDetailsFromOutboundMsg(message, responseMsg);

            // Set the host ID in the response message.
            responseMsg.setConfigParameter(BusinessMessage.CONFIG_GENERATOR_HOSTNAME, props.getProperty("env.general.hostname.full"));
            if (log.isDebugEnabled()) {
                log.debug("processMessage : Number of errors is " + responseMsg.getErrors().size());
            }
            boolean errorsFound = responseMsg.getErrors().size() > 0;

            // If any errors occurred and props are set to notify errors via eMail
            if (errorsFound) {
                MessageRelaySupport mrs = new MessageRelaySupport();
                mrs.emailErrorToAdmin(responseMsg, props);
            }

            // If deployed in RELAY mode and requested, return the response from the
            // client system to the caller via JMS (or send back errors via JMS if so configured).

            // JB: If the preceding code fails 'message' may be null here
            if ("RELAY".equalsIgnoreCase(deploymentMode) && message.getResponseProcessorClass() != null && (!errorsFound || (errorsFound
                    && relayErrorsViaJms))) {
                if (log.isDebugEnabled()) {
                    log.debug("processMessage : Returning response to caller via JMS");
                }

                MessageProducerService messageProducer = ServiceLocator.locateLocal(MessageProducerService.class);
                ECObjectMessage ecObjectMessage = new ECObjectMessage();
                ecObjectMessage.setObjectMessage(responseMsg);
                ecObjectMessage.setConnectionFactoryJNDIName(jmsConnectionFactory);
                ecObjectMessage.setDestinationJNDIName(inboundQueueName);
                messageProducer.sendMessage(ecObjectMessage, true);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("processMessage : Method Finish");
        }
    }

    /**
    * setMessageDrivenContext.
    * @param ctx context.
    */
    public void setMessageDrivenContext(final MessageDrivenContext ctx) {
        this.ctx = ctx;
    }

    /**
    * Set the name of the Inbound Queue.
    * @param inboundQueueName The inbound Queue Name.
    */
    public void setInboundQueueName(final String inboundQueueName) {
        this.inboundQueueName = inboundQueueName;
    }

    /**
    * Get the name of the inbound queue from the ejb-jar.xml file.
    * @return The inbound queue.
    */
    public String getInboundQueueName() {
        return inboundQueueName;

    }

    /**
    * @return the deploymentMode
    */

    public String getDeploymentMode() {
        return deploymentMode;
    }

    /**
    * @param deploymentMode the deploymentMode to set
    */

    public void setDeploymentMode(final String deploymentMode) {
        this.deploymentMode = deploymentMode;
    }
}
