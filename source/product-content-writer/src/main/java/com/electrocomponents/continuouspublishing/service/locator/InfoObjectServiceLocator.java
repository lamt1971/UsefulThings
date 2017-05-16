package com.electrocomponents.continuouspublishing.service.locator;

import com.electrocomponents.continuouspublishing.service.interfaces.InfoObjectService;
import com.electrocomponents.productcontentwriter.JndiConstants;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Semwal
 * Created : 3rd sep 2007 at 16:13:00
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
 * Locator for the InfoObject service.
 *
 * @author sanjay semwal
 */
public final class InfoObjectServiceLocator extends BaseLocator<InfoObjectService> {

    /** The singleton ErrorLogServiceLocator instance. */
    private static InfoObjectServiceLocator locator = null;
    
    /** ejb local interface binding jndi name */
    private String localEjbJndiName = null;

    private InfoObjectServiceLocator() {
        localEjbJndiName = buildEjbJndiName(this.getApplicationName(), JndiConstants.EJB_MODULE_NAME, InfoObjectService.SERVICE_NAME,
                InfoObjectService.class.getCanonicalName());
    }

    /**
     * @return InfoObjectServiceLocator instance
     */
    public static InfoObjectServiceLocator getInstance() {
        if (locator == null) {
            locator = new InfoObjectServiceLocator();
        }
        return locator;
    }

    /**
     * @return InfoObjectService
     */
    public InfoObjectService locate() {
        return super.locate(localEjbJndiName);
    }

    /**
     * @param appName The name of application
     * @return InfoObjectService
     */
    public InfoObjectService locate(final String appName) {
        return super.locate(buildEjbJndiName(appName, JndiConstants.EJB_MODULE_NAME, InfoObjectService.SERVICE_NAME, InfoObjectService.class
                .getCanonicalName()));
    }
}
