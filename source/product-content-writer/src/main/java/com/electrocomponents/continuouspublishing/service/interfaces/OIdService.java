/**
 *
 */
package com.electrocomponents.continuouspublishing.service.interfaces;

import com.electrocomponents.continuouspublishing.service.impl.OIdServiceBean;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author : Sanjay Semwal Created : 3rd Sep 2007 at 11:26:34
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref * Who * Date * Description
 * ************************************************************************************************ * * *
 * ************************************************************************************************
 */

/**
 * A service interface for generating OId.
 * @see OIdServiceBean
 * @author Sanjay Semwal
 */
public interface OIdService {
    
    String EJB_MODULE_NAME = "product-content-writer";

    /** The service name for the EJB. */
    public static final String SERVICE_NAME = "OIdService";

    /**
     * @param jndiName jndiName
     * @return the generated OId
     */
    long generateOId(String jndiName);
}
