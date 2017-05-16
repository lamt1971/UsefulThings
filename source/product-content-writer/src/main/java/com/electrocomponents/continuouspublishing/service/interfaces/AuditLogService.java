package com.electrocomponents.continuouspublishing.service.interfaces;

import org.w3c.dom.Document;

import com.electrocomponents.continuouspublishing.exception.AuditLogException;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Semwal
 * Created : 3rd Sep 2007 at 16:20:00
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
 * AuditLogService interface.
 * @author Sanjay Semwal
 */
public interface AuditLogService {
    
    String EJB_MODULE_NAME = "product-content-writer";

    /** The service for the EJB (less inteface). */
    public static final String SERVICE_NAME = "AuditLogService";

    /**
     * @param document Document object created from the xml message
     * @param messageContent messageContent
     * @param jndiNameUsed jndiname used to identify a datasource
     * @throws AuditLogException Thrown during a error while audit logging
     */
    void logForAudit(Document document, String messageContent, String jndiNameUsed) throws AuditLogException;

}
