package com.electrocomponents.messagerelay.process.send;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.commons.exception.ElectroException;
import com.electrocomponents.messagerelay.support.BusinessMessage;

/**
 * Copyright (c) Electrocomponents Plc.
 * Author         : e0180383
 * Creation Date  : 15-Jun-2006
 * Creation Time  : 16:26:35
 * IDE            : IntelliJ IDEA 5
 * *******************************************************************************************************************
 * Description :
 * Send a BusinessMessage to a remote host as a HTTP/S datastream and recieve and relay the response
 * back to the internal system if required.
 * *******************************************************************************************************************
 * Change History
 * *******************************************************************************************************************
 * * Change   * Author   * Date         * Description
 * *******************************************************************************************************************
 * * New      * e0180383 *  15-Jun-2006 * New class created
 * *******************************************************************************************************************
 * * 38867    * e0180383 *  23-Nov-2006 * Added Object payload handling to allow MimeMessage transmission.
 * *******************************************************************************************************************
 * * 10.1ELS  * e0161085 *  22-Jun-2009 * Commented logging of java encoding.
 * *******************************************************************************************************************
 * * GEN-1228 * e0161085 *  26-Aug-2010 * Tidied Code.
 * *******************************************************************************************************************
 * @author e0180383
 */
public class HttpStreamSender extends Sender {

    /**
     * Log4J.
     */
    private static Log log = LogFactory.getLog(HttpStreamSender.class);

    /** Notification Id String Constant. */
    public static final String NOTIFICATION_ID = "notificationId";

    /** Ecom Config Id String Constant. */
    public static final String ECOM_CONFIG_ID = "ecomConfigId";

    /** ADVANCE_SHIPPING_NOTICE("ASN") String Constant. */
    public static final String ADVANCE_SHIPPING_NOTICE = "ASN";

    /** PURCHASE_ORDER_RESPONSE("POR") String Constant. */
    public static final String PURCHASE_ORDER_RESPONSE = "POR";

    /** Document Number String Constant. */
    public static final String DOCUMENT_NUMBER = "DocumentNbr";

    /** Locale String Constant. */
    public static final String LOCALE = "Locale";

    /** Document Date String Constant. */
    public static final String DOCUMENT_DATE = "DocumentDate";

    /** Document Type String Constant. */
    public static final String DOCUMENT_TYPE = "DocumentType";

    /** Customer Identifier String Constant. */
    public static final String CUSTOMER_IDENTIFIER = "CustomerIdentifier";

    /** Account Number String Constant. */
    public static final String ACCOUNT_NUMBER = "AccountNumber";

    /** Document Format SBS_XML String Constant. */
    public static final String DOCUMENT_FORMAT_SBS_XML = "SBS_XML";

    /**
      * Send an outbound BusinessMessage to the target over HTTP or HTTPS and relay the response back
      * to the sending application.
      *
      * @param outboundMessage The BusinessMessage to be sent to the target system.
      * @return The response from the target system as a BusinessMessage
      * @throws ElectroException Exception.
      **/
    public BusinessMessage processMessage(final BusinessMessage outboundMessage) throws ElectroException {
        if (log.isDebugEnabled()) {
            log.debug("processMessage : Method Start");
        }

        BusinessMessage responseMsg;

        String targetUrl = outboundMessage.getConfigParameter(BusinessMessage.CONFIG_TARGET_URL);

        if (targetUrl == null) {
            String error = "processMessage : FATAL ERROR, the targetUrl was not specifed in the outbound message, correct at source";
            log.fatal(error);
            throw new ElectroException("HttpStreamSender." + error);
        } else {
            targetUrl = targetUrl.toLowerCase();
        }

        // If the target URL is https send using HTTPS
        if (targetUrl.indexOf("https") > -1) {
            responseMsg = sendMessageViaHttps(outboundMessage);
        } else {
            // else send via straight HTTP.
            responseMsg = sendMessageViaHttp(outboundMessage);
        }

        // Create response message if it is ASN / POR message.
        String messageType = outboundMessage.getConfigParameter(BusinessMessage.CONFIG_TRANSACTION_TYPE);
        if (StringUtils.isNotBlank(messageType)) {
            if (messageType.equals(ADVANCE_SHIPPING_NOTICE) || messageType.equals(PURCHASE_ORDER_RESPONSE)) {
                responseMsg.setConfigParameter(NOTIFICATION_ID, outboundMessage.getConfigParameter(NOTIFICATION_ID));
                responseMsg.setConfigParameter(ECOM_CONFIG_ID, outboundMessage.getConfigParameter(ECOM_CONFIG_ID));
            }
        }

        // Create response message if document format is SBS_XML.
        String documentFormat = outboundMessage.getConfigParameter(BusinessMessage.CONFIG_TRANSACTION_FORMAT);
        if (StringUtils.isNotBlank(documentFormat) && documentFormat.equals(DOCUMENT_FORMAT_SBS_XML)) {
            responseMsg.setConfigParameter(DOCUMENT_NUMBER, outboundMessage.getConfigParameter(DOCUMENT_NUMBER));
            responseMsg.setConfigParameter(LOCALE, outboundMessage.getConfigParameter(LOCALE));
            responseMsg.setConfigParameter(DOCUMENT_DATE, outboundMessage.getConfigParameter(DOCUMENT_DATE));
            responseMsg.setConfigParameter(DOCUMENT_TYPE, outboundMessage.getConfigParameter(DOCUMENT_TYPE));
            responseMsg.setConfigParameter(CUSTOMER_IDENTIFIER, outboundMessage.getConfigParameter(CUSTOMER_IDENTIFIER));
            responseMsg.setConfigParameter(ACCOUNT_NUMBER, outboundMessage.getConfigParameter(ACCOUNT_NUMBER));
        }
        if (log.isDebugEnabled()) {
            log.debug("processMessage : Method Finish");
        }
        return responseMsg;
    }

