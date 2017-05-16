package com.electrocomponents.service.message.producer.impl;

import java.util.Map;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.model.domain.message.ECByteMessage;
import com.electrocomponents.model.domain.message.ECMapMessage;
import com.electrocomponents.model.domain.message.ECMessage;
import com.electrocomponents.model.domain.message.ECObjectMessage;
import com.electrocomponents.model.domain.message.ECTextMessage;
import com.electrocomponents.service.core.validation.ValidationUtility;
import com.electrocomponents.service.exception.MessageTransactionSafeServiceException;
import com.electrocomponents.service.message.producer.interfaces.MessageProducerService;
import com.electrocomponents.service.message.producer.interfaces.MessageProducerServiceLocal;
import com.electrocomponents.service.message.producer.interfaces.MessageProducerServiceRemote;

/**<pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Raja Govindharaj
 * Created : 10 Feb 2010 at 11:52:24
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

/***********************************************************************************************************************
 * @author Raja Govindharaj
 */
@Stateless(name = MessageProducerService.SERVICE_NAME)
@Local(MessageProducerServiceLocal.class)
@Remote(MessageProducerServiceRemote.class)
public class MessageProducerServiceBean implements MessageProducerServiceLocal {
    /** Logger for SoaContactHelper. */
    private static final Log LOG = LogFactory.getLog(MessageProducerServiceBean.class);

    /**
     * The sendMessage method will accept the Message object and will identify message attributes and construct a JMS Message to publish
     * into producer.
     * @param message as parameter of com.electrocomponents.model.domain.message.MessageDTO.
     * @return Boolean is meant to notify whether message has been published successfully or not. if returns true, message successfully
     *         sent. if false, message sent failed.
     */
    public Boolean sendMessage(final ECMessage message) {
    	return sendMessage(message, false);
    }

