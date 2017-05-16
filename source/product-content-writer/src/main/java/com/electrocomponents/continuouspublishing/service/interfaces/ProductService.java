package com.electrocomponents.continuouspublishing.service.interfaces;

import org.w3c.dom.Document;

import com.electrocomponents.model.domain.Site;

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
 * A ProductService interface.
 * @author Sanjay Semwal, Yogesh Patil
 */
public interface ProductService {

    String EJB_MODULE_NAME = "product-content-writer";
    
    /** The service name for the EJB (less inteface). */
    static final String SERVICE_NAME = "ProductService";

    /**
     * @param document Document created from XML message
     * @param jndiNameUsed identifies jndiname for a datasource
     * @param site site for which the product will be saved. Pass Null to process all sites for
     * that publication.
     * @return boolean indiacating if the product to update by CWP process exists in ec_product table or not.
     */
    boolean updateProduct(Document document, String jndiNameUsed, Site site);
}
