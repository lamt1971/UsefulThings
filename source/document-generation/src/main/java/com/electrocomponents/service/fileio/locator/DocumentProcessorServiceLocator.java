package com.electrocomponents.service.fileio.locator;

import com.electrocomponents.model.domain.Locale;
import com.electrocomponents.service.core.client.BaseLocator;
import com.electrocomponents.service.fileio.interfaces.DocumentProcessorService;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut
 * Created : 13 Jan 2009 at 16:52:55
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *          *          *            *
 * ************************************************************************************************
 * </pre>
 */

/**
 * A locator class to locate DocumentProcessorService.
 * @author Ganesh Raut
 */
public final class DocumentProcessorServiceLocator extends BaseLocator<DocumentProcessorService> {

    /** The singleton instance. */
    private static DocumentProcessorServiceLocator documentProcessorServiceLocator = null;
    
    private String localEjbJndiName;

    /** Constructor. */
    private DocumentProcessorServiceLocator() {
        setApplicationName(resolveApplicationName());
        localEjbJndiName = buildEjbJndiName(this.getApplicationName(), DocumentProcessorService.EJB_MODULE_NAME, DocumentProcessorService.SERVICE_NAME,
                        DocumentProcessorService.class.getCanonicalName());
    }

    /**
     * @return the locator
     */
    public synchronized static DocumentProcessorServiceLocator getLocator() {
        if (documentProcessorServiceLocator == null) {
            documentProcessorServiceLocator = new DocumentProcessorServiceLocator();
        }

        return documentProcessorServiceLocator;
    }

    /**
     * Locate and retrieve an instance of a DocumentGeneratorService.
     * @param locale The locale we wish to retrieve a DocumentGeneratorService for.
     * @return A DocumentGenerator Service that supports the specifed locale.
     */
    public DocumentProcessorService locate(final Locale locale) {
        return super.locate(localEjbJndiName, locale);
    }

    /**
     * Locate and retrieve an instance of a DocumentGeneratorService.
     * @param appName The name of the application service to locate
     * @param locale The locale we wish to retrieve a DocumentGeneratorService for.
     * @return A DocumentGenerator Service that supports the specifed locale.
     */
    public DocumentProcessorService locate(final String appName, final Locale locale) {
        final String jndi = buildEjbJndiName(appName, DocumentProcessorService.EJB_MODULE_NAME, DocumentProcessorService.SERVICE_NAME,
                DocumentProcessorService.class.getCanonicalName());
        return super.locate(jndi, locale);
    }
}
