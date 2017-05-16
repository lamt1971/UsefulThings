package com.electrocomponents.messagerelay.support;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.commons.components.general.util.DateTimeUtils;

/**
 * Copyright (c) Electrocomponents Plc.
 * Author         : e0180383
 * Creation Date  : 15-Jun-2006
 * Creation Time  : 16:09:09
 * IDE            : IntelliJ IDEA 5
 * *******************************************************************************************************************
 * Description :
 * An POJO representing a Generic eCommerce Business Message.
 * *******************************************************************************************************************
 * Change History
 * *******************************************************************************************************************
 * * Change   * Author   * Date         * Description
 * *******************************************************************************************************************
 * * New      * e0180383 *  15-Jun-2006 * New class created
 * *******************************************************************************************************************
 * * 38867    * e0180383 *  23-Nov-2006 * Added Object payload handling to allow MimeMessage transmission.
 * *******************************************************************************************************************
 * * N/A      * e0161085 *  07-Mar-2007 * Added SVID.
 * *******************************************************************************************************************
 *
 * @author e0180383
 */
public class BusinessMessage implements Serializable {

    /**
     * The SVID.
     */
    public static final long serialVersionUID = -2897488680011841107L;

    /**
     * Log4J.
     */
    private static Log log = LogFactory.getLog(BusinessMessage.class);

    // Constants to be used as keys in the message component Collections...

    /**
     * The URL to which the message will be sent.
     */
    public static final String CONFIG_TARGET_URL = "CONFIG_TARGET_URL";
    public static final String CONFIG_SENDER_URL = "CONFIG_SENDER_URL"; // The URL which originated the content of the message (e.g. eproc
                                                                        // hub on outbound, customer system on a response)

    public static final String CONFIG_DISABLE_ROLLBACK_ON_ERROR = "CONFIG_DISABLE_ROLLBACK_ON_ERROR"; // If this is specified do not
                                                                                                      // rollback and retry if an exception
                                                                                                      // is caught.

    public static final String CONFIG_GENERATOR_HOSTNAME = "CONFIG_GENERATOR_HOSTNAME"; // The server that originated the content of the
                                                                                        // message (e.g. eproc server hostname or dmz JBoss
                                                                                        // server hostname).

    public static final String CONFIG_USERNAME = "CONFIG_USERNAME"; // Username to log into the target resource
    public static final String CONFIG_USERNAME_ENCODING = "CONFIG_USERNAME_ENCODING"; // Encoding scheme we must apply to the username.

    public static final String CONFIG_PASSWORD = "CONFIG_PASSWORD"; // Password to log into the target resource
    public static final String CONFIG_PASSWORD_ENCODING = "CONFIG_PASSWORD_ENCODING"; // Encoding scheme we must apply to the password.

    public static final String CONFIG_PAYLOAD_JAVA_CHARSET = "CONFIG_PAYLOAD_JAVA_CHARSET"; // The JAVA characterset encoding we must apply
                                                                                            // to the outgoing string data.

    public static final String CONFIG_RESPONSE_QUEUE = "CONFIG_RESPONSE_QUEUE"; // The JMS queue used to relay the response on Asynchronous
                                                                                // messages.

    public static final String CONFIG_RESPONSE_HOST = "CONFIG_RESPONSE_HOST"; // The name of the JMS Host which sent the message.

    public static final String CONFIG_PERSISTANCE_FILENAME = "CONFIG_PERSISTANCE_FILENAME"; // The full path to save the message to disk
    public static final String CONFIG_TRANSACTION_TYPE = "CONFIG_TRANSACTION_TYPE"; // Transaction Type, e.g. EINV / POR / ASN etc.
    public static final String CONFIG_TRANSACTION_FORMAT = "CONFIG_TRANSACTION_FORMAT"; // Transaction Format, e.g. XCBL3 / XCML

    public static final String CONFIG_CLIENTCERT_AUTH_KEYSTORE = "CONFIG_CLIENTCERT_AUTH_KEYSTORE"; // Name of the keystore
    public static final String CONFIG_CLIENTCERT_AUTH_KS_PASSWORD = "CONFIG_CLIENTCERT_AUTH_KS_PASSWORD"; // The keystore password
    public static final String CONFIG_CLIENTCERT_AUTH_TRUSTSTORE = "CONFIG_CLIENTCERT_AUTH_TRUSTSTORE"; // Name of the truststore

    public static final String CONFIG_TIMEOUT_MILLISECONDS = "CONFIG_TIMEOUT_MILLISECONDS"; // Sets the period the connection should hold
                                                                                            // open waiting for a response from the target.

    public static final String PAYLOAD_OUTBOUND_MESSAGE = "PAYLOAD_OUTBOUND_MESSAGE"; // Default indentifer for outbound String payloads.
    public static final String PAYLOAD_RESPONSE_MESSAGE = "PAYLOAD_RESPONSE_MESSAGE"; // Default indentifer for response String payloads.

