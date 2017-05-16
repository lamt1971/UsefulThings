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
 * ImageObjectService interface.
 * @author sanjay semwal
 */
public interface ImageObjectService {
    
    String EJB_MODULE_NAME = "product-content-writer";

    /** The service name for the EJB (less inteface). */
    static final String SERVICE_NAME = "ImageObjectService";

    /**
     * @param document Document created from XML message
     * @param jndiNameUsed identifies jndiname for a datasource
     */
    void saveImageMessageObject(Document document, String jndiNameUsed);

}
