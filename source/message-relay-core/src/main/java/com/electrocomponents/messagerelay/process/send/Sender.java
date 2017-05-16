package com.electrocomponents.messagerelay.process.send;

import com.electrocomponents.commons.exception.ElectroException;
import com.electrocomponents.messagerelay.process.MessageProcessor;
import com.electrocomponents.messagerelay.support.BusinessMessage;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Copyright (c) Electrocomponents Plc.
 * Author         : e0180383
 * Creation Date  : 21-Jun-2006
 * Creation Time  : 14:38:29
 * IDE            : IntelliJ IDEA 5
 * *******************************************************************************************************************
 * Description :
 * Base class for sending BusinessMessages
 * *******************************************************************************************************************
 * Change History
 * *******************************************************************************************************************
 * * Change   * Author   * Date         * Description
 * *******************************************************************************************************************
 * * New      * e0180383 *  21-Jun-2006 * New class created
 * *******************************************************************************************************************
 * * 10.1ELS  * e0161085 *  22-Jun-2009 * Commented logging of encoding.
 * *******************************************************************************************************************
 * * GEN-1228 * e0161085 *  26-Aug-2010 * Tidied Code.
 * *******************************************************************************************************************
 *
 * @author e0180383
 */
public abstract class Sender extends MessageProcessor {

    /**
     * Log4J.
     */
    private static Log log = LogFactory.getLog(Sender.class);

   /**
    * Send an outbound BusinessMessage to the target.
    *
    * @param message The BusinessMessage to be sent to the target system.
    * @return The response from the target system as a BusinessMessage
    * @throws ElectroException Exception.
    */
    public abstract BusinessMessage processMessage(final BusinessMessage message) throws ElectroException;

   /**
    * Setup any authentication required to send the message.
    *
    * @param outboundMessage The BusinessMessage to be sent to the target system.
    * @throws ElectroException Exception.
    */
    protected void  setupAuthentication(final BusinessMessage outboundMessage) throws ElectroException { }

   /**
    * Encode a String using the specifed encoding scheme.
    *
    * @param target The String to be encoded
    * @param encodingScheme The encoding method to be used, e.g BASE64, URLENCODE etc
    * @return The encoded String.
    * @throws ElectroException Exception.
    */
    protected String encode(final String target, final String encodingScheme) throws ElectroException {
       if (log.isDebugEnabled()) { log.debug("encode : Method Start"); }
       String encodedString = null;

       if (log.isDebugEnabled()) { log.debug("encode : Encoding scheme = " + encodingScheme); }
       //if (log.isDebugEnabled()) log.debug("encode : Plain data = " + target);

       try {
         if ("BASE64".equalsIgnoreCase(encodingScheme)) {
           encodedString = new String(Base64.encodeBase64(target.getBytes()));
         } else if ("URLENCODE".equalsIgnoreCase(encodingScheme)) {
           URLCodec ucodec = new URLCodec();
           encodedString = ucodec.encode(target);
         }
       } catch (EncoderException ee) {
         String error = "encode : An EncodingException was thrown, error was " + ee.getMessage();
         log.fatal(error);
         throw new ElectroException("Sender." + error, ee);
       }

       //if (log.isDebugEnabled()) log.debug("encode : Encoded data =" + encodedString);

       if (log.isDebugEnabled()) { log.debug("encode : Method Finish"); }
       return encodedString;
    }

}
