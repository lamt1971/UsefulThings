package com.electrocomponents.commons.components.general.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Copyright (c) Electrocomponents Plc.
 * Author         : E0180383
 * Creation Date  : 8/04/2003
 * Creation Time  : 10:13:10
 * *******************************************************************************************************************
 * Description :
 * Convert between HTML and JAVA charset naming conventions.
 * See http://java.sun.com/j2se/1.5.0/docs/guide/intl/encoding.doc.html for more information on encodings.
 * *******************************************************************************************************************
 * Change History
 * *******************************************************************************************************************
 * * Change   * Author   * Date       * Description
 * *******************************************************************************************************************
 * * New      * E0180383 * 27/07/2006 * New class created
 * *******************************************************************************************************************
 * * New      * e0161085 * 08/08/2007 * Amended for use with Eclipse.
 * *******************************************************************************************************************
 *
 * @author E0180383
 */

public final class CharsetNameConvertor {


    /**
     * Log4J Declaration.
     */
    private static Log log = LogFactory.getLog(CharsetNameConvertor.class);

    /**
     * Default Constructor.
     */
    private CharsetNameConvertor() {

    }

    /**
     * Convert HTTP request charset name to Java equivalent, if missing pass out the incoming value unchanged.
     * @param  tmpEncoding The incoming "HTML" character encoding identifier
     * @return The "JAVA" encoding name (if conversion was successful).
     */
    public static String htmlToJava(final String tmpEncoding) {
        if (log.isDebugEnabled()) { log.debug("htmlToJava : Method Start"); }
        if (log.isDebugEnabled()) { log.debug("htmlToJava : HTML Charset In=" + tmpEncoding + "#"); }

        String theEncoding = tmpEncoding;
        // Convert HTTP request charset name to Java equivalent, if missing use that from the init parameter or default to UTF8.

        if ("ISO-8859-1".equalsIgnoreCase(theEncoding) || "ISO8859_1".equalsIgnoreCase(theEncoding)) {
            theEncoding = "ISO8859_1";
        } else if ("Shift-JIS".equalsIgnoreCase(theEncoding)
                || "Shift_JIS".equalsIgnoreCase(theEncoding)
                || "SJIS".equalsIgnoreCase(theEncoding)) {
            theEncoding = "SJIS";
        } else if ("GB2312".equalsIgnoreCase(theEncoding)) // Chinese Characters.
        {
            theEncoding = "EUC_CN";
        } else if ("EUC_JP".equalsIgnoreCase(theEncoding)) // Japanse Characters (e.g. Fujitsu)
        {
            theEncoding = "EUC_JP";
        } else if ("UTF-8".equalsIgnoreCase(theEncoding) || "UTF8".equalsIgnoreCase(theEncoding)) {
            theEncoding = "UTF8";
        } else {
            if (log.isDebugEnabled()) { log.debug("Unrecognised charset"); }
        }

        if (log.isDebugEnabled()) { log.debug("htmlToJava : Java Charset Out=" + theEncoding + "#"); }
        if (log.isDebugEnabled()) { log.debug("htmlToJava : Method Finish"); }
        return theEncoding;
    }

    /**
    * Convert JAVA charset name to HTML equivalent, if missing pass out the incoming value unchanged.
    * @param  tmpEncoding The incoming "HTML" character encoding identifier
    * @return The "JAVA" encoding name (if conversion was successful).
    */
    public static String javaToHtml(final String tmpEncoding) {
        if (log.isDebugEnabled()) { log.debug("javaToHtml : Method Start"); }
        if (log.isDebugEnabled()) { log.debug("javaToHtml : Java Charset In=" + tmpEncoding); }

        String theEncoding = tmpEncoding;
        // Convert HTTP request charset name to Java equivalent, if missing pass out the incomin value unchanged.

        if ("ISO8859_1".equalsIgnoreCase(theEncoding)) {
            theEncoding = "ISO-8859-1";
        } else if ("SJIS".equalsIgnoreCase(theEncoding)) {
            theEncoding = "Shift-JIS";
        } else if ("EUC_CN".equalsIgnoreCase(theEncoding)) // Chinese Characters.
        {
            theEncoding = "GB2312";
        } else if ("EUC_JP".equalsIgnoreCase(theEncoding)) // Japanse Characters (e.g. Fujitsu)
        {
            theEncoding = "EUC_JP";
        } else if ("UTF8".equalsIgnoreCase(theEncoding) || "UTF8".equalsIgnoreCase(theEncoding)) {
            theEncoding = "UTF-8";
        } else {
            if (log.isDebugEnabled()) { log.debug("Unrecognised charset"); }
        }

        if (log.isDebugEnabled()) { log.debug("javaToHtml : HTML Charset Out=" + theEncoding); }
        if (log.isDebugEnabled()) { log.debug("htmlToJava : Method Finish"); }
        return theEncoding;
    }


    /**
     * Main Method.
     * @param args Arguments to be passed into this method.
     */
    public static void main(final String[] args) {
        System.out.println(CharsetNameConvertor.htmlToJava("utf-8"));
    }
}
