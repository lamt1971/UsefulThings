package com.electrocomponents.commons.components.general.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * <pre>
 * XMLSupport
 * ---------------------
 * Copyright (c) RS Components.
 * User: UK180383
 * Date: 03/07/2003
 * ********************************************************************************************************
 * Overview                                                                                               *
 * --------                                                                                               *
 * Utilities for parsing XML files. This class provides functionality such as                             *
 * extracting Attributes and Elements from XML documents. It will also convert                            *
 * an XML parse exception into a String.                                                                  *
 * ********************************************************************************************************
 * <P>                                                                                                    *
 * @version 2.0                                                                                           *
 * @author UK180383 (Stuart Sim)                                                                          *
 * ********************************************************************************************************
 * *          Change History                                                                              *
 * ********************************************************************************************************
 * * Number   * Who       * Date       * Description                                                      *
 * ********************************************************************************************************
 * * N/A      * N McKenna * 08/06/2000 * New development                                                  *
 * ********************************************************************************************************
 * * 28718    * UK160219  * 21/04/2005 * Added method 'getXMLElementDataWithAttrNotRequired'              *
 * ********************************************************************************************************
 * * 29818    * UK160219  * 22/04/2005 * (RMaguire/SSim) method parse(InputStream) is not used so         *
 * *          *           *            * commented out.                                                   *
 * *          *           *            *                                                                  *
 * *          *           *            * (RMaguire/SSim) parseNoDecode(String) not required as part of    *
 * *          *           *            * code tidy-up. Now uses parse(String)                             *
 * *          *           *            * (RMaguire/SSim) amended method decodeEntityReferences(in)        *
 * *          *           *            * as this resulted in valid chars being removed from data with the *
 * *          *           *            * XML tags (e.g. the '&' sign). To support this we have also       *
 * *          *           *            * included factory.setExpandEntityReferences(false);               *
 * ********************************************************************************************************
 * * 35308    * UK160219  * 28/11/2005 * Changed signature of existing 'encode' method to take two        *
 * *          *           *            * parameters and added 'encode(parm1)' overloading method to       *
 * *          *           *            * correspond with this. JP characters were being double encoded    *
 * *          *           *            * when they needed to be single-encoded.                           *
 * ********************************************************************************************************
 * * 37046    * UK160219  * 20/02/2006 * Amended method, 'getXMLAribaToCredential' to also check the      *
 * *          *           *            * domain='networkid' as this is the XML attribute associated with  *
 * *          *           *            * the TAG which holds the 'To' ANID.                               *
 * ********************************************************************************************************
 * *          * UK158854  * 30/05/06   * Correct Logging.                                                 *
 * ********************************************************************************************************
 * * 38830    * UK161085  * 31/10/06   * Added a fix to the xml validation.
 * *          *           *            * If the xml validation is turned off, the parser still needs to
 * *          *           *            * read the DTD file. However, with the DataCentre move, this is not
 * *          *           *            * possible due to the firewall. Therefore new code added to set the
 * *          *           *            * parser so that it no longer reads the DTD file.
 * ********************************************************************************************************
 * * 39200    * E0180383  * 13/04/2007 * Remove Ariba credential processing and move to Ariba handler
 * ********************************************************************************************************
 * * 9.3      * E0161085  * 08/12/2008 * Tidied Code.
 * ********************************************************************************************************
 * * 9.3      * E0161085  * 15/01/2009 * Used System codes for exceptions.
 * ********************************************************************************************************

 * </pre>
 */

public class XMLSupport {

    /**
     * Log4J Declaration.
     */
    private static Log log = LogFactory.getLog(XMLSupport.class);

    /**
     * Constructor is private to prevent the class user from instantiating
     * the class. This is not strictly necessary, but useful to reinforce the
     * idea that the class represents a group of related functions rather than
     * an abstract entity.
     */
    private XMLSupport() {
    }

    /**
     * Retrieve the textual data from within a specified XML document element.
     * @param doc XML Document.
     * @param xPath XML Path.
     * @return Returns the text String from the XML document. If the text
     * cannot be found, the return value is null.
     */
    public static String getXMLElementText(final Document doc, final String xPath) {
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementText 1 : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementText 1 : xPath = " + xPath);
        }

        // The extracted data
        String theData = null;

        // Root Element, xPath
        Element xmlNode = getXMLElement(doc, xPath);

        // Get the text from the found node
        theData = extractXMLText(xmlNode);

        if (log.isDebugEnabled()) {
            log.debug("getXMLElementText 1 : return=" + theData);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementText 1 : Method Finish");
        }

