package com.electrocomponents.continuouspublishing.service.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.electrocomponents.continuouspublishing.service.interfaces.ErrorLogService;
import com.electrocomponents.continuouspublishing.service.interfaces.ErrorLogServiceLocal;
import com.electrocomponents.continuouspublishing.service.interfaces.ErrorLogServiceRemote;
import com.electrocomponents.continuouspublishing.utility.NameValueMapping;
import com.electrocomponents.model.data.linelevel.EcMessageFailureEntity;
import com.electrocomponents.persistence.dao.jpa.EcMessageFailureDaoBean;
import com.electrocomponents.productcontentwriter.JndiConstants;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Semwal
 * Created : 3rd Sep 2007 at 11:13:00
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *          *          *            *
 * ************************************************************************************************
 */

/**
 * ErrorLogServiceBean logs an error message in the database table EC_Message_failure called if there is an error while processing the
 * message.
 * @author sanjay semwal
 */
@Stateless(name = ErrorLogService.SERVICE_NAME)
@Local(ErrorLogServiceLocal.class)
@Remote(ErrorLogServiceRemote.class)
public class ErrorLogServiceBean implements ErrorLogServiceLocal, ErrorLogServiceRemote {
    /** Log LOG. * */
    private static final Log LOG = LogFactory.getLog(ErrorLogServiceBean.class);

    /**
     * @param document Document created from XML message
     * @param messageContent the xml message as string This Method is for error
     * @param jndiNameUsed identifies jndiname for a datasource
     * @param error the error object
     * @param extraLogMessage additional log message to be added the error message is logged in the EC_Message_failure table
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void logErrorMessage(final Document document, final String messageContent, final String jndiNameUsed, final Exception error,
            final String extraLogMessage) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start logErrorMessage(1).");
        }
        String publication = "";
        final EcMessageFailureEntity ecm = new EcMessageFailureEntity();
        ecm.setMessageContent(messageContent);
        final String messageType = document.getDocumentElement().getNodeName();
        ecm.setMessageType(messageType);
        String stackTrace = "StackTrace :";
        if (extraLogMessage != null && !extraLogMessage.equals("")) {
            stackTrace = extraLogMessage + ": " + stackTrace;
        }

        try {
            StringWriter sw = new StringWriter();
            PrintWriter printWriter = new PrintWriter(sw);
            error.printStackTrace(printWriter);
            stackTrace = stackTrace + sw.toString();
            sw.close();
            printWriter.close();
        } catch (Exception e) {
            LOG.error("Error : " + e);
        }
        ecm.setErrorMessage(stackTrace);
        ecm.setCreatedTime(new Date());
        NodeList list = document.getElementsByTagName("Publication");
        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            publication = element.getAttribute("ID");
            ecm.setPublicationName(publication);
        }
        if (document.getDocumentElement().getNodeName().equals(NameValueMapping.ROOT_DOCUMENT_ELEMENT_PRODUCT_MESSAGE)) {
            list = document.getElementsByTagName("Product");
            for (int i = 0; i < list.getLength(); i++) {
                final Element element = (Element) list.item(i);
                final String productId = element.getAttribute("ID");
                ecm.setObjectID(productId);
            }
            ecm.setMessageType(NameValueMapping.MESSAGE_AUDIT_TYPE_PRODUCT);

        } else if (document.getDocumentElement().getNodeName().equals(NameValueMapping.ROOT_DOCUMENT_ELEMENT_HIERARCHY_MESSAGE)) {
            list = document.getElementsByTagName("Product");
            for (int i = 0; i < list.getLength(); i++) {
                final Element element = (Element) list.item(i);
                final String heirachyId = element.getAttribute("ID");
                ecm.setObjectID(heirachyId);
            }
            ecm.setMessageType(NameValueMapping.MESSAGE_AUDIT_TYPE_HIERARCHY);
        } else if (document.getDocumentElement().getNodeName().equals(NameValueMapping.ROOT_DOCUMENT_ELEMENT_TABLE_MESSAGE)) {
            ecm.setMessageType(NameValueMapping.MESSAGE_AUDIT_TYPE_TABLE);
            list = document.getElementsByTagName("ContentTable");
            for (int i = 0; i < list.getLength(); i++) {
                final Element element = (Element) list.item(i);
                final String tableId = element.getAttribute("ID");
                ecm.setObjectID(tableId);
            }
        } else if (document.getDocumentElement().getNodeName().equals(NameValueMapping.ROOT_DOCUMENT_ELEMENT_IMAGE_MESSAGE)) {

            list = document.getElementsByTagName("Asset");
            for (int i = 0; i < list.getLength(); i++) {
                final Element element = (Element) list.item(i);
                final String imageid = element.getAttribute("ID");
                ecm.setObjectID(imageid);
            }
            ecm.setMessageType(NameValueMapping.MESSAGE_AUDIT_TYPE_IMAGE);
        } else if (document.getDocumentElement().getNodeName().equals(NameValueMapping.ROOT_DOCUMENT_ELEMENT_INFO_MESSAGE)) {
            list = document.getElementsByTagName("Product");
            for (int i = 0; i < list.getLength(); i++) {
                final Element element = (Element) list.item(i);
                final String infoTextId = element.getAttribute("ID");
                ecm.setObjectID(infoTextId);
            }
            ecm.setMessageType(NameValueMapping.MESSAGE_AUDIT_TYPE_INFO);
        }

        EcMessageFailureDaoBean errorLogDaoBean = new EcMessageFailureDaoBean(jndiNameUsed);

        errorLogDaoBean.getEntityManager().persist(ecm);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish logErrorMessage(2).");
        }
    }

    /**
     * @param messageContent The xml message as string This Method is for error logging for xml message which fail while parsing, It logs
     * the error message in the EC_Message_failuretable
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void logErrorMessage(final String messageContent) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start logErrorMessage(2).");
        }

        final EcMessageFailureEntity ecm = new EcMessageFailureEntity();

        ecm.setErrorMessage("ERROR WHILE PARSING");
        ecm.setMessageType("MESSAGE_TYPE_PARSING_ERROR");
        ecm.setPublicationName("PUBLICATION_NAME_PARSING_ERROR");
        ecm.setCreatedTime(new Date());
        ecm.setObjectID("9999999");
        ecm.setMessageContent(messageContent);

        final String jndiName = JndiConstants.ENTITY_MANAGER_JNDI_NAME_EMEA;

        EcMessageFailureDaoBean errorLogDaoBean = new EcMessageFailureDaoBean(jndiName);

        errorLogDaoBean.getEntityManager().persist(ecm);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish logErrorMessage(2).");
        }

    }
}
