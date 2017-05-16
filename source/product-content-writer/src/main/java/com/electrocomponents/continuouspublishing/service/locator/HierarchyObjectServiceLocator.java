package com.electrocomponents.continuouspublishing.service.locator;

import com.electrocomponents.continuouspublishing.service.interfaces.HierarchyObjectService;
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
 * HierarchyObjectServiceLocator class that attempts to lookup a HierarchyObject
 * service from JNDI.
 * @author Sanjay Semwal
 */
public final class HierarchyObjectServiceLocator extends BaseLocator<HierarchyObjectService> {

    /** The singleton ErrorLogServiceLocator instance. */
    private static HierarchyObjectServiceLocator locator = null;

    /** ejb local interface binding jndi name */
    private String localEjbJndiName = null;
    
    private HierarchyObjectServiceLocator() {
    	localEjbJndiName = buildEjbJndiName(this.getApplicationName(), JndiConstants.EJB_MODULE_NAME, HierarchyObjectService.SERVICE_NAME,
                HierarchyObjectService.class.getCanonicalName());
    }

    /**
     * @return HeirarchyObjectServiceLocator instance
     */
    public static HierarchyObjectServiceLocator getInstance() {
        if (locator == null) {
            locator = new HierarchyObjectServiceLocator();
        }
        return locator;
    }

    /**
     *
     * @return HierarchyObjectService
     */
    public HierarchyObjectService locate() {
        return super.locate(localEjbJndiName);
    }

    /**
    * @param appName The name of application
    * @return HierarchyObjectService
    */
    public HierarchyObjectService locate(final String appName) {
        return super.locate(buildEjbJndiName(appName, JndiConstants.EJB_MODULE_NAME, HierarchyObjectService.SERVICE_NAME,
                HierarchyObjectService.class.getCanonicalName()));
    }

}