    /**
      * Send an outbound BusinessMessage to the target over STANDARD HTTPS and relay the response back
      * to the sending application.
      *
      * @param outboundMessage The BusinessMessage to be sent to the target system.
      * @return The response from the target system as a BusinessMessage
      * @throws ElectroException Exception.
      **/
    protected BusinessMessage sendMessageViaHttp(final BusinessMessage outboundMessage) throws ElectroException {
        if (log.isDebugEnabled()) {
            log.debug("sendMessageViaHttp : Method Start");
        }

        BusinessMessage responseMsg = new BusinessMessage();

        StringBuffer reply = new StringBuffer();
        String targetUrl = outboundMessage.getConfigParameter(BusinessMessage.CONFIG_TARGET_URL);
        String payloadJavaEncoding = outboundMessage.getConfigParameter(BusinessMessage.CONFIG_PAYLOAD_JAVA_CHARSET);
        String contentType = outboundMessage.getMessageHeader(BusinessMessage.HEADER_CONTENT_TYPE);

        if (log.isDebugEnabled()) {
            log.debug("sendMessageViaHttp : Target URL is " + targetUrl);
        }
        if (log.isDebugEnabled()) {
            log.debug("sendMessageViaHttp : Message Content-Type is " + contentType);
        }
        // if (log.isDebugEnabled()) log.debug("sendMessageViaHttp : JAVA Encoding of output is " + payloadJavaEncoding);

        if (contentType == null) {
            String error =
                    "sendMessageViaHttp : Content-Type header not set in outgoing message, "
                            + "THIS MUST BE CORRECTED ON CALLING APPLICATION !!";
            log.fatal(error);
            throw new ElectroException("HttpStreamSender." + error);
        }

        try {

            URL urlDefinition = new URL(targetUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) urlDefinition.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);

            // Set headers from the BusinessMessage to the outgoing transmission...
            Iterator headerItr = outboundMessage.getMessageHeaders().keySet().iterator();

            while (headerItr.hasNext()) {
                String name = (String) headerItr.next();
                String value = outboundMessage.getMessageHeaders().get(name);
                urlConnection.setRequestProperty(name, value);
            }

            // Search for a sendable object payload in the outbound BusinessMessage send it as appropriate.
            if (outboundMessage.getObjectPayloads().size() > 0) {
                Iterator objPayloadItr = outboundMessage.getObjectPayloads().keySet().iterator();
                boolean processablePayloadFound = false;
                while (objPayloadItr.hasNext() && !processablePayloadFound) {
                    String objectClassName = (String) objPayloadItr.next();

                    // 1). MimeMessage Payload
                    // =======================
                    // If we find a MimeMessage write it as bytes (we have to do this in this way as you can't
                    // get String data out of MimeMessage :o(
                    if ("javax.mail.internet.MimeMessage".equals(objectClassName)) {
                        DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
                        MimeMessage mm = (MimeMessage) outboundMessage.getObjectPayloads().get(objectClassName);
                        try {
                            mm.writeTo(out);
                        } catch (MessagingException me) {
                            String error =
                                    "sendMessageViaHttps : A Messaging Exception was thrown sending to " + targetUrl
                                            + " error detail was, " + me;
                            log.fatal(error);
                            throw new ElectroException("HttpsStreamSender." + error, me);
                        } finally {
                            out.flush();
                            out.close();
                        }
                        processablePayloadFound = true;
                    }
                }
            }

            // If we have any String payloads in the outbound BusinessMessage send them.
            if (outboundMessage.getStringPayloads().size() > 0) {
                // Concatenate String Payloads into from the BusinessMessage into a single string ready for transmission..
                StringBuffer stringPayload = new StringBuffer();
                Iterator stringPayloadItr = outboundMessage.getStringPayloads().values().iterator();

                while (stringPayloadItr.hasNext()) {
                    stringPayload.append((String) stringPayloadItr.next());
                }

                if (log.isDebugEnabled()) {
                    log.debug("String message before sending to Client - " + stringPayload.toString());
                }

                // Write content out to the destination over HTTP.
                Writer out = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), payloadJavaEncoding));

                out.write(stringPayload.toString());
                out.flush();
                out.close();
            }
            if (log.isDebugEnabled()) {
                log.debug("Response code received from Client - " + urlConnection.getResponseCode());
            }
            String line;
            // Set response code and response error stream in response business message
            responseMsg.addStringPayload(BusinessMessage.PAYLOAD_RESPONSE_CODE, String.valueOf(urlConnection.getResponseCode()));
            if (urlConnection.getErrorStream() != null) {
                BufferedReader input = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream(), payloadJavaEncoding));
                StringBuffer errorStream = new StringBuffer("");
                while ((line = input.readLine()) != null) {
                    errorStream.append(line).append("\n");
                }
                input.close();
                responseMsg.addStringPayload(BusinessMessage.PAYLOAD_RESPONSE_ERROR_STREAM, errorStream.toString());
            }

            // Read back in the response from the client (a line at a time)
            // BufferedReader with big 16K buffer to reduce across internet I/O - careful if you change this as you can effect performance!!
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), payloadJavaEncoding), 16384);

            while ((line = in.readLine()) != null) {
                reply.append(line).append("\n");
            }

            in.close();

            responseMsg.addStringPayload(BusinessMessage.PAYLOAD_RESPONSE_MESSAGE, reply.toString());
            if (log.isDebugEnabled()) {
                log.debug("Response Message received from Client - " + reply.toString());
            }
        } catch (IOException ioe) {
            String error =
                    "sendMessageViaHttp : An IOException was thrown sending to " + targetUrl + " error detail was, " + ioe.getMessage();
            log.fatal(error);
            // Return response message if it is ASN / POR message (Response code is other than 200).
            String messageType = outboundMessage.getConfigParameter(BusinessMessage.CONFIG_TRANSACTION_TYPE);
            if (StringUtils.isNotBlank(messageType)) {
                if (messageType.equals(ADVANCE_SHIPPING_NOTICE) || messageType.equals(PURCHASE_ORDER_RESPONSE)) {
                    return responseMsg;
                }
            }
            throw new ElectroException("HttpStreamSender." + error, ioe);
        }

        if (log.isDebugEnabled()) {
            log.debug("postAckStreamViaHttp : Method Finish");
        }
        return responseMsg;
    }

    /**
      * Send an outbound BusinessMessage to the target over SSL HTTPS and relay the response back
      * to the sending application.
      *
      * @param outboundMessage The BusinessMessage to be sent to the target system.
      * @return The response from the target system as a BusinessMessage
      * @throws ElectroException Exception.
      **/
    protected BusinessMessage sendMessageViaHttps(final BusinessMessage outboundMessage) throws ElectroException {
        if (log.isDebugEnabled()) {
            log.debug("sendMessageViaHttps : Method Start");
        }

        BusinessMessage responseMsg = new BusinessMessage();

        // Force useage of Java 1.4+ security classes...
        System.setProperty("java.protocol.handler.pkgs", "javax.net.ssl");

        StringBuffer reply = new StringBuffer();
        String targetUrl = outboundMessage.getConfigParameter(BusinessMessage.CONFIG_TARGET_URL);
        String payloadJavaEncoding = outboundMessage.getConfigParameter(BusinessMessage.CONFIG_PAYLOAD_JAVA_CHARSET);
        String contentType = outboundMessage.getMessageHeader(BusinessMessage.HEADER_CONTENT_TYPE);

        if (log.isDebugEnabled()) {
            log.debug("sendMessageViaHttps : Target URL is " + targetUrl);
        }
        if (log.isDebugEnabled()) {
            log.debug("sendMessageViaHttps : Message Content-Type is " + contentType);
        }
        // if (log.isDebugEnabled()) log.debug("sendMessageViaHttps : JAVA Encoding of output is " + payloadJavaEncoding);

        if (contentType == null) {
            String error =
                    "sendMessageViaHttps : Content-Type header not set in outgoing message, "
                            + "THIS MUST BE CORRECTED ON CALLING APPLICATION !!";
            log.fatal(error);
            throw new ElectroException("HttpStreamSender." + error);
        }

        try {

            // Frig the SSL cert hostname verification check...
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(final String s, final SSLSession sslSession) {
                    return true;
                }
            });

            URL urlDefinition = new URL(targetUrl);
            HttpsURLConnection urlConnection = (HttpsURLConnection) urlDefinition.openConnection();

            // Set the relevent Authentication as required (implemented in subclasses)
            setupAuthentication(outboundMessage);

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);

            // Set headers from the BusinessMessage to the outgoing transmission...
            Iterator headerItr = outboundMessage.getMessageHeaders().keySet().iterator();

            while (headerItr.hasNext()) {
                String name = (String) headerItr.next();
                String value = outboundMessage.getMessageHeaders().get(name);
                urlConnection.setRequestProperty(name, value);
            }

            // Search for a sendable object payload in the outbound BusinessMessage send it as appropriate.
            if (outboundMessage.getObjectPayloads().size() > 0) {
                Iterator objPayloadItr = outboundMessage.getObjectPayloads().keySet().iterator();
                boolean processablePayloadFound = false;
                while (objPayloadItr.hasNext() && !processablePayloadFound) {
                    String objectClassName = (String) objPayloadItr.next();
                    processablePayloadFound = true;
                }
            }

            // If we have any String payloads in the outbound BusinessMessage send them.
            if (outboundMessage.getStringPayloads().size() > 0) {
                // Concatenate String Payloads into from the BusinessMessage into a single string ready for transmission..
                StringBuffer stringPayload = new StringBuffer();
                Iterator stringPayloadItr = outboundMessage.getStringPayloads().values().iterator();

                while (stringPayloadItr.hasNext()) {
                    stringPayload.append((String) stringPayloadItr.next());
                }

                if (log.isDebugEnabled()) {
                    log.debug("String message before sending to Client - " + stringPayload.toString());
                }

                // Write content out to the destination over HTTP.
                Writer out = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), payloadJavaEncoding));

                out.write(stringPayload.toString());
                out.flush();
                out.close();
            }
            if (log.isDebugEnabled()) {
                log.debug("Response code received from Client - " + urlConnection.getResponseCode());
            }
            String line;
            // Set response code and response error stream in response business message
            responseMsg.addStringPayload(BusinessMessage.PAYLOAD_RESPONSE_CODE, String.valueOf(urlConnection.getResponseCode()));
            if (urlConnection.getErrorStream() != null) {
                BufferedReader input = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream(), payloadJavaEncoding));
                StringBuffer errorStream = new StringBuffer("");
                while ((line = input.readLine()) != null) {
                    errorStream.append(line).append("\n");
                }
                input.close();
                responseMsg.addStringPayload(BusinessMessage.PAYLOAD_RESPONSE_ERROR_STREAM, errorStream.toString());
            }

            // Read back in the response from the client (one character at a time)
            // BufferedReader with big 16K buffer to reduce across internet I/O - careful if you change this as you can effect performance!!
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), payloadJavaEncoding), 16384);

            // Read a line at a time for performance (this is very SLOOOW if you read a char at a time :o(
            while ((line = in.readLine()) != null) {
                reply.append(line).append("\n");
            }

            in.close();

            responseMsg.addStringPayload(BusinessMessage.PAYLOAD_RESPONSE_MESSAGE, reply.toString());
            if (log.isDebugEnabled()) {
                log.debug("Response Message received from Client - " + reply.toString());
            }
        } catch (IOException ioe) {
            String error =
                    "sendMessageViaHttps : An IOException was thrown sending to " + targetUrl + " error detail was, " + ioe.getMessage();
            log.fatal(error);
            // Return response message if it is ASN / POR message (Response code is other than 200).
            String messageType = outboundMessage.getConfigParameter(BusinessMessage.CONFIG_TRANSACTION_TYPE);
            if (StringUtils.isNotBlank(messageType)) {
                if (messageType.equals(ADVANCE_SHIPPING_NOTICE) || messageType.equals(PURCHASE_ORDER_RESPONSE)) {
                    return responseMsg;
                }
            }
            throw new ElectroException("HttpsStreamSender." + error, ioe);
        }

        if (log.isDebugEnabled()) {
            log.debug("postAckStreamViaHttps : Method Finish");
        }
        return responseMsg;
    }
}
