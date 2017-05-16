package com.electrocomponents.commons.components.general;

import java.security.Key;
import java.util.Base64;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 * Copyright (c) RS Components.
 * Created by IntelliJ IDEA.
 * User: UK183897
 * Date: 25-May-2004
 * ********************************************************************************************************
 * Class that has general formatting and validation routines
 * ********************************************************************************************************
 * <P>
 * @author UK180383
 * ********************************************************************************************************
 * *             Change History                                                                           *
 * ********************************************************************************************************
 * * Number   * Who             * Description                                                             *
 * ********************************************************************************************************
 * * New      * UK180383        * New class created, adapted from com.rswww.utils.Format class
 * ********************************************************************************************************
 * *          * UK161085        * Amended Stock Number check to allow for new Non Stocked Product Number format.
 * ********************************************************************************************************
 * * 38998    * UK161085        * Added Functions to Encrypt / Decrpyt a string to DES/ECB/PKCS5 standard.
 * *          *                 * This was taken from the format class used on the website. (Copyright Hima 2007)
 * ********************************************************************************************************
 * * 641540   * UK161085        * 15/02/08
 * *          *                 * Made a fix to the unforemat stock number so that it does not remove the "A"
 * *          *                 * suffix.
 * ********************************************************************************************************
 * </pre>
 */

public class Format {

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(Format.class);

    /** NON_CATALOGUE_PRODUCT_PREFIX. */
    public static final String NON_CATALOGUE_PRODUCT_PREFIX = "25";

    /** TAB. */
    private static final String TAB = "\t";

    /** HYPHEN. */
    private static final String HYPHEN = "-";

    /** The following is the secret Key used for Encrypting / de-encrypting Strings. */
    // This is old key to decrypt card with DES algorithm.
    // private static final String KEY_STRING = "24SSKEY6784";
    private static final String KEY_STRING = "A1a2$SqIcI3lwek!";

    /** The following is the secret Key used for Customer encrypting / de-encrypting of strings. */
    private static final String CUSTOMER_KEY_STRING = "40SSKEY5201";

    /** Algorithm we are using for Encryption/Decryption of Card. */
    private static final String ALOGORITHM = "AES";

    /** Mode and Padding we are using for Encryption/Decryption of Card. */
    private static final String PADDING = "/ECB/PKCS5Padding";

    /** utf-8 charset name. */
    private static final String CHARSET_UTF8 = "UTF-8";

