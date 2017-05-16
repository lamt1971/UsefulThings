package com.electrocomponents.commons.components.general.xml;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

/**
 * Copyright (c) Electrocomponents Plc.
 * Author         : e0160219
 * Creation Date  : 18-Apr-2006
 * Creation Time  : 15:53:00
 * IDE            : IntelliJ IDEA 5
 * *******************************************************************************************************************
 * Description :
 *
 * This class provides control over xsl:include/import or the document() function. This is used when you have a
 * 'primary' XSL[1] and it includes, imports or uses document() to bring in other XML/XSL files/streams from two or
 * more different points of control.
 * The 'resolve' method is called by the XSLT processor, to turn a URI into a Source object, when it encounters an
 * xsl:include, xsl:import, or document() function.


 * *******************************************************************************************************************
 * Change History                                                                                                    *
 * *******************************************************************************************************************
 * * Change   * Author   * Date         * Description                                                                *
 * *******************************************************************************************************************
 * * New      * e0160219 *  18-Apr-2006 * New class created
 * *******************************************************************************************************************
 * *          * UK158854  * 08/06/06   *  Correct Logging
 * *******************************************************************************************************************
 * * 38800    * E0180383  * 01/03/2007 * Changes to support Sony / Askul PunchOut development (RFC 1414)
 * *******************************************************************************************************************
 *
 * @author e0160219
 */
public class RSURIResolver implements URIResolver {

    /**
     * Log4J.
     *
     */
    private static Log log = LogFactory.getLog(RSURIResolver.class);

    /**
     * secondary XML Doc Reference String.
     */
    private static final String secondaryXMLDocReferenceString = "secondarydoc";

    /**
     * Secondary XML Document.
     */
    private Document secondaryXmlInputDocument;

    /**
     * Resolve the RS URI.
     * @param href href.
     * @param base Base.
     * @return The source.
     * @throws TransformerException Exception.
     */
    public Source resolve(final String href, final String base) throws TransformerException {

        if (log.isDebugEnabled()) {
            log.debug("resolve : Method Start");
        }

        if (href.endsWith(secondaryXMLDocReferenceString)) {
            try {
                Source xmlSource = new DOMSource(secondaryXmlInputDocument);
                return xmlSource;

            } catch (Exception err) {
                String error = "resolve : Creation of xmlSource failed";
                log.error(error);
                throw new TransformerException(err);
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("resolve : Method End");
            }
            return null;
        }
    }

    /**
     * Get the secondary XML Input document.
     * @return The secondary XML Input document.
     */
    public Document getSecondaryXmlInputDocument() {
        return secondaryXmlInputDocument;
    }

    /**
     * Set the secondary XML Input document.
     * @param secondaryXmlInputDocument The secondary XML Input document.
     */
    public void setSecondaryXmlInputDocument(final Document secondaryXmlInputDocument) {
        this.secondaryXmlInputDocument = secondaryXmlInputDocument;
    }
}