        return theData;
    }

    /**
     * Retrieve the textual data from within a specified XML document element.
     * @param doc XML Document.
     * @param xPath XML Path.
     * @return Returns the text String from the XML document. If the text
     * cannot be found, the return value is null.
     */
    public static String getXMLElementText(final Element doc, final String xPath) {
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementText 2 : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementText 2 : xPath = " + xPath);
        }

        // The extracted data
        String theData = null;

        // Root Element, xPath
        Element xmlNode = getXMLElement(doc, xPath);

        theData = extractXMLText(xmlNode);

        if (log.isDebugEnabled()) {
            log.debug("getXMLElementText 2 : return=" + theData);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementText 2 : Method Finish");
        }

        return theData;
    }

    /**
     * Returns the value of the attribute specified in the xPath and attributeName from the
     * specified XML document. If multiple matching attributes exist, only the
     * first matching attribute is examined. If there is no matching attribute
     * or the XPath + attributeName does not evaluate to an attribute, the method returns a
     * null pointer.
     *
     * author Nick McKenna
     * @param doc The XML document to search
     * @param xPath The XPath expression representing the element to find
     * @param attributeName The attribute to check inside the element specified in the XPath
     * @return The string that represents the value of the attribute specified in the XPath and attributeName
     * @throws ServletFailure Exception.
     */
    public static String getXMLAttribute(final Document doc, final String xPath, final String attributeName) throws ServletFailure {
        if (log.isDebugEnabled()) {
            log.debug("getXMLAttribute 1 : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLAttribute 1 : xPath = " + xPath);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLAttribute 1 : attributeName = " + attributeName);
        }

        String retval = null;

        // Root Element, xPath
        Element xmlNode = getXMLElement(doc, xPath);

        if (xmlNode != null) {
            retval = xmlNode.getAttribute(attributeName);
        } else {
            String err =
                    "getXMLAttribute 1 : The following XML could not be found: \n" + "Document: " + doc + "\n" + "XPath: " + xPath + "\n"
                            + "AttributeName: " + attributeName;
            log.fatal(err);
            // throw new ServletFailure("XMLSupport." + err);
            throw SystemCodes.generateServletFailure(SystemCodes.COMMON_OPERATION_FAILURE, "XMLSupport." + err);
        }

        if (log.isDebugEnabled()) {
            log.debug("getXMLAttribute 1 : Method Finish");
        }
        return retval;
    }

    /**
     * Returns the value of the attribute specified in the xPath and attributeName from the
     * specified XML Element. If multiple matching attributes exist, only the
     * first matching attribute is examined. If there is no matching attribute
     * or the XPath + attributeName does not evaluate to an attribute, the method returns a
     * null pointer.
     *
     * author Nick McKenna
     * @param element The XML document to search
     * @param xPath The XPath expression representing the element to find
     * @param attributeName The attribute to check inside the element specified in the XPath
     * @return The string that represents the value of the attribute specified in the XPath and attributeName
     * @throws ServletFailure Exception.
     */
    public static String getXMLAttribute(final Element element, final String xPath, final String attributeName) throws ServletFailure {
        if (log.isDebugEnabled()) {
            log.debug("getXMLAttribute 2 : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLAttribute 2 : xPath = " + xPath);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLAttribute 2 : attributeName = " + attributeName);
        }

        String retval;
        Element ele = element;

        if (xPath != null && !"".equals(xPath.trim())) {
            ele = getXMLElement(element, xPath);
        }

        if (ele != null) {
            retval = ele.getAttribute(attributeName);
        } else {
            String err =
                    "getXMLAttribute 2 : The following XML could not be found: \n" + "Element: " + ele + "\n" + "XPath: " + xPath + "\n"
                            + "AttributeName: " + attributeName;
            log.fatal(err);
            // throw new ServletFailure("XMLSupport." + err);
            throw SystemCodes.generateServletFailure(SystemCodes.COMMON_OPERATION_FAILURE, "XMLSupport." + err);
        }

        if (log.isDebugEnabled()) {
            log.debug("getXMLAttribute 2 : Method Finish");
        }
        return retval;
    }

    /**
     * Returns the value of the attribute specified in the xPath and attributeName from the
     * specified XML document. If multiple matching attributes exist, only the
     * first matching attribute is examined. If there is no matching attribute
     * or the XPath + attributeName does not evaluate to an attribute, the method returns a
     * null pointer.
     *
     * author Nick McKenna
     * @param doc The XML document to search
     * @param xPath The XPath expression representing the element to find
     * @param attributeName The attribute to check inside the element specified in the XPath
     * @return The string that represents the value of the attribute specified in the XPath and attributeName
     */
    public static String getXMLAttributeNotRequired(final Document doc, final String xPath, final String attributeName) {
        if (log.isDebugEnabled()) {
            log.debug("getXMLAttributeNotRequired 1 : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLAttributeNotRequired 1 : xPath = " + xPath);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLAttributeNotRequired 1 : attributeName = " + attributeName);
        }

        String retval = null;

        // Root Element, xPath
        Element xmlNode = getXMLElement(doc, xPath);

        if (xmlNode != null) {
            retval = xmlNode.getAttribute(attributeName);
        }

        if (log.isDebugEnabled()) {
            log.debug("getXMLAttributeNotRequired 1 : Method Finish");
        }
        return retval;
    }

    /**
     * Returns the value of the attribute specified in the xPath and attributeName from the
     * specified XML element. If multiple matching attributes exist, only the
     * first matching attribute is examined. If there is no matching attribute
     * or the XPath + attributeName does not evaluate to an attribute, the method returns a
     * null pointer.
     *
     * author Nick McKenna
     * @param root The XML element to search
     * @param xPath The XPath expression representing the element to find
     * @param attributeName The attribute to check inside the element specified in the XPath
     * @return The string that represents the value of the attribute specified in the XPath and attributeName
     */
    public static String getXMLAttributeNotRequired(final Element root, final String xPath, final String attributeName) {
        if (log.isDebugEnabled()) {
            log.debug("getXMLAttributeNotRequired 2 : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLAttributeNotRequired 2 : xPath = " + xPath);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLAttributeNotRequired 2 : attributeName = " + attributeName);
        }

        String retval = null;

        // Root Element, xPath
        Element xmlNode = getXMLElement(root, xPath);

        if (xmlNode != null) {
            retval = xmlNode.getAttribute(attributeName);
        }

        if (log.isDebugEnabled()) {
            log.debug("getXMLAttributeNotRequired 2 : Method Finish");
        }
        return retval;
    }

    /**
     * Take a complete XML document as a String and parse it into an Document.
     * The parser is set to non-validating mode.
     *
     * @param tmpIn The XML document (in String format) to parse
     * @return The Document object that represents the String in. This
     *         value will be null if the String is not a valid XML document or
     *         if an error (e.g. IO error) occurs during parsing.
     * @throws ServletFailure Servlet Failure.
     */
    public static Document parse(final String tmpIn) throws ServletFailure {
        if (log.isDebugEnabled()) {
            log.debug("parse : Method Start");
        }
        String in = tmpIn;
        if (log.isDebugEnabled()) {
            log.debug("parse : in = " + in);
        }

        Document theXMLDoc; // XML Document that is returned by this method

        // Delete entity references as the parser truncates text with these in!
        in = decodeEntityReferences(in);

        // Create a DOMParser to parse the document.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        factory.setValidating(false);
        factory.setExpandEntityReferences(false);

        // The parser instance
        DocumentBuilder theParser = null;

        try {
            theParser = factory.newDocumentBuilder();
            // M. Reilly 30/10/06 Added the following to ensure that the DTD is not validated.
            // Even if the Validation is turned off, the parser still looks for the DTD and will
            // fail if not found.
            theParser.setEntityResolver(new NoOpEntityResolver());

        } catch (ParserConfigurationException pce) {
            String err = "parse : Could not create the DOM parser";
            log.fatal(err + " : " + pce);
            // throw new ServletFailure(pce, "XMLSupport." + err);
            throw SystemCodes.generateServletFailure(SystemCodes.COMMON_OPERATION_FAILURE, "XMLSupport." + err);
        }

        // Try to parse the String
        try {
            InputStreamReader theInputStream = new InputStreamReader(new ByteArrayInputStream(in.getBytes("UTF8")), "UTF8");
            InputSource theStream = new InputSource(theInputStream);

            // Parse the document from the InputStream
            theXMLDoc = theParser.parse(theStream);

            // Close the stream as it is no longer required
            theInputStream.close();

        } catch (UnsupportedEncodingException uee) {
            String error = "parse 1 : Failed to Parse XML document : " + uee;
            log.info(error);
            // throw new ServletFailure(uee, "XMLSupport." + error);
            throw SystemCodes.generateServletFailure(SystemCodes.COMMON_OPERATION_FAILURE, "XMLSupport." + error);
        } catch (SAXParseException s) {
            String error = "parse 2 : Failed to Parse XML document : " + xmlError(s);
            log.info(error);
            // throw new ServletFailure(s, "XMLSupport." + error);
            throw SystemCodes.generateServletFailure(SystemCodes.COMMON_OPERATION_FAILURE, "XMLSupport." + error);
        } catch (IOException e) {
            String error = "parse 3 : Failed to Parse XML document : " + e;
            log.info(error);
            // throw new ServletFailure(e, "XMLSupport." + error);
            throw SystemCodes.generateServletFailure(SystemCodes.COMMON_OPERATION_FAILURE, "XMLSupport." + error);
        } catch (SAXException se) {
            String error = "parse 4 : Failed to Parse XML document : " + se;
            log.info(error);
            // throw new ServletFailure(se, "XMLSupport." + error);
            throw SystemCodes.generateServletFailure(SystemCodes.COMMON_OPERATION_FAILURE, "XMLSupport." + error);
        }

        if (theXMLDoc != null) {
            theXMLDoc.setDocumentURI("/");
        }

        if (log.isDebugEnabled()) {
            log.debug("parse : Method Finish");
        }
        return theXMLDoc;
    }

    /**
     * Convert a SAXParseException into a String detailing the error. The error
     * string will have this format:
     * <p>
     * XML parse error in file <i> filename </i> at line <i> line number </i>, character
     * <i> column number </i> : <i> ErrorMessage </i>
     *
     * @param s The exception to translate into a String
     * @return The String representing the exception parameter
     */
    public static String xmlError(final SAXParseException s) {
        if (log.isDebugEnabled()) {
            log.debug("xmlError : Method Start");
        }

        // Extract the details of the error location from the Exception
        int lineNum = s.getLineNumber();
        int colNum = s.getColumnNumber();
        String file = s.getSystemId();
        String err = s.getMessage();

        String finalError = "XML parse error in file " + file + " at line " + lineNum + ", character " + colNum + " : " + err;

        if (log.isDebugEnabled()) {
            log.debug("xmlError : return = " + finalError);
        }
        if (log.isDebugEnabled()) {
            log.debug("xmlError : Method Finish");
        }
        return finalError;
    }

    /**
     * Encode the given character occurences into a new string.
     *
     * @param inXmlstr The String to examine for occurences of the character to
     *               be encoded.
     * @param from   The character to be encoded into something else (to).
     * @param to     The String to replace the character 'from'.
     * @return       The newly encoded string
     */
    public static String encode(final String inXmlstr, final char from, final String to) {
        if (log.isDebugEnabled()) {
            log.debug("encode 1 : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("encode 1 : from = " + from);
        }
        if (log.isDebugEnabled()) {
            log.debug("encode 1 : to = " + to);
        }

        // Start index
        int begidx = 0;
        String xmlstr = inXmlstr;

        // Index of the next occurence of the character
        int quotidx = xmlstr.indexOf(from);

        // While their are more occurences of the character
        while (quotidx >= 0) {
            // Get the left substring, add the replacement string, add the right substring
            xmlstr = xmlstr.substring(0, quotidx) + to + xmlstr.substring(quotidx + 1);
            begidx = quotidx + 1; // skip quote

            // Find the next occurence of the character
            quotidx = xmlstr.indexOf(from, begidx);
        }

        if (log.isDebugEnabled()) {
            log.debug("encode 1 : Method Finish");
        }
        return xmlstr;
    }

    /**
     * Convert the request input stream into a text string.
     *
     * @param  request HTTP Servlet request to extract string from.
     * @return The String version of the document in the request object.
     * @throws ServletFailure Exception.
     */
    /*
     * TODO: remove this commented out code - this method is servlet-specific so shouldn't be in static-utilities
     * 
    public static String createString(final HttpServletRequest request) throws ServletFailure {
        if (log.isDebugEnabled()) {
            log.debug("createString : Method Start");
        }

        char ch = 'a'; // Next character to be read from the input stream
        String xmlStr = ""; // Final XML document in String format
        BufferedReader in = null; // Input stream of parameters

        // Only attempt to read a string if there is information on the stream
        if (request.getContentLength() > 0) {
            try {
                // Input stream of parameters
                in = request.getReader();

                // Read the first character from the stream
                int next = in.read();

                while (next != -1) {
                    // Translate the int into a character
                    ch = (char) next;
                    // Append to the string
                    xmlStr = xmlStr + ch;
                    // Read the next character
                    next = in.read();
                }
            } catch (IOException ioe) {
                String error = "createString : Failed to Create XML String.";
                log.fatal(error + " : " + ioe);
                // throw new ServletFailure(ioe, "XMLSupport." + error);
                throw SystemCodes.generateServletFailure(SystemCodes.COMMON_OPERATION_FAILURE, "XMLSupport." + error);
            }
        } else {
            xmlStr = null;
        }

        if (log.isDebugEnabled()) {
            log.debug("createString : Method Finish");
        }
        return xmlStr;
    }*/

    /**
     * Get the XML element that matches the specified pseudo-xPath expression.
     *
     * @param xPath The pseudo-xPath expression.
     * @param root The document to search
     * @return Element The element.
     */
    public static Element getXMLElement(final Document root, final String xPath) {
        if (log.isDebugEnabled()) {
            log.debug("getXMLElement (Doc) : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElement (Doc) : root = " + root);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElement (Doc) : xPath = " + xPath);
        }

        Element ret = null;
        String remainder = null;

        // Read the first token in the expression
        /*
         * //firstToken/NextToken
         */
        if (xPath != null) {
            String firstToken = extractFirstToken(xPath);
            if (firstToken != null) {
                remainder = extractRemainder(firstToken, xPath);

                /*
                 * Find the element for the firstToken This should really be root.getChildNodes(), but doing it this way allows us to easily
                 * ignore XML which has rubbish headers (e.g. SOAP envelope) at the top.
                 */
                NodeList nl = root.getElementsByTagName(firstToken);
                if (nl != null) {
                    // Try the first Element node
                    int count = 0;
                    while ((count < nl.getLength()) && (ret == null)) {
                        Node n = nl.item(count);
                        if (n instanceof Element) {
                            ret = (Element) n;
                            // Recursive call if there is more xPath to process
                            if ((remainder != null) && (remainder.length() > 0)) {
                                ret = getXMLElement(ret, remainder);
                            }
                        }
                        count++;
                    }
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("getXMLElement (Doc) : return = " + ret);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElement (Doc) : Method Finish");
        }
        return ret;
    }

    /**
     * Get the XML element that matches the specified pseudo-xPath expression.
     *
     * @param xPath The pseudo-xPath expression.
     * @param root The element to start the search at.
     * @return Element The element.
     */
    public static Element getXMLElement(final Element root, final String xPath) {
        if (log.isDebugEnabled()) {
            log.debug("getXMLElement (Ele) : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElement (Ele) : root = " + root);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElement (Ele) : xPath = " + xPath);
        }

        Element ret = null;
        String remainder = null;

        // Read the first token in the expression
        /*
         * //firstToken/NextToken
         */
        if (xPath != null) {
            String firstToken = extractFirstToken(xPath);
            if (firstToken != null) {
                remainder = extractRemainder(firstToken, xPath);

                /*
                 * Find the element for the firstToken This should really be root.getChildNodes(), but doing it this way allows us to easily
                 * ignore XML which has rubbish headers (e.g. SOAP envelope) at the top.
                 */
                NodeList nl = root.getElementsByTagName(firstToken);
                if (nl != null) {
                    // Try the first Element node
                    int count = 0;
                    while ((count < nl.getLength()) && (ret == null)) {
                        Node n = nl.item(count);
                        if (n instanceof Element) {
                            ret = (Element) n;
                            // Recursive call if there is more xPath to process
                            if ((remainder != null) && (remainder.length() > 0)) {
                                ret = getXMLElement(ret, remainder);
                            }
                        }
                        count++;
                    }
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("getXMLElement (Ele) : return = " + ret);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElement (Ele) : Method Finish");
        }
        return ret;
    }

    /**
     * Get XML Data from Element with Attributes.
     * @param root Root Element.
     * @param xPath  xPath
     * @param attribName Attribute Name.
     * @param attribValue Attribute Value.
     * @param elementName Element Name.
     * @return Value.
     */
    public static String getXMLDataFromElementWithAttr(final Element root, final String xPath, final String attribName,
            final String attribValue, final String elementName) {
        if (log.isDebugEnabled()) {
            log.debug("getXMLDataFromElementWithAttr : Method Start");
        }
        Element e1 = null;
        String ret = null;

        NodeList nl = XMLSupport.getElements(root, xPath);
        if (log.isDebugEnabled()) {
            log.debug("getXMLDataFromElementWithAttr : Number of items in List = " + nl.getLength());
        }

        if (nl != null) {
            e1 = XMLSupport.findMatchIgnoreCase(nl, attribName, attribValue);
            if (log.isDebugEnabled()) {
                log.debug("getXMLDataFromElementWithAttr : A");
            }
            if (e1 != null) {
                if (log.isDebugEnabled()) {
                    log.debug("getXMLDataFromElementWithAttr : Found requested attrib value");
                }
                ret = XMLSupport.getXMLElementText(e1, elementName);
                if (log.isDebugEnabled()) {
                    log.debug("getXMLDataFromElementWithAttr : B");
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("getXMLDataFromElementWithAttr : return = " + ret);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLDataFromElementWithAttr : Method Finish");
        }
        return ret;
    }

    /**
     * Find the first token in the expression. Strip off leading /'s.
     *
     * @param inXPath The pseudo-xPath expression.
     * @return The next token in the xPath
     */
    private static String extractFirstToken(final String inXPath) {
        if (log.isDebugEnabled()) {
            log.debug("extractFirstToken : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("extractFirstToken : xPath = " + inXPath);
        }

        String nextToken = null;
        String xPath = inXPath;

        // Strip off leading /'s
        while ((xPath.length() > 0) && (xPath.charAt(0) == '/')) {
            xPath = xPath.substring(1);
        }

        // Read up until the next / or the end of the String
        int nextSlash = xPath.indexOf('/');
        if (nextSlash == -1) {
            nextToken = xPath;
        } else {
            nextToken = xPath.substring(0, nextSlash);
        }

        if (log.isDebugEnabled()) {
            log.debug("extractFirstToken : return = " + nextToken);
        }
        if (log.isDebugEnabled()) {
            log.debug("extractFirstToken : Method Finish");
        }
        return nextToken;
    }

    /**
     * Return the substring of xPath that does not contain the first occurence
     * of token.
     *
     * @param token The token to remove from the xPath
     * @param xPath The xPath to create a substring of
     * @return The substring of xPath
     */
    private static String extractRemainder(final String token, final String xPath) {
        if (log.isDebugEnabled()) {
            log.debug("extractRemainder : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("extractRemainder : token = " + token);
        }
        if (log.isDebugEnabled()) {
            log.debug("extractRemainder : xPath = " + xPath);
        }

        // Find the start and end point of the token
        int startIndex = xPath.indexOf(token);
        int endIndex = startIndex + token.length();

        if (log.isDebugEnabled()) {
            log.debug("extractRemainder : return = " + xPath.substring(endIndex));
        }
        if (log.isDebugEnabled()) {
            log.debug("extractRemainder : Method Finish");
        }
        return xPath.substring(endIndex);
    }

    /**
     * Get all the elements that match the xPath description.
     *
     * @param doc Document to search
     * @param xPath The description of nodes to find
     * @return The list of matching nodes
     */
    public static NodeList getElements(final Document doc, final String xPath) {
        if (log.isDebugEnabled()) {
            log.debug("getElements (Doc) : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("getElements (Doc) : doc = " + doc);
        }
        if (log.isDebugEnabled()) {
            log.debug("getElements (Doc) : xPath = " + xPath);
        }

        NodeList ret = null;
        String remainder = null;

        // Read the first token in the expression
        /*
         * //firstToken/NextToken
         */
        if (xPath != null) {
            String firstToken = extractFirstToken(xPath);
            if (firstToken != null) {
                remainder = extractRemainder(firstToken, xPath);

                /*
                 * Find the element for the firstToken This should really be root.getChildNodes(), but doing it this way allows us to easily
                 * ignore XML which has rubbish headers (e.g. SOAP envelope) at the top.
                 */
                NodeList nl = doc.getElementsByTagName(firstToken);
                if (nl != null) {
                    if (remainder.length() == 0) {
                        ret = nl;
                    } else {
                        Node n = nl.item(0);
                        if (n != null) {
                            if (n instanceof Element) {
                                ret = getElements(((Element) n), remainder);
                            }
                        }
                    }
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("getElements (Doc) : return = " + ret);
        }
        if (log.isDebugEnabled()) {
            log.debug("getElements (Doc) : Method Finish");
        }
        return ret;
    }

    /**
     * Get all the elements that match the xPath description.
     *
     * @param node Node to search from
     * @param xPath The description of nodes to find
     * @return The list of matching nodes
     */
    public static NodeList getElements(final Element node, final String xPath) {
        if (log.isDebugEnabled()) {
            log.debug("getElements (Ele) : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("getElements (Ele) : node = " + node);
        }
        if (log.isDebugEnabled()) {
            log.debug("getElements (Ele) : xPath = " + xPath);
        }

        NodeList ret = null;
        String remainder = null;

        if (xPath != null) {
            String firstToken = extractFirstToken(xPath);
            if (firstToken != null) {
                remainder = extractRemainder(firstToken, xPath);

                /*
                 * Find the element for the firstToken This should really be root.getChildNodes(), but doing it this way allows us to easily
                 * ignore XML which has rubbish headers (e.g. SOAP envelope) at the top.
                 */
                NodeList nl = node.getElementsByTagName(firstToken);
                if (nl != null) {
                    if (remainder.length() == 0) {
                        ret = nl;
                    } else {
                        Node n = nl.item(0);
                        if (n != null) {
                            if (n instanceof Element) {
                                ret = getElements(((Element) n), remainder);
                            }
                        }
                    }
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("getElements (Ele) : return = " + ret);
        }
        if (log.isDebugEnabled()) {
            log.debug("getElements (Ele) : Method Finish");
        }
        return ret;
    }

    /**
     * Find the data contained within a specified XML element. The XML element is
     * specified by an xPath, an attribute and an atribute value. The method
     * searches for all elements that match the xPath. It then searches through
     * these elements to find the first element with an attribute of the specified
     * name with the specified value.
     *
     * @param doc The XML document to search
     * @param xPath The xPath to the matching elements
     * @param attribName The name of the attribute to search for
     * @param attribValue The value of the attribute
     * @return The value contained within the found XML element
     * @throws ServletFailure Exception.
     */
    public static String getXMLElementDataWithAttr(final Document doc, final String xPath, final String attribName, final String attribValue)
            throws ServletFailure {
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementDataWithAttr : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementDataWithAttr : doc = " + doc);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementDataWithAttr : xPath = " + xPath);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementDataWithAttr : attribName = " + attribName);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementDataWithAttr : attribValue = " + attribValue);
        }

        String value = null;

        // Find the XML elements that match the xPath
        NodeList nl = getElements(doc, xPath);

        // Check that the node list was found and it has elements in it
        if ((nl != null) && (nl.getLength() > 0)) {
            // Find the XML element that matches the attrib name/value pair
            Element e = findMatch(nl, attribName, attribValue);

            // Check that an element was found
            if (e != null) {
                // Find the value of the XML element
                value = extractXMLText(e);
            } else {
                String error =
                        "getXMLElementDataWithAttr: Failed to find XML element with " + "attribute name/value pair. Document = " + doc
                                + ", xPath = " + xPath + ", attribute name = " + attribName + ", attribute value = " + attribValue;
                log.fatal(error);
                // throw new ServletFailure("XMLSupport." + error);
                throw SystemCodes.generateServletFailure(SystemCodes.COMMON_OPERATION_FAILURE, "XMLSupport." + error);
            }
        } else {
            String error =
                    "getXMLElementDataWithAttr: Failed to find XML element with " + "attribute name/value pair. Document = " + doc
                            + ", xPath = " + xPath + ", attribute name = " + attribName + ", attribute value = " + attribValue;
            log.fatal(error);
            // throw new ServletFailure("XMLSupport." + error);
            throw SystemCodes.generateServletFailure(SystemCodes.COMMON_OPERATION_FAILURE, "XMLSupport." + error);
        }

        if (log.isDebugEnabled()) {
            log.debug("getXMLElementDataWithAttr : return = " + value);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementDataWithAttr : Method Finished");
        }
        return value;
    }

    /**
     * Find the data contained within a specified XML element. The XML element is
     * specified by an xPath, an attribute and an atribute value. The method
     * searches for all elements that match the xPath. It then searches through
     * these elements to find the first element with an attribute of the specified
     * name with the specified value.
     * Does NOT throw an EXCEPTION if the required element is not found.
     *
     * @param doc XML Document.
     * @param xPath XPath.
     * @param attribName Attribute Name.
     * @param attribValue Attribute Value.
     * @return The contents of the element
     */
    public static String getXMLElementDataWithAttrNotRequired(final Document doc, final String xPath, final String attribName,
            final String attribValue) {
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementDataWithAttrNotRequired : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementDataWithAttrNotRequired : doc = " + doc);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementDataWithAttrNotRequired : xPath = " + xPath);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementDataWithAttrNotRequired : attribName = " + attribName);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementDataWithAttrNotRequired : attribValue = " + attribValue);
        }

        String value = null;

        // Find the XML elements that match the xPath
        NodeList nl = getElements(doc, xPath);

        // Check that the node list was found and it has elements in it
        if ((nl != null) && (nl.getLength() > 0)) {
            // Find the XML element that matches the attrib name/value pair
            Element e = findMatch(nl, attribName, attribValue);

            // Check that an element was found
            if (e != null) {
                // Find the value of the XML element
                value = extractXMLText(e);
            } else {
                String error =
                        "getXMLElementDataWithAttrNotRequired : Failed to find XML element with "
                                + "attribute name/value pair. Document = " + doc + ", xPath = " + xPath + ", attribute name = "
                                + attribName + ", attribute value = " + attribValue;
                if (log.isDebugEnabled()) {
                    log.debug(error);
                }
            }
        } else {
            String error =
                    "getXMLElementDataWithAttrNotRequired : Failed to find XML element. " + "Document = " + doc + ", xPath = " + xPath;
            log.fatal(error);
        }

        if (log.isDebugEnabled()) {
            log.debug("getXMLElementDataWithAttrNotRequired : return = " + value);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementDataWithAttrNotRequired : Method Finished");
        }
        return value;
    }

    /**
     * Find the attribute contained within a specified XML element. The XML element is
     * specified by an xPath, an attribute and an atribute value. The method
     * searches for all elements that match the xPath. It then searches through
     * these elements to find the first element with an attribute of the specified
     * name with the specified value.
     *
     * @param doc The XML document to search
     * @param xPath The xPath to the matching elements
     * @param attribName The name of the attribute to search for
     * @param attribValue The value of the attribute
     * @param targetAttribName Target Attribute Name.
     * @return The attribute contained within the found XML element
     */
    public static String getXMLElementAttrWithAttr(final Document doc, final String xPath, final String attribName,
            final String attribValue, final String targetAttribName) {
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementAttrWithAttr : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementAttrWithAttr : doc = " + doc);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementAttrWithAttr : xPath = " + xPath);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementAttrWithAttr : attribName = " + attribName);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementAttrWithAttr : attribValue = " + attribValue);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementAttrWithAttr : targetAttribName = " + targetAttribName);
        }

        String value = null;

        // Find the XML elements that match the xPath
        NodeList nl = getElements(doc, xPath);

        // Check that the node list was found and it has elements in it
        if ((nl != null) && (nl.getLength() > 0)) {
            // Find the XML element that matches the attrib name/value pair
            Element e = findMatch(nl, attribName, attribValue);

            // Check that an element was found
            if (e != null) {
                // Find the value of the XML element
                value = e.getAttribute(targetAttribName);
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("getXMLElementAttrWithAttr : return = " + value);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementAttrWithAttr : Method Finished");
        }
        return value;
    }

    /**
     * Find the XML element that matches the ??.
     *
     * @param nl Node list to search through
     * @param attribName Name of the attribute to match on
     * @param attribValue Value of the attribute to match to
     * @return The first element in the node list that matches the specified
     *         name / value pair
     */
    public static Element findMatchValue(final NodeList nl, final String attribName, final String attribValue) {
        if (log.isDebugEnabled()) {
            log.debug("findMatchValue : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("findMatchValue : nl = " + nl);
        }
        if (log.isDebugEnabled()) {
            log.debug("findMatchValue : attribName = " + attribName);
        }
        if (log.isDebugEnabled()) {
            log.debug("findMatchValue : attribValue = " + attribValue);
        }

        Element target = null;
        int size = nl.getLength(); // Size of the list

        // Find the first element in the list that matches the specified criteria
        int count = 0;
        while ((target == null) && (count < size)) {
            // Get the next node
            Node next = nl.item(count);
            if (next instanceof Element) {
                // Check the element for a specified attribute
                Element e = (Element) next;
                String attribVal = "";
                try {
                    attribVal = getXMLElementText(e, attribName);
                } catch (Exception ex) {
                    log.error("findMatchValue : An exception occurred, detail was : " + ex.toString());
                }

                // If the String matches the attribValue, we have the correct node
                if ((attribVal != null) && (attribVal.equals(attribValue))) {
                    target = e;
                }
            }
            count++;
        }

        if (log.isDebugEnabled()) {
            log.debug("findMatchValue : return = " + target);
        }
        if (log.isDebugEnabled()) {
            log.debug("findMatchValue : Method Finish");
        }
        return target;
    }

    /**
     * Find the XML element that matches the attrib name/value pair.
     *
     * @param nl Node list to search through
     * @param attribName Name of the attribute to match on
     * @param attribValue Value of the attribute to match to
     * @return The first element in the node list that matches the specified
     *         name / value pair
     */
    public static Element findMatch(final NodeList nl, final String attribName, final String attribValue) {
        if (log.isDebugEnabled()) {
            log.debug("findMatch : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("findMatch : nl = " + nl);
        }
        if (log.isDebugEnabled()) {
            log.debug("findMatch : attribName = " + attribName);
        }
        if (log.isDebugEnabled()) {
            log.debug("findMatch : attribValue = " + attribValue);
        }

        Element target = null;
        int size = nl.getLength(); // Size of the list

        // Find the first element in the list that matches the specified
        // criteria
        int count = 0;
        while ((target == null) && (count < size)) {
            // Get the next node
            Node next = nl.item(count);
            if (next instanceof Element) {
                // Check the element for a specified attribute
                Element e = (Element) next;
                String attribVal = e.getAttribute(attribName);
                // If the String matches the attribValue, we have the correct node
                if (attribVal.equals(attribValue)) {
                    target = e;
                }
            }

            count++;
        }

        if (log.isDebugEnabled()) {
            log.debug("findMatch : return = " + target);
        }
        if (log.isDebugEnabled()) {
            log.debug("findMatch : Method Finish");
        }
        return target;
    }

    /**
     * Find the XML element that matches the attrib name/value pair.
     *
     * @param nl Node list to search through
     * @param attribName Name of the attribute to match on
     * @param attribValue Value of the attribute to match to
     * @return The first element in the node list that matches the specified
     *         name / value pair
     */
    public static Element findMatchIgnoreCase(final NodeList nl, final String attribName, final String attribValue) {
        if (log.isDebugEnabled()) {
            log.debug("findMatchIgnoreCase : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("findMatchIgnoreCase : nl = " + nl);
        }
        if (log.isDebugEnabled()) {
            log.debug("findMatchIgnoreCase : attribName = " + attribName);
        }
        if (log.isDebugEnabled()) {
            log.debug("findMatchIgnoreCase : attribValue = " + attribValue);
        }

        Element target = null;
        int size = nl.getLength(); // Size of the list

        // Find the first element in the list that matches the specified
        // criteria
        int count = 0;
        while ((target == null) && (count < size)) {
            // Get the next node
            Node next = nl.item(count);
            if (next instanceof Element) {
                // Check the element for a specified attribute
                Element e = (Element) next;
                String attribVal = e.getAttribute(attribName);
                // If the String matches the attribValue, we have the correct node
                if (attribVal.equalsIgnoreCase(attribValue)) {
                    target = e;
                }
            }
            count++;
        }

        if (log.isDebugEnabled()) {
            log.debug("findMatchIgnoreCase : return = " + target);
        }
        if (log.isDebugEnabled()) {
            log.debug("findMatchIgnoreCase : Method Finish");
        }
        return target;
    }

    /**
     * Extract the character data from a specified XML element.
     *
     * @param xmlNode The node to extract character data from
     * @return The character data contained in the node. Returns null if no
     *         character data could be found
     */
    public static String extractXMLText(final Element xmlNode) {
        if (log.isDebugEnabled()) {
            log.debug("extractXMLText : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("extractXMLText : xmlNode = " + xmlNode);
        }

        String theData = null;

        // Check that the parameter is valid
        if (xmlNode != null) {
            theData = "";

            // Get the child nodes of the selected Element
            NodeList nl = xmlNode.getChildNodes();

            // Check for a null pointer
            if (nl != null) {
                // Search through the child nodes of this Element until the text node
                // is found
                int len = nl.getLength();

                int count = 0;
                while (count < len) {
                    Node next = nl.item(count);

                    // If the node contains character data, extract it and stop searching
                    if ((next != null) && (next instanceof CharacterData)) {
                        theData += (((CharacterData) next).getData());
                        if (log.isDebugEnabled()) {
                            log.debug("extractXMLText : APPENDING CHARACTERDATA");
                        }
                    } else if (next instanceof EntityReference) {
                        NodeList childNodes = next.getChildNodes();
                        int numChildNodes = childNodes.getLength();

                        int localCount = 0;
                        while (localCount < numChildNodes) {
                            Node localNode = childNodes.item(localCount);
                            String val = localNode.getNodeValue();
                            theData += val;
                            localCount++;
                        }
                    }

                    // Try the next node in the list
                    count++;
                } // end while
            } // end if
        } // end if

        if (log.isDebugEnabled()) {
            log.debug("extractXMLText : return = " + theData);
        }
        if (log.isDebugEnabled()) {
            log.debug("extractXMLText : Method Finish");
        }
        return theData;
    }

    /**
     * Find the element that matches the xPath. This does a comprehensive search
     * of all possible paths and returns a list of matching nodes.
     * @param doc The XML Document.
     * @param xPath The xPath.
     * @return List of Nodes
     */
    public static List getXMLElementsPrecise(final Document doc, final String xPath) {
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementsPrecise 1 : Method Start");
        }

        List ret = new LinkedList();
        String remainder = null;

        if (xPath != null) {
            String firstToken = extractFirstToken(xPath);
            if (firstToken != null) {
                remainder = extractRemainder(firstToken, xPath);

                /*
                 * Find the element for the firstToken This should really be root.getChildNodes(), but doing it this way allows us to easily
                 * ignore XML which has rubbish headers (e.g. SOAP envelope) at the top.
                 */
                NodeList nl = doc.getElementsByTagName(firstToken);
                if (nl != null) {
                    if (remainder.length() == 0) {
                        ret.add(nl);
                    } else {
                        int sizeList = nl.getLength();
                        for (int count = 0; count < sizeList; count++) {
                            Node n = nl.item(count);
                            if (n != null) {
                                if (n instanceof Element) {
                                    List nextNodes = getXMLElementsPrecise(((Element) n), remainder);
                                    ret.add(nextNodes);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Sort list into a list of only nodelists
        List realRet = createListOfNodelist(ret);
        realRet = createListOfNodes(realRet);

        if (log.isDebugEnabled()) {
            log.debug("getXMLElementsPrecise 1 : Method Finish");
        }
        return realRet;
    }

    /**
     * Get all the elements that match the xPath description.
     * @param node The node.
     * @param xPath The xPath.
     * @return List of NodeLists
     */
    private static List getXMLElementsPrecise(final Element node, final String xPath) {
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementsPrecise 2 : Method Start");
        }

        List ret = new LinkedList();
        String remainder = null;

        // Read the first token in the expression
        /*
         * //firstToken/NextToken
         */
        if (xPath != null) {
            String firstToken = extractFirstToken(xPath);
            if (firstToken != null) {
                remainder = extractRemainder(firstToken, xPath);

                /*
                 * Find the element for the firstToken This should really be root.getChildNodes(), but doing it this way allows us to easily
                 * ignore XML which has rubbish headers (e.g. SOAP envelope) at the top.
                 */
                NodeList nl = node.getElementsByTagName(firstToken);
                if (nl != null) {
                    if (remainder.length() == 0) {
                        ret.add(nl);
                    } else {
                        int sizeList = nl.getLength();
                        for (int count = 0; count < sizeList; count++) {
                            Node n = nl.item(count);
                            if (n != null) {
                                if (n instanceof Element) {
                                    List nextNodes = getXMLElementsPrecise(((Element) n), remainder);
                                    ret.add(nextNodes);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("getXMLElementsPrecise 2 : Method Finish");
        }
        return ret;
    }

    /**
     * Get all the elements that match the xPath description.
     * @param node The node.
     * @param xPath The xPath.
     * @return List of NodeLists
     */
    public static List getXMLElementsPreciseFromElement(final Element node, final String xPath) {
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementsPreciseFromElement : Method Start");
        }

        List ret = new LinkedList();
        String remainder = null;

        if (xPath != null) {
            String firstToken = extractFirstToken(xPath);
            if (firstToken != null) {
                remainder = extractRemainder(firstToken, xPath);

                /*
                 * Find the element for the firstToken This should really be root.getChildNodes(), but doing it this way allows us to easily
                 * ignore XML which has rubbish headers (e.g. SOAP envelope) at the top.
                 */
                NodeList nl = node.getElementsByTagName(firstToken);
                if (nl != null) {
                    if (remainder.length() == 0) {
                        ret.add(nl);
                    } else {
                        int sizeList = nl.getLength();
                        for (int count = 0; count < sizeList; count++) {
                            Node n = nl.item(count);
                            if (n != null) {
                                if (n instanceof Element) {
                                    List nextNodes = getXMLElementsPrecise(((Element) n), remainder);
                                    ret.add(nextNodes);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Sort list into a list of only nodelists
        List realRet = createListOfNodelist(ret);
        realRet = createListOfNodes(realRet);

        if (log.isDebugEnabled()) {
            log.debug("getXMLElementsPreciseFromElement : Method Finish");
        }
        return realRet;
    }

    /**
     * Create a list of NodeLists from a list of lists/nodelists.
     * @param source The list of nodelists.
     * @return The list of nodeslists.
     */
    private static List createListOfNodelist(final List source) {
        if (log.isDebugEnabled()) {
            log.debug("createListOfNodeList : Method Start");
        }

        List ret = new LinkedList();

        // Get a list of all nodes in the list
        Iterator i = source.iterator();
        while (i.hasNext()) {
            Object o = i.next();
            // Extract all nodelists
            if (o instanceof NodeList) {
                ret.add(o);
            } else {
                // Extract all nodelists from the list
                List temp = createListOfNodelist((List) o);
                Iterator j = temp.iterator();
                while (j.hasNext()) {
                    ret.add(j.next());
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("createListOfNodeList : Method Finish");
        }
        return ret;
    }

    /**
     * Takes a list of NodeLists and turns them into a List of Nodes.
     * @param source The list of nodelists.
     * @return The list of nodes.
     */
    private static List createListOfNodes(final List source) {
        if (log.isDebugEnabled()) {
            log.debug("createListOfNodes : Method Start");
        }

        List ret = new LinkedList();

        Iterator i = source.iterator();
        while (i.hasNext()) {
            NodeList nl = (NodeList) i.next();
            int size = nl.getLength();
            for (int count = 0; count < size; count++) {
                ret.add(nl.item(count));
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("createListOfNodes : Method Finish");
        }
        return ret;
    }

    /**
     * Retrieve the textual data from within a specified XML document element.
     * @param doc The Document.
     * @param xPath The xpath.
     * @return Returns the text String from the XML document. If the text
     * cannot be found, the return value is null.
     */
    public static String getXMLElementTextNotRequired(final Document doc, final String xPath) {
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementTextNotRequired 1 : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementTextNotRequired 1 : xPath = " + xPath);
        }

        // The extracted data
        String theData = null;

        // Root Element, xPath
        Element xmlNode = getXMLElement(doc, xPath);

        // Get the text from the found node
        if (xmlNode != null) {
            theData = extractXMLText(xmlNode);
        }

        if (log.isDebugEnabled()) {
            log.debug("getXMLElementTextNotRequired 1 : returning - " + theData);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementTextNotRequired 1 : Method Finish");
        }
        return theData;
    }

    /**
     * Retrieve the textual data from within a specified XML element.
     * @param root The root element.
     * @param xPath The xpath.
     * @return Returns the text String from the XML document. If the text
     * cannot be found, the return value is null.
     */
    public static String getXMLElementTextNotRequired(final Element root, final String xPath) {
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementTextNotRequired 2 : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementTextNotRequired 2 : xPath = " + xPath);
        }

        // The extracted data
        String theData = null;

        // Root Element, xPath
        Element xmlNode = getXMLElement(root, xPath);

        // Get the text from the found node
        if (xmlNode != null) {
            theData = extractXMLText(xmlNode);
        }

        if (log.isDebugEnabled()) {
            log.debug("getXMLElementTextNotRequired 2 : returning - " + theData);
        }
        if (log.isDebugEnabled()) {
            log.debug("getXMLElementTextNotRequired 2 : Method Finish");
        }
        return theData;
    }

    /**
     * Concatenate the text of the elements that match the xPath.
     * @param doc The xml document.
     * @param xPath The xpath.
     * @param maxLen The maximum length of the test.
     * @return The concatenated text.
     */
    public static String concatenateXMLElementText(final Document doc, final String xPath, final int maxLen) {
        if (log.isDebugEnabled()) {
            log.debug("concatenateXMLElementText 1 : Method Start");
        }
        // Get the matching elements
        List elements = getXMLElementsPrecise(doc, xPath);

        String ret = "";
        boolean first = true;

        // Extract the text from each node in turn
        Iterator i = elements.iterator();
        while (i.hasNext()) {
            Object o = i.next();
            if (o instanceof Element) {
                Element xmlNode = (Element) o;
                String temp = extractXMLText(xmlNode);
                if ((temp != null) && (containsCharacters(temp))) {
                    if (first) {
                        ret += temp;
                        first = false;
                    } else {
                        ret += (", " + temp);
                    }
                }
            }
        }

        // Limit to the specified size
        if (ret.length() > maxLen) {
            ret = ret.substring(0, maxLen);
        }

        if (log.isDebugEnabled()) {
            log.debug("concatenateXMLElementText 1 : Method Finish");
        }
        return ret;
    }

    /**
     * Concatenate the text of the elements that match the xPath.
     * @param doc The xml document.
     * @param xPath The xpath.
     * @param maxLen The maximum length of the test.
     * @return The concatendated text.
     */
    public static String concatenateXMLElementText(final Element doc, final String xPath, final int maxLen) {
        if (log.isDebugEnabled()) {
            log.debug("concatenateXMLElementText 2 : Method Start");
        }
        // Get the matching elements
        List elements1 = getXMLElementsPrecise(doc, xPath);
        // Sort list into a list of only nodelists
        List elements2 = createListOfNodelist(elements1);
        List elements = createListOfNodes(elements2);

        String ret = "";
        boolean first = true;

        // Extract the text from each node in turn
        Iterator i = elements.iterator();
        while (i.hasNext()) {
            Object o = i.next();
            if (o instanceof Element) {
                Element xmlNode = (Element) o;
                String temp = extractXMLText(xmlNode);
                if ((temp != null) && (containsCharacters(temp))) {
                    if (first) {
                        ret += temp;
                        first = false;
                    } else {
                        ret += (", " + temp);
                    }
                }
            }
        }

        // Limit to the specified size
        if (ret.length() > maxLen) {
            ret = ret.substring(0, maxLen);
        }

        if (log.isDebugEnabled()) {
            log.debug("concatenateXMLElementText 2 : Method Finish");
        }
        return ret;
    }

    /**
     * Look for a non-whitespace character.
     * @param testString The string to check against.
     * @return True if contains characters.
     */
    private static boolean containsCharacters(final String testString) {
        if (log.isDebugEnabled()) {
            log.debug("containsCharacters : Method Start");
        }
        int length = testString.length();
        int count = 0;
        boolean found = false;

        while ((count < length) && (!found)) {
            found = (!(Character.isWhitespace(testString.charAt(count))));
            count++;
        }

        if (log.isDebugEnabled()) {
            log.debug("containsCharacters : Method Finish");
        }
        return found;
    }

    /**
     * Encode a string.
     * @param xmlstr The string to encode.
     * @return The string encoded.
     */
    public static String encode(final String xmlstr) {
        if (log.isDebugEnabled()) {
            log.debug("encode 2 : Method Start");
        }
        if (log.isDebugEnabled()) {
            log.debug("encode 2 : Method End");
        }
        return encode(xmlstr, true);
    }

    /**
      * Encode the String into valid xml content.
      *
      * @param xmlstr The String to examine for occurences of the character to be encoded.
      * @param doubleEncode Check for double or single HTML encoding of "&" characters.
      * @return The newly encoded string
      **/
    public static String encode(final String xmlstr, final boolean doubleEncode) {
        if (log.isDebugEnabled()) {
            log.debug("encode 3 : Method Start");
        }
        StringBuffer ret = new StringBuffer();

        for (int count = 0; count < xmlstr.length(); count++) {
            char next = xmlstr.charAt(count);
            int hashValue = new Character(next).hashCode();

            // If the character is from the ISO-8859-1 (Latin 1) character set it has to be encoded
            // to work correctly with the Parsers.
            if (hashValue > 159 && hashValue < 256) {
                if (doubleEncode) {
                    ret.append("&amp;#" + hashValue + ";");
                } else {
                    ret.append("&#" + hashValue + ";");
                }
            } else {

                switch (next) {
                case '<':
                    ret.append("&lt;");
                    break;

                case '>':
                    ret.append("&gt;");
                    break;

                case '\'':
                    ret.append("&apos;");
                    break;

                case '\"':
                    ret.append("&quot;");
                    break;

                case '&':
                    if (doubleEncode) {
                        ret.append("&amp;amp;");
                    } else {
                        ret.append("&amp;");
                    }
                    break;

                default:
                    ret.append(next);
                    break;
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("encode 3 : Method Finish");
        }
        return ret.toString();
    }

    /**
     * Decode any Entity References in the supplied String.
     * @param tmpSource The source document.
     * @return The string containing the decoded entity references.
     */
    private static String decodeEntityReferences(final String tmpSource) {
        if (log.isDebugEnabled()) {
            log.debug("decodeEntityReferences : Method Start");
        }

        String source = tmpSource;
        source = replaceString(source, "&quot;", "");
        source = replaceString(source, "&apos;", "");
        source = replaceString(source, "&lt;", "");
        source = replaceString(source, "&gt;", "");

        if (log.isDebugEnabled()) {
            log.debug("decodeEntityReferences : Method Finish");
        }
        return source;
    }

    /**
     * Search the source String for the search String and replace it with the
     * replace String.
     * @param inSource The Source as a string.
     * @param search Search Term.
     * @param replace The value to replace with.
     * @return The replaced character string.
     */
    private static String replaceString(final String inSource, final String search, final String replace) {
        if (log.isDebugEnabled()) {
            log.debug("replaceString : Method Start");
        }

        String source = inSource;
        int searchLen = search.length();

        int index = source.indexOf(search);
        while (index != -1) {
            StringBuffer temp = new StringBuffer(source.length());

            // Do the replace
            temp.append(source.substring(0, index));
            temp.append(replace);
            if ((index + searchLen) != source.length()) {
                temp.append(source.substring(index + searchLen));
            }

            // Convert back to a String
            source = temp.toString();

            // Find the next match
            index = source.indexOf(search);
        }
        if (log.isDebugEnabled()) {
            log.debug("replaceString : Method Finish");
        }
        return source;
    }

    /**
     * Get the data out of a character data node.
     * @param source The Source.
     * @return The character data as a string.
     */
    public static String getXMLNodeCharData(final Node source) {
        if (log.isDebugEnabled()) {
            log.debug("getXMLNodeCharData : Method Start");
        }
        String theData = null;

        // Get the child nodes of the selected Element
        NodeList nl = source.getChildNodes();

        // Search through the child nodes of this Element until the text node
        // is found
        int len = nl.getLength();
        int count = 0;
        while ((theData == null) && (count < len)) {
            Node next = nl.item(count);
            // If the node contains character data, extract it and stop searching
            if (next instanceof CharacterData) {
                theData = ((CharacterData) next).getData();
            }

            // Try the next node in the list
            count++;
        }

        if (theData == null) {
            theData = "";
        }

        if (log.isDebugEnabled()) {
            log.debug("getXMLNodeCharData : Method Finish");
        }
        return theData;
    }

    /**
     * Find the text value of an XML element which has a sibling with a specified
     * text value.
     *
     * @param doc The XMl document to search
     * @param baseXPath The xPath to the element that holds the siblings to be checked.
     *                  This xPath specifies a number of nodes that hold pairs of siblings.
     * @param subElementXPath The xPath from a parent element to a sibling that will
     *                        contain a known identifying string.
     * @param identifyingValue The node with this value in a specified sibling is
     *                         the matching node.
     * @param targetSubElementXPath The sibling that contains the value to be
     *                              retrieved in specified by this xPath.
     * @return String The element text.
     */
    public static String selectMatchingElementText(final Document doc, final String baseXPath, final String subElementXPath,
            final String identifyingValue, final String targetSubElementXPath) {
        if (log.isDebugEnabled()) {
            log.debug("selectMatchingElementText 1 : Method Start");
        }
        String target = "";

        // Find the possible matching nodes
        List matches = XMLSupport.getXMLElementsPrecise(doc, baseXPath);

        // Find the correct one of the potential nodes
        boolean found = false;
        Iterator i = matches.iterator();
        while (i.hasNext() && (!found)) {
            Node next = (Node) i.next();
            // The target node has a sub-node with the name ContactNumberTypeCoded
            if (next instanceof Element) {
                Element e = (Element) next;
                String text = XMLSupport.getXMLElementText(e, subElementXPath);
                if (identifyingValue.equalsIgnoreCase(text)) {
                    // When a match is found, set the phone number
                    found = true;
                    target = XMLSupport.getXMLElementText(e, targetSubElementXPath);
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("selectMatchingElementText 1 :- Method Finish");
        }
        return target;
    }

    /**
     * Find the text value of an XML element which has a sibling with a specified
     * text value.
     *
     * @param element The XMl element to search
     * @param baseXPath The xPath to the element that holds the siblings to be checked.
     *                  This xPath specifies a number of nodes that hold pairs of siblings.
     * @param subElementXPath The xPath from a parent element to a sibling that will
     *                        contain a known identifying string.
     * @param identifyingValue The node with this value in a specified sibling is
     *                         the matching node.
     * @param targetSubElementXPath The sibling that contains the value to be
     *                              retrieved in specified by this xPath.
     * @return String The element text.
     */
    public static String selectMatchingElementText(final Element element, final String baseXPath, final String subElementXPath,
            final String identifyingValue, final String targetSubElementXPath) {
        if (log.isDebugEnabled()) {
            log.debug("selectMatchingElementText 2 : Method Start");
        }
        String target = "";

        // Find the possible matching nodes
        List matches = XMLSupport.getXMLElementsPreciseFromElement(element, baseXPath);

        // Find the correct one of the potential nodes
        boolean found = false;
        Iterator i = matches.iterator();
        while (i.hasNext() && (!found)) {
            Node next = (Node) i.next();
            // The target node has a sub-node with the name ContactNumberTypeCoded
            if (next instanceof Element) {
                Element e = (Element) next;
                String text = XMLSupport.getXMLElementText(e, subElementXPath);
                if (identifyingValue.equalsIgnoreCase(text)) {
                    // When a match is found, set the phone number
                    found = true;
                    target = XMLSupport.getXMLElementText(e, targetSubElementXPath);
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("selectMatchingElementText 2 : Method Finish");
        }

        return target;
    }

    /**
     * Encode the String into valid html content.
     *
     * @param htmlstr The String to examine for occurences of the characters to
     *               be encoded.
     * @return       The newly encoded string
     */
    public static String encodeHtml(final String htmlstr) {
        if (log.isDebugEnabled()) {
            log.debug("encodeHtml : Method Start");
        }
        StringBuffer ret = new StringBuffer((int) (htmlstr.length() * 1.1));

        for (int count = 0; count < htmlstr.length(); count++) {
            char next = htmlstr.charAt(count);
            switch (next) {
            case '<':
                ret.append("&lt;");
                break;

            case '>':
                ret.append("&gt;");
                break;

            case '&':
                ret.append("&amp;");
                break;

            default:
                ret.append(next);
                break;
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("encodeHtml : Method Finish");
        }
        return ret.toString();
    }

    /**
     * Convert the W3C Element object to a String representation (including all tags etc).
     *
     * NOTE : OutputKeys.OMIT_XML_DECLARATION - This switches off the <?xml version="1.0" encoding="UTF-8"?> being
     *        automatically added to the string by the transformer (it's designed to work with Documents.
     *
     * @param e The XML Element we wish to convert to String form.
     * @return  The Element's XML contents as a string
     */
    public static String elementToString(final Element e) {
        if (log.isDebugEnabled()) {
            log.debug("elementToString : Method Start");
        }
        TransformerFactory tFactory = TransformerFactory.newInstance();
        StringWriter sw = new StringWriter();
        try {
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

            DOMSource source = new DOMSource(e);

            StreamResult result = new StreamResult(sw);

            transformer.transform(source, result);
        } catch (TransformerException e1) {
            e1.printStackTrace();
        }
        if (log.isDebugEnabled()) {
            log.debug("elementToString : Method Finish");
        }
        return sw.toString();
    }

    /**
     * Convert the W3C Document object to a String representation (including all tags etc).
     *
     * @param   doc The XML Document we wish to convert to String form.
     * @return  The Document's XML contents as a string
     */
    public static String documentToString(final Document doc) {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        StringWriter sw = new StringWriter();
        try {
            Transformer transformer = tFactory.newTransformer();

            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(sw);

            transformer.transform(source, result);
        } catch (TransformerException e1) {
            e1.printStackTrace();
        }
        return sw.toString();
    }

}
