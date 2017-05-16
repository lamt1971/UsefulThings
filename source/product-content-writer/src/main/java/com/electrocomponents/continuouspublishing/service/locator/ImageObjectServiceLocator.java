package com.electrocomponents.continuouspublishing.service.locator;

import com.electrocomponents.continuouspublishing.service.interfaces.ImageObjectService;
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
 * Locator for the ImageObject service.
 * @author sanjay semwal
 */
public final class ImageObjectServiceLocator extends BaseLocator<ImageObjectService> {

    /** The singleton ErrorLogServiceLocator instance. */
    private static ImageObjectServiceLocator locator = null;
    
    /** ejb local interface binding jndi name */
    private String localEjbJndiName = null;

    private ImageObjectServiceLocator() {
    	localEjbJndiName = buildEjbJndiName(this.getApplicationName(), JndiConstants.EJB_MODULE_NAME, ImageObjectService.SERVICE_NAME,
                ImageObjectService.class.getCanonicalName());
    }

    /**
     * @return ImageObjectServiceLocator instance
     */
    public static ImageObjectServiceLocator getInstance() {
        if (locator == null) {
            locator = new ImageObjectServiceLocator();
        }
        return locator;
    }

    /**
    * @return ImageObjectService
    */
    public ImageObjectService locate() {
        return super.locate(localEjbJndiName);
    }

    /**
     * @param appName The name of the application
     * @return ImageObjectService
     */
    public ImageObjectService locate(final String appName) {
        return super.locate(buildEjbJndiName(appName, JndiConstants.EJB_MODULE_NAME, ImageObjectService.SERVICE_NAME,
                ImageObjectService.class.getCanonicalName()));
    }

}