    /**
     * Encoding table.
     */
    private static final byte[] ENCODING_TABLE = { (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6',
            (byte) '7', (byte) '8', (byte) '9', (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F' };

    /**
     * Encodes a string to hexadecimal format.
     * @param b array of bytes
     * @return String encoded string
     */
    public static String encodeToHex(final byte[] b) {
        StringBuilder s = new StringBuilder(2 * b.length);

        for (int i = 0; i < b.length; i++) {

            int v = b[i] & 0xff;

            s.append((char) ENCODING_TABLE[v >> 4]);
            s.append((char) ENCODING_TABLE[v & 0xf]);
        }

        return s.toString();
    }

    /**
     * Encodes a string to hexadecimal format.
     * @param originalString originalString.
     * @return String encoded string
     */
    public static String encodeToHex(final String originalString) {
        String encodedString = null;
        if (originalString != null) {
            byte[] bytes = null;
            try {
                bytes = originalString.getBytes(CHARSET_UTF8);
            } catch (Exception e) {
                bytes = originalString.getBytes();
            }
            encodedString = encodeToHex(bytes);
        }
        return encodedString;

    }

    /**
    * Converts a hexadecimal character to an integer.
    *
    * @param ch A character to convert to an integer digit
    * @param index The index of the character in the source
    * @return An integer
    * @throws DecoderException Thrown if ch is an illegal hex character
    */
    protected static int toDigit(char ch, int index) throws RuntimeException {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal charcter " + ch + " at index " + index);
        }
        return digit;
    }

    /**
     * Decodes a string from a hexadecimal format.
     *
     * @param hexEncodedStr Hexadecimal string
     * @return String decoded string
     */
    public static String decodeHexToStr(final String hexEncodedStr) {
        String decodedStr = null;
        if (hexEncodedStr != null) {
            try {
                char[] hexCharArray = hexEncodedStr.toCharArray();
                int hexArrayLength = hexCharArray.length;

                byte[] out = new byte[hexArrayLength >> 1];
                // two characters form the hex value.
                for (int i = 0, j = 0; j < hexArrayLength; i++) {
                    int f = toDigit(hexCharArray[j], j) << 4;
                    j++;
                    f = f | toDigit(hexCharArray[j], j);
                    j++;
                    out[i] = (byte) (f & 0xFF);
                }

                decodedStr = new String(out, CHARSET_UTF8);
            } catch (Exception e) {
                LOG.error("Decode Hex To String exception.", e);
            }
        }
        return decodedStr;
    }

    /**
     * converts Japanese full width Latin to the ASCII eqivalent replaces control characters with spaces removes all duplicate spaces.
     * @param inText the inText
     * @return converted string
     */
    public String translateText(final String inText) {
        return translateText(inText, false);
    }

    /**
     * converts Japanese full width Latin to the ASCII eqivalent replaces control characters with spaces optionally inserts spaces between
     * ASCII and other characters removes all duplicate spaces.
     * @param inText inText
     * @param splitAscii splitAscii
     * @return converted string
     */
    public static String translateText(final String inText, final boolean splitAscii) {
        final String translateFskLatin = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
        boolean thisASCII = false;
        boolean lastASCII = false;
        StringBuffer searchText = new StringBuffer();
        char[] inChar = inText.toCharArray();
        int iLen = inChar.length;
        for (int i = 0; i < iLen; i++) {
            int thisChar = inChar[i];
            thisASCII = false;
            if (thisChar < 32) {
                thisASCII = true;
                thisChar = 32;
            } else if (thisChar < 128) {
                thisASCII = true;
            } else if (thisChar > 65280 && thisChar < 65377) {
                thisChar = translateFskLatin.charAt(thisChar - 65281);
            } else if (thisChar == 12288) {
                thisASCII = true;
                thisChar = 32;
            }
            if (thisASCII != lastASCII && splitAscii && i > 0) {
                searchText.append(" ");
                lastASCII = thisASCII;
            }
            searchText.append((char) thisChar);
        }

        for (int i = searchText.indexOf("  "); i >= 0; i = searchText.indexOf("  ")) {
            searchText.replace(i, i + 2, " ");
        }
        return searchText.toString().trim();
    }

    /**
     * @param inString inString
     * @return string
     */
    public static String removePunctuation(final String inString) {
        if (StringUtils.isBlank(inString)) {
            return ("");
        }
        return (inString.replaceAll("[., \\-_\"':;]", ""));
    }

    /**
     * @param inString inString
     * @return string
     */
    public static String stripAndCase(final String inString) {
        if (StringUtils.isBlank(inString)) {
            return ("");
        }

        return stripAndCase(inString, "0-9a-zA-Z");
    }

    /**
     * @param inString inString
     * @param nonStripChars nonStripChars
     * @return string
     */
    public static String stripAndCase(final String inString, final String nonStripChars) {
        String tmpNonStripChars = nonStripChars;
        if (StringUtils.isBlank(tmpNonStripChars)) {
            tmpNonStripChars = "0-9a-zA-Z";
        }
        return (inString.toUpperCase().replaceAll("[^" + tmpNonStripChars + "]", ""));
    }

    /**
     * converts European characters to English Equivalents.
     * @param inString inString
     * @return string with characters replaced
     */
    public static String translateNonEnglishCharacters(final String inString) {
        // Must have an input
        if (StringUtils.isBlank(inString)) {
            return "";
        }

        String outString = inString.replaceAll("[\u00c0\u00c1\u00c2\u00c3\u00c4\u00c5\u00e0\u00e1\u00e2\u00e3\u00e4\u00e5]", "A");
        outString = outString.replaceAll("[\u00c6\u00e6]", "AE");
        outString = outString.replaceAll("[\u00c7\u00e7]", "C");
        outString = outString.replaceAll("[\u00c8\u00c9\u00ca\u00cb\u00e8\u00e9\u00ea\u00eb]", "E");
        outString = outString.replaceAll("[\u00cc\u00cd\u00ce\u00cf\u00ec\u00ed\u00ee\u00ef]", "I");
        outString = outString.replaceAll("[\u00d1\u00f1]", "N");
        outString = outString.replaceAll("[\u00d2\u00d3\u00d4\u00d5\u00d6\u00d8\u00f2\u00f3\u00f4\u00f5\u00f6\u00f8]", "O");
        outString = outString.replaceAll("[\u00d9\u00da\u00db\u00dc\u00f9\u00fa\u00fb\u00fc]", "U");
        outString = outString.replaceAll("[\u00d1\u00fd\u00ff]", "Y");
        return (outString);
    }

    /**
     * Formatting to justify text.
     * @param txt txt
     * @return String
     */
    public static String rightPad(final String txt) {

        int startIndex;
        String pad = "                                       ";

        if (txt.length() < pad.length()) {
            startIndex = txt.length() + 1;
            return txt.concat(pad.substring(startIndex));
        } else {
            return txt + TAB;
        }
    }

    /**
     * @param stockNumber stockNumber
     * @return string
     */
    public static String formatForSerach(final String stockNumber) {
        if (StringUtils.isBlank(stockNumber)) {
            return "";
        }
        String fmtStr = unformatSN(stockNumber).replaceAll("^0+", "");
        return fmtStr;
    }

    /**
     * @param stockNumber stockNumber
     * @return string
     */
    public static String formatSN(final String stockNumber) {
        if (StringUtils.isBlank(stockNumber)) {
            return "";
        }
        String fmtStr = unformatSN(stockNumber);
        if (fmtStr.indexOf(NON_CATALOGUE_PRODUCT_PREFIX) == 0 && fmtStr.length() == 10) {
            return fmtStr;
        }
        if (fmtStr.length() > 6) {
            fmtStr = fmtStr.replaceAll("^0+", "");
        }
        if (fmtStr.length() < 6) {
            fmtStr = ("000000" + fmtStr).substring(fmtStr.length());
        }
        return (fmtStr.substring(0, 3) + "-" + fmtStr.substring(3));
    }

    /**
     * Unformat the Stock Number. Convert the stock number into a 7 digit (plus alpha) format.
     * @param stockNumber The stock Number to convert.
     * @return Unformatted product Number.
     */
    public static String unformatSN(final String stockNumber) {
        String fmtStr;
        String lastChar = "";

        fmtStr = new String(stockNumber.toUpperCase()).replaceAll("[^0-9A-Z]", "");
        if (fmtStr.length() == 0) {
            return "";
        }

        if (isNonStockedSN(fmtStr)) {
            return fmtStr;
        }
        lastChar = fmtStr.substring(fmtStr.length() - 1).replaceAll("[0-9]", "");

        // replace all non-digits and any leading zeros
        fmtStr = fmtStr.replaceAll("\\D", "").replaceAll("^0+", "");

        if (fmtStr.length() < 1) {
            return "";
        }
        if (StringUtils.isNotBlank(lastChar)) {
            fmtStr += lastChar;
        }
        if (fmtStr.length() < 7) {
            // must be a least 7 characters long
            fmtStr = ("0000000" + fmtStr).substring(fmtStr.length());
        }
        return (fmtStr);
    }

    /**
     * @param rawSN rawSN
     * @return string
     */
    public static boolean checkSN(final String rawSN) {
        if (StringUtils.isBlank(rawSN)) {
            return false;
        }
        String fmtSN = rawSN.toUpperCase().replaceAll("[^0-9A-Z]", "");
        int length = 10 - NON_CATALOGUE_PRODUCT_PREFIX.length();
        Pattern p = Pattern.compile(NON_CATALOGUE_PRODUCT_PREFIX + "[0-9]{" + length + "}");

        Matcher m = p.matcher(fmtSN.toUpperCase());
        return checkSN(fmtSN, m.matches());
    }

    /**
     * @param rawSN rawSN
     * @param isNonCatalogue isNonCatalogue
     * @return boolean
     */
    public static boolean checkSN(final String rawSN, final boolean isNonCatalogue) {
        if (StringUtils.isBlank(rawSN)) {
            return false;
        }
        int[] checkArray = { 1, 5, 1, 9, 7, 3, 1, 6 };
        int iLen = checkArray.length;
        // remove any alpha chars
        String fmtSN = rawSN.replaceAll("\\D", "");
        // set the check number
        int checkValue = 0;
        if (isNonCatalogue) {
            // the check value is offset by 3 and we need to remove the prefix
            // checkValue = 3;
            fmtSN = fmtSN.substring(NON_CATALOGUE_PRODUCT_PREFIX.length());
        }
        // pad the raw sn to the right length
        if (fmtSN.length() < 9) {
            // must be a least 9 characters long
            fmtSN = ("000000000" + fmtSN).substring(fmtSN.length());
        }

        // loop and do the multiplication
        for (int i = 0; i < iLen; i++) {
            checkValue += (checkArray[i] * Integer.parseInt(fmtSN.substring(i, i + 1), 10));
        }
        String checkString = Integer.toString(checkValue);
        String testString = fmtSN.substring(fmtSN.length() - 1);
        if ((testString.equals(checkString.substring(checkString.length() - 1)))) {
            return true;
        }

        checkString = Integer.toString(checkValue + 7);
        if ((testString.equals(checkString.substring(checkString.length() - 1)))) {
            return true;
        }

        checkString = Integer.toString(checkValue + 3);
        if ((testString.equals(checkString.substring(checkString.length() - 1)))) {
            return true;
        }

        return false;
    }

    /**
     * @param stockNumber stockNumber
     * @return boolean
     */
    public static boolean isNonStockedSN(final String stockNumber) {
        if (StringUtils.isBlank(stockNumber)) {
            return false;
        }
        Pattern p = Pattern.compile(NON_CATALOGUE_PRODUCT_PREFIX + "[0-9]{9}");
        Matcher m = p.matcher(stockNumber.toUpperCase());
        if (m.matches()) {
            return checkSN(stockNumber, true);
        }
        return false;
    }

    /**
     * @param string string
     * @return string
     */
    public static String removeEscapeCharactors(final String string) {
        String retString = string.replaceAll("&", "&amp;");

        return retString;

    }

    /**
     * @param string string
     * @return string
     */
    public static String escapeCategoryNames(final String string) {
        String retString = string.replaceAll("&amp;", "&#38;");
        return retString;
    }

    /**
     * @param string string
     * @return string
     */
    public static String formatBrandandTFGNames(final String string) {
        String retString = string;
        retString = retString.replaceAll("&", "&#38;");
        retString = retString.replaceAll("/", "&#47;");
        retString = retString.replaceAll("\"", "&#34;");
        retString = retString.replaceAll("<", "&#60;");
        retString = retString.replaceAll(">", "&#62;");
        retString = retString.replaceAll("'", "&#39;");

        return retString;
    }

    /**
     * @param string string
     * @return string
     */
    public static String formateAttibuteValues(final String string) {
        String retString = string;
        retString = retString.replaceAll("&", "&#38;");
        retString = retString.replaceAll("\\(", "&#40;");
        retString = retString.replaceAll("\\)", "&#41;");
        retString = retString.replaceAll("/", "&#47;");
        return retString;
    }

    /**
     * @param string string
     * @return string
     */
    public static String formateAttributeNames(final String string) {
        String retString = string;
        if (retString.startsWith("7777")) {
            retString = "-";
        } else if (retString.startsWith("8888")) {
            retString = "N/A";
        }

        return escapeFromVerity(retString);
    }

    /**
     * @param results results
     * @return list
     */
    // JB - eh?
    /*public static LinkedList removeDuplicates(final LinkedList results) {
        HashSet h = new HashSet(results);
        results.clear();
        results.addAll(h);
        TreeSet ht = new TreeSet(results);
        results.clear();
        results.addAll(ht);

        return results;
    }*/

    /**
     * as space comes out as _ from verity used to replace "_" with " " to display it to user.
     * @param name name
     * @return String
     */
    public static String escapeFromVerity(final String name) {
        String retName = name;
        retName = retName.replaceAll("_", " ");
        retName = encodeapos(retName);
        return retName;
    }

    /**
     * as space needs to be sent as _ to verity used to replace " " with "_" to display it to user.
     * @param name name
     * @return String
     */
    public static String formatAttributeNamesToVaerity(final String name) {
        String retName = name;
        if ("-".equals(retName)) {
            retName = "7777";
        }
        if ("N/A".equals(retName)) {
            retName = "8888";
        }
        return escapeToVerity(retName);
    }

    /**
     * used for category names, brand names etc to escape when sending to verity.
     * @param string string
     * @return String
     */
    public static String escapeToVerity(final String string) {
        String retString = string;
        retString = retString.replaceAll(" ", "_");
        retString = decodeapos(retString);
        return retString;
    }

    /**
     * @param string string
     * @return string
     */
    public static String encodeapos(final String string) {
        String retString = string;
        if (StringUtils.isBlank(retString)) {
            return "";
        }
        retString = retString.replaceAll("&#39;", "-39-");
        return retString;
    }

    /**
     * @param string string
     * @return string
     */
    public static String decodeapos(final String string) {
        String retString = string;
        if (StringUtils.isBlank(retString)) {
            return "";
        }
        retString = retString.replaceAll("-39-", "'");
        return retString;
    }

    /**
     * @param string string
     * @return String
     */
    public static String encodeAMP(final String string) {
        String retString = string;
        if (StringUtils.isBlank(retString)) {
            return "";
        }
        retString = retString.replaceAll("&", "-38-");
        return retString;
    }

    /**
     * @param string string
     * @return String
     */
    public static String decodeAMP(final String string) {
        String retString = string;
        if (StringUtils.isBlank(retString)) {
            return "";
        }
        retString = retString.replaceAll("-38-", "&");
        return retString;
    }

    /**
     * @param string string
     * @return String
     */
    public static String encodecots(final String string) {
        String retString = string;
        if (StringUtils.isBlank(retString)) {
            return "";
        }
        retString = retString.replaceAll("\"", "-34-");
        retString = retString.replaceAll("'", "-39-");
        return retString;

    }

    /**
     * @param string string
     * @return string
     */
    public static String decodecots(final String string) {
        String retString = string;
        if (StringUtils.isBlank(retString)) {
            return "";
        }
        retString = retString.replaceAll("-34-", "\"");
        retString = retString.replaceAll("-39-", "'");
        return retString;

    }

    /**
     * applied for attribute values can have unicode charactors.
     * @param string string
     * @return String
     */
    public static String convertUnicodetohexattName(final String string) {
        String retString = string;
        retString = formatBrandandTFGNames(retString);
        retString = coverttoHex(retString);
        return retString;
    }

    /**
     * @param string string
     * @return string
     */
    public static String convertUnicodetohexattValue(final String string) {
        String retString = string;
        retString = formatBrandandTFGNames(retString);
        retString = retString.replaceAll(",", "&#44;");
        retString = coverttoHex(retString);
        return retString;
    }

    /**
     * @param string string
     * @return string
     */
    public static String coverttoHex(final String string) {
        String retString = string;
        char[] ch = retString.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char charact = ch[i];
            // System.out.println(ch[i+1]);
            int hashcode = new Character(charact).hashCode();
            if (hashcode > 160) {
                retString = retString.replaceAll("" + charact, "&#" + hashcode + ";");
            }
        }
        return retString;

    }

    /**
     * @param string string
     * @return string
     */
    public static String escapeHTMLTags(final String string) {
        String retString = string;
        retString = retString.replaceAll("&#62;", ">");
        retString = retString.replaceAll("&#60;", "<");
        retString = retString.replaceAll("&#47;", "/");
        retString = decodeapos(retString);
        return retString;

    }

    /**
     * @param string string
     * @return string
     */
    public static String removeOIDFromCatPath(final String string) {
        String retString = string;
        if (retString.indexOf("#OID#") != -1) {
            retString = retString.substring(0, retString.lastIndexOf("#OID#"));
        }
        return retString;
    }

    /**
     * @param string string
     * @return string
     */
    public static String removeIDFromTFGName(final String string) {
        String retString = string;
        if (StringUtils.isBlank(retString)) {
            return "";
        }
        if (retString.indexOf("#ID#") != -1) {
            StringTokenizer st = new StringTokenizer(retString, "#");
            while (st.hasMoreTokens()) {
                String ss = st.nextToken();
                if (ss.startsWith("PSF")) {
                    retString = retString.replaceFirst("#ID#" + ss + "#ID#", "");
                    break;
                }
            }
        }
        return retString;
    }

    /**
     * encrypt Encrypt a string to DE/ECB/PKCS5Padding.
     * @param source - String to be encoded
     * @return String - The encoded version
     */
    public static String encrypt(final String source) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start encrypt");
        }
        String encrypted = null;
        try {
            // Get our secret key
            Key key = getKeyAES(KEY_STRING);
            // Create the cipher
            Cipher desCipher = Cipher.getInstance(ALOGORITHM + PADDING);
            // Initialize the cipher for encryption
            desCipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] utf8 = source.getBytes("UTF8");
            // Encrypt
            byte[] enc = desCipher.doFinal(utf8);
            // Encode bytes to base64 to get a string
         