    public static final String HEADER_CONTENT_TYPE = "Content-Type"; // The Content-Type header for the message, content should be of the
                                                                     // form text/xml; charset=utf-8 etc.
    public static final String PAYLOAD_RESPONSE_CODE = "PAYLOAD_RESPONSE_CODE"; // Response code from customer.

    public static final String PAYLOAD_RESPONSE_ERROR_STREAM = "PAYLOAD_RESPONSE_ERROR_STREAM"; // Response error stream in case of error.

    private HashMap<String, String> configParameters = new HashMap<String, String>();
    private HashMap<String, String> messageHeaders = new HashMap<String, String>();
    private HashMap<String, String> postParameters = new HashMap<String, String>();
    private HashMap<String, String> stringPayloads = new HashMap<String, String>();
    private HashMap<String, Object> objectPayloads = new HashMap<String, Object>();
    private List<ErrorInfo> errors = new LinkedList<ErrorInfo>();
    private String processorClass;
    private String responseProcessorClass;

    private BusinessMessage outboundMessage;

    public void addMessageHeader(String headerName, String headerValue) {
        messageHeaders.put(headerName, headerValue);
    }

    public void setConfigParameter(String configParameterName, String configParameterValue) {
        configParameters.put(configParameterName, configParameterValue);
    }

    public void addPostParameter(String postParameterName, String postParameterValue) {
        postParameters.put(postParameterName, postParameterValue);
    }

    public void addStringPayload(String stringPayloadName, String stringPayloadValue) {
        stringPayloads.put(stringPayloadName, stringPayloadValue);
    }

    public void addObjectPayload(String objectPayloadClassName, String objectPayload) {
        objectPayloads.put(objectPayloadClassName, objectPayload);
    }

    public void setError(ErrorInfo error) {
        errors.add(error);
    }

    public String getMessageHeader(String headerName) {
        return messageHeaders.get(headerName);
    }

    public String getConfigParameter(String configParameterName) {
        return configParameters.get(configParameterName);
    }

    public String getPostParameter(String postParameterName) {
        return postParameters.get(postParameterName);
    }

    public String getStringPayload(String stringPayloadName) {
        return stringPayloads.get(stringPayloadName);
    }

    public HashMap<String, String> getConfigParameters() {
        return configParameters;
    }

    public List<ErrorInfo> getErrors() {
        return errors;
    }

    public void setConfigParameters(HashMap<String, String> configParameters) {
        this.configParameters = configParameters;
    }

    public HashMap<String, String> getMessageHeaders() {
        return messageHeaders;
    }

    public void setMessageHeaders(HashMap<String, String> messageHeaders) {
        this.messageHeaders = messageHeaders;
    }

    public HashMap<String, String> getPostParameters() {
        return postParameters;
    }

    public void setPostParameters(HashMap<String, String> postParameters) {
        this.postParameters = postParameters;
    }

    public String getProcessorClass() {
        return processorClass;
    }

    public void setProcessorClass(String processorClass) {
        this.processorClass = processorClass;
    }

    public String getResponseProcessorClass() {
        return responseProcessorClass;
    }

    public void setResponseProcessorClass(String responseProcessorClass) {
        this.responseProcessorClass = responseProcessorClass;
    }

    public HashMap<String, String> getStringPayloads() {
        return stringPayloads;
    }

    public void setStringPayloads(HashMap<String, String> stringPayloads) {
        this.stringPayloads = stringPayloads;
    }

    public HashMap<String, Object> getObjectPayloads() {
        return objectPayloads;
    }

    public void setObjectPayloads(HashMap<String, Object> objectPayloads) {
        this.objectPayloads = objectPayloads;
    }

    public void setErrors(List<ErrorInfo> errors) {
        this.errors = errors;
    }

    public BusinessMessage getOutboundMessage() {
        return outboundMessage;
    }

