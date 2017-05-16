package com.electrocomponents.continuouspublishing.service.locator;

import com.electrocomponents.continuouspublishing.service.interfaces.ProductService;
import com.electrocomponents.productcontentwriter.JndiConstants;

/**
 * Locator for the Product service.
 * @author sanjay semwal
 */
public final class ProductServiceLocator extends BaseLocator<ProductService> {

    /** The singleton ProductServiceLocator instance. */
    private static ProductServiceLocator locator = null;
    
    /** ejb local interface binding jndi name */
    private String localEjbJndiName = null;

    private ProductServiceLocator() {
        localEjbJndiName = buildEjbJndiName(this.getApplicationName(), JndiConstants.EJB_MODULE_NAME, ProductService.SERVICE_NAME,
                ProductService.class.getCanonicalName());
    }

    /**
     * @return ProductServiceLocator instance
     */
    public static ProductServiceLocator getInstance() {
        if (locator == null) {
            locator = new ProductServiceLocator();
        }
        return locator;
    }

    /**
     *
     * @return ProductService
     */
    public ProductService locate() {
        return super.locate(localEjbJndiName);
    }

    /**
    * @param appName Name of the application
    * @return ProductService
    */
    public ProductService locate(final String appName) {
        return super.locate(buildEjbJndiName(appName, JndiConstants.EJB_MODULE_NAME, ProductService.SERVICE_NAME, ProductService.class
                .getCanonicalName()));
    }

}
