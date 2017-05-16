package com.electrocomponents.continuouspublishing.service.interfaces;

import org.w3c.dom.Document;

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
 * ErrorLogService interface.
 * @author sanjay semwal
 */
public interface ErrorLogService {
    
    String EJB_MODULE_NAME = "product-content-writer";

    /** The service name for the EJB (less interface). */
    static final String SERVICE_NAME = "ErrorLogService";

    /**
     * @param document Document created from XML message
     * @param messageContent the xml message as string This Method is for error
     * @param jndiNameUsed identifies jndiname for a datasource
     * @param error the Exception
     * @param extraLogMessage the additional log message
     */
    void logErrorMessage(Document document, String messageContent, String jndiNameUsed, Exception error, String extraLogMessage);

    /**
     * @param message The xml message as string
     */
    void logErrorMessage(String message);

}
