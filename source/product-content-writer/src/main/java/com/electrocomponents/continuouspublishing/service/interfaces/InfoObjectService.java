package com.electrocomponents.continuouspublishing.service.interfaces;

import org.w3c.dom.Document;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Semwal
 * Created : 3rd Sep 2007 at 14:13:00
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
 * InfoObjectService interface.
 * @author sanjay semwal
 */
public interface InfoObjectService {
    
    String EJB_MODULE_NAME = "product-content-writer";

    /** The service name for the EJB (less inteface). */
    static final String SERVICE_NAME = "InfoObjectService";

    /**
     * @param document Document created from XML message
     * @param jndiNameUsed identifies jndiname for a datasource
     */
    void saveInfoMessageObject(Document document, String jndiNameUsed);

}
