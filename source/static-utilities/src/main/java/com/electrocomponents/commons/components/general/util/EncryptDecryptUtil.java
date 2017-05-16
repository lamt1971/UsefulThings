/**
 * 
 */
package com.electrocomponents.commons.components.general.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 * Copyright (c) RS Components.
 * Created by IntelliJ IDEA.
 * User: C0950079
 * Date: 08-08-2016
 * ********************************************************************************************************
 * Class that has general formatting and validation routines
 * ********************************************************************************************************
 * <P>
 * @author C0950079
 * ********************************************************************************************************
 * *             Change History                                                                           *
 * ********************************************************************************************************
 * * Number   * Who             * Description                                                             *
 * ********************************************************************************************************
 * * New      * C0950079        * New class created, copied the encryption / decryption related methods
 * 								  from Format.java.
 * ********************************************************************************************************
 * </pre>
 */
public class EncryptDecryptUtil implements Serializable {

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(EncryptDecryptUtil.class);

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

    /**
     * The keyBase.
     */
    private String keyBase;

    /**
     * @param str1 parameter to create keyBase.
     * @param str2 parameter to create keyBase.
     * @throws Exception the exception.
     */
    public EncryptDecryptUtil(final String str1, final String str2) throws Exception {
        keyBase = str1 + str2;
        if (LOG.isDebugEnabled()) {
            LOG.debug(keyBase);
        }
    }

    /**
     * This method performs the function of encrypting the String passed as parameter.
     * @param orgStr the orgStr.
     * @return String which is the encrypted String
     * @throws GeneralSecurityException the exception.
     */
    public String encryptAndEncode(final String orgStr) throws GeneralSecurityException {
        SecretKeySpec skeySpec = new SecretKeySpec(keyBase.getBytes(), "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        if (LOG.isDebugEnabled()) {
            LOG.debug(Cipher.getMaxAllowedKeyLength("Blowfish"));
        }

        byte[] enc = new byte[2];
        try {
            if (orgStr == null) {
                return null;
            }
            if ("".equals(orgStr)) {
                throw new GeneralSecurityException();
            }

            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] utf8 = orgStr.getBytes("UTF-8");
            enc = cipher.doFinal(utf8);

        } catch (GeneralSecurityException ex) {

            if (LOG.isWarnEnabled()) {
                LOG.warn("Exception while encoding : " + ex.getMessage());
            }
            throw new GeneralSecurityException();

        } catch (UnsupportedEncodingException ex) {
            LOG.error("IN UNSUPPORTEDENCODING EXCEPTION BLOCK.", ex);
            throw new GeneralSecurityException();
        }
        String retString = Base64.getEncoder().encodeToString(enc);
        retString = retString.replace("/", "SLASH");

        return retString;

    }

    /**
     * This method performs the function of decrypting the String passed as parameter.
     * @param orgString the orgString.
     * @return String which is the decrypted String
     * @throws GeneralSecurityException the exception.
     */
    public String decryptAndDecode(final String orgString) throws GeneralSecurityException {
        SecretKeySpec skeySpec = new SecretKeySpec(keyBase.getBytes(), "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        if (LOG.isDebugEnabled()) {
            LOG.debug(Cipher.getMaxAllowedKeyLength("Blowfish"));
        }

        String orgStr = orgString;
        if (orgStr == null) {
            return null;
        }
        if ("".equals(orgStr)) {
            throw new GeneralSecurityException();
        }

        // This piece of code is added as the Browser replaces the '+' char with a ' ', hence a padding exception
        // occurs, so we substitute the ' '
        // with a '+'
        orgStr = orgStr.replace(' ', '+');
        // In the encoded String we replace SLASH with / string in order to undo the replacement done during Encryption
        orgStr = orgStr.replace("SLASH", "/");
        try {

            cipher.init(Cipher.DECRYPT_MODE, skeySpec);

            byte[] dec = Base64.getDecoder().decode(orgStr);
            byte[] utf8 = cipher.doFinal(dec);

            return new String(utf8, "UTF-8");

        } catch (UnsupportedEncodingException ex) {
            LOG.error("IN UNSUPPORTEDENCODING EXCEPTION BLOCK : ", ex);

            throw new GeneralSecurityException();
        }
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

}
