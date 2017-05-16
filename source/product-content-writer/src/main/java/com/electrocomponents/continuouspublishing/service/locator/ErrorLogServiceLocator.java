package com.electrocomponents.continuouspublishing.service.locator;

import com.electrocomponents.continuouspublishing.service.interfaces.ErrorLogService;
import com.electrocomponents.productcontentwriter.JndiConstants;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Semwal
 * Created : 3rd Sep 2007 at 14:50:38
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
 * Locator for the ErrorLog service.
 * @author sanjay semwal
 */
public final class ErrorLogServiceLocator extends BaseLocator<ErrorLogService> {

    /** The singleton ErrorLogServiceLocator instance. */
    private static ErrorLogServiceLocator locator = null;
    
    /** ejb local interface binding jndi name */
    private String localEjbJndiName = null;

    private ErrorLogServiceLocator() {
    	localEjbJndiName = buildEjbJndiName(this.getApplicationName(), JndiConstants.EJB_MODULE_NAME, ErrorLogService.SERVICE_NAME,
                ErrorLogService.class.getCanonicalName());
    }

    /**
     * @return ErrorLogServiceLocator instance
     */
    public static ErrorLogServiceLocator getInstance() {
        if (locator == null) {
            locator = new ErrorLogServiceLocator();
        }
        return locator;
    }

    /**
     *
     * @return ErrorLogService
     */
    public ErrorLogService locate() {
        return super.locate(localEjbJndiName);
    }

    /**
    *@param appName The application name
    * @return ErrorLogService
    */
    public ErrorLogService locate(final String appName) {
        return super.locate(buildEjbJndiName(appName, JndiConstants.EJB_MODULE_NAME, ErrorLogService.SERVICE_NAME, ErrorLogService.class
                .getCanonicalName()));
    }

}