    public void setOutboundMessage(BusinessMessage outboundMessage) {
        this.outboundMessage = outboundMessage;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final BusinessMessage that = (BusinessMessage) o;

        if (configParameters != null ? !configParameters.equals(that.configParameters) : that.configParameters != null)
            return false;
        if (errors != null ? !errors.equals(that.errors) : that.errors != null)
            return false;
        if (messageHeaders != null ? !messageHeaders.equals(that.messageHeaders) : that.messageHeaders != null)
            return false;
        if (postParameters != null ? !postParameters.equals(that.postParameters) : that.postParameters != null)
            return false;
        if (processorClass != null ? !processorClass.equals(that.processorClass) : that.processorClass != null)
            return false;
        if (stringPayloads != null ? !stringPayloads.equals(that.stringPayloads) : that.stringPayloads != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (configParameters != null ? configParameters.hashCode() : 0);
        result = 29 * result + (messageHeaders != null ? messageHeaders.hashCode() : 0);
        result = 29 * result + (postParameters != null ? postParameters.hashCode() : 0);
        result = 29 * result + (stringPayloads != null ? stringPayloads.hashCode() : 0);
        result = 29 * result + (errors != null ? errors.hashCode() : 0);
        result = 29 * result + (processorClass != null ? processorClass.hashCode() : 0);
        return result;
    }

    /**
    * Format the Message object for display or persistence.
    * @return The object as a string.
    */
    public String toString() {

        StringBuffer msgString = new StringBuffer();

        msgString
                .append("********************************************************************************************************************************\n");
        msgString
                .append("****                                    Electrocomponents Plc. - eCommerce Services                                         ****\n");
        msgString
                .append("****                                                                                                                        ****\n");
        msgString
                .append("****                                ECOMMERCE BUSINESS MESSAGING SYSTEM - MESSAGE DETAIL                                    ****\n");
        msgString
                .append("****                                           Version 1.01 - November 2006                                                 ****\n");
        msgString
                .append("********************************************************************************************************************************\n");
        msgString.append("****                                       File Created " + DateTimeUtils.getIso8601TimeStamp()
                + "                                           ****\n");
        msgString
                .append("********************************************************************************************************************************\n");
        msgString.append("\n");
        msgString.append("Message Processor Class : " + processorClass + "\n");
        msgString.append("Response Message Processor Class : " + responseProcessorClass + "\n\n");

        // Write out any config parameters that are present.
        if (configParameters.size() > 0) {

            int count = 1;
            Iterator<String> itr = configParameters.keySet().iterator();

            msgString.append("ConfigParameters\n");
            msgString.append("----------------\n");

            while (itr.hasNext()) {
                String name = itr.next();
                String value = configParameters.get(name);

                msgString.append(count + ").\nName=" + name + "\nValue=" + value + "\n");
                count++;
            }
            msgString.append("\n");
        }

        // Write out any headers that are present.
        if (messageHeaders.size() > 0) {

            int count = 1;
            Iterator<String> itr = messageHeaders.keySet().iterator();

            msgString.append("Message Headers\n");
            msgString.append("---------------\n");

            while (itr.hasNext()) {
                String name = itr.next();
                String value = messageHeaders.get(name);

                msgString.append(count + ").\nName=" + name + "\nValue=" + value + "\n");
                count++;
            }
            msgString.append("\n");
        }

        // Write out any HTTP POST parameters that are present.
        if (postParameters.size() > 0) {

            int count = 1;
            Iterator<String> itr = postParameters.keySet().iterator();

            msgString.append("Post Parameters\n");
            msgString.append("---------------\n");

            while (itr.hasNext()) {
                String name = itr.next();
                String value = postParameters.get(name);

                msgString.append(count + ").\nName=" + name + "\nValue=" + value + "\n");
                count++;
            }
            msgString.append("\n");
        }

        // Write out any String payloads that are present.
        if (stringPayloads.size() > 0) {

            int count = 1;
            Iterator<String> itr = stringPayloads.keySet().iterator();

            msgString.append("String Payloads\n");
            msgString.append("---------------\n");

            while (itr.hasNext()) {
                String name = itr.next();
                String value = stringPayloads.get(name);

                msgString.append(count + ").\nName=" + name + "\nValue=" + value + "\n");
                count++;
            }
            msgString.append("\n");
        }

        // Write out the class names of any Object payloads that are present.
        if (objectPayloads.size() > 0) {

            int count = 1;
            Iterator<String> itr = objectPayloads.keySet().iterator();

            msgString.append("Object Payloads\n");
            msgString.append("---------------\n");
            while (itr.hasNext()) {
                String name = itr.next();

                msgString.append(count + ").\nObject Payload Class=" + name + "\n");
                count++;
            }
            msgString.append("\n");
        }

        // Write out any Errors that are present.
        if (errors.size() > 0) {

            int count = 1;
            Iterator<ErrorInfo> itr = errors.iterator();

            msgString.append("Errors\n");
            msgString.append("------\n");

            while (itr.hasNext()) {
                ErrorInfo error = itr.next();
                String errorType = error.getErrorType();
                String errorGeneratingClass = error.getErrorGeneratingClass();
                String errorMessage = error.getErrorMessage();

                msgString.append(count + ").\n\nA " + errorType + " error occurred in " + errorGeneratingClass);
                msgString.append("\nThe error message was :-\n\n" + errorMessage + "\n\n");
                count++;
            }
            msgString.append("\n");
        }
        msgString.append("\n\n");
        msgString
                .append("********************************************************************************************************************************\n");
        msgString
                .append("****                                                   END OF MESSAGE...                                                    ****\n");
        msgString
                .append("********************************************************************************************************************************");

        return msgString.toString();
    }

}
