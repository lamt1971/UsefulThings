package com.electrocomponents.bridgeprocess.service.core.impl;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.MessageDrivenContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.bridgeprocess.exception.InterfacesException;
import com.electrocomponents.bridgeprocess.service.core.interfaces.InterfaceService;
import com.electrocomponents.bridgeprocess.service.core.interfaces.InterfacesLoggingService;
import com.electrocomponents.model.data.linelevel.EcInterfacesMessageFailureEntity;
import com.electrocomponents.model.domain.DateTime;
import com.electrocomponents.model.domain.linelevel.EcInterfaceMessageFailure;
import com.electrocomponents.service.core.client.ServiceLocator;

/**
 * ProductBridgeMessageBean processes the messages which come on the MQSeries queue from ADAMs.
 * @author C0950079
 */

public class EcProductBridgeServiceMDB implements MessageListener {
    /** Log LOG. * */
    private static final Log LOG = LogFactory.getLog(EcProductBridgeServiceMDB.class);

    /**
     * serviceFQN - the fully qualified name of the business interface which will process this message.
     */
    @Resource(name = "serviceFQN")
    private String serviceFQN;

    /** MessageSoruce Resource injection.   */
    @Resource(name = "messageSource")
    private String messageSource;

    /**  MessageType Resource injection.  */
    @Resource(name = "messageType")
    private String messageType;

    /** Message Driven Context - Used for setting roll-back on transaction. * */
    @Resource
    private MessageDrivenContext messageDrivenContext;

    /** Constructor. */
    public EcProductBridgeServiceMDB() {
    }

