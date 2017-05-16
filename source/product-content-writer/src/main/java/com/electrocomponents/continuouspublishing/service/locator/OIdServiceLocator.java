/**
 *
 */
package com.electrocomponents.continuouspublishing.service.locator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.continuouspublishing.service.interfaces.OIdService;
import com.electrocomponents.productcontentwriter.JndiConstants;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Semwal
 * Created : 3rd sep 2007  at 14:13:00
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
 * Locate a instance of a OIdService for a specified locale.
 *
 * @author Sanjay Semwal
 */
public class OIdServiceLocator extends BaseLocator<OIdService> {

    /** The singleton OIdServiceLocator instance. */
    private static OIdServiceLocator osl = null;
    
    /** ejb local interface binding jndi name */
    private String localEjbJndiName = null;

    private OIdServiceLocator() {
        localEjbJndiName = buildEjbJndiName(this.getApplicationName(), JndiConstants.EJB_MODULE_NAME, OIdService.SERVICE_NAME,
                OIdService.class.getCanonicalName());
    }

    /**
     * Singleton to return the instance of the locator.
     *
     * @return The OIdServiceLocator instance..
     */
    public static OIdServiceLocator getLocator() {

        if (osl == null) {
            osl = new OIdServiceLocator();
        }

        return osl;
    }

    /**
     * Locate and retrieve an instance of a OIdService.
     *
     * @return A OId service
     */
    public OIdService locate() {
        return super.locate(localEjbJndiName);
    }

    /**
     * Locate and retrieve an instance of a OIdService.
     * @param appName Name of the application
     * @return A OId service
     */
    public OIdService locate(final String appName) {
        return super.locate(buildEjbJndiName(appName, JndiConstants.EJB_MODULE_NAME, OIdService.SERVICE_NAME, OIdService.class
                .getCanonicalName()));
    }
}
