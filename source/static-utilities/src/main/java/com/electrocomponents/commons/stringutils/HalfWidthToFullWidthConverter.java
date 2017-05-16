/**
 *
 */
package com.electrocomponents.commons.stringutils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : C0950079
 * Created : 12 Aug 2008 at 09:13:29
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
 * Util class to convert Half Width katakana characters to their Full width equivalent characters.
 * @author C0950079
 */
public final class HalfWidthToFullWidthConverter {

    /** A commons logging logger. */
    private static final Log LOG = LogFactory.getLog(HalfWidthToFullWidthConverter.class);

    /**
     * fullWidthKatakana characters.
     */
    private static final char[] FULL_WIDTH_KATAKANA = { '\u3002', '\u300C', '\u300D', '\u3001', '\u30FB', '\u30F2', '\u30A1', '\u30A3',
            '\u30A5', '\u30A7', '\u30A9', '\u30E3', '\u30E5', '\u30E7', '\u30C3', '\u30FC', '\u30A2', '\u30A4', '\u30A6', '\u30A8',
            '\u30AA', '\u30AB', '\u30AD', '\u30AF', '\u30B1', '\u30B3', '\u30B5', '\u30B7', '\u30B9', '\u30BB', '\u30BD', '\u30BF',
            '\u30C1', '\u30C4', '\u30C6', '\u30C8', '\u30CA', '\u30CB', '\u30CC', '\u30CD', '\u30CE', '\u30CF', '\u30D2', '\u30D5',
            '\u30D8', '\u30DB', '\u30DE', '\u30DF', '\u30E0', '\u30E1', '\u30E2', '\u30E4', '\u30E6', '\u30E8', '\u30E9', '\u30EA',
            '\u30EB', '\u30EC', '\u30ED', '\u30EF', '\u30F3', '\u309B', '\u309C' };

    /**
     * default constructor.
     */
    private HalfWidthToFullWidthConverter() {

    }

    /**
     * @param inputString half width inputString.
     * @return output full width equivalent.
     */
    public static String halfToFullWidthKatakana(final String inputString) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(" Method Start ");
        }
        String outputString = null;

        if (inputString == null || inputString.trim().length() <= 0) {
            return outputString;
        }

        int bufferLength = inputString.length();
        char[] input = inputString.toCharArray();
        char[] output = new char[bufferLength + 1];
        int ixIn = 0;
        int ixOut = 0;

        while (ixIn < bufferLength) {
            if (input[ixIn] >= '\uFF61' && input[ixIn] <= '\uFF9F') {
                if (ixIn + 1 >= bufferLength) {
                    output[ixOut++] = FULL_WIDTH_KATAKANA[input[ixIn++] - '\uFF61'];
                } else {
                    if (input[ixIn + 1] == '\uFF9E' || input[ixIn + 1] == '\u3099' || input[ixIn + 1] == '\u309B') {
                        if (input[ixIn] == '\uFF73') {
                            output[ixOut++] = '\u30F4';
                            ixIn += 2;
                        } else if ((input[ixIn] >= '\uFF76' && input[ixIn] <= '\uFF84')
                                || (input[ixIn] >= '\uFF8A' && input[ixIn] <= '\uFF8E')) {

                            output[ixOut] = FULL_WIDTH_KATAKANA[input[ixIn] - '\uFF61'];
                            output[ixOut++]++;
                            ixIn += 2;
                        } else {
                            output[ixOut++] = FULL_WIDTH_KATAKANA[input[ixIn++] - '\uFF61'];
                        }
                    } else if (input[ixIn + 1] == '\uFF9F' || input[ixIn + 1] == '\u309A' || input[ixIn + 1] == '\u309C') {
                        if (input[ixIn] >= '\uFF8A' && input[ixIn] <= '\uFF8E') {
                            output[ixOut] = FULL_WIDTH_KATAKANA[input[ixIn] - '\uFF61'];
                            output[ixOut++] += 2;
                            ixIn += 2;
                        } else {
                            output[ixOut++] = FULL_WIDTH_KATAKANA[input[ixIn++] - '\uFF61'];
                        }

                    } else {
                        output[ixOut++] = FULL_WIDTH_KATAKANA[input[ixIn++] - '\uFF61'];
                    }
                }

            } else {
                output[ixOut++] = input[ixIn++];
            }

        }

        outputString = new String(output);
        if (LOG.isDebugEnabled()) {
            LOG.debug(" Method Finish ");
        }
        return outputString.substring(0, ixOut);

    }

}
