package com.electrocomponents.continuouspublishing.service.impl;

import javax.annotation.Resource;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import com.electrocomponents.continuouspublishing.exception.AuditLogException;
import com.electrocomponents.continuouspublishing.exception.MessageParsingException;
import com.electrocomponents.continuouspublishing.service.interfaces.AuditLogService;
import com.electrocomponents.continuouspublishing.service.interfaces.ErrorLogService;
import com.electrocomponents.continuouspublishing.service.interfaces.HierarchyObjectService;
import com.electrocomponents.continuouspublishing.service.interfaces.ImageObjectService;
import com.electrocomponents.continuouspublishing.service.interfaces.InfoObjectService;
import com.electrocomponents.continuouspublishing.service.interfaces.ProductService;
import com.electrocomponents.continuouspublishing.service.interfaces.TableObjectService;
import com.electrocomponents.continuouspublishing.utility.DOMParserUtility;
import com.electrocomponents.continuouspublishing.utility.NameValueMapping;
import com.electrocomponents.continuouspublishing.utility.RegionUtility;
import com.electrocomponents.model.data.usermessaging.MailSettings;
import com.electrocomponents.model.domain.Locale;
import com.electrocomponents.productcontentwriter.JndiConstants;
import com.electrocomponents.service.core.client.ServiceLocator;
import com.electrocomponents.service.core.usermessaging.interfaces.UserMessagingService;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Semwal,Yogesh Patil
 * Created : 23 Aug 2007 at 12:00:00
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *          * Yogesh Patil    * 26 Mar 2008         * Added code to set rollback for failed transactions
 *                                                    Added EJB context for doing this.
 *          *                 *                     *
 *          * Yogesh Patil    * 31 Mar 20008        * Added method putMessageOnEmailQueue to send the broken
 *                                                  * messages through email using UserMessagingService.
 *          *                 *                     *
 *          * Yogesh Patil    * 30 Apr 20008        * Added method call to process packaged variants of product
 *                                                  * in saveMessage method.
 *          *                 *                     *
 * ************************************************************************************************
 * ************************************************************************************************
 */

/**
 * ContinuousPublishingMessageBean processes the messages which come on the MQSeries queue.
 * @author sanjay semwal,Yogesh Patil
 */
@MessageDriven(name = "ContinuousPublishingMessageBean")
public class ContinuousPublishingMessageBean implements MessageListener {

    /** ErrorLogService errorLogService. * */
    private ErrorLogService errorLogService;

    /** AuditLogService auditLogService. * */
    private AuditLogService auditLogService;

    /** ProductService productService. * */
    private ProductService productService;

    /** InfoObjectService infoObjectService. * */
    private InfoObjectService infoObjectService;

    /** ImageObjectService imageObjectService. * */
    private ImageObjectService imageObjectService;

    /** HeirarchyObjectService heirarchyObjectService. * */
    private HierarchyObjectService heirarchyObjectService;

    /** TableObjectService tableObjectService. * */
    private TableObjectService tableObjectService;

    /** Log LOG. * */
    private Log logger = LogFactory.getLog(ContinuousPublishingMessageBean.class);

    /** Message Driven Context - Used for setting rollback on transaction. * */
    @Resource
    private MessageDrivenContext messageDrivenContext;

    /**
     * Email address on which a mail will be sent in case a broken messages arrives.
     */
    @Resource(name = "message_email_failure_to")
    private String messageEmailFailureTo;

    /** The email address which appears in the FROM field of the broken email message. * */
    @Resource(name = "message_email_failure_from")
    private String messageEmailFailureFrom;


    
    /**
     * In the contructor all the services which are used in the onMessage method are initilized to optimise resources while looking up for
     * the services.
     */
    public ContinuousPublishingMessageBean() {
        errorLogService = ServiceLocator.locateLocal(ErrorLogService.class);
        auditLogService = ServiceLocator.locateLocal(AuditLogService.class);
        productService = ServiceLocator.locateLocal(ProductService.class);
        infoObjectService = ServiceLocator.locateLocal(InfoObjectService.class);
        imageObjectService = ServiceLocator.locateLocal(ImageObjectService.class);
        heirarchyObjectService = ServiceLocator.locateLocal(HierarchyObjectService.class);
        tableObjectService = ServiceLocator.locateLocal(TableObjectService.class);
    }
 
