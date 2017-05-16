package com.electrocomponents.continuouspublishing.service.locator;

import com.electrocomponents.continuouspublishing.service.interfaces.TableObjectService;
import com.electrocomponents.productcontentwriter.JndiConstants;

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
 * Locator for the Table Object service.
 * @author sanjay semwal
 */
public final class TableObjectServiceLocator extends BaseLocator<TableObjectService> {

    /** The singleton TableObjectServiceLocator instance. */
    private static TableObjectServiceLocator locator = null;
    
    /** ejb local interface binding jndi name */
    private String localEjbJndiName = null;

    private TableObjectServiceLocator() {
        localEjbJndiName = buildEjbJndiName(this.getApplicationName(), JndiConstants.EJB_MODULE_NAME, TableObjectService.SERVICE_NAME,
                TableObjectService.class.getCanonicalName());
    }

    /**
     * @return TableObjectServiceLocator instance
     */
    public static TableObjectServiceLocator getInstance() {
        if (locator == null) {
            locator = new TableObjectServiceLocator();
        }
        return locator;
    }

    /**
     *
     * @return TableObjectService
     */
    public TableObjectService locate() {
        return super.locate(localEjbJndiName);
    }

    /**
    * @param appName Name of the application
    * @return TableObjectService
    */
    public TableObjectService locate(final String appName) {
        return super.locate(buildEjbJndiName(appName, JndiConstants.EJB_MODULE_NAME, TableObjectService.SERVICE_NAME,
                TableObjectService.class.getCanonicalName()));
    }

}
