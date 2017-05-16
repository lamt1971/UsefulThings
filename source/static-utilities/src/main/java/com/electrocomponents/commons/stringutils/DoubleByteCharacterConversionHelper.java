package com.electrocomponents.commons.stringutils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sharma Jampani
 * Created : 3 Apr 2014 at 10:35:52
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
 * This is a utility class to convert the Double Byte Characters (e.g. CJK Full-Width AlphaNumeric) into it's equivalent Single Byte Character.
 * @author c0950667
 */
public final class DoubleByteCharacterConversionHelper {

    /** A commons logging logger. */
    private static final Log LOG = LogFactory.getLog(DoubleByteCharacterConversionHelper.class);

    /**
     * Empty Default Constructor.
     */
    private DoubleByteCharacterConversionHelper() {

    }

    /**
     * This method is used to convert the Double Byte Characters to Single Byte for returning correct/all the search results.
     * @param searchTerm the Search Term String
     * @return convertedSearchTerm
     */
    public static String convertSearchTermToSingleByte(final String searchTerm) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(" Start convertSearchTermToSingleByte");
        }
        StringBuffer outputString = new StringBuffer();

        if (StringUtils.isNotBlank(searchTerm)) {
            Map<String, String> alphaNumericMap = getDoubleByteAlphaNumericCharConversionMap();
            Pattern p = Pattern.compile("[\u2010,\uFF00-\uFF5A]");
            for (int i = 0, n = searchTerm.length(); i < n; i++) {
                CharSequence updatedSearchTerm = searchTerm.subSequence(i, i + 1);
                Matcher m = p.matcher(updatedSearchTerm);

                if (m.matches()) {
                    updatedSearchTerm = convert(updatedSearchTerm.toString());
                    for (Map.Entry<String, String> entry : alphaNumericMap.entrySet()) {
                        if (entry.getKey().equalsIgnoreCase(updatedSearchTerm.toString())) {
                            updatedSearchTerm = entry.getValue();
                            break;
                        }
                    }

                }
                outputString.append(updatedSearchTerm);
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(" Finish convertSearchTermToSingleByte");
        }
        return outputString.toString();

    }

    /**
     * This is a private method to populate the Map with Double byte code and its equivalent single byte character.
     * @return map
     */
    private static Map<String, String> getDoubleByteAlphaNumericCharConversionMap() {
        Map<String, String> alphaNumbericMap = new LinkedHashMap<String, String>();

        // Populate Numerics in map.
        alphaNumbericMap.put("\\uFF10", "0");
        alphaNumbericMap.put("\\uFF11", "1");
        alphaNumbericMap.put("\\uFF12", "2");
        alphaNumbericMap.put("\\uFF13", "3");
        alphaNumbericMap.put("\\uFF14", "4");
        alphaNumbericMap.put("\\uFF15", "5");
        alphaNumbericMap.put("\\uFF16", "6");
        alphaNumbericMap.put("\\uFF17", "7");
        alphaNumbericMap.put("\\uFF18", "8");
        alphaNumbericMap.put("\\uFF19", "9");

        // Populate Upper case Alphabets in map.
        alphaNumbericMap.put("\\uFF21", "A");
        alphaNumbericMap.put("\\uFF22", "B");
        alphaNumbericMap.put("\\uFF23", "C");
        alphaNumbericMap.put("\\uFF24", "D");
        alphaNumbericMap.put("\\uFF25", "E");
        alphaNumbericMap.put("\\uFF26", "F");
        alphaNumbericMap.put("\\uFF27", "G");
        alphaNumbericMap.put("\\uFF28", "H");
        alphaNumbericMap.put("\\uFF29", "I");
        alphaNumbericMap.put("\\uFF2A", "J");
        alphaNumbericMap.put("\\uFF2B", "K");

        alphaNumbericMap.put("\\uFF2C", "L");
        alphaNumbericMap.put("\\uFF2D", "M");
        alphaNumbericMap.put("\\uFF2E", "N");
        alphaNumbericMap.put("\\uFF2F", "O");
        alphaNumbericMap.put("\\uFF30", "P");
        alphaNumbericMap.put("\\uFF31", "Q");
        alphaNumbericMap.put("\\uFF32", "R");
        alphaNumbericMap.put("\\uFF33", "S");
        alphaNumbericMap.put("\\uFF34", "T");
        alphaNumbericMap.put("\\uFF35", "U");
        alphaNumbericMap.put("\\uFF36", "V");

        alphaNumbericMap.put("\\uFF37", "W");
        alphaNumbericMap.put("\\uFF38", "X");
        alphaNumbericMap.put("\\uFF39", "Y");
        alphaNumbericMap.put("\\uFF3A", "Z");

        // Populate lower case Alphabets in map.
        alphaNumbericMap.put("\\uFF41", "a");
        alphaNumbericMap.put("\\uFF42", "b");
        alphaNumbericMap.put("\\uFF43", "c");
        alphaNumbericMap.put("\\uFF44", "d");
        alphaNumbericMap.put("\\uFF45", "e");
        alphaNumbericMap.put("\\uFF46", "f");
        alphaNumbericMap.put("\\uFF47", "g");
        alphaNumbericMap.put("\\uFF48", "h");
        alphaNumbericMap.put("\\uFF49", "i");
        alphaNumbericMap.put("\\uFF4A", "j");
        alphaNumbericMap.put("\\uFF4B", "k");

        alphaNumbericMap.put("\\uFF4C", "l");
        alphaNumbericMap.put("\\uFF4D", "m");
        alphaNumbericMap.put("\\uFF4E", "n");
        alphaNumbericMap.put("\\uFF4F", "o");
        alphaNumbericMap.put("\\uFF50", "p");
        alphaNumbericMap.put("\\uFF51", "q");
        alphaNumbericMap.put("\\uFF52", "r");
        alphaNumbericMap.put("\\uFF53", "s");
        alphaNumbericMap.put("\\uFF54", "t");
        alphaNumbericMap.put("\\uFF55", "u");
        alphaNumbericMap.put("\\uFF56", "v");

        alphaNumbericMap.put("\\uFF57", "w");
        alphaNumbericMap.put("\\uFF58", "x");
        alphaNumbericMap.put("\\uFF59", "y");
        alphaNumbericMap.put("\\uFF5A", "z");

        // Populate special characters in map.
        alphaNumbericMap.put("\\uFF3F", "-");
        alphaNumbericMap.put("\\uFF40", ",");
        alphaNumbericMap.put("\\uFF08", "(");
        alphaNumbericMap.put("\\uFF09", ")");
        alphaNumbericMap.put("\\uFF0F", "/");
        alphaNumbericMap.put("\\uFF0D", "-");
        alphaNumbericMap.put("\\u2010", "-");

        return alphaNumbericMap;

    }

    /**
     * This method is used to get the unicode for the string passed.
     * @param str the Search Term String
     * @return unicode String
     */
    private static String convert(final String str) {
        StringBuffer unicodeString = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if ((ch >= 0x0020) && (ch <= 0x007e)) // Does the char need to be converted to unicode?
            {
                unicodeString.append(ch); // No.
            } else { // Yes.
                unicodeString.append("\\u"); // standard unicode format.
                String hex = Integer.toHexString(str.charAt(i) & 0xFFFF); // Get hex value of the char.
                for (int j = 0; j < 4 - hex.length(); j++) {
                    // Prepend zeros because unicode requires 4 digits
                    unicodeString.append("0");
                }
                unicodeString.append(hex.toUpperCase()); // standard unicode format.

            }
        }
        return (new String(unicodeString)); // Return the stringbuffer cast as a string.
    }

}