    /**
     * This Method takes a Message object as an argument, parses the message and calls appropriate service method of stateless session bean depending upon the message type received.
     * @param jmsMessage 
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void onMessage(final Message jmsMessage) {
        consumeMessage(serviceFQN, jmsMessage);
    }

    /**
     * This Method additionally takes in the serviceFQN that will be used by the System Insight to monitor statistics on how various messages are being consumed by different components
     * that use this facade.
     * @param jmsMessage 
     * @param serviceFQN
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void consumeMessage(final String serviceFQN, final Message jmsMessage) {

        TextMessage textMessage = null;
        String messageId = null;
        String xmlMessage = null;
        boolean isRedelivery = false;
        InterfacesLoggingService loggingService = null;

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Start onMessage.");
            }

            messageId = jmsMessage.getJMSMessageID();

            LOG.info(this.messageSource + " - " + this.messageType + " MESSAGE Received :" + messageId);

            loggingService = ServiceLocator.locateLocal(InterfacesLoggingService.class);

            if (jmsMessage instanceof TextMessage || jmsMessage instanceof MapMessage) {
                isRedelivery = jmsMessage.getJMSRedelivered();

                if (jmsMessage instanceof TextMessage) {
                    textMessage = (TextMessage) jmsMessage;
                    xmlMessage = textMessage.getText();
                    saveMessage(xmlMessage);
                } else {
                    MapMessage mapMessage = (MapMessage) jmsMessage;
                    saveMapMessage(mapMessage);
                }

                LOG.info(this.messageSource + " - " + this.messageType + " MESSAGE Processed :" + messageId);

            } else {

                if (LOG.isInfoEnabled()) {
                    LOG.info("WARNING: NON-TEXT/MAP Message received!\n" + jmsMessage.getClass());
                }
                EcInterfaceMessageFailure failure = new EcInterfacesMessageFailureEntity();
                failure.setMessage((xmlMessage != null ? xmlMessage : jmsMessage.toString()));
                failure.setErrrorText("NON-TEXT/MAP JMS message received! Further processing not possible.\n MSG ID: " + messageId + "\n");
                failure.setMessageSource(messageSource);
                failure.setMessageType(messageType);
                loggingService.logErrorMessage(failure);

            }
        } catch (final JMSException e) {
            if (!this.messageDrivenContext.getRollbackOnly()) {
                this.messageDrivenContext.setRollbackOnly();
            }
            // Logging the full stack trace whenever we are not logging the
            // exception in database using error log service.
            if (LOG.isErrorEnabled()) {
                LOG.error("JMSException  while processing Messgae :" + e.getMessage() + " JMSMessageID=" + messageId, e);
            }
        } catch (RuntimeException rex) {

            LOG.error("Runtime exception encountered while processing message." + "JMSMessageID=" + messageId, rex);

            logRetryFailure(rex, jmsMessage, isRedelivery, xmlMessage, loggingService, messageId);

            if (!this.messageDrivenContext.getRollbackOnly()) {
                this.messageDrivenContext.setRollbackOnly();
            }
        } catch (Exception ex) {

            LOG.error("Exception encountered while processing message." + "JMSMessageID=" + messageId, ex);

            if (!this.messageDrivenContext.getRollbackOnly()) {
                this.messageDrivenContext.setRollbackOnly();
            }
        } catch (Throwable t) {
            LOG.error("Throwable encountered while processing message." + "JMSMessageID=" + messageId, t);

            t.printStackTrace();
            if (!this.messageDrivenContext.getRollbackOnly()) {
                this.messageDrivenContext.setRollbackOnly();
            }
        }
    }

    /**
     * The logRetryFailure will log the error into database after retry failed.
     * @param rex as RuntimeException.
     * @param jmsMessage as Message.
     * @param isRedelivery as re-delivery flag.
     * @param xmlMessage as String Message.
     * @param loggingService as Logging Service.
     * @param messageId as Message Id.
     */
    private void logRetryFailure(final RuntimeException rex, final Message jmsMessage, final Boolean isRedelivery, final String xmlMessage,
            final InterfacesLoggingService loggingService, final String messageId) {
        if (!isRedelivery) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Runtime exception. Message will be attempted to be redelivered again. Message Details are:");
                LOG.error(jmsMessage);
                LOG.error("Full Message Content: \n" + xmlMessage + "\n");
            }
            if (rex.getCause() instanceof InterfacesException) {
                InterfacesException exception = (InterfacesException) rex.getCause();
                loggingService.logErrorMessage(exception.getMessageFailureEntity());
            } else {
                EcInterfaceMessageFailure failure = new EcInterfacesMessageFailureEntity();
                failure.setMessage((xmlMessage != null ? xmlMessage : jmsMessage.toString()));
                failure.setReceivedDate(new DateTime(new Date()));
                failure.setErrrorText("MSG ID: " + messageId + "\n" + rex.getMessage() + "\n" + InterfacesException.getStacktrace(rex));
                failure.setMessageSource(messageSource);
                failure.setMessageType(messageType);
                loggingService.logErrorMessage(failure);
            }
        }
    }

    /**
     * @param mapMessage mapMessage takes a Message object as argument parses the message and calls appropriate service method of stateless
     * session bean depending upon the message type received and also creates a audit log/error log depending upon whether the process was
     * successful
     */
    @SuppressWarnings("unchecked")
    private void saveMapMessage(final MapMessage mapMessage) {
        try {           
            InterfaceService<MapMessage> service = ServiceLocator.locateLocal(loadServiceClass(serviceFQN));
            service.processBridgeMessage(mapMessage);
        } catch (RuntimeException rex) {
            // Error logging to be done by Caller as per the message redelivery value.
            throw rex;
        } catch (final Exception e) {
            if (!this.messageDrivenContext.getRollbackOnly()) {
                this.messageDrivenContext.setRollbackOnly();
            }
            if (LOG.isInfoEnabled()) {
                LOG.info("Exception while processing MESSAGE :" + e.getMessage());
            }
        }
    }

    /**
     * @param xmlMessage xmlMessage takes a Message object as argument parses the message and calls appropriate service method of stateless
     * session bean depending upon the message type received and also creates a audit log/error log depending upon whether the process was
     * successful
     */
    @SuppressWarnings("unchecked")
    private void saveMessage(final String xmlMessage) {
        try {
            InterfaceService<String> service = ServiceLocator.locateLocal(loadServiceClass(serviceFQN));
            service.processBridgeMessage(xmlMessage);
        } catch (RuntimeException rex) {
            // Error logging to be done by Caller as per the message redelivery value.
            throw rex;
        } catch (final Exception e) {
            if (!this.messageDrivenContext.getRollbackOnly()) {
                this.messageDrivenContext.setRollbackOnly();
            }
            if (LOG.isInfoEnabled()) {
                LOG.info("Exception while processing MESSAGE :" + e.getMessage());
            }
        }
    }
    
    /**
     * Load the Class object for the provided FQN.
     * @param fqn
     * @return
     * @throws ClassNotFoundException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Class<? extends InterfaceService> loadServiceClass(String fqn) throws ClassNotFoundException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Looking up InterfaceService class " + serviceFQN);
        }
        Class<? extends InterfaceService> serviceClass = (Class<? extends InterfaceService>) Class.forName(serviceFQN);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieved class " + serviceClass.getName() + " for FQN " + serviceFQN);
        }
        return serviceClass;
    }
}