    /**
     * @param jmsMessage This Method takes a Message object as argument parses the message and calls appropriate service method of stateless
     * session bean depending upon the message type received and also creates a audit log/error log depending upon whether the process was
     * successful
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void onMessage(final Message jmsMessage) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of onMessage");
        }
        Document document = null;
        TextMessage textMessage = null;
        String message = null;
        String messageId = null;
        try {
            if (jmsMessage instanceof TextMessage) {
                textMessage = (TextMessage) jmsMessage;

                message = textMessage.getText();
                messageId = jmsMessage.getJMSMessageID();
                document = DOMParserUtility.getDomObject(message);
                String region = RegionUtility.getRegion(document);
                final String jndiNameEurope = JndiConstants.ENTITY_MANAGER_JNDI_NAME_EMEA;
                final String jndiNameNonEurope = JndiConstants.ENTITY_MANAGER_JNDI_NAME_APAC;
                String jndiNameUsed = "";

                if (region.equalsIgnoreCase("EUROPE")) {
                    jndiNameUsed = jndiNameEurope;
                } else if (region.equalsIgnoreCase("NONEUROPE")) {
                    jndiNameUsed = jndiNameNonEurope;
                }
                if (region.equalsIgnoreCase("EUROPE") || region.equalsIgnoreCase("NONEUROPE")) {
                    saveMessage(document, message, jndiNameUsed);
                } else if (region.equalsIgnoreCase("INTERNATIONAL")) {
                    saveMessage(document, message, jndiNameEurope);
                    saveMessage(document, message, jndiNameNonEurope);
                }
            } else {
                logger.info("Invalid message received :" + messageId);
            }
        } catch (final JMSException e) {
            if (!this.messageDrivenContext.getRollbackOnly()) {
                this.messageDrivenContext.setRollbackOnly();
            }

            // Logging the full stack trace whenever we are not logging the
            // exception in database using error log service.

            logger.error("JMSException  while processing Messgae :" + messageId, e);

        } catch (final MessageParsingException e) {

            logger.error("MessageParsingException while processing message:" + "\n", e);

            if (!this.messageDrivenContext.getRollbackOnly()) {
                this.messageDrivenContext.setRollbackOnly();
            }
            // Note:Both cwp and web/domain-service needs to be running on
            // same verison of jboss for this to work.
            // Comment out the try -- catch block below to stop sending
            // emails.
            // DEPENDECIES ON THIS CODE : Line 202 should not be commented,
            // as it logs the error in db.
            try {
                if (!jmsMessage.getJMSRedelivered()) {
                    StringBuilder errorMsg = new StringBuilder();
                    errorMsg.append(e.toString());
                    if (e.getCause() != null) {
                        errorMsg.append("<br/>").append("<b>Cause :</b><br/>").append(e.getCause().toString());
                    }
                    putMessageOnEmailQueue(errorMsg.toString(), message, messageId);
                    errorLogService.logErrorMessage(message);
                }
            } catch (JMSException jex) {
                if (logger.isErrorEnabled()) {
                    logger.error("JMSException  while getting redelivered property :" + e.getMessage() + " JMSMessageID=" + messageId, e);
                }
            }
        } catch (Exception e) {
            // Any other exception should never occur, but if it does, then
            // we'll retry the message delivery.
            if (!this.messageDrivenContext.getRollbackOnly()) {
                this.messageDrivenContext.setRollbackOnly();
            }
            logger.error("Exception while processing message:" + messageId, e);
        } catch (Throwable t) {
            // Any other exception should never occur, but if it does, then
            // we'll retry the message delivery.
            if (!this.messageDrivenContext.getRollbackOnly()) {
                this.messageDrivenContext.setRollbackOnly();
            }
            logger.error("Throwable encountered while processing message:" + messageId, t);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("End of onMessage");
        }
    }

    /**
     * @param document Document object created from the xml message
     * @param xmlMessage xmlMessage
     * @param jndiNameUsed jndiname used to identify a datasource This method takes a Message object as argument parses the message and
     * calls appropriate service method of stateless session bean depending upon the message type received and also creates a audit
     * log/error log depending upon whether the process was successful
     */
    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    private void saveMessage(final Document document, final String xmlMessage, final String jndiNameUsed) {
        try {
            if (document.getDocumentElement().getNodeName().equals(NameValueMapping.ROOT_DOCUMENT_ELEMENT_PRODUCT_MESSAGE)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("MessageType is Product");
                }
                /**
                 * Uncomment this to enable product processing WITHOUT PPK processing
                 */
                boolean isProductFound = productService.updateProduct(document, jndiNameUsed, null);
                // boolean variable indicates whether product to update by CWP exists in ec_product table or not.
                if (!isProductFound) {
                    errorLogService.logErrorMessage(document, xmlMessage, jndiNameUsed, new Exception(
                            "No results / Error Occured when retrieving a product from EC_Product"), null);
                    return;
                }
                /**
                 * @author Yogesh Patil. This method call causes the PPK entries to be updated. Remove/Comment-out this call on line# 255
                 * and UNCOMMENT the function call above on line# 248 to stop processing of PPK versions.
                 */
                // productService.saveProductMessageObjectWithPPK(document, jndiNameUsed);
            } else if (document.getDocumentElement().getNodeName().equals(NameValueMapping.ROOT_DOCUMENT_ELEMENT_HIERARCHY_MESSAGE)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("MessageType is heirarchy");
                }
                heirarchyObjectService.saveHeirarchyMessageObject(document, jndiNameUsed);
            } else if (document.getDocumentElement().getNodeName().equals(NameValueMapping.ROOT_DOCUMENT_ELEMENT_TABLE_MESSAGE)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("MessageType is Table");
                }
                tableObjectService.saveTableObjectMessageObject(document, jndiNameUsed);
            } else if (document.getDocumentElement().getNodeName().equals(NameValueMapping.ROOT_DOCUMENT_ELEMENT_IMAGE_MESSAGE)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("MessageType is Image");
                }
                imageObjectService.saveImageMessageObject(document, jndiNameUsed);
            } else if (document.getDocumentElement().getNodeName().equals(NameValueMapping.ROOT_DOCUMENT_ELEMENT_INFO_MESSAGE)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("MessageType is Info");
                }
                infoObjectService.saveInfoMessageObject(document, jndiNameUsed);
            }
            auditLogService.logForAudit(document, xmlMessage, jndiNameUsed);
        } catch (final AuditLogException e) {
            // We are NOT rolling back the transaction. As the mesasge is saved
            // already in appropriate table
            // at this point. There is no point in rolling back everything if we
            // can't do a record keeping.
            if (logger.isInfoEnabled()) {
                logger.info("Exception while Audit Logging:" + e.getMessage());
            }
            // Logging the full stack trace whenever we are not logging the
            // exception in database using error log service.
            if (logger.isErrorEnabled()) {
                logger.error("AuditLogException :" + "\n", e);
            }
        } catch (final Exception e) {
            if (!this.messageDrivenContext.getRollbackOnly()) {
                this.messageDrivenContext.setRollbackOnly();
            }
            if (logger.isInfoEnabled()) {
                logger.info("Exception while processing MESSAGE :" + e.getMessage());
            }
            errorLogService.logErrorMessage(document, xmlMessage, jndiNameUsed, e, "Transaction has been rolled back");
        }

    }

    /**
     * @param msg The string message as received from JMS broker
     * @param msgId The message id sent by the JMS broker
     * @param errorMessage the error message to add to the email. This method uses the UserMessagingService from the RS-WEB to send a HTML
     * email message. The method sends the message to an email alias.
     */
    @TransactionAttribute(value = TransactionAttributeType.MANDATORY)
    private void putMessageOnEmailQueue(final String errorMessage, final String msg, final String msgId) {

        try {
            StringBuffer buffer = new StringBuffer(5000);
            String message = msg;
            message = message.replaceAll("<", "&lt;");
            message = message.replaceAll(">", "&gt;");
            buffer.append("<html><head></head><body><h4>Message ").append(msgId).append("</h4><br/><h5><u>Error Message:</u></h5><br/>");
            buffer.append(errorMessage).append("<h5><u>Received Message:</u></h5><pre>").append(message);
            buffer.append("</pre></body></html>");
            UserMessagingService messagingService = ServiceLocator.locateLocalOrRemote(UserMessagingService.class, new Locale("uk"));
            MailSettings mailSettings = new MailSettings();
            mailSettings.setToAddress(messageEmailFailureTo);

            mailSettings.setFromAddress(messageEmailFailureFrom);
            mailSettings.setSubject("Broken message received.");
            mailSettings.setLocale(new Locale("uk"));

            if (!messagingService.sendEmail(buffer.toString(), mailSettings)) {
                // This needs to be done because the current service
                // implementation only returns false
                // if it can't send the message.
                throw new RuntimeException(
                        "Messaging Service returned false on method sendHtmlContentEmail. Mail sending failed for MessageId :" + msgId);
            }
        } catch (Exception e) {

            // We don't want to loose the message if we can't put it on email
            // queue.
            if (!this.messageDrivenContext.getRollbackOnly()) {
                this.messageDrivenContext.setRollbackOnly();
            }

            if (logger.isInfoEnabled()) {
                logger.info("Exception " + e.getMessage() + " in putMessageOnMailQueue for MessageId :" + msgId + " Message :" + msg);
            }

            // Logging the full stack trace whenever we are not logging the
            // exception in database using error log service.
            if (logger.isErrorEnabled()) {
                logger.error("Exception while sending broken message to UserMessagingService:" + "\n", e);
            }
        }
    }
}
