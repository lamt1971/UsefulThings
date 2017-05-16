package com.electrocomponents.continuouspublishing.service.locator;

import com.electrocomponents.continuouspublishing.service.interfaces.AuditLogService;
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
 * AuditLogServiceLocator class that attempts to lookup a AuditLogService service from JNDI.
 * @author Sanjay Semwal
 */
public final class AuditLogServiceLocator extends BaseLocator<AuditLogService> {

    /** The singleton AuditLogServiceLocator instance. */
    private static AuditLogServiceLocator locator = null;
    
    /** ejb local interface binding jndi name */
    private String localEjbJndiName = null;

    private AuditLogServiceLocator() {
        localEjbJndiName = buildEjbJndiName(this.getApplicationName(), JndiConstants.EJB_MODULE_NAME, AuditLogService.SERVICE_NAME,
                AuditLogService.class.getCanonicalName());
    }

    /**
     * Singleton to return the instance of the locator.
     * @return The AuditLogServiceLocator instance..
     */
    public static AuditLogServiceLocator getInstance() {
        if (locator == null) {
            locator = new AuditLogServiceLocator();
        }
        return locator;
    }

    /**
     * Locate and return a AuditLogService instance for the specified locale.
     * @return A AuditLogService service.
     */
    public AuditLogService locate() {
        return super.locate(localEjbJndiName);
    }

    /**
     * Locate and return a AuditLogService instance for the specified locale.
     * @param appName The application name.
     * @return A AuditLogService service.
     */
    public AuditLogService locate(final String appName) {
        return super.locate(buildEjbJndiName(appName, JndiConstants.EJB_MODULE_NAME, AuditLogService.SERVICE_NAME, AuditLogService.class
                .getCanonicalName()));
    }

}
