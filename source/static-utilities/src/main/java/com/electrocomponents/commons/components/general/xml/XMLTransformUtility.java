package com.electrocomponents.commons.components.general.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut
 * Created : 14 Aug 2007 at 09:27:03
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
 * An utility class to convert an xml file to a required mail format using XSLT.
 * @author Ganesh Raut
 */
public final class XMLTransformUtility {

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(XMLTransformUtility.class);

    /** XMLTransfarmUtility instance. */
    private static XMLTransformUtility xmlTransformUtility = null;

    /** Constructor. */
    private XMLTransformUtility() {
        super();
    }

    /**
     * @return Instance of XMLTransfarmUtility.
     */
    public static XMLTransformUtility getInstance() {
        if (xmlTransformUtility == null) {
            xmlTransformUtility = new XMLTransformUtility();
        }
        return xmlTransformUtility;
    }

    /**
     * Generates a response message to be sent back to the customer. The source XML is a combination of the original customer order and the
     * Standard response. The destination XML is transformed, (from the source XML), into a format acceptable to the customer.
     * @param inputXML - The master input XML document to be transformed (this drives the stylesheet processing)
     * @param stylesheet - for the transformation. May be 'STANDARD', or customer-specific format.
     * @return The transformed response message.
     * @throws TransformerException Exception
     */
    public static String transformXML(final String inputXML, final String stylesheet) throws TransformerException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start transformXML(1).");
        }

        String retString;
        InputStream xmlStream = null;
        InputStream stylesheetStream = null;

        try {
            xmlStream = new ByteArrayInputStream(inputXML.getBytes("UTF8"));
            stylesheetStream = new ByteArrayInputStream(stylesheet.getBytes("UTF8"));
        } catch (UnsupportedEncodingException uee) {
            String error = "transformResponseXML : Failure transforming Order Response XML : " + uee;
            LOG.fatal(error);
        }

        // create a TransformerFactory instance which is then used to create the
        // Transformer.
        TransformerFactory transFact = TransformerFactory.newInstance();
        Transformer trans = null;
        try {
            // Process the stylesheet Source into a Transformer object.
            trans = transFact.newTransformer(new StreamSource(stylesheetStream));
        } catch (TransformerConfigurationException e) {
            String error = "transformResponseXML : Failure creating Transformer : " + e;
            LOG.fatal(error);
            throw e;
        }

        /*
         * Takes the customer incoming Order XML Data as a Stream and generates a Source object for use in the XSLT Transformation.
         */
        Source xmlSource = new StreamSource(xmlStream);

        // A holder for the transformation result.
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);

        try {
            // Process the Source tree to the output result - (which is the
            // StringWriter).
            trans.transform(xmlSource, result);
        } catch (TransformerException te) {
            String error = "TransformerException thrown executing the XSLT transformation : " + te;
            LOG.fatal(error);
            throw te;
        }

        retString = sw.getBuffer().toString();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish transformXML(1).");
        }
        return retString;
    }

    /**
     * Generates a response message to be sent back to the customer. The source XML is a combination of the original customer order and the
     * Standard response. The destination XML is transformed, (from the source XML), into a format acceptable to the customer.
     * @param inputXML - The master input XML document to be transformed (this drives the stylesheet processing)
     * @param stylesheet - for the transformation. May be 'STANDARD', or customer-specific format.
     * @param encodingType - encoding type used for transformation.
     * @return The transformed response message.
     * @throws TransformerException Exception
     */
    public static String transformXML(final String inputXML, final String stylesheet, final String encodingType)
            throws TransformerException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start transformXML(inputXML, stylesheet, encodingType).");
        }
        String retString;
        InputStream xmlStream = null;
        InputStream stylesheetStream = null;

        try {
            xmlStream = new ByteArrayInputStream(inputXML.getBytes(encodingType));
            stylesheetStream = new ByteArrayInputStream(stylesheet.getBytes(encodingType));
        } catch (UnsupportedEncodingException uee) {
            String error = "transformResponseXML : Failure transforming Order Response XML : " + uee;
            LOG.fatal(error);
        }

        // create a TransformerFactory instance which is then used to create the
        // Transformer.
        TransformerFactory transFact = TransformerFactory.newInstance();
        Transformer trans = null;
        try {
            // Process the stylesheet Source into a Transformer object.
            trans = transFact.newTransformer(new StreamSource(stylesheetStream));
        } catch (TransformerConfigurationException e) {
            String error = "transformResponseXML : Failure creating Transformer : " + e;
            LOG.fatal(error);
            throw e;
        }

        /*
         * Takes the customer incoming Order XML Data as a Stream and generates a Source object for use in the XSLT Transformation.
         */
        Source xmlSource = new StreamSource(xmlStream);

        // A holder for the transformation result.
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);

        try {
            // Process the Source tree to the output result - (which is the
            // StringWriter).
            trans.transform(xmlSource, result);
        } catch (TransformerException te) {
            String error = "TransformerException thrown executing the XSLT transformation : " + te;
            LOG.fatal(error);
            throw te;
        }

        retString = sw.getBuffer().toString();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish transformXML(inputXML, stylesheet, encodingType).");
        }
        return retString;
    }

    /**
     * Generates a transformed HTMl response message to be sent back to the customer with given xml and xslt. Also the parameters required
     * for XSLT can be passed in the map The destination XML is transformed, (from the source XML), into a HTML format used to diacceptable
     * to the customer
     * @param inputXML - The master input XML document to be transformed (this drives the stylesheet processing)
     * @param stylesheet - for the transformation. May be 'STANDARD', or customer-specific format.
     * @param paramMap - Represents the name value pair for the parameters.
     * @return The transformed response message.
     * @throws TransformerException Exception
     */
    public static String transformXML(final String inputXML, final String stylesheet, final Map<String, String> paramMap)
            throws TransformerException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start transformXML(2).");
        }

        String retString;
        InputStream xmlStream = null;
        InputStream stylesheetStream = null;

        try {
            xmlStream = new ByteArrayInputStream(inputXML.getBytes("UTF8"));
            stylesheetStream = new ByteArrayInputStream(stylesheet.getBytes("UTF8"));
        } catch (UnsupportedEncodingException uee) {
            String error = "transformResponseXML : Failure transforming Order Response XML : " + uee;
            LOG.fatal(error);
        }

        // create a TransformerFactory instance which is then used to create the
        // Transformer.
        TransformerFactory transFact = TransformerFactory.newInstance();
        Transformer trans = null;
        try {
            // Process the stylesheet Source into a Transformer object.
            trans = transFact.newTransformer(new StreamSource(stylesheetStream));
            if (paramMap != null) {
                for (Map.Entry<String, String> paramMapEntrySet : paramMap.entrySet()) {
                    String paramName = paramMapEntrySet.getKey();
                    String paramValue = paramMapEntrySet.getValue();
                    trans.setParameter(paramName, paramValue);
                }
            }
        } catch (TransformerConfigurationException e) {
            String error = "transformResponseXML : Failure creating Transformer : " + e;
            LOG.fatal(error);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Finish transformXML(2)-1.");
            }
            throw e;
        }

        /*
         * Takes the customer incoming Order XML Data as a Stream and generates a Source object for use in the XSLT Transformation.
         */
        Source xmlSource = new StreamSource(xmlStream);

        // A holder for the transformation result.
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);

        try {
            // Process the Source tree to the output result - (which is the
            // StringWriter).
            trans.transform(xmlSource, result);
        } catch (TransformerException te) {
            String error = "TransformerException thrown executing the XSLT transformation : " + te;
            LOG.fatal(error);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Finish transformXML(2)-2.");
            }
            throw te;
        }

        retString = sw.getBuffer().toString();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish transformXML(2)-3.");
        }
        return retString;
    }

    /**
     * Generates a response message to be sent back to the customer. The source XML is a combination of the original customer order and the
     * Standard response. The destination XML is transformed, (from the source XML), into a format acceptable to the customer.
     * @param primaryXml - The master input XML document to be transformed (this drives the style-sheet processing).
     * @param secondaryXml - secondaryXml String.
     * @param styleSheet - XSLT document.
     * @param encodingType - encodingType.
     * @param paramMap - Represents the name value pair for the parameters.
     * @param isIntended - Represents the name value pair for the parameters.
     * @param intendationUnit - Represents the name value pair for the parameters.
     * @return The transformed response message.
     * @throws Exception If the transformation process fails for any reason.
     */
    public static synchronized String transformResponseXML(final String primaryXml, final String secondaryXml, final String styleSheet,
            final String encodingType, final Map<String, String> paramMap, final boolean isIntended, final int intendationUnit)
            throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("transformResponseXML : Method Start");
        }

        String retString;
        InputStream secondaryXmlStream;
        InputStream stylesheetStream;

        secondaryXmlStream = new ByteArrayInputStream(secondaryXml.getBytes(encodingType));
        stylesheetStream = new ByteArrayInputStream(styleSheet.getBytes(encodingType));

        org.w3c.dom.Document primaryXmlDoc = XMLSupport.parse(primaryXml);

        /*
         * Implementation of the URIResolver Interface. An object that implements this interface that can be called by the XSLT processor to
         * turn a URI, (used in document('secondarydoc'), xsl:import, or xsl:include), into a Source object
         */
        RSURIResolver resolver = new RSURIResolver();

        /*
         * set the content which will be used by the RSURIResolver to create the Source object which is accessed when the XSLT processor
         * encounters a document('secondarydoc'), xsl:import, or xsl:include function in the XSLT stylesheet.
         */
        resolver.setSecondaryXmlInputDocument(primaryXmlDoc);

        // create a TransformerFactory instance which is then used to create the Transformer.
        TransformerFactory transFact = TransformerFactory.newInstance();

        /*
         * set an object that is used by default during the transformation to resolve URIs used in the XSLT stylesheet, (i.e. (used in
         * document(), xsl:import, or xsl:include))
         */
        transFact.setURIResolver(resolver);
        // Process the stylesheet Source into a Transformer object.
        Transformer trans = transFact.newTransformer(new StreamSource(stylesheetStream));
        if (isIntended) {
            /** Added two properties below for intending in the output XML */
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(intendationUnit));
        }

        if (paramMap != null) {
            for (Map.Entry<String, String> paramMapEntrySet : paramMap.entrySet()) {
                String paramName = paramMapEntrySet.getKey();
                String paramValue = paramMapEntrySet.getValue();
                trans.setParameter(paramName, paramValue);
            }
        }

        // Takes the customer incoming Order XML Data as a Stream and generates a Source object for use in the XSLT Transformation.
        Source xmlSource = new StreamSource(secondaryXmlStream);

        // A holder for the transformation result.
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);

        // Process the Source tree to the output result - (which is the StringWriter).
        trans.transform(xmlSource, result);

        retString = sw.getBuffer().toString();

        if (LOG.isDebugEnabled()) {
            LOG.debug("transformResponseXML : Return value = " + retString + " #");
            LOG.debug("transformResponseXML : Method Finish");
        }
        return retString;
    }

    /**
     * Method used to check that xmlString is a well-formed XML document.
     * @param xmlString as String.
     * @return boolean.
     */
    public static boolean isWellFormedXmlDocument(final String xmlString) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start isWellFormedXmlDocument.");
        }
        boolean isWellFormed = false;
        try {
            DocumentBuilderFactory dBF = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dBF.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(xmlString));
            builder.parse(inputSource);
            isWellFormed = true;
        } catch (java.net.UnknownHostException exception) {
            // Returning true because we are not bothered about the DTDs at all. We only have to check that output XML is well formed.
            isWellFormed = true;
        } catch (Exception e) {
            isWellFormed = false;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish isWellFormedXmlDocument.");
        }
        return isWellFormed;
    }

}