    /**
     * The sendMessage method will accept the Message object and will identify message attributes and construct a JMS Message to publish
     * into producer.
     * @param message as parameter of com.electrocomponents.model.domain.message.MessageDTO.
     * @param transacted If true then message will be sent in a new or existing transaction
     * @return Boolean is meant to notify whether message has been published successfully or not. if returns true, message successfully
     *         sent. if false, message sent failed.
     */
    public Boolean sendMessage(final ECMessage message, final boolean transacted) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("sendMessage started");
        }

        Boolean isMessageSent = Boolean.FALSE;
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        Context ctx = null;

        try {

            validate(message);

            ctx = new InitialContext();

            ConnectionFactory cf = (ConnectionFactory) ctx.lookup(message.getConnectionFactoryJNDIName());

            if (LOG.isDebugEnabled()) {
                LOG.debug("ConnectionFactory created : " + cf);
            }

            Destination destination = (Destination) ctx.lookup(message.getDestinationJNDIName());

            if (LOG.isDebugEnabled()) {
                LOG.debug("Destination created : " + destination);
            }

            connection = cf.createConnection();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Connection created : " + connection);
            }

            session = connection.createSession(transacted, message.getMessageAcknowledgeMode());

            if (LOG.isDebugEnabled()) {
                LOG.debug("Session created : " + session);
            }

            producer = session.createProducer(destination);

            if (LOG.isDebugEnabled()) {
                LOG.debug("MessageProducer created : " + producer);
            }

            Message jmsMessage = covertJMSMessage(message, session);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Message created : " + jmsMessage);
            }

            producer.send(jmsMessage);

            if (LOG.isInfoEnabled()) {
                LOG.debug("Message has successfully been sent for message id : " + jmsMessage.getJMSMessageID());
            }
            isMessageSent = Boolean.TRUE;
        } catch (JMSException jex) {
            LOG.error("Error sending message to queue " + message.getDestinationJNDIName() + " using connection factory " + message.getConnectionFactoryJNDIName());
            isMessageSent = Boolean.FALSE;
            LOG.error("JMSException: " + jex.getMessage(), jex);
            throw new MessageTransactionSafeServiceException("JMSException : " + jex.getMessage());
        } catch (Exception ex) {
            LOG.error("Error sending message to queue " + message.getDestinationJNDIName() + " using connection factory " + message.getConnectionFactoryJNDIName());
            isMessageSent = Boolean.FALSE;
            LOG.error("Exception: " + ex.getMessage(), ex);
            throw new MessageTransactionSafeServiceException("Exception : " + ex.getMessage());
        } catch (Throwable th) {
            LOG.error("Error sending message to queue " + message.getDestinationJNDIName() + " using connection factory " + message.getConnectionFactoryJNDIName());
            isMessageSent = Boolean.FALSE;
            LOG.error("Throwable : " + th.getMessage(), th);
            throw new MessageTransactionSafeServiceException("Throwable : " + th.getMessage());
        } finally {
            /* Releasing resources : Closing session and connection. */
            try {
                if (producer != null) {
                    producer.close();
                    producer = null;
                }
            } catch (Exception ex) {
                LOG.error("Exception while closing the producer. Message: " + ex.getMessage());
            }
            try {
                if (session != null) {
                    session.close();
                    session = null;
                }
            } catch (Exception ex) {
                LOG.error("Exception while closing the session. Message: " + ex.getMessage());
            }
            try {
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (Exception ex) {
                LOG.error("Exception while closing the queue connection. Message: " + ex.getMessage());
            }

            try {
                if (ctx != null) {
                    ctx.close();
                    ctx = null;
                }
            } catch (Exception ex) {
                LOG.error("Exception while closing the naming context : " + ex.getMessage());
            }

        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("sendMessage finished");
        }
        return isMessageSent;
    }

    /**
     * The validate method validates all the parameter which are mandatory.
     * @param ecMessage as EC Message.
     */
    private void validate(final ECMessage ecMessage) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("validate started");
        }
        if (null == ecMessage) {
            LOG.error("Error on Validations : ECMessage object cannot be empty");
            throw new MessageTransactionSafeServiceException("ECMessage object cannot be empty");
        } else {
            ValidationUtility.validateMandatoryParameter("connectionFactoryJNDIName", ecMessage.getConnectionFactoryJNDIName(),
                    MessageProducerServiceBean.class.getName());
            ValidationUtility.validateMandatoryParameter("destinationJNDIName", ecMessage.getDestinationJNDIName(),
                    MessageProducerServiceBean.class.getName());
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("validate finished");
        }
    }

    /**
     * @param message as EC Message.
     * @param session as JMS session.
     * @return Message as JMS message.
     */
    public Message covertJMSMessage(final ECMessage message, final Session session) {
        Message jmsMessge = null;
        if (LOG.isDebugEnabled()) {
            LOG.debug("covertJMSMessage started");
        }
        try {
            if (message.getClass().getName().equals(ECTextMessage.class.getName())) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("ECTextMessage has been identified");
                }
                ECTextMessage textEcTextMessage = (ECTextMessage) message;
                jmsMessge = session.createTextMessage(textEcTextMessage.getTextMessage());
            } else if (message.getClass().getName().equals(ECByteMessage.class.getName())) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("ECByteMessage has been identified");
                }
                ECByteMessage ecBytesMessage = (ECByteMessage) message;
                BytesMessage bytesMessage = session.createBytesMessage();
                bytesMessage.writeBytes(ecBytesMessage.getBytesMessage());
                jmsMessge = bytesMessage;
            } else if (message.getClass().getName().equals(ECObjectMessage.class.getName())) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("ECObjectMessage has been identified");
                }
                ECObjectMessage ecObjectMessage = (ECObjectMessage) message;
                jmsMessge = session.createObjectMessage(ecObjectMessage.getObjectMessage());
            } else if (message.getClass().getName().equals(ECMapMessage.class.getName())) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("ECMapMessage has been identified");
                }
                ECMapMessage ecMaptMessage = (ECMapMessage) message;
                MapMessage mapMessage = session.createMapMessage();
                Map<String, Object> map = ecMaptMessage.getMapMessage();

                Set<String> keys = map.keySet();
                for (String key : keys) {
                    Object object = map.get(key);
                    if (null != object) {
                        if (Number.class.getName().equals(object.getClass().getName())) {
                            Number value = (Number) object;
                            mapMessage.setObject(key, value);
                        } else if (Character.class.getName().equals(object.getClass().getName())) {
                            Character value = (Character) object;
                            mapMessage.setChar(key, value);
                        } else if (Boolean.class.getName().equals(object.getClass().getName())) {
                            Boolean value = (Boolean) object;
                            mapMessage.setBoolean(key, value);
                        } else if (String.class.getName().equals(object.getClass().getName())) {
                            String value = (String) object;
                            mapMessage.setString(key, value);
                        } else {
                            mapMessage.setObject(key, object);
                        }
                    }
                }

                jmsMessge = mapMessage;
            } else {
                LOG.error("Unsupported message has been found : " + message);
                throw new MessageTransactionSafeServiceException("Unsupported ECMessage object has found : " + message.getClass()
                        .getName());
            }

            Map<String, Object> map = message.getHeaderMap();
            Set<String> keys = map.keySet();
            for (String key : keys) {
                Object object = map.get(key);
                if (null != object) {
                    if (Number.class.getName().equals(object.getClass().getName())) {
                        Number value = (Number) object;
                        jmsMessge.setObjectProperty(key, value);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Number value has been set to Header : " + key + "=" + value);
                        }
                    } else if (Boolean.class.getName().equals(object.getClass().getName())) {
                        Boolean value = (Boolean) object;
                        jmsMessge.setBooleanProperty(key, value);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Boolean value has been set to Header : " + key + "=" + value);
                        }
                    } else if (String.class.getName().equals(object.getClass().getName())) {
                        String value = (String) object;
                        jmsMessge.setStringProperty(key, value);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("String value has been set to Header : " + key + "=" + value);
                        }
                    } else {
                        jmsMessge.setObjectProperty(key, object);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Object value has been set to Header : " + key + "=" + object);
                        }
                    }
                }
            }

            String correlationID = message.getCorrelationID();
            if (!StringUtils.isEmpty(correlationID)) {
                jmsMessge.setJMSCorrelationID(correlationID);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("JMS Correlation ID has been set : " + correlationID);
                }
            }

        } catch (JMSException jmsException) {
            LOG.error("JMSException : ", jmsException);
            throw new MessageTransactionSafeServiceException("JMSException : " + jmsException.getMessage());

        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("covertJMSMessage finished");
        }

        return jmsMessge;

    }
}