            // JB: here we use the default charset to encode the encrypted bytes into a base64 String 
            // any multibyte characters will already have been split up in the previous step
            encrypted = Base64.getEncoder().encodeToString(enc);
        } catch (Exception e) {
            LOG.error("Error in enrypting value:" + e.getMessage(), e);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("End encrypt");
        }
        return encrypted;
    }

    /**
     * encrypt Encrypt a string to DE/ECB/PKCS5Padding.
     * @param source - String to be encoded
     * @return String - The encoded version
     */
    public static String customerEncrypt(final String source) {
        try {

            // Get our secret key
            Key key = getKey(CUSTOMER_KEY_STRING);

            // Create the cipher
            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

            // Initialize the cipher for encryption
            desCipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] utf8 = source.getBytes("UTF8");

            // Encrypt
            byte[] enc = desCipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            // JB: here we use the default charset to encode the encrypted bytes into a base64 String 
            // any multibyte characters will already have been split up in the previous step
            return Base64.getEncoder().encodeToString(enc);

        } catch (Exception e) {
            LOG.error("Customer Encrypt", e);
        }
        return null;
    }

    /**
     * Decrypt a DES/ECB/PKCS5Padding encoded String.
     * @param source - String to be decoded.
     * @return String - The string that has been decoded.
     */
    public static String decrypt(final String source) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start decrypt");
        }
        String decryptd = null;
        try {
            // Get our secret key
            Key key = getKeyAES(KEY_STRING);
            // Create the cipher
            Cipher desCipher = Cipher.getInstance(ALOGORITHM + PADDING);
            // Initialize the cipher for encryption
            desCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] dec = Base64.getDecoder().decode(source);
            // Decrypt
            byte[] utf8 = desCipher.doFinal(dec);
            // Decode using utf-8
            decryptd = new String(utf8, "UTF8");
        } catch (Exception e) {
            LOG.error("Error in Decrytion::" + e.getMessage(), e);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("End decrypt");
        }
        return decryptd;
    }

    /**
     * Decrypt a DES/ECB/PKCS5Padding encoded String.
     * @param source - String to be decoded.
     * @return String - The string that has been decoded.
     */
    public static String customerDecrypt(final String source) {
        try {

            // Get our secret key
            Key key = getKey(CUSTOMER_KEY_STRING);

            // Create the cipher
            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            // Initialize the cipher for encryption
            desCipher.init(Cipher.DECRYPT_MODE, key);

            byte[] dec = Base64.getDecoder().decode(source);

            // Decrypt
            byte[] utf8 = desCipher.doFinal(dec);

            // Decode using utf-8
            return new String(utf8, "UTF8");

        } catch (Exception e) {
            LOG.error("Customer decrypt exception.", e);
        }
        return null;
    }

    /**
     * getKey Return the Secret Key used for the encryption / De-encryption process.
     * @param keyString keyString
     * @return The Secret Key....
     */

    private static Key getKey(final String keyString) {
        try {
            byte[] bytes = keyString.getBytes();
            DESKeySpec pass = new DESKeySpec(bytes);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
            SecretKey s = skf.generateSecret(pass);
            return s;
        } catch (Exception e) {
            LOG.error("Error in retrieving key.", e);
        }
        return null;
    }

    /**
     * getKey Return the Secret Key used for the encryption / De-encryption process.
     * @param keyString keyString
     * @return The Secret Key....
     */
    private static Key getKeyAES(final String keyString) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getKeyAES");
        }
        SecretKey s = null;
        try {
            byte[] bytes = keyString.getBytes();
            s = new SecretKeySpec(bytes, ALOGORITHM);
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Error in getting keys::" + e.getMessage());
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("End getKeyAES");
        }
        return s;
    }

    /**
     * The removeSpecialCharacters method remove all special character as given.
     * @param sourceString as Source String to be normalised.
     * @param specialChar as Special Characters.
     * @return String
     */
    public static String removeSpecialCharacters(final String sourceString, final String specialChar) {
        char[] sourceCharArray = sourceString.toCharArray();
        char[] splCharArray = specialChar.toCharArray();
        boolean isNotSpecialChar = false;
        StringBuilder builder = new StringBuilder(sourceString.length());
        for (char outterChar : sourceCharArray) {
            isNotSpecialChar = true;
            for (char innnerChar : splCharArray) {
                if (outterChar == innnerChar) {
                    isNotSpecialChar = false;
                    break;
                }
            }
            if (isNotSpecialChar) {
                builder.append(outterChar);
            }
        }

        return builder.toString();

    }

    /**
     * The replaceSpecialCharacters method replace all special character with given delimiter.
     * @param sourceString as Source String to be normalised.
     * @param specialChar as Special Characters.
     * @param delimiter is char.
     * @return String
     */
    public static String replaceSpecialCharacters(final String sourceString, final String specialChar, final char delimiter) {
        char[] sourceCharArray = sourceString.toCharArray();
        char[] splCharArray = specialChar.toCharArray();
        StringBuilder builder = new StringBuilder(sourceString.length());
        for (char outterChar : sourceCharArray) {
            for (char innnerChar : splCharArray) {
                if (outterChar == innnerChar) {
                    outterChar = delimiter;
                    break;
                }
            }

            builder.append(outterChar);

        }

        return builder.toString();

    }

    /**
     * Method to format post code string for Japan at the time of saving in DB or passing to SAP/CRM.
     * This method removes the hyphen character from input post code.
     * @param postalCode the String object to format
     * @return formatted String object
     */
    public static String formatJapanPostCodeForSave(final String postalCode) {
        return (StringUtils.isBlank(postalCode) ? "" : postalCode.replaceAll(HYPHEN, ""));
    }

    /**
     * Method to format post code string for Japan at the time of displaying.
     * @param postalCode the String object to format
     * @return formatted String object
     */
    public static String formatJapanPostCodeForDisplay(final String postalCode) {

        /**
         * Return blank if input post code is null or empty.
         */
        if (StringUtils.isBlank(postalCode)) {
            return "";
        }

        String tempPostCode = postalCode.replaceAll(HYPHEN, "");

        /**
         * Return post code as it is, if formatted post code length is less than 7
         */
        if (tempPostCode.length() < 7) {
            return postalCode;
        } else {
            /**
             * Format the code in the format xxx-xxxx
             */
            return (tempPostCode.substring(0, 3) + HYPHEN + tempPostCode.substring(3));
        }
    }
}
