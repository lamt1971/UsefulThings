package com.electrocomponents.continuouspublishing.utility;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.electrocomponents.continuouspublishing.exception.MessageParsingException;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Semwal
 * Created : 3rd Sep 2007 at 16:20:00
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
 * DOMParserUtility is a utility class to convert an xml string message into Document object. If the xml string is not valid UTF-8, it
 * throws an MessageParsingException.
 * @author sanjay semwal
 */
public final class DOMParserUtility {

    /** Log LOG. * */
    private static final Log LOG = LogFactory.getLog(DOMParserUtility.class);

    /** Constructor. * */
    private DOMParserUtility() {
    }

    /**
     * @param xmlmessage XML string
     * @return Document Document object
     * @throws MessageParsingException This Method creates a Document object from the xml string passed in the argument.
     */
    public static Document getDomObject(final String xmlmessage) throws MessageParsingException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getDomObject.");
            LOG.debug("XML Message is" + xmlmessage);
        }
        Document doc = null;
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final ByteArrayInputStream bais = new ByteArrayInputStream(xmlmessage.toString().getBytes("UTF-8"));
            final InputSource inputSource = new InputSource(bais);
            doc = builder.parse(inputSource);
        } catch (final Exception e) {

            if (LOG.isErrorEnabled()) {
                LOG.error("Failed to create a Document object from a XML Message" + e.getMessage());

            }
            throw new MessageParsingException("Failed to create a Document object from a XML Message", e);

        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getDomObject.");
        }
        return doc;
    }

}
