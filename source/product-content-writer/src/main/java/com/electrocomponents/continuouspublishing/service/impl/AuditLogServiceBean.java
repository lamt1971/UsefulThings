package com.electrocomponents.continuouspublishing.service.impl;

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

import com.electrocomponents.continuouspublishing.exception.AuditLogException;
import com.electrocomponents.continuouspublishing.service.interfaces.AuditLogServiceLocal;
import com.electrocomponents.continuouspublishing.service.interfaces.AuditLogServiceRemote;
import com.electrocomponents.continuouspublishing.utility.NameValueMapping;
import com.electrocomponents.model.data.linelevel.EcMessageAuditEntity;
import com.electrocomponents.persistence.dao.jpa.EcMessageAuditDaoBean;

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
 * AuditLogServiceBean logs an audit message in the database table EC_Message_Audit after the message has been successfully processed.
 * @author sanjay semwal
 */
@Stateless(name = AuditLogServiceBean.SERVICE_NAME)
@Local(AuditLogServiceLocal.class)
@Remote(AuditLogServiceRemote.class)
public class AuditLogServiceBean implements AuditLogServiceLocal, AuditLogServiceRemote {

    /** Log LOG. * */
    private static final Log LOG = LogFactory.getLog(AuditLogServiceBean.class);

    /**
     * @param document Document created from XML message
     * @param messageContent the xml message as string
     * @param jndiNameUsed identifies jndiname for a datasource
     * @throws AuditLogException This Method is for logging an audit message in the EC_Message_Audit table
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void logForAudit(final Document document, final String messageContent, final String jndiNameUsed) throws AuditLogException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start logForAudit.");
        }
        String publication = "";

        final EcMessageAuditEntity ecm = new EcMessageAuditEntity();
        try {
            ecm.setCreatedTime(new Date());
            NodeList list = document.getElementsByTagName("Publication");
            for (int i = 0; i < list.getLength(); i++) {
                final Element element = (Element) list.item(i);
                publication = element.getAttribute("ID");
                ecm.setPublicationName(publication);
            }
            ecm.setMessageContent(messageContent);
            ecm.setConfirmationMessage("Message was successfully inserted/updated in the related tables");

            if (document.getDocumentElement().getNodeName().equals(NameValueMapping.ROOT_DOCUMENT_ELEMENT_PRODUCT_MESSAGE)) {
                list = document.getElementsByTagName("Product");
                for (int i = 0; i < list.getLength(); i++) {
                    final Element element = (Element) list.item(i);
                    final String productId = element.getAttribute("ID");
                    ecm.setObjectId(productId);
                }
                ecm.setMessageType(NameValueMapping.MESSAGE_AUDIT_TYPE_PRODUCT);

            } else if (document.getDocumentElement().getNodeName().equals(NameValueMapping.ROOT_DOCUMENT_ELEMENT_HIERARCHY_MESSAGE)) {
                list = document.getElementsByTagName("Product");
                for (int i = 0; i < list.getLength(); i++) {
                    final Element element = (Element) list.item(i);
                    final String heirachyId = element.getAttribute("ID");
                    ecm.setObjectId(heirachyId);
                }
                ecm.setMessageType(NameValueMapping.MESSAGE_AUDIT_TYPE_HIERARCHY);
            } else if (document.getDocumentElement().getNodeName().equals(NameValueMapping.ROOT_DOCUMENT_ELEMENT_TABLE_MESSAGE)) {
                ecm.setMessageType(NameValueMapping.MESSAGE_AUDIT_TYPE_TABLE);
                list = document.getElementsByTagName("ContentTable");
                for (int i = 0; i < list.getLength(); i++) {
                    final Element element = (Element) list.item(i);
                    final String tableId = element.getAttribute("ID");
                    ecm.setObjectId(tableId);
                }
            } else if (document.getDocumentElement().getNodeName().equals(NameValueMapping.ROOT_DOCUMENT_ELEMENT_IMAGE_MESSAGE)) {

                list = document.getElementsByTagName("Asset");
                for (int i = 0; i < list.getLength(); i++) {
                    final Element element = (Element) list.item(i);
                    final String imageid = element.getAttribute("ID");
                    ecm.setObjectId(imageid);
                }

                ecm.setMessageType(NameValueMapping.MESSAGE_AUDIT_TYPE_IMAGE);
            } else if (document.getDocumentElement().getNodeName().equals(NameValueMapping.ROOT_DOCUMENT_ELEMENT_INFO_MESSAGE)) {
                list = document.getElementsByTagName("Product");
                for (int i = 0; i < list.getLength(); i++) {
                    final Element element = (Element) list.item(i);
                    final String infoTextId = element.getAttribute("ID");
                    ecm.setObjectId(infoTextId);
                }
                ecm.setMessageType(NameValueMapping.MESSAGE_AUDIT_TYPE_INFO);
            }
            EcMessageAuditDaoBean ecMessageAuditDao = new EcMessageAuditDaoBean(jndiNameUsed);
            ecMessageAuditDao.makePersistent(ecm);
        } catch (final Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Exception while audit logging.", e);
            }
            throw new AuditLogException(e);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish logForAudit.");
        }
    }

}
